import uuid
import shutil
import threading
from pathlib import Path
from fastapi import APIRouter, UploadFile, File, HTTPException

from app.config import ORIGINALS_DIR
from app.database import (
    create_source_video, get_source_video, get_all_source_videos,
    create_job, get_job, get_all_jobs, get_segments_for_job, get_segment,
)
from app.models import DecomposeRequest
from app.video_processor import get_video_duration
from app.decomposer import run_decompose_job

router = APIRouter(prefix="/api")


@router.post("/upload")
async def upload_video(file: UploadFile = File(...)):
    """Upload a source video file."""
    if not file.filename:
        raise HTTPException(status_code=400, detail="No file provided")

    ext = Path(file.filename).suffix or ".mp4"
    unique_name = f"{uuid.uuid4().hex}{ext}"
    save_path = ORIGINALS_DIR / unique_name

    with open(save_path, "wb") as f:
        shutil.copyfileobj(file.file, f)

    duration = get_video_duration(save_path)
    video_id = create_source_video(
        filename=file.filename,
        storage_path=unique_name,
        duration_seconds=duration,
    )

    return {
        "id": video_id,
        "filename": file.filename,
        "duration_seconds": duration,
    }


@router.post("/decompose")
async def decompose_video(request: DecomposeRequest):
    """Start a decomposition job for a source video.

    Analyzes the video for on-screen text markers and splits it into
    separate clips at each boundary where the text changes.
    """
    source = get_source_video(request.source_video_id)
    if not source:
        raise HTTPException(status_code=404, detail="Source video not found")

    source_path = ORIGINALS_DIR / source["storage_path"]
    if not source_path.exists():
        raise HTTPException(status_code=404, detail="Source video file not found on disk")

    job_id = create_job(
        source_video_id=request.source_video_id,
        frame_interval=request.frame_interval,
    )

    # Run decomposition in a background thread
    thread = threading.Thread(target=run_decompose_job, args=(job_id,), daemon=True)
    thread.start()

    return {"job_id": job_id, "status": "pending"}


@router.get("/videos")
async def list_videos():
    """List all uploaded source videos."""
    return {"videos": get_all_source_videos()}


@router.get("/videos/{video_id}")
async def get_video(video_id: int):
    video = get_source_video(video_id)
    if not video:
        raise HTTPException(status_code=404, detail="Video not found")
    return video


@router.get("/jobs")
async def list_jobs():
    """List all decomposition jobs."""
    return {"jobs": get_all_jobs()}


@router.get("/jobs/{job_id}")
async def get_job_detail(job_id: int):
    job = get_job(job_id)
    if not job:
        raise HTTPException(status_code=404, detail="Job not found")
    segments = get_segments_for_job(job_id)
    return {"job": job, "segments": segments}


@router.get("/segments/{segment_id}")
async def get_segment_detail(segment_id: int):
    segment = get_segment(segment_id)
    if not segment:
        raise HTTPException(status_code=404, detail="Segment not found")
    return segment
