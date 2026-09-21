# goingmobile/src/ -- the phase-1 renamed reference tree

Faithfully-renamed Java for the canonical Going Mobile build, produced by
[`tools/rename_gm.py`](../../tools/rename_gm.py) from
[`../decompiled/`](../decompiled/) (plus three hand-written files), mapping
every class to its real name and every member whose role is confirmed from
the code. The full mapping, everything that kept its obfuscated name and
why, and the open questions are in [`../docs/CLASS_MAP.md`](../docs/CLASS_MAP.md).

| File | From | Role |
|---|---|---|
| `Game.java` | `f.java` | the engine (`FullCanvas implements Runnable`): tick loop, menus, scoring, boss, spawn/collision helpers, save format |
| `Player.java` | `a.java` | Ratchet: physics, weapons, melee, animation, magnet pickups |
| `Enemy.java` | `d.java` | the 5-slot enemy pool, 5 types, per-type anim/geometry tables |
| `Projectile.java` | `g.java` | both projectile pools, 33 types |
| `LevelMap.java` | `c.java` | tilemaps, tile legend, solid-column masks, tile renderer |
| `Entity.java` | `h.java` | *(hand-written)* Player/Enemy base: position, health, anim state |
| `MenuItem.java` | `e.java` | *(hand-written)* one row of a menu page |
| `SoundPlayer.java` | `b.java` | *(hand-written)* the whole audio layer (MMAPI) |
| `ratchetandclank.java` | *(name kept)* | the MIDlet: RMS saves, strings, bitmap font |

## Compile check

`src/` compiles **standalone** against the real MIDP-2.0 / CLDC /
Nokia-UI API stub jars (in `tools/midp-stubs/`, gitignored -- fetched, see
`tools/README.md`) with **zero errors and zero warnings**:

```
javac -encoding UTF-8 -nowarn ^
  -classpath "tools/midp-stubs/midpapi20.jar;tools/midp-stubs/cldcapi11.jar;tools/midp-stubs/nokiaui.jar" ^
  -d build_src goingmobile/src/*.java
```

This is what proves the rename is complete: every cross-file reference
that the renamer missed or got wrong would be a missing or mistyped
symbol. The handful of places where the raw Vineflower output did not
compile at all (mistyped locals, a dead `new byte[]` assigned to
`short[]`) are decompiler artifacts, fixed behavior-preservingly by the
script and listed in `../docs/CLASS_MAP.md`.

## Method and limits

The renamer applies textual rewrites only where they are unambiguous --
the obfuscator reused single letters for *fields and methods of the same
class* (legal in the JVM, unrenamable by any textual rule), and every
other class also has same-letter fields. So:

- All 8 obfuscated class names are renamed, with every cross-file
  reference (constructor, `new`, `extends`, typed locals, casts,
  class-qualified statics).
- Members are renamed wherever the name is 2+ letters (never a method
  name) or otherwise provably unambiguous in context; single-letter
  members of `Game` and the `ratchetandclank` methods stay obfuscated and
  are documented in `CLASS_MAP.md` instead.
- What is *not* claimed: no behavior was run -- this tree proves types
  and structure, matching the dawnstar/stormhold phase-1 methodology.
