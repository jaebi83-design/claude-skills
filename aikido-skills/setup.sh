#!/bin/bash
set -e

echo "=== Aikido Skills Setup ==="

# Install system dependencies
echo "Installing FFmpeg..."
apt-get update -qq && apt-get install -y -qq ffmpeg > /dev/null 2>&1 || {
    echo "Warning: Could not install FFmpeg via apt. Please install it manually."
}

# Install Python dependencies
echo "Installing Python dependencies..."
pip install -r requirements.txt

# Create data directories
echo "Creating data directories..."
mkdir -p data/originals data/clips

# Initialize database with seed data
echo "Initializing database..."
python -c "from app.database import init_db; init_db()"

echo "=== Setup complete! ==="
echo "Run: uvicorn app.main:app --host 0.0.0.0 --port 8000"
