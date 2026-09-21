# rac-j2me-decomp

Decompilation projects for the two *Ratchet & Clank* J2ME (mobile Java)
games: **Going Mobile** (Sony Pictures Mobile, 2005, ported by Metaflow)
and **Clone Home** (the unreleased sequel, dumped as "Ratchet and Clank
2"). Set up following the same overall process as the sibling
`tes-travels-decomp` project -- understand the original game, then build
a from-scratch PC port -- adapted from the *Elder Scrolls Travels* jars
to these two.

## The targets

`roms/*.jar` (kept out of git, see `.gitignore`) -- the original retail
dumps:

| Game | Vendor | Engine | Entry point |
|---|---|---|---|
| Going Mobile | Sony Pictures_Retail_Etty (port by Metaflow) | custom single-canvas engine, Nokia UI `FullCanvas` | `ratchetandclank` |
| Clone Home ("Ratchet and Clank 2", v1.0.22) | Sony Pictures (built with JGTools) | custom `GameCanvas` engine + `RP*` resource banks | `RatchetMIDlet` |

Going Mobile ships as **three different builds of the same v1.0.9
MIDlet**: the canonical one plus two alternate dumps. The `a1` alternate
is a genuinely different build with unobfuscated asset names
(`level*.bin`, `enemy.bin`, `player.bin`, ...) -- it is the Rosetta stone
for the canonical build's obfuscated `n1..n12`/`o`/`p`/`q`/`r` data
files. See `goingmobile/docs/ROADMAP.md` for the full byte-level
comparison.

Like tes-travels-decomp's targets, these are **plain JVM bytecode**, not
native ARM binaries -- a Java decompiler (Vineflower) reconstructs real,
close-to-original Java source directly from the `.class` files. The
obfuscation is minifier-style single/double-letter names only.

## Structure

Each game gets the identical layout (as in tes-travels-decomp):

- `<game>/extracted/` -- the jar's raw contents, unzipped (gitignored,
  regenerate with `tools/extract_jar.py`).
- `<game>/decompiled/` -- Java source recovered by Vineflower (tracked;
  this is the actual decompilation output). Classes are still obfuscated
  to single/double letter names -- renaming them in place as their roles
  are understood is phase 1 of each game's roadmap.
- `<game>/docs/ROADMAP.md`, `<game>/docs/ASSET_FORMATS.md` -- per-game
  status and what's known/unknown about that game's custom binary asset
  formats.
- `<game>/port/` -- the PC port scaffold: CMake + Ninja + MSVC, C++17,
  matching the tes-travels-decomp ports' toolchain exactly (same
  `build.bat` invocation of the VS Build Tools). Currently a placeholder
  Win32 window only, proven to build -- no game logic ported yet.

Going Mobile additionally has `extracted_a/`, `extracted_a1/` and
`decompiled_a/`, `decompiled_a1/` for the two alternate builds (same
layout, suffixed), used for cross-build diffing.

Shared:

- `roms/` -- the original `.jar`/`.jad` files (gitignored -- copyrighted
  game data, never commit).
- `tools/` -- `extract_jar.py`, `decompile.py` (the jar -> Java
  pipeline, see `tools/README.md`), `vineflower.jar` and `midp-stubs/`
  (gitignored, fetched).

## Status

**Phase 0 is done for both games**: jars unpacked (all four builds),
decompiled to readable Java, engines and asset-format families
identified from the loader code (see each game's `docs/`), PC port
toolchain proven working (both `port/` scaffolds configure, build, and
link cleanly with `port/build.bat`).

**Going Mobile's phase 1 is done**: all 9 decompiled classes read
through, understood, and renamed into a compile-checked
`goingmobile/src/` reference tree (zero errors against the real
MIDP/CLDC/Nokia-UI stub jars) -- see `goingmobile/docs/CLASS_MAP.md` and
`goingmobile/src/README.md`. Confirmed along the way: the full level
tile legend, the byte-exact 220-byte save-game layout, the weapon system
(projectile type = `3*weapon + level`; 7 guns + unlockable RYNO), the
128x128 screen, and two phase-0 guesses corrected (`f2.v` is the bitmap
font, `/o` is the menu-definition table).

What's already understood without further RE (see the `ASSET_FORMATS.md`
files for the full write-ups):

- Going Mobile's level tilemaps (`n1..n12`: 28x18-byte grids, tile id =
  byte - 0x20, full tile legend), string tables (`m*.txt`), bitmap font
  (`f2.v`), menu table (`/o`), enemy/player animation tables (`/p`,
  `/r`), enemy geometry (`/q`), save-game layout (RMS record store
  `RANDCSm`), and the manifest's boss-tuning attributes.
- Clone Home's `RP1`/`RP2`/`RP3` container format (parsed from
  `a.java`'s own loader), its embedded-PNG and UTF-8 string-table
  resource types, and the complete 218-byte save-game layout from
  `RatchetMIDlet`'s serializer.

**Clone Home's phase 1 (read-through and renaming) has not started.**
Suggested starting point: `RatchetMIDlet.java` (small, already readable)
-> `a.java` (the engine base + resource loader) -> `b.java`. Going
Mobile's phase 1 -- including the cross-build comparison that settled
every open member (`docs/BUILD_COMPARISON.md`; the `(a)` build is a
128x160 port of the same revision, a1 is a newer trial build with the
full class map recovered) -- is complete.

## Building the ports

```
goingmobile\port\build.bat
clonehome\port\build.bat
```

each configure (CMake + Ninja) and build a placeholder Win32 window
against the VS Build Tools toolchain -- proof the scaffold works, no
game logic yet.
