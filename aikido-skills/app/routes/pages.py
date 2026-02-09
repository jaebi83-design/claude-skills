from fastapi import APIRouter, Request
from typing import Optional
from app.database import get_clips, get_clip_by_id, get_attack_types, get_techniques, get_source_video

router = APIRouter()


@router.get("/")
async def browse(
    request: Request,
    attack_type_id: Optional[int] = None,
    technique_id: Optional[int] = None,
    energy: Optional[str] = None,
):
    clips = get_clips(attack_type_id=attack_type_id, technique_id=technique_id, energy=energy)
    attacks = get_attack_types()
    techniques = get_techniques()
    return request.app.state.templates.TemplateResponse(
        "index.html",
        {
            "request": request,
            "clips": clips,
            "attacks": attacks,
            "techniques": techniques,
            "selected_attack": attack_type_id,
            "selected_technique": technique_id,
            "selected_energy": energy,
        },
    )


@router.get("/upload")
async def upload_page(request: Request):
    attacks = get_attack_types()
    techniques = get_techniques()
    return request.app.state.templates.TemplateResponse(
        "upload.html",
        {"request": request, "attacks": attacks, "techniques": techniques},
    )


@router.get("/clip/{clip_id}")
async def clip_detail(request: Request, clip_id: int):
    clip = get_clip_by_id(clip_id)
    if not clip:
        from fastapi import HTTPException
        raise HTTPException(status_code=404, detail="Clip not found")
    return request.app.state.templates.TemplateResponse(
        "clip.html",
        {"request": request, "clip": clip},
    )
