# Going Mobile -- roadmap

> **Target changed 2026-09-22**: the canonical decompilation/port target is
> now `roms/RAC-GoingMobile-a1.jar` (176x220, the higher-resolution build
> with the fully-featured `Canvas`+`Commands` menu/unlock UI), not the
> original `RAC-GoingMobile.jar`. Phases 0-3.1 below were all done against
> the *original* jar before this change; that work (`extracted/`,
> `decompiled/`, `src/`, `CLASS_MAP.md`'s canonical section, and the
> `port/` C++ tree through milestone 3.1) is kept as a **reference build**
> -- it shares enough logic with a1 (same physics/tile/font conventions,
> confirmed by `BUILD_COMPARISON.md`) to be a pattern library for the a1
> read-through, but it is no longer what the port ships. See "The target"
> and "Status" below for the reset phase plan.

## The target

`roms/RAC-GoingMobile-a1.jar` -- a J2ME MIDlet (`MIDP-2.0`/`CLDC-1.0`),
MIDlet-Vendor `Sony Pictures_Retail_Etty`, MIDlet-Name "Ratchet and Clank
Going Mobile", `Created-By: Metaflow`, `MIDlet-Version: 1.0.9` (same
retail version string as the other two builds; this is a distinct,
newer revision -- see "The three builds"). Entry point `ratchetandclank`
(`MIDlet-1: Ratchet and Clank Going Mobile, /icon.png, ratchetandclank`).
Manifest carries the same three boss-tuning attributes as the other builds
(`BOSS_HIT_POINT_SHELL=35`, `BOSS_HIT_POINT_KERNEL=100`,
`BOSS_FIRE_RATE=22`) plus `MIDlet-Spec-Code: true`; it does **not** carry
an `Unlock-Code` attribute, so `g`'s unlock-code check
(`getAppProperty("Unlock-Code")`, `decompiled_a1/g.java:76`) reads null in
this shipped jar -- the trial/registration UI exists in the code but has
no code configured to check against in this build.

Screen is **176x220** (`h.java`'s `setClip(0, H, 176, 220-H)` etc., vs the
original canonical build's 128x128 and the `(a)` build's 128x160) -- a
Series 60/UIQ-class handset, not a Series 40 one. The engine drops Nokia's
`FullCanvas` for a plain MIDP `Canvas implements CommandListener`, i.e. a
real soft-key/Commands menu system instead of the original's fully custom
`keyPressed` menu state machine, though rendering still leans on
`com.nokia.mid.ui.DirectGraphics`/`DirectUtils` for pixel ops.

The engine is 12 obfuscated classes plus the MIDlet (roles cross-mapped
onto the original build's confirmed names in
[`BUILD_COMPARISON.md`](BUILD_COMPARISON.md); full phase-1 member-level
read-through for a1 not yet done -- tracked in "Status" below):

| Class | Size | Role (cross-mapped, not yet member-verified) |
|---|---|---|
| `ratchetandclank` | 10KB | the MIDlet; RMS saves, `getAppProperty`, `GameIsWon` |
| `a` | 5KB | the bitmap font (standalone class here, not MIDlet-embedded); loads `/f2.v` + `/f3.v` |
| `b` | 41KB | **the player** (`extends i`); loads `/player.bin` |
| `c` | 4KB | the MMAPI sound player |
| `d` | 18KB | level tilemaps; loads `/level<N>` (13 levels) + `mapData.txt` |
| `e` | 4KB | the canvas shell: `extends Canvas implements Runnable, CommandListener` |
| `f` | 22KB | **the enemies** (`extends i`); loads `/enemy.bin` + `/enemy_spr_box.bin` |
| `g` | 58KB | intro/splash manager **plus** the unlock-code registration UI (`Form`/`TextField`) |
| `h` | **195KB** | **the engine** (6600+ lines): tick loop, player/enemy/projectile pools, `mapData.txt` world map, fonts, menus, boss fight, save/load |
| `i` | <1KB | Player/Enemy base class (position, health, animation state) |
| `j` | 15KB | **projectiles**: two 10-slot pools, 33 types (evolved tuning vs. the original build) |

