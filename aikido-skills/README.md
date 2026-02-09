# Aikido Skills

A web application for organizing Aikido training videos by attack, technique, and energy.

## Features

- **Upload** full-length Aikido training videos
- **Split** videos into short clips by marking start/end timestamps
- **Categorize** each clip by attack type, technique, and energy (omote/ura)
- **Browse** and filter clips through a web interface
- **Stream** video clips with full seeking support

## Quick Start

```bash
cd aikido-skills
chmod +x setup.sh
./setup.sh
uvicorn app.main:app --host 0.0.0.0 --port 8000
```

Then open http://localhost:8000 in your browser.

## Categories

### Attack Types
shomen uchi, yokomen uchi, tsuki, katate dori, morote dori, ryote dori, kata dori, mune dori, ushiro ryote dori, ushiro kubishime

### Techniques
ikkyo, nikkyo, sankyo, yonkyo, gokyo, irimi nage, kote gaeshi, shiho nage, tenchi nage, kaiten nage, kokyu nage, kokyu ho, sumi otoshi, juji garami nage

### Energy
- **Omote** - entering/direct energy
- **Ura** - turning/redirecting energy

## Tech Stack

- **Backend:** Python, FastAPI, Uvicorn
- **Database:** SQLite
- **Video Processing:** FFmpeg
- **Frontend:** Jinja2 templates, vanilla CSS/JS
