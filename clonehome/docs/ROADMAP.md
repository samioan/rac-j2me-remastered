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

**Phase 1 (not started): read through and rename**, starting from
`RatchetMIDlet.java` (small, already readable) -> `a.java` (the engine
base + resource loader) -> `b.java`. The save-game format is already
fully recoverable from `RatchetMIDlet`'s serializer without touching `b`.

**Phase 2 (partially done from phase-0 read-through, see
[`ASSET_FORMATS.md`](ASSET_FORMATS.md)):** the `RP*` container format is
confirmed from `a`'s own parser (header, length table, flag table,
payload, `bank<<10|index` addressing); the per-resource payload formats
(embedded PNGs with a byte-swap decode, string tables, level streams) are
partially confirmed -- the PNG chunk walker (`IEND`-terminated, custom
`PAGG` chunk type constant) and UTF-8 string-table parser in `a.java`
are read; what each resource id *contains* still needs mapping from
`b`/`c`'s call sites.

**Phase 3 (not started): PC port.** Scaffold is in `port/` (CMake + Ninja
+ MSVC, matching the `tes-travels-decomp` ports' toolchain) -- currently
just proves the build works. Because all content is data-driven out of
the `RP*` banks (not hand-written per level in Java), a faithful port
likely means porting the *engine* (the `a`/`b`/`c` classes) to C++ and
loading the original `RP1`/`RP2`/`RP3` files directly -- same conclusion
as tes-travels-decomp reached for Oblivion's Superscape `.scr`/`.cml`
content. The jad's 240x320 SEMC screen size is the port's natural window
size.
