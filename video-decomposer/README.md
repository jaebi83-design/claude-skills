# Video Decomposer

A web application that takes a video file, detects on-screen text overlays (technique names, titles, section markers), and automatically splits the video into separate clips at each text boundary.

## How It Works

1. **Upload** a video containing multiple techniques/segments separated by text markers
2. The app **samples frames** at regular intervals and runs OCR (Tesseract) on each frame
3. When the detected text **changes significantly**, that point is marked as a segment boundary
4. The video is **split** at each boundary into individual clips
5. **Browse** and play the resulting clips, each labeled with the detected text

## Features

- Upload videos and automatically detect text-based segment boundaries
- Configurable frame sampling interval for speed vs accuracy tradeoff
- Background processing with real-time status updates
- Video streaming with seek support (HTTP range requests)
- Thumbnail generation for each segment
- Browse, filter, and play extracted clips

## Quick Start

```bash
cd video-decomposer
bash setup.sh
uvicorn app.main:app --host 0.0.0.0 --port 8000
```

Then open http://localhost:8000 in your browser.

## System Requirements

- Python 3.10+
- FFmpeg
- Tesseract OCR

## Tech Stack

- **Backend:** FastAPI, SQLite, pytesseract, Pillow
- **Frontend:** Jinja2 templates, vanilla JavaScript, CSS
- **Processing:** FFmpeg (video splitting), Tesseract (OCR)
