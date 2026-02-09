from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent
DATA_DIR = BASE_DIR / "data"
ORIGINALS_DIR = DATA_DIR / "originals"
CLIPS_DIR = DATA_DIR / "clips"
THUMBNAILS_DIR = DATA_DIR / "thumbnails"
FRAMES_DIR = DATA_DIR / "frames"
DB_PATH = DATA_DIR / "decomposer.db"

# Ensure directories exist
for d in [DATA_DIR, ORIGINALS_DIR, CLIPS_DIR, THUMBNAILS_DIR, FRAMES_DIR]:
    d.mkdir(parents=True, exist_ok=True)

HOST = "0.0.0.0"
PORT = 8000
MAX_UPLOAD_SIZE_MB = 500

# Text detection settings
FRAME_SAMPLE_INTERVAL = 1.0  # seconds between frame samples
OCR_CONFIDENCE_THRESHOLD = 40  # minimum confidence for OCR text
TEXT_CHANGE_THRESHOLD = 0.6  # similarity threshold to consider text "changed"
