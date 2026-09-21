#!/usr/bin/env python3
"""Decompile a game's extracted .class files to Java source with Vineflower.

Run tools/extract_jar.py first. This writes <game>/decompiled*/**/*.java and
deletes everything Vineflower also copies over from the input directory
(images, .mid/.wav sounds, level .bin data, ...) since that's a duplicate of
<game>/extracted*/ -- decompiled*/ should contain source only.

Usage: python tools/decompile.py <game>
  <game> is one of: goingmobile, goingmobile-a, goingmobile-a1, clonehome

Vineflower 1.12.0 requires Java 17+. find_java() tries $JAVA_HOME, then
`java` on PATH, then any installed Temurin JDK (the usual Adoptium install
location) -- the PATH java on this machine is 1.8, too old to load it.
"""
import os
import re
import shutil
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
VINEFLOWER = ROOT / "tools" / "vineflower.jar"

# game key -> (game dir, extraction subdir, decompile output subdir),
# matching tools/extract_jar.py's BUILDS.
BUILDS = {
    "goingmobile": ("goingmobile", "extracted", "decompiled"),
    "goingmobile-a": ("goingmobile", "extracted_a", "decompiled_a"),
    "goingmobile-a1": ("goingmobile", "extracted_a1", "decompiled_a1"),
    "clonehome": ("clonehome", "extracted", "decompiled"),
}


def _java_major(exe: str) -> int:
    """Major Java version of `exe` (8 for 1.8.x, 21 for 21.x), 0 if unknown."""
    try:
        out = subprocess.run(
            [exe, "-version"], capture_output=True, text=True, check=True
        )
    except (OSError, subprocess.CalledProcessError):
        return 0
    m = re.search(r'version "([^"]+)"', out.stderr + out.stdout)
    if not m:
        return 0
    parts = m.group(1).split(".")
    if parts[0] == "1":  # legacy scheme: 1.8.0_503 -> 8
        return int(parts[1]) if len(parts) > 1 else 0
    return int(parts[0])


def find_java() -> str:
    """A Java 17+ executable: $JAVA_HOME, then PATH, then Temurin installs."""
    candidates: list[str] = []
    java_home = os.environ.get("JAVA_HOME")
    if java_home:
        candidates.append(str(Path(java_home) / "bin" / "java.exe"))
    candidates.append("java")
    adoptium = Path("C:/Program Files/Eclipse Adoptium")
    if adoptium.exists():
        candidates += sorted(str(p) for p in adoptium.glob("jdk-*/bin/java.exe"))
    for exe in candidates:
        if _java_major(exe) >= 17:
            return exe
    sys.exit(
        "no Java 17+ found (tried $JAVA_HOME, PATH, and installed Temurin JDKs) "
        "-- Vineflower 1.12.0 needs it. Install one from https://adoptium.net/"
    )


def decompile(game: str, java_exe: str) -> None:
    game_dir, src_sub, out_sub = BUILDS[game]
    src = ROOT / game_dir / src_sub
    if not src.exists():
        sys.exit(f"{src} does not exist -- run tools/extract_jar.py {game} first")
    out = ROOT / game_dir / out_sub
    if out.exists():
        shutil.rmtree(out)
    out.mkdir(parents=True)

    subprocess.run(
        [java_exe, "-jar", str(VINEFLOWER), "-log=WARN", str(src), str(out)],
        check=True,
    )

    # Vineflower mirrors non-.class input files into the output tree too;
    # strip everything but the decompiled sources.
    for path in sorted(out.rglob("*")):
        if path.is_file() and path.suffix != ".java":
            path.unlink()
    for path in sorted(out.rglob("*"), reverse=True):
        if path.is_dir() and not any(path.iterdir()):
            path.rmdir()

    n = sum(1 for _ in out.rglob("*.java"))
    print(f"{game}: decompiled {n} source files -> {out.relative_to(ROOT)}")


def main() -> None:
    if not VINEFLOWER.exists():
        sys.exit(
            f"{VINEFLOWER} not found -- download it from "
            "https://github.com/Vineflower/vineflower/releases (vineflower-<ver>.jar)"
        )
    games = sys.argv[1:] or list(BUILDS)
    java_exe = find_java()
    for game in games:
        if game not in BUILDS:
            sys.exit(f"unknown game {game!r}, expected one of {list(BUILDS)}")
        decompile(game, java_exe)


if __name__ == "__main__":
    main()
