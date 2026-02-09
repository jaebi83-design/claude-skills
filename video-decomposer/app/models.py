from pydantic import BaseModel
from typing import Optional


class DecomposeRequest(BaseModel):
    source_video_id: int
    frame_interval: Optional[float] = 1.0


class TextBoundary(BaseModel):
    timestamp: float
    text: str


class SegmentInfo(BaseModel):
    index: int
    label: str
    start_time: float
    end_time: float
