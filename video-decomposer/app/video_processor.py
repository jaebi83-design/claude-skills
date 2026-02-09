import subprocess
from pathlib import Path


def extract_clip(source_path: Path, output_path: Path, start: float, end: float) -> Path:
    """Extract a clip from source video between start and end timestamps."""
    duration = end - start
    cmd = [
        "ffmpeg", "-y",
        "-ss", str(start),
        "-i", str(source_path),
        "-t", str(duration),
        "-c:v", "libx264",
        "-preset", "fast",
        "-crf", "23",
        "-c:a", "aac",
        "-movflags", "+faststart",
        str(output_path),
    ]
    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode != 0:
        raise RuntimeError(f"FFmpeg clip extraction failed: {result.stderr}")
    return output_path


def get_video_duration(video_path: Path) -> float:
    """Get video duration in seconds using ffprobe."""
    cmd = [
        "ffprobe",
        "-v", "error",
        "-show_entries", "format=duration",
        "-of", "default=noprint_wrappers=1:nokey=1",
        str(video_path),
    ]
    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode != 0:
        return 0.0
    return float(result.stdout.strip())


def extract_frames(source_path: Path, output_dir: Path, interval: float = 1.0) -> list[Path]:
    """Extract frames from video at the given interval (in seconds).

    Returns list of frame image paths sorted by timestamp.
    Frame filenames encode the timestamp: frame_000001.500.png = 1.5 seconds.
    """
    output_dir.mkdir(parents=True, exist_ok=True)

    # Use ffmpeg to extract frames at the specified interval
    output_pattern = str(output_dir / "frame_%06d.png")
    cmd = [
        "ffmpeg", "-y",
        "-i", str(source_path),
        "-vf", f"fps=1/{interval}",
        output_pattern,
    ]
    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode != 0:
        raise RuntimeError(f"Frame extraction failed: {result.stderr}")

    # Collect and sort frame files
    frames = sorted(output_dir.glob("frame_*.png"))
    return frames


def get_frame_timestamp(frame_path: Path, interval: float) -> float:
    """Derive the timestamp of a frame from its sequential filename and interval.

    frame_000001.png -> 0.0s (first frame)
    frame_000002.png -> interval seconds
    etc.
    """
    # Extract the number from frame_NNNNNN.png
    stem = frame_path.stem  # "frame_000001"
    num_str = stem.split("_")[1]
    frame_number = int(num_str)
    return (frame_number - 1) * interval


def generate_thumbnail(video_path: Path, output_path: Path, time_offset: float = 1.0) -> Path:
    """Generate a thumbnail image from a video at the given time offset."""
    cmd = [
        "ffmpeg", "-y",
        "-ss", str(time_offset),
        "-i", str(video_path),
        "-vframes", "1",
        "-vf", "scale=320:-1",
        str(output_path),
    ]
    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode != 0:
        raise RuntimeError(f"Thumbnail generation failed: {result.stderr}")
    return output_path
