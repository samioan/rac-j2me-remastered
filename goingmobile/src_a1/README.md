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
| `Game.java` | `h.java` | the engine (195KB, the big one) | not started |

10 of 11 classes done (all the small ones, plus `LevelMap`/`Projectile`/
`Enemy`/`Player`/`IntroManager`). Remaining
orchestration methods on `ratchetandclank` (`startNewGame`/
`continueGame`/`returnToIntro`/`playSoundIfEnabled`) and most of
`LevelMap`'s tile legend are tentative pending `IntroManager`/`Game`'s
own phase-1 -- see `../docs/CLASS_MAP.md`. Class-level renames
(`Player`/`Enemy`/`IntroManager`/`Game`/`Projectile`) are already used
throughout the done files even though those classes' own members aren't
renamed yet -- see CLASS_MAP.md's policy note.

## Compile check

Not yet run against `src_a1/` (too few files exist for a meaningful
link-check). Once enough of the tree exists, the same command as the
legacy build's (`../src/README.md`) applies, swapping in
`goingmobile/src_a1/*.java` and a different output dir -- a1 still targets
MIDP-2.0/CLDC-1.0 and still uses `com.nokia.mid.ui.DirectGraphics`/
`DirectUtils` for pixel ops (just not `FullCanvas`), so the same stub jars
in `tools/midp-stubs/` apply.

## Method and limits

Same phase-1 bar as `../src/` (see its README): rename only where the role
is confirmed or provably unambiguous from use sites; leave obfuscated and
document in `CLASS_MAP.md` otherwise. No behavior has been run -- this
tree proves types and structure, not correctness.