Content is a superset of the original build: 13 levels (`level0`-`level12`)
vs. 12, `.wav` sound variants alongside `.mid`, an extra `f3.v` video/font
asset, `enemy.bin`/`player.bin` state-script data (replacing some of the
original's inline tables), and unobfuscated resource names throughout
(`level0.bin`, `mapData.txt`, `txt_en.txt`, per-thing `.png`s) -- this is
also why a1 was the key to identifying the original build's obfuscated
asset names in the first place (see "The three builds").

It's the same 2D side-scrolling platformer/shooter (bolts, weapon store
with 7 guns + RYNO, boss level) as the original build, at higher
resolution and with a real MIDP menu UI -- the PC port target is
unchanged in kind (a MIDP+Nokia-UI shim or straight C++ reimplementation),
just re-based on this build's screen size, classes, and asset formats.

## The three builds

`roms/` holds **three different retail builds of the same v1.0.9 MIDlet**
(all dumped as separate ROMs, hence the `(a)`/`(a1)` suffixes in the
original filenames):

| Build | Jar | Layout | Notes |
|---|---|---|---|
| **target (a1)** | `RAC-GoingMobile-a1.jar` | 85 entries, 12 classes | **The current decompilation/port target** (176x220, MIDP `Canvas`+`Commands` UI). A genuinely different, newer build: **unobfuscated asset names** (`level0`..`level12.bin`, `enemy.bin`, `player.bin`, `mapData.txt`, `txt_*.txt`, per-thing `.png` sprites), `.wav` sounds (MIDP-2.0) instead of some `.mid`s, an extra `f3.v` video, and the engine split differently (`h.java` 195KB is the engine, not `f.java`). `f2.v`, `menu.mid` and `icon.png` are byte-identical to the original build's. |
| legacy/reference | `RAC-GoingMobile.jar` | 69 entries, 9 classes (`a`..`h`, `ratchetandclank`) | **The former target** (128x128, custom `FullCanvas` menu). `.mid` sounds, obfuscated asset names (`n1`..`n12`, `o`, `p`, `q`, `r`, `t`, `f2.v`). Fully decompiled/ported through phase 3.1 (see "Status") before the target changed; kept as a cross-reference build -- same physics/tile/font conventions as a1, confirmed by `BUILD_COMPARISON.md`. |
| alt-a | `RAC-GoingMobile-a.jar` | same 69 entries, same names as the legacy build | **A 128x160 screen-port of the legacy build's revision**: vertical constants retargeted (`-124`->`-92` camera clamp, `108`->`140` play area), no multi-tap name-entry feature; the other 59 files -- all level data `n1`..`n12`, sounds, string tables, `f2.v` -- are **byte-identical** to the legacy build. Not related to a1's revision; kept only as a secondary cross-reference. |

