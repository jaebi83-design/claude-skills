"""Import pre-split video clips into the Aikido Skills database.

Usage:
    python scripts/import_clips.py /path/to/clip.mp4
    python scripts/import_clips.py /path/to/clips/
    python scripts/import_clips.py /path/to/clips/ --attack "shomen uchi" --technique "ikkyo" --energy omote
    python scripts/import_clips.py /path/to/clips/ --dry-run
"""
import argparse
import shutil
import sys
import uuid
from pathlib import Path

# Add project root to path
sys.path.insert(0, str(Path(__file__).resolve().parent.parent))

from app.config import CLIPS_DIR
from app.database import (
    init_db,
    create_source_video,
    create_clip,
    get_attack_type_by_name,
    get_technique_by_name,
)
from app.video_processor import get_video_duration

VIDEO_EXTENSIONS = {".mp4", ".mov", ".avi", ".mkv", ".webm"}


def find_video_files(path: Path) -> list[Path]:
    """Find video files from a path (single file or directory)."""
    if path.is_file():
        if path.suffix.lower() in VIDEO_EXTENSIONS:
            return [path]
        else:
            print(f"Skipping non-video file: {path}")
            return []
    elif path.is_dir():
        files = []
        for f in sorted(path.iterdir()):
            if f.is_file() and f.suffix.lower() in VIDEO_EXTENSIONS:
                files.append(f)
        return files
    else:
        print(f"Path not found: {path}")
        return []


def resolve_attack(name: str) -> int | None:
    """Resolve attack name to ID, or exit with error."""
    if not name:
        return None
    attack = get_attack_type_by_name(name)
    if not attack:
        print(f"Error: Unknown attack type '{name}'")
        print("Valid attacks:")
        from app.database import get_attack_types
        for a in get_attack_types():
            print(f"  - {a['name']}")
        sys.exit(1)
    return attack["id"]


def resolve_technique(name: str) -> int | None:
    """Resolve technique name to ID, or exit with error."""
    if not name:
        return None
    technique = get_technique_by_name(name)
    if not technique:
        print(f"Error: Unknown technique '{name}'")
        print("Valid techniques:")
        from app.database import get_techniques
        for t in get_techniques():
            print(f"  - {t['name']}")
        sys.exit(1)
    return technique["id"]


def import_clip(
    file_path: Path,
    attack_type_id: int | None = None,
    technique_id: int | None = None,
    energy: str | None = None,
    notes: str | None = None,
    dry_run: bool = False,
) -> int | None:
    """Import a single clip file into the system."""
    duration = get_video_duration(file_path)

    if dry_run:
        print(f"  [dry-run] Would import: {file_path.name} ({duration:.1f}s)")
        return None

    # Generate UUID filename for storage
    ext = file_path.suffix or ".mp4"
    clip_filename = f"{uuid.uuid4().hex}{ext}"

    # Copy file to clips directory
    dest = CLIPS_DIR / clip_filename
    shutil.copy2(file_path, dest)

    # Create a placeholder source_video record (for the foreign key)
    source_id = create_source_video(
        filename=file_path.name,
        storage_path=str(file_path),
        duration_seconds=duration,
        notes="Imported via CLI",
    )

    # Create the clip record
    clip_id = create_clip(
        source_video_id=source_id,
        filename=clip_filename,
        storage_path=clip_filename,
        start_time=0.0,
        end_time=duration,
        attack_type_id=attack_type_id,
        technique_id=technique_id,
        energy=energy,
        notes=notes,
    )

    print(f"  Imported: {file_path.name} -> clip #{clip_id} ({duration:.1f}s)")
    return clip_id


def main():
    parser = argparse.ArgumentParser(
        description="Import pre-split video clips into Aikido Skills"
    )
    parser.add_argument(
        "path",
        type=Path,
        help="Video file or directory of video files to import",
    )
    parser.add_argument(
        "--attack",
        type=str,
        default=None,
        help='Attack type name (e.g. "shomen uchi")',
    )
    parser.add_argument(
        "--technique",
        type=str,
        default=None,
        help='Technique name (e.g. "ikkyo")',
    )
    parser.add_argument(
        "--energy",
        type=str,
        choices=["omote", "ura"],
        default=None,
        help="Energy type: omote or ura",
    )
    parser.add_argument(
        "--notes",
        type=str,
        default=None,
        help="Notes to attach to all imported clips",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Show what would be imported without doing it",
    )

    args = parser.parse_args()

    # Initialize database
    init_db()

    # Resolve metadata names to IDs
    attack_type_id = resolve_attack(args.attack)
    technique_id = resolve_technique(args.technique)

    # Find video files
    files = find_video_files(args.path)
    if not files:
        print("No video files found.")
        sys.exit(0)

    print(f"Found {len(files)} video file(s) to import:")
    if args.dry_run:
        print("(DRY RUN - no changes will be made)\n")
    else:
        print()

    imported = 0
    for f in files:
        clip_id = import_clip(
            f,
            attack_type_id=attack_type_id,
            technique_id=technique_id,
            energy=args.energy,
            notes=args.notes,
            dry_run=args.dry_run,
        )
        if clip_id is not None:
            imported += 1

    print()
    if args.dry_run:
        print(f"Would import {len(files)} clip(s).")
    else:
        print(f"Successfully imported {imported} clip(s).")
        print("View them at: http://localhost:8000/")


if __name__ == "__main__":
    main()
