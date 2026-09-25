#!/usr/bin/env python3
"""Clone Home phase 1: rename the decompilation into clonehome/src/.

Unlike tools/rename_gm.py (regex over single-letter identifiers), this renames
at the class-file level: Vineflower's --user-renamer-class hook is given
tools/ch_rename/ChRenamer.java, which reads clonehome/docs/names.map, so every
reference -- including inherited members reached through a subclass, and the
same single letter reused in different classes -- is rewritten correctly.

    python tools/rename_ch.py            # regenerate clonehome/src/ and compile-check it
    python tools/rename_ch.py --no-check # skip the javac check

Steps: compile ChRenamer, run Vineflower over clonehome/extracted/ with the
map, apply PATCHES (decompiler artifacts that do not compile), write
clonehome/src/*.java, then compile them against the MIDP/CLDC/Nokia-UI stub
jars in tools/midp-stubs/. The compiler is the final arbiter: src/ must build
with zero errors.

names.map format (one entry per line, '#' comments):
    class  <old> <New>
    field  <oldClass>.<oldField> <newName>
    method <oldClass>.<oldMethod>(<paramTypes>) <newName>
paramTypes are Java-style, comma separated, package-free, with obfuscated
class names left as they are: (int,byte[]) (Graphics,f[],int) ().
"""
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from decompile import VINEFLOWER, find_java  # noqa: E402

ROOT = Path(__file__).resolve().parent.parent
GAME = ROOT / "clonehome"
EXTRACTED = GAME / "extracted"
MAP = GAME / "docs" / "names.map"
OUT = GAME / "src"
RENAMER_SRC = ROOT / "tools" / "ch_rename" / "ChRenamer.java"
STUBS = ROOT / "tools" / "midp-stubs"

# Decompiler artifacts that are not valid Java, fixed textually after the
# rename. (file, old, new); every entry must match exactly once.
PATCHES: list[tuple[str, str, str]] = [
    # Vineflower mistypes the byte[] in Game.readByteArray as an unknown class `B`.
    ("Game.java", "B var1 = null;", "byte[] var1 = null;"),
    ("Game.java", "((Object[])var1)[var2] = bB.readByte();", "var1[var2] = bB.readByte();"),
    ("Game.java", "return (byte[])var1;", "return var1;"),
    # Vineflower types iinc'd int locals as byte when they start from a small constant; these
    # step past 127 (wrapping the menu backdrop and the code-entry row), so they are ints.
    ("Game.java", "for (byte var8 = -16; var3 < 12; var8 += 28) {", "for (int var8 = -16; var3 < 12; var8 += 28) {"),
    ("Game.java", "         byte var4 = -6;", "         int var4 = -6;"),
    ("Game.java", "      byte var4 = 51;", "      int var4 = 51;"),
]


def run(cmd, **kw):
    return subprocess.run([str(c) for c in cmd], check=True, **kw)


def main() -> None:
    check = "--no-check" not in sys.argv
    java = Path(find_java())
    javac = java.with_name("javac.exe" if java.suffix == ".exe" else "javac")

    with tempfile.TemporaryDirectory(prefix="ch_rename_") as tmp:
        tmp = Path(tmp)
        cls_dir, out_dir = tmp / "cls", tmp / "out"
        cls_dir.mkdir()
        out_dir.mkdir()

        run([javac, "-d", cls_dir, "-cp", VINEFLOWER, RENAMER_SRC])
        sep = ";" if sys.platform == "win32" else ":"
        run([
            java, f"-Dch.map={MAP}", "-cp", f"{VINEFLOWER}{sep}{cls_dir}",
            "org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler",
            "-log=WARN", "-ren=1", "-urc=ChRenamer",
            EXTRACTED, out_dir,
        ])

        sources = sorted(out_dir.glob("*.java"))
        if not sources:
            sys.exit("Vineflower produced no sources")

        if OUT.exists():
            shutil.rmtree(OUT)
        OUT.mkdir()
        texts = {p.name: p.read_text(encoding="utf-8") for p in sources}
        for fname, old, new in PATCHES:
            if texts[fname].count(old) != 1:
                sys.exit(f"patch does not match exactly once in {fname}: {old[:60]!r}")
            texts[fname] = texts[fname].replace(old, new)
        for name, text in texts.items():
            (OUT / name).write_text(text, encoding="utf-8", newline="\n")
        print(f"wrote {len(texts)} files -> {OUT.relative_to(ROOT)}")

        if check:
            classpath = sep.join(str(j) for j in sorted(STUBS.glob("*.jar")))
            build = tmp / "build"
            build.mkdir()
            run([javac, "-nowarn", "-proc:none", "-encoding", "UTF-8",
                 "--release", "8", "-cp", classpath, "-d", build,
                 *sorted(OUT.glob("*.java"))])
            print("compile check: OK (0 errors)")


if __name__ == "__main__":
    main()
