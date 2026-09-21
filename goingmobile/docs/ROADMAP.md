# Going Mobile -- roadmap

## The target

`roms/RAC-GoingMobile.jar` -- a J2ME MIDlet (`MIDP-2.0`/`CLDC-1.0`),
MIDlet-Vendor `Sony Pictures_Retail_Etty`, MIDlet-Name "Ratchet and Clank
Going Mobile", `Created-By: Metaflow` (Metaflow did the actual port; the
game is Sony Pictures Mobile's 2005 mobile *Ratchet & Clank*). Entry point
`ratchetandclank` (`MIDlet-1: Ratchet and Clank Going Mobile, /icon.png,
ratchetandclank`). The manifest also carries three tuning attributes the
MIDlet reads at startup via `getAppProperty` in
`ratchetandclank.startApp()` (`decompiled/ratchetandclank.java`):
`BOSS_HIT_POINT_SHELL=35`, `BOSS_HIT_POINT_KERNEL=100`, `BOSS_FIRE_RATE=22`
-- stored straight into `f.f`/`f.g`/`f.h`.

The engine is a single big obfuscated class plus helpers:

| Class | Role (evidence) |
|---|---|
| `ratchetandclank` | MIDlet; owns RMS save handling (record store `RANDCSm`: 3 x 220-byte save slots + one 3-byte settings record), the `.txt` string-table loaders, and a `fillRect`-based bitmap font renderer |
| `f` (127KB source) | the game: `extends com.nokia.mid.ui.FullCanvas implements Runnable` -- renderer, input, main loop, level flow; loads `/o` on every level start |
| `a` (40KB) | entity/actor class (`extends h`), with baked-in enemy/weapon tuning tables; loads `/r`; Nokia `DirectUtils` pixel manipulation |
| `c` (20KB) | level tilemaps: reads `/n<level>` into `byte[12][28][18]` (28x18 cells, one byte per cell, tile id = raw byte - 0x20) and the tile renderer (22px columns, 14px rows) |
| `d` (21KB) | sprite/animation data: `extends h`, loads `/p` and `/q` |
| `g`, `b`, `e`, `h` | smaller helpers; `h` is the entity base class |

It's a 2D side-scrolling platformer/shooter (scrolling 28x18 tile grids,
bolts, weapon store, boss) with Nokia-UI-specific rendering
(`com.nokia.mid.ui.DirectGraphics`/`DirectUtils`/`FullCanvas`) -- a PC port
needs a MIDP+Nokia-UI shim layer or a straight C++ reimplementation.

## The three builds

`roms/` holds **three different retail builds of the same v1.0.9 MIDlet**
(all dumped as separate ROMs, hence the `(a)`/`(a1)` suffixes in the
original filenames):

| Build | Jar | Layout | Notes |
|---|---|---|---|
| canonical | `RAC-GoingMobile.jar` | 69 entries, 9 classes (`a`..`h`, `ratchetandclank`) | `.mid` sounds, obfuscated asset names (`n1`..`n12`, `o`, `p`, `q`, `r`, `t`, `f2.v`) |
| alt-a | `RAC-GoingMobile-a.jar` | same 69 entries, same names | 10 files differ from canonical (manifest, 8 class files, `icon.png`, `a.png`); the other 59 -- all level data `n1`..`n12`, all sounds, all string tables, `f2.v` -- are **byte-identical**. Same code recompiled (near-identical class sizes), different obfuscation/build. |
| alt-a1 | `RAC-GoingMobile-a1.jar` | 85 entries, 12 classes | a genuinely different build: **unobfuscated asset names** (`level0`..`level12.bin`, `enemy.bin`, `player.bin`, `mapData.txt`, `txt_*.txt`, per-thing `.png` sprites), `.wav` sounds (MIDP-2.0) instead of some `.mid`s, an extra `f3.v` video, and the engine split differently (`h.java` 195KB is the engine, not `f.java`). `f2.v`, `menu.mid` and `icon.png` are byte-identical to the canonical build's. |

The canonical build is the decompilation target (`extracted/`,
`decompiled/`); the other two unpack to `extracted_a/`, `extracted_a1/` and
decompile to `decompiled_a/`, `decompiled_a1/` for cross-build diffing.
This matters: **a1's readable asset names are the key to the canonical
build's obfuscated ones** -- e.g. canonical `/q` (40 bytes) is
byte-identical to a1's `enemy_spr_box.bin`, canonical `/m.txt` strings
match a1's `txt_en.txt`, so each `n*`/`o`/`p`/`r` file can be identified by
content-matching against a1's named equivalents (see
[`ASSET_FORMATS.md`](ASSET_FORMATS.md)).

## Status

**Phase 0 (this setup) is done:** all three jars unpacked, decompiled to
readable-but-unrenamed Java via [`../../tools/decompile.py`](../../tools/decompile.py)
(9 + 9 + 11 source files). Clean recovery -- obfuscation is
minifier-style single-letter names only, control flow and types intact.

**Phase 1 (not started): read through and rename**, starting from
`ratchetandclank.java` -> `f.java` (the engine), cross-checking against
`decompiled_a1/h.java` (same engine, different obfuscation pass, so
matching method bodies between the two recovers each build's mapping for
free) and `decompiled_a/` (same code as canonical, different
obfuscation -- a pure diff target).

**Phase 2 (partially done from phase-0 read-through, see
[`ASSET_FORMATS.md`](ASSET_FORMATS.md)):** the level tilemap format
(`n1`..`n12`), string tables (`m*.txt`, `about/help/credits*.txt`) and the
save-game layout (RMS records) are already understood from the loader
code; `f2.v` (the intro video format), `o`, `p`, `r` and the a1-only
formats (`mapData.txt`, `enemy.bin`, `player.bin`) still need their
parsers read through. The a1 build's named files make the confirmations
much easier.

**Phase 3 (not started): PC port.** Scaffold is in `port/` (CMake + Ninja
+ MSVC, matching the `tes-travels-decomp` ports' toolchain) -- currently
just proves the build works, no game logic yet. Port strategy: behavioral
reimplementation in C++, not byte-exact recompilation; the Nokia-UI
full-screen canvas maps to a plain Win32 software-rendered window, the
`.mid`/`.wav` soundtrack needs an audio backend decision.
