"""Standalone script to initialize and seed the database."""
import sys
from pathlib import Path

# Add project root to path
sys.path.insert(0, str(Path(__file__).resolve().parent.parent))

from app.database import init_db

if __name__ == "__main__":
    init_db()
    print("Database initialized and seeded successfully.")
