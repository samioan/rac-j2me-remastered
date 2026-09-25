# Clone Home -- roadmap

## The target

`roms/RAC-CloneHome.jar` (with its `roms/RAC-CloneHome.jad` descriptor)
-- a J2ME MIDlet (`MIDP-2.0`/`CLDC-1.1`), MIDlet-Vendor `Sony Pictures`,
MIDlet-Name "Ratchet and Clank 2", `MIDlet-Version 1.0.22`,
`Created-By: JGTools`. Entry point `RatchetMIDlet`
(`MIDlet-1: Ratchet and Clank 2,/i.png,RatchetMIDlet`). This is the
unreleased/prototype second mobile *Ratchet & Clank* known as
**Clone Home** (a build of it leaked as "Ratchet and Clank 2"); the jad
targets a Sony Ericsson 240x320 handset (`SEMC-Screen-Size: 240,320`,
`QP-*` qualification-program entries, `QP-UseMultipleSounds: Y`) and the
MIDlet reads an optional `Cheat-Mode` app property
(`RatchetMIDlet.<init>`).

Class layout (all heavily obfuscated -- single-letter classes, and inside
`b` even the *fields* are single/double letters):

| Class | Role (evidence) |
|---|---|
| `RatchetMIDlet` | MIDlet; owns the save format (3 slots x 218-byte records + a settings record; full serialize/deserialize in `a(byte[])`/`b(byte[])` with a byte-exact field layout) and a `%0`/`%1` string-substitution helper |
| `b` (238KB source!) | the game: `extends a`, which is `abstract class a extends GameCanvas implements Runnable` -- MIDP-2.0 GameCanvas game loop, renderer, input; uses Nokia `DirectGraphics`/`DirectUtils` for pixel work; state machine field `b.I` (values 0..19 seen in `destroyApp`'s save-on-exit switch) |
| `a` (36KB) | the engine base (`GameCanvas` + `Runnable` + MIDlet glue) **and the resource-bank loader** -- parses `RP1`/`RP2`/`RP3`, serves images/PNGs, string tables and byte resources by id `bank << 10 \| index`, walks PNG chunk streams, does RMS record I/O |
| `c` (44KB) | level/world logic (DataInputStream-driven) |
| `d`, `e`, `f` | smaller helpers (entity data, sound wrapper, resource record type) |

Unlike goingmobile's one-file-per-asset layout, everything here is packed
into three `RP<n>` resource banks (see
[`ASSET_FORMATS.md`](ASSET_FORMATS.md)) -- images, strings and level data
all come out of `a`'s bank loader by numeric id.

## Status

**Phase 0 (this setup) is done:** jar unpacked (`extracted/`, 12 entries),
decompiled to readable-but-unrenamed Java (`decompiled/`, 7 files) via
[`../../tools/decompile.py`](../../tools/decompile.py). Clean recovery
despite the obfuscation; only `b.java`'s sheer size (238KB, thousands of
single-letter fields) makes the read-through daunting.

**Phase 1 (done, with a tail): read through and rename.** All seven classes
are read and renamed into [`../src/`](../src/) -- `a`=`Engine`, `b`=`Game`,
`c`=`Enemy`, `d`=`Projectile`, `e`=`SoundPlayer`, `f`=`Sprite` -- with 644
members named from confirmed call sites (`RatchetMIDlet` and the five small
classes are fully renamed; `Game` has 135 of its 155 methods and 243 fields named).
Unlike Going Mobile's regex renamer, this one works on the class files
(`tools/rename_ch.py` + `tools/ch_rename/ChRenamer.java`, driven by
[`names.map`](names.map)), so it is exact, and `src/` compiles against the
MIDP stubs with 0 errors. [`CLASS_MAP.md`](CLASS_MAP.md) has the evidence: the
tile-code semantics, the game-state machine, the save format, the enemy and
projectile classes, the data tables. The tail is listed under "Not yet mapped"
there: mainly the title-menu screen machine (`screen`), the credits scroller,
the name/code entry, and a handful of sprite arrays.

