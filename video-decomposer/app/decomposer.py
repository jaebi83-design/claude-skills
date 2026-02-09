import shutil
import uuid
from pathlib import Path

from app.config import ORIGINALS_DIR, CLIPS_DIR, THUMBNAILS_DIR, FRAMES_DIR
from app.database import (
    get_source_video, create_segment, update_job_status, get_job,
)
from app.video_processor import (
    extract_frames, extract_clip, get_video_duration, generate_thumbnail,
)
from app.text_detector import detect_text_boundaries, normalize_text


def _label_to_filename(label: str) -> str:
    """Convert a segment label to a safe filename."""
    # Replace spaces with underscores, remove non-alphanumeric characters
    import re
    name = re.sub(r"[^a-zA-Z0-9\s_-]", "", label)
    name = re.sub(r"\s+", "_", name.strip())
    return name if name else "segment"


def _unique_clip_filename(label: str, clips_dir: Path) -> str:
    """Generate a unique filename based on the label, avoiding collisions."""
    base = _label_to_filename(label)
    filename = f"{base}.mp4"
    if not (clips_dir / filename).exists():
        return filename
    # Append a counter if the name already exists
    counter = 2
    while (clips_dir / f"{base}_{counter}.mp4").exists():
        counter += 1
    return f"{base}_{counter}.mp4"
    """Run a full decomposition job: detect text boundaries, then split video.

    1. Extract frames from the source video at the configured interval
    2. Run OCR on each frame to detect on-screen text
    3. Identify boundaries where text changes (new technique markers)
    4. Split the original video at those boundaries into separate clips
    5. Store segment metadata in the database
    """
    job = get_job(job_id)
    if not job:
        return

    update_job_status(job_id, "processing")

    source_path = ORIGINALS_DIR / job["source_storage_path"]
    if not source_path.exists():
        update_job_status(job_id, "failed", error_message="Source video file not found")
        return

    # Create a working directory for this job's frames
    job_frames_dir = FRAMES_DIR / f"job_{job_id}"
    job_frames_dir.mkdir(parents=True, exist_ok=True)

    try:
        duration = get_video_duration(source_path)
        interval = job["frame_interval"]

        # Step 1: Extract frames
        frames = extract_frames(source_path, job_frames_dir, interval)
        if not frames:
            update_job_status(job_id, "failed", error_message="No frames extracted")
            return

        # Step 2+3: Detect text boundaries
        boundaries = detect_text_boundaries(frames, interval)

        # Step 4: Build segments from boundaries
        segments = _boundaries_to_segments(boundaries, duration)

        # Step 5: Extract clips and create database records
        for seg in segments:
            clip_filename = _unique_clip_filename(seg["label"], CLIPS_DIR)
            clip_path = CLIPS_DIR / clip_filename

            extract_clip(source_path, clip_path, seg["start_time"], seg["end_time"])

            # Generate thumbnail from the start of the clip
            thumb_base = _label_to_filename(seg["label"])
            thumb_filename = f"{thumb_base}_thumb.jpg"
            thumb_path = THUMBNAILS_DIR / thumb_filename
            try:
                generate_thumbnail(clip_path, thumb_path, time_offset=0.5)
            except RuntimeError:
                thumb_filename = None

            create_segment(
                job_id=job_id,
                source_video_id=job["source_video_id"],
                segment_index=seg["index"],
                label=seg["label"],
                start_time=seg["start_time"],
                end_time=seg["end_time"],
                clip_filename=clip_filename,
                thumbnail_filename=thumb_filename,
            )

        update_job_status(job_id, "complete", segments_found=len(segments))

    except Exception as e:
        update_job_status(job_id, "failed", error_message=str(e))

    finally:
        # Clean up extracted frames
        shutil.rmtree(job_frames_dir, ignore_errors=True)


def _boundaries_to_segments(boundaries: list[dict], total_duration: float) -> list[dict]:
    """Convert a list of text boundaries into segment definitions.

    Each boundary marks the start of a new segment. The segment extends
    until the next boundary (or end of video).
    """
    if not boundaries:
        # No text detected — treat entire video as one segment
        return [{
            "index": 0,
            "label": "Full Video",
            "start_time": 0.0,
            "end_time": total_duration,
        }]

    segments = []
    for i, boundary in enumerate(boundaries):
        start = boundary["timestamp"]
        if i + 1 < len(boundaries):
            end = boundaries[i + 1]["timestamp"]
        else:
            end = total_duration

        # Skip very short segments (< 0.5 seconds)
        if end - start < 0.5:
            continue

        label = boundary["text"].strip() if boundary["text"].strip() else f"Segment {i + 1}"
        # Use the normalized text as a cleaner label, but fall back to raw text
        normalized = normalize_text(boundary["text"])
        if normalized:
            label = boundary["text"].strip()

        segments.append({
            "index": len(segments),
            "label": label,
            "start_time": start,
            "end_time": end,
        })

    return segments
