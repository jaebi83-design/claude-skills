from fastapi import APIRouter, Request, HTTPException

from app.database import (
    get_all_source_videos, get_all_jobs, get_job,
    get_segments_for_job, get_segment,
)

router = APIRouter()


@router.get("/")
async def index(request: Request):
    videos = get_all_source_videos()
    jobs = get_all_jobs()
    return request.app.state.templates.TemplateResponse(
        "index.html",
        {"request": request, "videos": videos, "jobs": jobs},
    )


@router.get("/upload")
async def upload_page(request: Request):
    return request.app.state.templates.TemplateResponse(
        "upload.html",
        {"request": request},
    )


@router.get("/job/{job_id}")
async def job_detail(request: Request, job_id: int):
    job = get_job(job_id)
    if not job:
        raise HTTPException(status_code=404, detail="Job not found")
    segments = get_segments_for_job(job_id)
    return request.app.state.templates.TemplateResponse(
        "job.html",
        {"request": request, "job": job, "segments": segments},
    )


@router.get("/clip/{segment_id}")
async def clip_detail(request: Request, segment_id: int):
    segment = get_segment(segment_id)
    if not segment:
        raise HTTPException(status_code=404, detail="Segment not found")
    return request.app.state.templates.TemplateResponse(
        "clip.html",
        {"request": request, "segment": segment},
    )