**Phase 2 (mostly done, see [`ASSET_FORMATS.md`](ASSET_FORMATS.md)):** the
`RP*` container, every resource's *type byte* (images, atlases, anim sets, data,
strings, audio), the level tile/info format, the 28 game tables, the string table
(449 strings) and the sound cues are confirmed and verified against the real
banks by independent parsing. The sprite-atlas format is parsed and verified (`tools/parse_ch.py`, all 30 atlases). Animation sets (type 247) are parsed and verified too. What remains is labelling which of the 68 image resources is which sprite (start from
`Game.loadAssetsStep`).

**Phase 3 (in progress): PC port -- the game runs.** Instead of hand-porting the ~12.8K lines of `Game`/`Enemy`/`Projectile`, [`tools/java2cpp.py`](../../tools/java2cpp.py) translates the renamed Java in `src/` straight to C++ (`port/src/gen/`, regenerated whenever `names.map` changes): class refs become pointers, arrays `Arr<T>`, `String` a UTF-16 value type, with a small type inference for `->`/`.`, string `+` and `>>>`. The platform side is hand-written in `port/src/`: `assets.cpp` (banks, atlases, anim sets, strings, PNG/JPEG via stb), `gfx.cpp` (240x320 software `Graphics`), `engine.cpp` (a port of `Engine.java`: timing, keys, records as files, bitmap font, tile layer, animation player), `jrt.h` (Java runtime shims), `platform.h` (MIDlet/Display/DirectGraphics/Player stand-ins) and `main.cpp` (Win32 window, crash handler, headless `--dump out.bmp --frames N --press K@F[:HOLD]`). Verified: the Javaground splash, the title menu, New Game into level 1 with HUD, and running right all work. Audio works (WAV cues via `PlaySound`, the looping MIDI via MCI, restarted by `Player::pumpLoops`). `--fuzz SEED` injects random keys headless: 13 seeds x 30-60K frames ran with no crash. The Java game itself throws an uncaught `ArrayIndexOutOfBounds` (e.g. `onOneWayTile` on the bottom tile row); the port logs these to `exceptions.log` and continues. `--level WORLD:SECTION` jumps straight into any level: all 78 sections of worlds 0-16 (arena waves included) load, render and survived 4000 fuzzed frames each with no crash (many log the game's own bottom-row `ArrayIndexOutOfBounds`). Not done: a real play-through (boss fights, the world map, finishing a level), regression frames, packaging/release.
+ MSVC, matching the `tes-travels-decomp` ports' toolchain) -- currently
just proves the build works. Because all content is data-driven out of
the `RP*` banks (not hand-written per level in Java), a faithful port
likely means porting the *engine* (the `a`/`b`/`c` classes) to C++ and
loading the original `RP1`/`RP2`/`RP3` files directly -- same conclusion
as tes-travels-decomp reached for Oblivion's Superscape `.scr`/`.cml`
content. The jad's 240x320 SEMC screen size is the port's natural window
size.

## Playing the port

Run `clonehome/port/build/clonehome_port.exe` (it finds `clonehome/extracted` itself; elsewhere put `RP1`-`RP3` next to
the exe or pass `--data <dir>`). Saves live in `saves/` next to the exe. Controls (`port/src/input.cpp`, modelled on the
Going Mobile port; physical input -> action -> phone keypad code by context):

| Action | Keyboard / mouse | Gamepad (XInput, PlayStation layout) |
|---|---|---|
| Move | arrows, WASD | d-pad, left stick |
| Jump | Space, Left Shift | Cross (A) |
| Fire (also confirms in menus) | J, X, Left Ctrl, left mouse | Circle (B), right trigger |
| Wrench (melee only) | K, C, `/` | Square (X), left trigger |
| Weapon wheel | Q, E, Tab, right mouse | Triangle (Y), either shoulder |
| Pause | Esc, P | Start |
| Back (menus) | Backspace | Select/Back |

In menus: Enter/Space/Fire confirm, and wrench/Pause/Back/right mouse go back; a held arrow repeats. The number row and numpad
still send the raw handset digits (the game reads 1-9 directly, and the name/code entry screens are typed with them).

Speed: the game does one logic step per frame (and only when the frame time exceeds 40 ms), and the phone build had a 50 ms
minimum frame time, i.e. about 20 steps/s. The port paces at a fixed rate instead, default **25 Hz** (matches the game's own 40 ms tick constant);
change it with `--hz N` or **F5 (slower) / F6 (faster)** in the window (title bar shows the rate). `--stats` writes the
measured frame rate to `perf.log` on exit.
