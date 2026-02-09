from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent
DATA_DIR = BASE_DIR / "data"
ORIGINALS_DIR = DATA_DIR / "originals"
CLIPS_DIR = DATA_DIR / "clips"
DB_PATH = DATA_DIR / "aikido.db"

# Ensure directories exist
for d in [DATA_DIR, ORIGINALS_DIR, CLIPS_DIR]:
    d.mkdir(parents=True, exist_ok=True)

HOST = "0.0.0.0"
PORT = 8000
MAX_UPLOAD_SIZE_MB = 500
