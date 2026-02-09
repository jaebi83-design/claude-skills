from fastapi import FastAPI
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
from pathlib import Path
from app.database import init_db
from app.routes import pages, api, video

app = FastAPI(title="Aikido Skills")

# Templates
templates_dir = Path(__file__).parent / "templates"
app.state.templates = Jinja2Templates(directory=str(templates_dir))

# Static files
static_dir = Path(__file__).parent / "static"
app.mount("/static", StaticFiles(directory=str(static_dir)), name="static")

# Routes
app.include_router(video.router)
app.include_router(api.router)
app.include_router(pages.router)


@app.on_event("startup")
async def startup():
    init_db()
