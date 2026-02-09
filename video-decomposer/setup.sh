#!/bin/bash
set -e

echo "=== Video Decomposer Setup ==="

# Install system dependencies
echo "Installing FFmpeg and Tesseract OCR..."
apt-get update -qq && apt-get install -y -qq ffmpeg tesseract-ocr > /dev/null 2>&1 || {
    echo "Warning: Could not install system dependencies via apt."
    echo "Please install FFmpeg and Tesseract OCR manually."
}

# Install Python dependencies
echo "Installing Python dependencies..."
pip install -r requirements.txt

# Create data directories
echo "Creating data directories..."
mkdir -p data/originals data/clips data/thumbnails data/frames

# Initialize database
echo "Initializing database..."
python -c "from app.database import init_db; init_db()"

echo ""
echo "=== Setup complete! ==="
echo "Run: uvicorn app.main:app --host 0.0.0.0 --port 8000"
