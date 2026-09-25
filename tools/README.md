# tools/

Shared tooling for both games. Nothing here is project source -- it's
either fetched (`vineflower.jar`, `midp-stubs/`) or regenerates output
already tracked elsewhere in the repo, so only the scripts themselves are
tracked (see `.gitignore`).

- **`vineflower.jar`** -- [Vineflower](https://github.com/Vineflower/vineflower)
  1.12.0, a maintained fork of Fernflower. Not tracked in git (~1.6MB
  binary, trivially refetched). Get it with:
  ```
  curl -L -o tools/vineflower.jar https://github.com/Vineflower/vineflower/releases/download/1.12.0/vineflower-1.12.0.jar
  ```
- **`midp-stubs/`** -- stub jars for the real MIDP-2.0/CLDC-1.1/Nokia-UI
  APIs (`midpapi20.jar`, `cldcapi11.jar`, `nokiaui.jar`), for compile-
  checking renamed phase-1 source trees the same way tes-travels-decomp
  does. Not tracked in git.
- **`extract_jar.py`** -- unpacks `roms/RAC-*.jar` (a plain zip) into
  `<game>/extracted*/`, unmodified. Run with no args to do all four
  builds (goingmobile + its two alternate dumps, clonehome).
- **`decompile.py`** -- runs Vineflower over `<game>/extracted*/` and
  writes Java source to `<game>/decompiled*/`. Needs a Java 17+
  (`find_java()` tries `$JAVA_HOME`, then `java` on PATH, then installed
  Temurin JDKs -- the PATH java on this machine is 1.8, too old for
  Vineflower 1.12). Run with no args to do all four builds.
- **`rename_gm.py`** -- goingmobile phase 1: renames
  `goingmobile/decompiled/` into the compile-checked
  `goingmobile/src/` reference tree (class-level mapping plus confirmed
  member renames, the mapping tables live in the script; see
  `goingmobile/docs/CLASS_MAP.md`). Regenerates `src/` except the three
  hand-written files (`Entity.java`, `MenuItem.java`, `SoundPlayer.java`).
- **`rename_ch.py`** (+ **`ch_rename/ChRenamer.java`**) -- clonehome phase 1:
  renames `clonehome/extracted/` into the compile-checked `clonehome/src/`
  by running Vineflower with a custom `--user-renamer-class` that reads
  `clonehome/docs/names.map`. Unlike `rename_gm.py` this rewrites the class
  files' symbol tables, so single-letter names reused across classes cannot
  collide and every reference (including inherited members) is correct.
  Applies a few textual patches for decompiler artifacts, then compiles the
  result against `midp-stubs/`. Regenerates `src/` entirely; edit `names.map`,
  not `src/`.
- **`parse_ch.py`** -- clonehome phase 2: parses the `RP*` banks and all 30 sprite
  atlases, asserting exact byte consumption and in-bounds rectangles. Output to
  stdout only (derived data, never commit).
- **`parse_gm.py`** -- goingmobile phase 2: parsers/validators for every
  custom asset format of the canonical build (tilemaps, menu table,
  bitmap font with ASCII rendering, animation/geometry tables). Each one
  replicates its loader byte-for-byte and asserts exact byte
  consumption; `all` runs everything. Output goes to stdout (derived
  from copyrighted game data -- never commit it).

## Why Vineflower and not Ghidra

Both games are J2ME MIDlets: plain JVM class files (`MIDP-2.0`
manifests, one `CLDC-1.0`, one `CLDC-1.1`). A Java decompiler reconstructs
real, close-to-original Java directly -- there is no ARM/native step at
all. The obfuscation here is just minifier-style single/double-letter
class and member names (`a.class`, `b.class`, ...); the actual control
flow and types come back intact.

```
python tools/extract_jar.py
python tools/decompile.py
```

regenerates `<game>/extracted*/` and `<game>/decompiled*/` for all four
builds from the `roms/*.jar` files.
