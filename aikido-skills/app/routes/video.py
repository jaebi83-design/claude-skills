import os
from pathlib import Path
from fastapi import APIRouter, Request, HTTPException
from fastapi.responses import StreamingResponse
from app.config import CLIPS_DIR, ORIGINALS_DIR

router = APIRouter()


def _parse_range(range_header: str, file_size: int) -> tuple[int, int]:
    """Parse a Range header like 'bytes=0-1023'."""
    try:
        range_spec = range_header.replace("bytes=", "")
        parts = range_spec.split("-")
        start = int(parts[0]) if parts[0] else 0
        end = int(parts[1]) if parts[1] else file_size - 1
        end = min(end, file_size - 1)
        return start, end
    except (ValueError, IndexError):
        return 0, file_size - 1


async def _file_iterator(path: Path, start: int, chunk_size: int, block_size: int = 65536):
    """Async generator that yields file chunks."""
    bytes_remaining = chunk_size
    with open(path, "rb") as f:
        f.seek(start)
        while bytes_remaining > 0:
            read_size = min(block_size, bytes_remaining)
            data = f.read(read_size)
            if not data:
                break
            bytes_remaining -= len(data)
            yield data


def _stream_video_file(request: Request, file_path: Path):
    """Stream a video file with range request support."""
    if not file_path.exists():
        raise HTTPException(status_code=404, detail="Video not found")

    file_size = os.path.getsize(file_path)
    range_header = request.headers.get("range")

    if range_header:
        start, end = _parse_range(range_header, file_size)
        chunk_size = end - start + 1
        headers = {
            "Content-Range": f"bytes {start}-{end}/{file_size}",
            "Accept-Ranges": "bytes",
            "Content-Length": str(chunk_size),
            "Content-Type": "video/mp4",
        }
        return StreamingResponse(
            _file_iterator(file_path, start, chunk_size),
            status_code=206,
            headers=headers,
        )

    return StreamingResponse(
        _file_iterator(file_path, 0, file_size),
        media_type="video/mp4",
        headers={"Accept-Ranges": "bytes", "Content-Length": str(file_size)},
    )


@router.get("/video/clips/{filename}")
async def stream_clip(request: Request, filename: str):
    file_path = CLIPS_DIR / filename
    return _stream_video_file(request, file_path)


@router.get("/video/originals/{filename}")
async def stream_original(request: Request, filename: str):
    file_path = ORIGINALS_DIR / filename
    return _stream_video_file(request, file_path)
