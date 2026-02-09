import re
from difflib import SequenceMatcher
from pathlib import Path

import pytesseract
from PIL import Image

from app.config import OCR_CONFIDENCE_THRESHOLD, TEXT_CHANGE_THRESHOLD


def extract_text_from_frame(frame_path: Path) -> str:
    """Run OCR on a single frame and return detected text.

    Uses Tesseract with confidence filtering to reduce noise.
    """
    img = Image.open(frame_path)

    # Get detailed OCR data with confidence scores
    data = pytesseract.image_to_data(img, output_type=pytesseract.Output.DICT)

    words = []
    for i, word in enumerate(data["text"]):
        conf = int(data["conf"][i])
        cleaned = word.strip()
        if cleaned and conf >= OCR_CONFIDENCE_THRESHOLD:
            words.append(cleaned)

    return " ".join(words)


def normalize_text(text: str) -> str:
    """Normalize detected text for comparison."""
    text = text.lower().strip()
    text = re.sub(r"[^a-z0-9\s]", "", text)
    text = re.sub(r"\s+", " ", text)
    return text


def texts_are_similar(text_a: str, text_b: str) -> bool:
    """Check if two text strings are similar enough to be the same marker."""
    norm_a = normalize_text(text_a)
    norm_b = normalize_text(text_b)

    if not norm_a and not norm_b:
        return True
    if not norm_a or not norm_b:
        return False

    ratio = SequenceMatcher(None, norm_a, norm_b).ratio()
    return ratio >= TEXT_CHANGE_THRESHOLD


def detect_text_boundaries(frames: list[Path], interval: float) -> list[dict]:
    """Analyze a sequence of frames and detect where on-screen text changes.

    Returns a list of boundary markers:
        [{"timestamp": float, "text": str}, ...]

    Each boundary represents a point where the displayed text changed,
    indicating a new technique/segment.
    """
    if not frames:
        return []

    boundaries = []
    previous_text = None

    for frame_path in frames:
        # Derive timestamp from sequential frame number
        stem = frame_path.stem
        num_str = stem.split("_")[1]
        frame_number = int(num_str)
        timestamp = (frame_number - 1) * interval

        current_text = extract_text_from_frame(frame_path)

        if previous_text is None:
            # First frame: always record as a boundary
            boundaries.append({"timestamp": timestamp, "text": current_text})
            previous_text = current_text
            continue

        # Check if text has changed significantly
        if not texts_are_similar(previous_text, current_text):
            # Text changed — this is a segment boundary
            boundaries.append({"timestamp": timestamp, "text": current_text})
            previous_text = current_text

    return boundaries
