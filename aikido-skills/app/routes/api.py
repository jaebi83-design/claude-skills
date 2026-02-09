import uuid
import shutil
from pathlib import Path
from fastapi import APIRouter, UploadFile, File, HTTPException
from typing import Optional
from app.config import ORIGINALS_DIR, CLIPS_DIR
from app.database import (
    create_source_video, create_clip, get_clips, get_clip_by_id,
    get_source_video, get_attack_types, get_techniques,
)
from app.models import ProcessRequest
from app.video_processor import extract_clip, get_video_duration

router = APIRouter(prefix="/api")


@router.post("/upload")
async def upload_video(file: UploadFile = File(...)):
    """Upload a source video file."""
    if not file.filename:
        raise HTTPException(status_code=400, detail="No file provided")

    # Generate unique filename to avoid collisions
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
        "storage_path": unique_name,
        "duration_seconds": duration,
    }


@router.post("/process")
async def process_video(request: ProcessRequest):
    """Extract clips from a source video based on timestamp definitions."""
    source = get_source_video(request.source_video_id)
    if not source:
        raise HTTPException(status_code=404, detail="Source video not found")

    source_path = ORIGINALS_DIR / source["storage_path"]
    if not source_path.exists():
        raise HTTPException(status_code=404, detail="Source video file not found on disk")

    results = []
    for i, clip_def in enumerate(request.clips):
        clip_filename = f"{uuid.uuid4().hex}.mp4"
        clip_path = CLIPS_DIR / clip_filename

        try:
            extract_clip(source_path, clip_path, clip_def.start_time, clip_def.end_time)
        except RuntimeError as e:
            raise HTTPException(status_code=500, detail=f"Clip {i} failed: {str(e)}")

        clip_id = create_clip(
            source_video_id=request.source_video_id,
            filename=clip_filename,
            storage_path=clip_filename,
            start_time=clip_def.start_time,
            end_time=clip_def.end_time,
            attack_type_id=clip_def.attack_type_id,
            technique_id=clip_def.technique_id,
            energy=clip_def.energy,
            notes=clip_def.notes,
        )
        results.append({"id": clip_id, "filename": clip_filename})

    return {"clips": results}


@router.get("/clips")
async def list_clips(
    attack_type_id: Optional[int] = None,
    technique_id: Optional[int] = None,
    energy: Optional[str] = None,
):
    """List clips with optional filtering."""
    clips = get_clips(attack_type_id=attack_type_id, technique_id=technique_id, energy=energy)
    return {"clips": clips}


@router.get("/clips/{clip_id}")
async def get_clip(clip_id: int):
    """Get a single clip by ID."""
    clip = get_clip_by_id(clip_id)
    if not clip:
        raise HTTPException(status_code=404, detail="Clip not found")
    return clip


@router.get("/attacks")
async def list_attacks():
    return {"attacks": get_attack_types()}


@router.get("/techniques")
async def list_techniques():
    return {"techniques": get_techniques()}
