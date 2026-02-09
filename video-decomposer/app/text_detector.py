import re
from difflib import SequenceMatcher
from pathlib import Path

import pytesseract
from PIL import Image

from app.config import (
    OCR_CONFIDENCE_THRESHOLD, TEXT_CHANGE_THRESHOLD,
    SCAN_REGION_TOP_PERCENT, MIN_PERSIST_FRAMES,
)


def extract_text_from_frame(frame_path: Path) -> str:
    """Run OCR on the bottom portion of a frame and return detected text.

    Crops to the bottom region (configured by SCAN_REGION_TOP_PERCENT)
    to focus on subtitle/label text and ignore visual noise from the
    main video content.
    """
    img = Image.open(frame_path)

    # Crop to bottom region where labels appear
    width, height = img.size
    top = int(height * SCAN_REGION_TOP_PERCENT)
    img = img.crop((0, top, width, height))

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

    Only considers text in the bottom portion of the frame.
    Requires text to persist for MIN_PERSIST_FRAMES consecutive frames
    before counting it as a real boundary (filters out OCR noise).

    Returns a list of boundary markers:
        [{"timestamp": float, "text": str}, ...]
    """
    if not frames:
        return []

    # First pass: extract text from every frame
    frame_texts = []
    for frame_path in frames:
        stem = frame_path.stem
        num_str = stem.split("_")[1]
        frame_number = int(num_str)
        timestamp = (frame_number - 1) * interval
        text = extract_text_from_frame(frame_path)
        frame_texts.append({"timestamp": timestamp, "text": text})

    # Second pass: group consecutive frames with similar text
    # and only emit a boundary when the text persists
    boundaries = []
    current_text = frame_texts[0]["text"]
    current_start = frame_texts[0]["timestamp"]
    current_count = 1

    for i in range(1, len(frame_texts)):
        ft = frame_texts[i]

        if texts_are_similar(current_text, ft["text"]):
            # Same text continues
            current_count += 1
        else:
            # Text changed — save previous if it persisted long enough
            if current_count >= MIN_PERSIST_FRAMES or len(boundaries) == 0:
                boundaries.append({"timestamp": current_start, "text": current_text})

            # Start tracking new text
            current_text = ft["text"]
            current_start = ft["timestamp"]
            current_count = 1

    # Don't forget the last group
    if current_count >= MIN_PERSIST_FRAMES or len(boundaries) == 0:
        boundaries.append({"timestamp": current_start, "text": current_text})

    return boundaries
