# goingmobile/src_a1/ -- the phase-1 renamed reference tree (a1, current target)

Faithfully-renamed Java for `roms/RAC-GoingMobile-a1.jar`, the current
decompilation/port target (see `../../docs/ROADMAP.md`'s target-change
note). Produced by hand from [`../decompiled_a1/`](../decompiled_a1/) --
no renamer script yet (a1's structure differs enough from the legacy
build's that `tools/rename_gm.py`'s per-file qualifier tables don't
directly apply; one may get built once more classes are mapped and the
renames start following a regular enough shape). The full mapping,
confidence levels, and open questions are in
[`../docs/CLASS_MAP.md`](../docs/CLASS_MAP.md)'s "a1 class map" section.

| File | From | Role | Status |
|---|---|---|---|
| `Entity.java` | `i.java` | Player/Enemy base: position, health, anim state | done |
| `SoundPlayer.java` | `c.java` | the MMAPI audio layer | done |
| `Font.java` | `a.java` | `f2.v`/`f3.v` bitmap font | done |
| `CanvasShell.java` | `e.java` | `Canvas implements CommandListener` | done |
| `ratchetandclank.java` | `ratchetandclank.java` | RMS saves, strings | done |
| `LevelMap.java` | `d.java` | tilemaps, room/sub-grid tables | done |
| `Enemy.java` | `f.java` | the enemy pool | done |
| `Projectile.java` | `j.java` | both projectile pools | done |
| `Player.java` | `b.java` | Ratchet: physics, weapons, melee | done |
| `IntroManager.java` | `g.java` | splash + full menu system + unlock-code UI | done (mostly obfuscated -- see CLASS_MAP.md) |
| `Game.java` | `h.java` | the engine (195KB, the big one) | done (mostly obfuscated -- see CLASS_MAP.md) |

**11 of 11 classes done.** `Game`'s own phase-1 confirmed the structural
finding `IntroManager.java` had already predicted: a1 moved the legacy
build's in-`Game` menu system entirely into `IntroManager`, so `Game`
itself is gameplay-only (tick/render/collision engine + save-data
serialization). Remaining orchestration methods on `ratchetandclank`
(`startNewGame`/`continueGame`/`returnToIntro`/`playSoundIfEnabled`) and
most of `LevelMap`'s tile legend are still tentative -- confirming them
would need tracing through `Game`'s own obfuscated internals, which
weren't fully derived (see CLASS_MAP.md's "Game" section for what was
and wasn't renamed, and its "known follow-ups" list, including several
`ratchetandclank` methods `Game` calls that aren't in that class's own
phase-1 yet). Class-level renames (`Player`/`Enemy`/`IntroManager`/
`Game`/`Projectile`) are already used throughout the done files even
though those classes' own members aren't all renamed yet -- see
CLASS_MAP.md's policy note. `CanvasShell.java`'s 6 dispatch calls into
`Game` and `SoundPlayer.java`'s one call into `Game` still use `Game`'s
pre-rename letter names (`.p()`/`.q()`/`.w()`/`.r()`/`.b()`/`.c()`/
`.e(30)`) rather than its now-confirmed `pause`/`resume`/`render`/`tick`/
`keyPressed`/`keyReleased`/`sleep` -- a known, not-yet-done cross-file
cleanup, same as `Enemy.java` never being touched up after `Player.java`
renamed its own `b()`/`c()`.

## Compile check

`src_a1/` now compiles **standalone** against the real MIDP-2.0/CLDC/
Nokia-UI API stub jars, same command as the legacy build's
(`../src/README.md`), swapping in `goingmobile/src_a1/*.java` and a
different output dir:

```
javac -encoding UTF-8 -nowarn ^
  -classpath "tools/midp-stubs/midpapi20.jar;tools/midp-stubs/cldcapi11.jar;tools/midp-stubs/nokiaui.jar" ^
  -d build_src_a1 goingmobile/src_a1/*.java
```

Zero errors. This was the last prerequisite `PORT_ROADMAP.md` sets for
milestone 3.1a. Getting there surfaced (and fixed) far more than the
already-known cross-file staleness -- see `../docs/CLASS_MAP.md`'s
"Cross-file consistency pass" section for the four new bug classes it
found (a decompiler-artifact keyword collision, missed bare class-
qualifier renames in `Game`/`IntroManager`'s scripted substitution, a
wider field-letter collision between `Game`'s own kept-obfuscated fields
and five other classes' names, and an overload-blind blanket rename that
corrupted a `Graphics`-arg overload).

## Method and limits

Same phase-1 bar as `../src/` (see its README): rename only where the role
is confirmed or provably unambiguous from use sites; leave obfuscated and
document in `CLASS_MAP.md` otherwise. No behavior has been run -- this
tree proves types and structure, not correctness.
