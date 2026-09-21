# Going Mobile -- cross-build comparison

The three retail builds of the same v1.0.9 MIDlet (see
[`ROADMAP.md`](ROADMAP.md)) compared source-to-source, to settle phase-1
open questions and map the alternate builds onto the canonical class
names. Everything below is from the decompiled trees
(`../decompiled/`, `../decompiled_a/`, `../decompiled_a1/`).

## The `(a)` build: a 128x160 retarget of the same game

Method counts, line counts and (per class) literal multisets are nearly
identical to the canonical build -- **same code, different target
screen**:

- Screen: canonical is 128x128 (play area `setClip(0, 20, 128, 108)`);
  the `(a)` build is **128x160** (`setClip(0, 20, 128, 140)`), i.e. a
  taller Series 40 handset. All the vertical constants move accordingly:
  camera y clamp `-124` -> `-92` (252-160), `108` -> `140`, `128` -> `160`
  in the vertical-usage sites (Player/Enemy/Projectile/Game).
- The `(a)` MIDlet is 53 lines shorter: it has **no multi-tap name-entry
  table** (`char[][]` keypad map absent) -- the Ratchet: Deadlocked
  skin-name reward feature is missing. Same RMS record store
  (`RANDCSm`), same boss-tuning manifest properties, font still present
  (`short[][] p`).
- LevelMap differs more (3 extra methods, 46 extra lines): consistent
  with the taller view needing different render/tile logic; the level
  files themselves (`n1`..`n12`) are byte-identical between the two
  builds, as are all sounds/strings/images.
- SoundPlayer's read buffer is `8192` in canonical vs `400`-ish in
  `(a)` (1 literal difference) -- a memory-tuning change.

So the `(a)` build is a **screen-port of the same revision** -- its
decompiled tree can be diffed line-by-line against `src/` after applying
only the constant substitutions, which is exactly what makes it useful
for cross-checking the canonical read-through.

## The `(a1)` build: a newer, restructured MIDP-2.0 trial version

The a1 build is a genuinely different (newer) revision with a different
architecture -- not a retarget. Its class map onto the canonical names:

| a1 class | Canonical equivalent | Evidence |
|---|---|---|
| `ratchetandclank` | `ratchetandclank` (MIDlet) | `extends MIDlet`, RMS, 388 lines |
| `a` | *(the font, inside the MIDlet in canonical)* | loads `/f2.v` + `/f3.v`, `short[][]` glyph table, 180 lines |
| `b` | `Player` | `extends i`, loads `/player.bin` |
| `c` | `SoundPlayer` | `implements Runnable, PlayerListener` (MMAPI) |
| `d` | `LevelMap` | loads `"/level" + ...` (13 levels vs 12) |
| `e` | *(the canvas shell, inside Game in canonical)* | `extends Canvas implements Runnable, CommandListener` -- a1 drops Nokia `FullCanvas` for a plain MIDP Canvas + Commands, keeping only `DirectGraphics`/`DirectUtils` for pixel ops |
| `f` | `Enemy` | `extends i`, loads `/enemy.bin` + `/enemy_spr_box.bin` |
| `g` | *(none)* | intro/splash manager **plus an unlock-code registration UI**: a `Form` + `TextField` checked against the `Unlock-Code` app property -- a1 shipped as a time/unlock-limited trial |
| `h` | `Game` | the engine (6622 lines): owns the `new j[10]` projectile pools, the player/enemy instances, `mapData.txt` world map, the `f2.v`/`f3.v` fonts |
| `i` | `Entity` | base of `b` (Player) and `f` (Enemy) |
| `j` | `Projectile` | two 10-slot pools created in `h`, 33-entry per-type tables (evolved tuning vs canonical: e.g. speeds `{10,16,16,...}`, render modes 13-24 with -1 sentinels vs canonical 8-11) |

Content differences (all consistent with a1 being a *later* revision):
13 levels (level0-12) vs 12, multi-grid level files, `mapData.txt`
world map, `.wav` sound variants, `enemy.bin`/`player.bin` state scripts,
bigger `txt_*.txt` string tables (including the full story dialogue),
and the trial/unlock wrapper. `f2.v` is byte-identical to the canonical
build's (the font), confirming the format identification.

## Settled open questions (from this comparison)

1. **`Game.bk`/`bl` (were "message-chain table")**: they are the
   **infolink tables**. `m*.txt` strings 75-84 are "Level N unlocked."
   messages; infolink tiles 77-96 (the special level-transition tiles)
   each show `bl[tile-77]` (75-90) with timer `bk[tile-77]` (all 1s);
   `L()` (the message-advance key) marks the level complete and shows
   the results screen when the shown message id falls in
   `[bl[0], bl[15] + bk[15]]` = [75, 91]. Renamed to
   `INFOLINK_MESSAGE_IDS` / `INFOLINK_MESSAGE_TICKS`.
2. **`Player.q`**: `ladderExitTimer` -- set to 10 when jumping off a
   ladder; while nonzero it suppresses the platform-landing check
   (`Game.e()`) and the zip-line grab (`Player` update), so the player
   can jump past ladder tops and platforms without snapping back.
3. **The `t` asset**: dead data. It is byte-identical in the two builds
   that share this code (canonical and `(a)`), absent from the a1
   build's file list, and referenced by no code path in any of the
   three decompiled trees (all resource loads are accounted for:
   `/n*`, `/m*`, `/o`, `/p`, `/q`, `/r`, `/f2.v`, `/help|about|credits`,
   sounds, `.png`s). Conclusion: a leftover file from an earlier build,
   safe to ignore (and for the port: never load it).
