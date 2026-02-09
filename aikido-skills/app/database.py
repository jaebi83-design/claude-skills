import sqlite3
from pathlib import Path
from app.config import DB_PATH

SCHEMA = """
CREATE TABLE IF NOT EXISTS attack_types (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    name_jp TEXT,
    description TEXT
);

CREATE TABLE IF NOT EXISTS techniques (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    name_jp TEXT,
    description TEXT
);

CREATE TABLE IF NOT EXISTS source_videos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    filename TEXT NOT NULL,
    storage_path TEXT NOT NULL,
    duration_seconds REAL,
    uploaded_at TEXT DEFAULT (datetime('now')),
    notes TEXT
);

CREATE TABLE IF NOT EXISTS clips (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    source_video_id INTEGER NOT NULL REFERENCES source_videos(id),
    filename TEXT NOT NULL,
    storage_path TEXT NOT NULL,
    start_time REAL NOT NULL,
    end_time REAL NOT NULL,
    duration_seconds REAL NOT NULL,
    attack_type_id INTEGER REFERENCES attack_types(id),
    technique_id INTEGER REFERENCES techniques(id),
    energy TEXT CHECK(energy IN ('omote', 'ura')),
    notes TEXT,
    created_at TEXT DEFAULT (datetime('now'))
);
"""

SEED_ATTACKS = [
    ("shomen uchi", "正面打ち", "Overhead strike to the head"),
    ("yokomen uchi", "横面打ち", "Diagonal strike to the side of the head"),
    ("tsuki", "突き", "Straight thrust/punch"),
    ("katate dori", "片手取り", "Single-hand grab"),
    ("morote dori", "諸手取り", "Two-hands-on-one grab"),
    ("ryote dori", "両手取り", "Both wrists grabbed"),
    ("kata dori", "肩取り", "Shoulder grab"),
    ("mune dori", "胸取り", "Chest grab"),
    ("ushiro ryote dori", "後ろ両手取り", "Both wrists grabbed from behind"),
    ("ushiro kubishime", "後ろ首絞め", "Rear choke"),
]

SEED_TECHNIQUES = [
    ("ikkyo", "一教", "First teaching - arm pin"),
    ("nikkyo", "二教", "Second teaching - wrist lock"),
    ("sankyo", "三教", "Third teaching - wrist twist"),
    ("yonkyo", "四教", "Fourth teaching - pressure point"),
    ("gokyo", "五教", "Fifth teaching - arm lock"),
    ("irimi nage", "入身投げ", "Entering throw"),
    ("kote gaeshi", "小手返し", "Wrist turn/reversal"),
    ("shiho nage", "四方投げ", "Four-direction throw"),
    ("tenchi nage", "天地投げ", "Heaven and earth throw"),
    ("kaiten nage", "回転投げ", "Rotary throw"),
    ("kokyu nage", "呼吸投げ", "Breath throw"),
    ("kokyu ho", "呼吸法", "Breath exercise (seated)"),
    ("sumi otoshi", "隅落とし", "Corner drop"),
    ("juji garami nage", "十字搦投げ", "Cross-arm throw"),
]


def get_db() -> sqlite3.Connection:
    conn = sqlite3.connect(str(DB_PATH))
    conn.row_factory = sqlite3.Row
    conn.execute("PRAGMA foreign_keys = ON")
    return conn


def init_db():
    conn = get_db()
    conn.executescript(SCHEMA)
    for name, name_jp, desc in SEED_ATTACKS:
        conn.execute(
            "INSERT OR IGNORE INTO attack_types (name, name_jp, description) VALUES (?, ?, ?)",
            (name, name_jp, desc),
        )
    for name, name_jp, desc in SEED_TECHNIQUES:
        conn.execute(
            "INSERT OR IGNORE INTO techniques (name, name_jp, description) VALUES (?, ?, ?)",
            (name, name_jp, desc),
        )
    conn.commit()
    conn.close()


def get_attack_types():
    conn = get_db()
    rows = conn.execute("SELECT * FROM attack_types ORDER BY name").fetchall()
    conn.close()
    return [dict(r) for r in rows]


def get_techniques():
    conn = get_db()
    rows = conn.execute("SELECT * FROM techniques ORDER BY name").fetchall()
    conn.close()
    return [dict(r) for r in rows]


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


def create_clip(
    source_video_id: int,
    filename: str,
    storage_path: str,
    start_time: float,
    end_time: float,
    attack_type_id: int = None,
    technique_id: int = None,
    energy: str = None,
    notes: str = None,
) -> int:
    duration = end_time - start_time
    conn = get_db()
    cur = conn.execute(
        """INSERT INTO clips
           (source_video_id, filename, storage_path, start_time, end_time,
            duration_seconds, attack_type_id, technique_id, energy, notes)
           VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""",
        (source_video_id, filename, storage_path, start_time, end_time,
         duration, attack_type_id, technique_id, energy, notes),
    )
    clip_id = cur.lastrowid
    conn.commit()
    conn.close()
    return clip_id


def get_clips(attack_type_id: int = None, technique_id: int = None, energy: str = None):
    conn = get_db()
    query = """
        SELECT c.*, a.name as attack_name, a.name_jp as attack_jp,
               t.name as technique_name, t.name_jp as technique_jp
        FROM clips c
        LEFT JOIN attack_types a ON c.attack_type_id = a.id
        LEFT JOIN techniques t ON c.technique_id = t.id
        WHERE 1=1
    """
    params = []
    if attack_type_id:
        query += " AND c.attack_type_id = ?"
        params.append(attack_type_id)
    if technique_id:
        query += " AND c.technique_id = ?"
        params.append(technique_id)
    if energy:
        query += " AND c.energy = ?"
        params.append(energy)
    query += " ORDER BY a.name, t.name, c.energy"
    rows = conn.execute(query, params).fetchall()
    conn.close()
    return [dict(r) for r in rows]


def get_clip_by_id(clip_id: int):
    conn = get_db()
    row = conn.execute(
        """SELECT c.*, a.name as attack_name, a.name_jp as attack_jp,
                  t.name as technique_name, t.name_jp as technique_jp,
                  s.filename as source_filename
           FROM clips c
           LEFT JOIN attack_types a ON c.attack_type_id = a.id
           LEFT JOIN techniques t ON c.technique_id = t.id
           LEFT JOIN source_videos s ON c.source_video_id = s.id
           WHERE c.id = ?""",
        (clip_id,),
    ).fetchone()
    conn.close()
    return dict(row) if row else None


def get_source_video(video_id: int):
    conn = get_db()
    row = conn.execute("SELECT * FROM source_videos WHERE id = ?", (video_id,)).fetchone()
    conn.close()
    return dict(row) if row else None


def get_attack_type_by_name(name: str):
    conn = get_db()
    row = conn.execute(
        "SELECT * FROM attack_types WHERE LOWER(name) = LOWER(?)", (name,)
    ).fetchone()
    conn.close()
    return dict(row) if row else None


def get_technique_by_name(name: str):
    conn = get_db()
    row = conn.execute(
        "SELECT * FROM techniques WHERE LOWER(name) = LOWER(?)", (name,)
    ).fetchone()
    conn.close()
    return dict(row) if row else None