All three jars are unpacked/decompiled: the target build's raw decompile
lives in `extracted_a1/`/`decompiled_a1/` (naming kept from before the
target change, to avoid a disruptive repo-wide rename -- **`_a1` marks the
current target**, not an alternate); the legacy build's in `extracted/`/
`decompiled/`; the `(a)` build's in `extracted_a/`/`decompiled_a/`. This
matters both ways: **a1's readable asset names were the key to unlocking
the legacy build's obfuscated ones** during that build's phase 2 (e.g.
legacy `/q` is byte-identical to a1's `enemy_spr_box.bin`, legacy `/m.txt`
strings match a1's `txt_en.txt` -- see [`ASSET_FORMATS.md`](ASSET_FORMATS.md)),
and now the legacy build's already-confirmed logic (renamed in
[`CLASS_MAP.md`](CLASS_MAP.md)'s canonical section) is the reading aid for
a1's phase 1, since large parts of the two builds share behavior even
though the code was re-obfuscated and restructured between them.

## Status

**Phase 0 is done for all three builds:** all three jars unpacked,
decompiled to readable-but-unrenamed Java via
[`../../tools/decompile.py`](../../tools/decompile.py) (9 + 9 + 11 source
files). Clean recovery -- obfuscation is minifier-style single-letter
names only, control flow and types intact.

**Phase 1 is done for both the legacy build and a1.** The legacy build's
all 9 classes were read through, renamed into a compile-checked
[`../src/`](../src/) reference tree (zero errors against the real
MIDP/CLDC/Nokia-UI stub jars), with the complete class/member mapping in
[`CLASS_MAP.md`](CLASS_MAP.md). a1's cross-build comparison (see
[`BUILD_COMPARISON.md`](BUILD_COMPARISON.md)) settled a **class-level**
mapping (`a` = font, `b` = Player, `c` = SoundPlayer, `d` = LevelMap, `e`
= canvas shell, `f` = Enemy, `g` = intro/unlock manager, `h` = Game/
engine, `i` = Entity, `j` = Projectile), and a1's own member-level
read-through is now done too: all 11 classes renamed into
[`../src_a1/`](../src_a1/) and **now compile clean** with zero errors
against the real MIDP/CLDC/Nokia-UI stub jars (see `src_a1/README.md`),
with the complete mapping in `CLASS_MAP.md`'s "a1 class map" section.
Getting the compile check to pass (it had never actually been run until
milestone 3.1a's prerequisites were checked) surfaced 1479 errors, mostly
the already-documented cross-file staleness plus four new bug classes
fixed along the way -- see `CLASS_MAP.md`'s "Cross-file consistency pass"
section. `Game` (the 195KB engine) and
`IntroManager` (58KB) were done via scripted substitution rather than
full hand-transcription given their size, with a documented set of
fields/methods deliberately left obfuscated on both (same treatment the
legacy build gave its own `Game`); the cross-file follow-ups this raised
(`CanvasShell.java`/`SoundPlayer.java` call sites using pre-rename names,
and more) were resolved by the compile-check pass above rather than left
open -- the remaining open item is `ratchetandclank.java` still having a
few of its own members undocumented (surfaced by `Game`'s read-through,
tracked in `CLASS_MAP.md`).

**Phase 2 is done for both the legacy build and a1.** Every legacy-build
asset format is confirmed *and tool-verified*: `tools/parse_gm.py`
replicates each loader byte-for-byte and all validations pass -- see
[`ASSET_FORMATS.md`](ASSET_FORMATS.md). a1's own formats (different from
the legacy build's: `enemy.bin`/`player.bin` state scripts, the
world-map `mapData.txt`, the 13-level multi-grid `level0.bin`..
`level12.bin` set, and the second bitmap font `f3.v`) are now confirmed
and tool-verified the same way, by `tools/parse_gm_a1.py` against
`extracted_a1/` -- including one new finding, a level-11-specific
read-past-end-of-file edge case (a cousin of the legacy build's already-
documented "n11 doesn't exist" finding), folded into `ASSET_FORMATS.md`.

[`PORT_ROADMAP.md`](PORT_ROADMAP.md), separately from this file. Milestone
3.1 (boot/splash/menu/save surface -- MIDlet lifecycle, RMS saves, the
string/font pipeline, every menu screen, the whole `game.h`/`.cpp` boot
chain) is done and builds clean against the legacy build's data.
Milestone **3.1a** (re-basing that slice onto a1, now that phase 1/2 are
both done for a1) is in progress: a new `port/src_a1/` tree/CMake target
builds clean at a1's real screen size (176x220) with a real MIDlet boot
(RMS saves confirmed 214 bytes/slot, not the legacy build's 220), `Font`,
`CanvasShell`'s `Canvas`+`CommandListener` input dispatch, `Game`'s full
42-step boot chain (asset loading plus constructing real `Player`/`Enemy`/
`LevelMap`/`Projectile` objects -- data tables and asset loaders real,
behavior still stubbed, same precedent milestone 3.1 set for the legacy
build), and `IntroManager`'s splash sequence -- visually verified by
screenshotting the running exe, real splash logos render correctly.
`IntroManager`'s interactive menu screens (main menu, language select,
save slots, etc.) are the next slice -- see `PORT_ROADMAP.md`'s 3.1a entry
for the full current state, including a structural correction (`Game`
turns out to have its own separate in-level UI state machine, deferred to
3.2a rather than being part of `IntroManager` as first assumed).
