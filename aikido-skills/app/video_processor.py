import subprocess
from pathlib import Path


def extract_clip(source_path: Path, output_path: Path, start: float, end: float) -> Path:
    """Extract a clip from source video using FFmpeg."""
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
        raise RuntimeError(f"FFmpeg failed: {result.stderr}")
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
