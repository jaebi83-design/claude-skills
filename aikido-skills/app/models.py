from pydantic import BaseModel
from typing import Optional


class ClipDefinition(BaseModel):
    start_time: float
    end_time: float
    attack_type_id: Optional[int] = None
    technique_id: Optional[int] = None
    energy: Optional[str] = None
    notes: Optional[str] = None


class ProcessRequest(BaseModel):
    source_video_id: int
    clips: list[ClipDefinition]
