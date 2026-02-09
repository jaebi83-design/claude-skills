import sqlite3
from app.config import DB_PATH

SCHEMA = """
CREATE TABLE IF NOT EXISTS source_videos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    filename TEXT NOT NULL,
    storage_path TEXT NOT NULL,
    duration_seconds REAL,
    uploaded_at TEXT DEFAULT (datetime('now')),
    notes TEXT
);

CREATE TABLE IF NOT EXISTS decompose_jobs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    source_video_id INTEGER NOT NULL REFERENCES source_videos(id),
    status TEXT NOT NULL DEFAULT 'pending' CHECK(status IN ('pending', 'processing', 'complete', 'failed')),
    frame_interval REAL NOT NULL DEFAULT 1.0,
    segments_found INTEGER DEFAULT 0,
    error_message TEXT,
    created_at TEXT DEFAULT (datetime('now')),
    completed_at TEXT
);

CREATE TABLE IF NOT EXISTS segments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    job_id INTEGER NOT NULL REFERENCES decompose_jobs(id),
    source_video_id INTEGER NOT NULL REFERENCES source_videos(id),
    segment_index INTEGER NOT NULL,
    label TEXT,
    start_time REAL NOT NULL,
    end_time REAL NOT NULL,
    duration_seconds REAL NOT NULL,
    clip_filename TEXT,
    thumbnail_filename TEXT,
    created_at TEXT DEFAULT (datetime('now'))
);
"""


def get_db() -> sqlite3.Connection:
    conn = sqlite3.connect(str(DB_PATH))
    conn.row_factory = sqlite3.Row
    conn.execute("PRAGMA foreign_keys = ON")
    return conn


def init_db():
    conn = get_db()
    conn.executescript(SCHEMA)
    conn.commit()
    conn.close()


def create_source_video(filename: str, storage_path: str, duration_seconds: float = None, notes: str = None) -> int:
    conn = get_db()
    cur = conn.execute(
        "INSERT INTO source_videos (filename, storage_path, duration_seconds, notes) VALUES (?, ?, ?, ?)",
        (filename, storage_path, duration_seconds, notes),
    )
    vid = cur.lastrowid
    conn.commit()
    conn.close()
    return vid


def get_source_video(video_id: int):
    conn = get_db()
    row = conn.execute("SELECT * FROM source_videos WHERE id = ?", (video_id,)).fetchone()
    conn.close()
    return dict(row) if row else None


def get_all_source_videos():
    conn = get_db()
    rows = conn.execute("SELECT * FROM source_videos ORDER BY uploaded_at DESC").fetchall()
    conn.close()
    return [dict(r) for r in rows]


def create_job(source_video_id: int, frame_interval: float = 1.0) -> int:
    conn = get_db()
    cur = conn.execute(
        "INSERT INTO decompose_jobs (source_video_id, frame_interval) VALUES (?, ?)",
        (source_video_id, frame_interval),
    )
    job_id = cur.lastrowid
    conn.commit()
    conn.close()
    return job_id


def update_job_status(job_id: int, status: str, segments_found: int = None, error_message: str = None):
    conn = get_db()
    if status == "complete":
        conn.execute(
            "UPDATE decompose_jobs SET status = ?, segments_found = ?, completed_at = datetime('now') WHERE id = ?",
            (status, segments_found, job_id),
        )
    elif status == "failed":
        conn.execute(
            "UPDATE decompose_jobs SET status = ?, error_message = ?, completed_at = datetime('now') WHERE id = ?",
            (status, error_message, job_id),
        )
    else:
        conn.execute(
            "UPDATE decompose_jobs SET status = ? WHERE id = ?",
            (status, job_id),
        )
    conn.commit()
    conn.close()


def get_job(job_id: int):
    conn = get_db()
    row = conn.execute(
        """SELECT j.*, v.filename as source_filename, v.storage_path as source_storage_path,
                  v.duration_seconds as source_duration
           FROM decompose_jobs j
           JOIN source_videos v ON j.source_video_id = v.id
           WHERE j.id = ?""",
        (job_id,),
    ).fetchone()
    conn.close()
    return dict(row) if row else None


def get_all_jobs():
    conn = get_db()
    rows = conn.execute(
        """SELECT j.*, v.filename as source_filename
           FROM decompose_jobs j
           JOIN source_videos v ON j.source_video_id = v.id
           ORDER BY j.created_at DESC"""
    ).fetchall()
    conn.close()
    return [dict(r) for r in rows]


def create_segment(
    job_id: int,
    source_video_id: int,
    segment_index: int,
    label: str,
    start_time: float,
    end_time: float,
    clip_filename: str = None,
    thumbnail_filename: str = None,
) -> int:
    duration = end_time - start_time
    conn = get_db()
    cur = conn.execute(
        """INSERT INTO segments
           (job_id, source_video_id, segment_index, label, start_time, end_time,
            duration_seconds, clip_filename, thumbnail_filename)
           VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)""",
        (job_id, source_video_id, segment_index, label, start_time, end_time,
         duration, clip_filename, thumbnail_filename),
    )
    seg_id = cur.lastrowid
    conn.commit()
    conn.close()
    return seg_id


def get_segments_for_job(job_id: int):
    conn = get_db()
    rows = conn.execute(
        "SELECT * FROM segments WHERE job_id = ? ORDER BY segment_index",
        (job_id,),
    ).fetchall()
    conn.close()
    return [dict(r) for r in rows]


def get_segment(segment_id: int):
    conn = get_db()
    row = conn.execute(
        """SELECT s.*, v.filename as source_filename, v.storage_path as source_storage_path
           FROM segments s
           JOIN source_videos v ON s.source_video_id = v.id
           WHERE s.id = ?""",
        (segment_id,),
    ).fetchone()
    conn.close()
    return dict(row) if row else None
