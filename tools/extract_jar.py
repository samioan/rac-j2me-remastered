#!/usr/bin/env python3
"""Unpack a game's .jar (a plain zip) into <game>/extracted*/, unmodified.

Usage: python tools/extract_jar.py <game>
  <game> is one of: goingmobile, goingmobile-a, goingmobile-a1, clonehome
  (matches roms/RAC-*.jar and the <game>/extracted*/ directory)

goingmobile ships as three different retail builds of the same v1.0.9
MIDlet -- the canonical one plus two alternate dumps that differ at the
byte level (see goingmobile/docs/ROADMAP.md). The canonical build unpacks
to goingmobile/extracted/; the alternates to goingmobile/extracted_a/ and
goingmobile/extracted_a1/ so they can be diffed against it.
"""
import sys
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent

# game key -> (jar in roms/, game dir, extraction subdir)
BUILDS = {
    "goingmobile": ("RAC-GoingMobile.jar", "goingmobile", "extracted"),
    "goingmobile-a": ("RAC-GoingMobile-a.jar", "goingmobile", "extracted_a"),
    "goingmobile-a1": ("RAC-GoingMobile-a1.jar", "goingmobile", "extracted_a1"),
    "clonehome": ("RAC-CloneHome.jar", "clonehome", "extracted"),
}


def extract(game: str) -> None:
    jar_name, game_dir, sub = BUILDS[game]
    jar_path = ROOT / "roms" / jar_name
    out_dir = ROOT / game_dir / sub
    out_dir.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(jar_path) as zf:
        zf.extractall(out_dir)
    print(f"{game}: extracted {jar_name} -> {out_dir.relative_to(ROOT)}")


def main() -> None:
    games = sys.argv[1:] or list(BUILDS)
    for game in games:
        if game not in BUILDS:
            sys.exit(f"unknown game {game!r}, expected one of {list(BUILDS)}")
        extract(game)


if __name__ == "__main__":
    main()
