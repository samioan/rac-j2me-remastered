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

The engine is a single big obfuscated class plus helpers (roles confirmed
by the phase-1 read-through; full write-up in [`CLASS_MAP.md`](CLASS_MAP.md)):

| Class | Role (confirmed) |
|---|---|
| `ratchetandclank` | MIDlet; owns RMS save handling (record store `RANDCSm`: 3 x 220-byte save slots + one 3-byte settings record), the `.txt` string-table loaders, the word-wrap reader, and the `f2.v` bitmap font (a `fillRect`-based glyph renderer) |
| `f` (127KB source) | the game: `extends com.nokia.mid.ui.FullCanvas implements Runnable` -- 128x128 screen, tick loop via `callSerially`, renderer, input, menus, scoring, boss fight, save/load; owns the player/enemy/projectile pools |
| `a` (40KB) | **the player** (Ratchet): platformer physics, weapons (projectile type = `3*weapon + level`), melee, ladders/ledges/zip lines/swings, animation from `/r`; loads `/r` |
| `c` (20KB) | level tilemaps: reads `/n<level>` into `byte[12][28][18]` (28x18 cells, one byte per cell, tile id = raw byte - 0x20), the full tile legend, and the tile renderer (22px columns, 14px rows) |
| `d` (21KB) | **the enemies**: 5-slot pool, 5 types, per-type animation tables from `/p` and hitbox/geometry tables from `/q`; walk/fly/wall-crawl AI |
| `g` (17KB) | **projectiles**: both pools (player + enemy shots, 10 each), 33 types with per-type speed/size/damage tables, rotated rendering |
| `b`, `e`, `h` | the MMAPI sound player, one menu row, and the Player/Enemy base class |

It's a 2D side-scrolling platformer/shooter (scrolling 28x18 tile grids,
616x252 world vs 128x128 view, bolts, weapon store with 7 guns + RYNO,
boss level) with Nokia-UI-specific rendering
(`com.nokia.mid.ui.DirectGraphics`/`DirectUtils`/`FullCanvas`) -- a PC port
needs a MIDP+Nokia-UI shim layer or a straight C++ reimplementation.

## The three builds

`roms/` holds **three different retail builds of the same v1.0.9 MIDlet**
(all dumped as separate ROMs, hence the `(a)`/`(a1)` suffixes in the
original filenames):

| Build | Jar | Layout | Notes |
|---|---|---|---|
| canonical | `RAC-GoingMobile.jar` | 69 entries, 9 classes (`a`..`h`, `ratchetandclank`) | `.mid` sounds, obfuscated asset names (`n1`..`n12`, `o`, `p`, `q`, `r`, `t`, `f2.v`) |
| alt-a | `RAC-GoingMobile-a.jar` | same 69 entries, same names | **A 128x160 screen-port of the same revision**: vertical constants retargeted (`-124`->`-92` camera clamp, `108`->`140` play area), no multi-tap name-entry feature; the other 59 files -- all level data `n1`..`n12`, sounds, string tables, `f2.v` -- are **byte-identical**. |
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

**Phase 1 is done for the canonical build**: all 9 classes read through,
renamed into a compile-checked [`../src/`](../src/) reference tree (zero
errors against the real MIDP/CLDC/Nokia-UI stub jars), with the complete
class/member mapping, everything that kept an obfuscated name and why, and
the open questions in [`CLASS_MAP.md`](CLASS_MAP.md). Notable phase-1
findings beyond the initial read: `f2.v` is the **bitmap font** (not a
video), `/o` is the **menu-definition table**, the save record layout is
now byte-exact (see `ASSET_FORMATS.md`), and the engine runs on a
128x128 screen.

**The phase-1 cross-build follow-up is done too** (see
[`BUILD_COMPARISON.md`](BUILD_COMPARISON.md)): the `(a)` build proved to be
a **128x160 screen-port of the same revision** (no name-entry feature, all
game data byte-identical), and the a1 build mapped onto the canonical
class names (it's a newer, restructured MIDP-2.0 trial version with an
unlock-code registration UI -- `a` = the font, `b` = Player, `c` =
SoundPlayer, `d` = LevelMap, `e` = canvas shell, `f` = Enemy, `g` =
intro/unlock manager, `h` = Game, `i` = Entity, `j` = Projectile). The
last open members were settled by it: `bk`/`bl` are the infolink
("Level N unlocked") tables, `Player.q` is the post-ladder-jump grace
timer, and the `t` asset is dead data.

**Phase 2 is done.** Every canonical-build asset format is confirmed
*and tool-verified*: `tools/parse_gm.py` replicates each loader
byte-for-byte and all validations pass (exact consumption for every
file, the font rendering readable text, the menu tree resolving every
string, all 11 level maps with zero unexplained tiles) -- see
[`ASSET_FORMATS.md`](ASSET_FORMATS.md). Two finds from writing the
parsers: the shipped level set is `n1..n10` + `n12` (no n11 -- the level
select maps 11 to the boss, so the 12-grid read is dead code), and the
original's special-tile logic has a latent out-of-bounds that the shipped
levels never trigger (the port must bounds-check).

**Phase 3 (in progress): PC port.** Tracked milestone-by-milestone in
[`PORT_ROADMAP.md`](PORT_ROADMAP.md), separately from this file. Milestone
3.1 (boot/splash/menu/save surface -- MIDlet lifecycle, RMS saves, the
string/font pipeline, every menu screen, the whole `game.h`/`.cpp` boot
chain) is done and builds clean; gameplay itself (milestone 3.2 -- level
loading, player/enemy/projectile physics and AI, the boss fight) is stubbed
and not started. Port strategy: behavioral reimplementation in C++, not
byte-exact recompilation; the Nokia-UI full-screen canvas maps to a plain
Win32 software-rendered window (raw GDI `StretchDIBits`, no engine
dependency), the `.mid` soundtrack plays through MCI.
