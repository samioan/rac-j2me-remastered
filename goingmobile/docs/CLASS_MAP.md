# Going Mobile -- class map (phase 1)

`src/` is the phase-1 renamed reference tree for the **canonical build**
(`roms/RAC-GoingMobile.jar`, decompiled in `../decompiled/`). All 9 classes
were read through in full, given real names, and the result
**compiles with zero errors against the real MIDP-2.0 / CLDC / Nokia-UI stub
jars** (`tools/midp-stubs/`, see `../src/README.md` for the exact command).

How it was produced: `tools/rename_gm.py` applies the class-level mapping
below to `../decompiled/` (all cross-file references included), plus
member-level renames for every member whose role is *confirmed from the
code* and whose name is unambiguous in context. `Entity.java`,
`MenuItem.java` and `SoundPlayer.java` are hand-written and fully renamed.
Members whose obfuscated name could not be renamed safely or whose role is
not confirmed are left as-is and documented here -- the compiler is the
arbiter that no reference was missed, and every risky textual rewrite was
verified against the original before applying.

## Class mapping

| Old | New | Role (evidence) |
|---|---|---|
| `f` | `Game` | the game: `extends com.nokia.mid.ui.FullCanvas implements Runnable`; 128x128 screen, ~4.5 Hz logic tick via `callSerially`; splash chain, menus (`/o` + `m*.txt`), scoring, boss fight, save/load, all spawn/collision helpers |
| `a` | `Player` | Ratchet: platformer physics (jump phases, double jump, ledge grab, ladders, zip lines, swings, enemy-bounce), weapon firing, melee, animation from `/r`, magnet-bolt pickup |
| `d` | `Enemy` | the 5-slot enemy pool; 5 enemy types (0-4), per-type animation tables from `/p`, geometry from `/q`; walking/flying/wall-crawling AI, squash-and-bounce |
| `g` | `Projectile` | both projectile pools (player shots + enemy shots, 10 each, 33 types); per-type speed/half-size/damage tables, rotated rendering, trails |
| `c` | `LevelMap` | level tilemaps: 12 grids of 28x18 tiles from `/n<level>`, the tile legend, solid-column bitmasks, the tile renderer |
| `h` | `Entity` | base class of Player/Enemy: fixed-point position, row wrapping, health, animation state (hand-written, fully renamed) |
| `e` | `MenuItem` | one row of a menu page from the `/o` menu table (hand-written, fully renamed) |
| `b` | `SoundPlayer` | the whole audio layer: 7 MIDI cues on a background thread via MIDP-2.0 MMAPI (hand-written, fully renamed) |
| `ratchetandclank` | *(unchanged)* | the MIDlet (its real name, from the manifest); RMS save store `RANDCSm`, `.txt` string loading, word-wrap, the bitmap font (`f2.v`), boss tuning from manifest attributes |

Cross-build note: the `a1` build's engine (`../decompiled_a1/h.java`,
195KB) is the same game with a different obfuscation pass; matching method
bodies between the two recovers both mappings -- still to be done, and the
class names above are expected to carry over.

## Entity (from `h`) -- fully renamed

Position convention: `posX` is horizontal position in 1/256-pixel units;
`row` is the 14-pixel-tall tile row, `posInRow` the 1/256-px offset inside
it (wrapping at 3584 = 14*256 via `wrapRow()`); `column()` derives the
22-pixel-wide tile column from `posX`.

| Old | New | Notes |
|---|---|---|
| `R` | `kind` | enemy type 0-4 (Player: always 0) |
| `S` | `row` | |
| `T` | `posX` | int, `px << 8` |
| `U` | `velY` | 1/256 px per tick (up positive; jump = 2560) |
| `V` | `velX` | |
| `W` | `fieldW` | **unconfirmed** -- never read anywhere in the codebase |
| `X` | `spawnRow` | copy of `row` kept at spawn |
| `Y` | `posInRow` | |
| `Z` | `activeFlag` | set to 1 at spawn; role otherwise unconfirmed |
| `aa` | `health` | |
| `ab`/`ac` | `animFrame`/`animCounter` | frame index + tick counter |
| `ad` | `animState` | the animation selector (0 idle, 2 fall, 3 land, 4 run, 5/6 climb, 7-8 fire, 9 hurt, 10 dead, 11 hyper, 12-14 zip/swing, ...27 anims total) |
| `ae` | `animRestart` | 1 = restart anim, 2 = hold last frame |
| `af` | `animHold` | hurt-flash frame counter |
| `ag` | `facingRight` | |
| `ah` | `subState` | enemy wall-attachment (1 ceiling, 2 left wall, 3 right wall) |
| `a(byte)` | `setAnimState(byte)` | resets frame/counter |
| `p()` | `column()` | |
| `q()` | `wrapRow()` | |

## MenuItem (from `e`) -- fully renamed

`type` (0 label, 1-3 selectable rows, 4 text screens, 5 value row, 6
conditional row, 8 scroller, 9 spacer), `action` (menu page to open;
-104 language, -105 name entry, 125 load game, 126 quit, 127 new game),
`stringId` (index into `ratchetandclank.strings`), `enabled`.

## SoundPlayer (from `b`) -- fully renamed

`queue(id, loopCount)` records a request (first wins), `stop()` clears it;
the `run()` loop builds a `javax.microedition.media.Player` per cue from
the in-memory copies. Sound ids: PORT/KILLEN/BUBBLE/DEATH/BOX/SHOOT/MENU.
All shipped cues are `.mid` (`audio/midi`); the code also knows `.wav`.

## Player (from `a`)

Statics (all kept obfuscated -- their single-letter names collide with the
class letters, but their roles are confirmed from use sites):

| Old | Role |
|---|---|
| `a`, `b` | 15-entry hit/effect tables (values -2..9, 44 = sentinel) -- exact meaning unconfirmed |
| `c` | 100-entry sprite geometry for the render method: per frame, clip x/y/w/h and source offsets at strides of 15/30/45/60/75 |
| `d` | ammo capacity per weapon x level (24 = 8 weapons x 3 levels) |
| `e` | initial ammo per weapon (0, 50, 20, 15, 20, 100, 1, 30) |
| `f` | ammo added per pickup box per weapon (0, 15, 7, 5, 8, 30, 1, 10) |
| `g` | weapon charge ticks per level x weapon (`[3][8]`) |
| `h` | weapon prices in the store (0, 0, 250, 400, 700, 950, 1650, 0) |
| `i` | boss damage per shell state (3, 6, 6) |
| `j`, `l`, `m`, `n`, `o` | small per-weapon-pose pairs (muzzle x/y/w/h; `l`/`o` = {16,13}/{4,-7}...) |
| `k` | jump velocity (1280); `j` = terminal fall speed (1792) |
| `G`/`H`/`I` | **renamed** `ANIM_FRAMES` `[1][15][5]`, `ANIM_FRAME_COUNTS`, `ANIM_FRAME_EXTRA` -- the player animation table loaded from `/r` |

Weapons (confirmed against `m*.txt` strings 48-55: Lancer, Boar-Zooka,
R.Y.N.O. ...): index 0 = wrench (melee), 1-7 = guns; each gun's projectile
type is `3 * weapon + level` (0/3/6/9/12/15/18 + level 0-2); the RYNO
(weapon 7) is unlocked by collecting `TITANIUM_BOLTS_FOR_RYNO` (9) titanium
bolts (`Game.g()` sets `ownedWeapons |= 128`).

Renamed instance fields: `J`->`currentWeapon`, `K`->`ammo[8]`,
`L`->`weaponXp[8]` (20 kills = level up, string 73 "Weapon Level Up!"),
`M`->`ownedWeapons` (bit per weapon), `N`->`weaponLevel[8]`,
`B`->`invulnTimer`, `D`->`onLadder`, `E`->`platformUnder`,
`F`->`meleeActive`, `C`->`swingTargetType` (hyper-shot target tiles 97/98),
`A`->`weaponPose` (muzzle table index), `z`->`attackTimer`,
`p`->`jumpPhase` (-1 grounded, 0/1 rising/falling, 2 double jump),
`q`->`ladderExitTimer` (10 ticks after a ladder jump; suppresses the
platform-landing and zip-line grabs), `r`->`actionState` (-2 alive, -1
dying, 0/3 hyper-shot swing), `s`->`specialTimer` (death/hyper timing),
`t`/`u`->`swingTargetX/Y`, `v`/`w`->`swingCurX/Y`, `x`/`y`->`swingStepX/Y`,
`O`/`P`->`swingVelX/Y`, `Q`->`swingEndX`, `ai`->`game`.

Kept obfuscated (method names of the class collide, or role not fully
confirmed): `Player.a`, `b` (15-entry hit/effect tables). Physics
methods `i() j() k()` (jump, main update, tile interactions), fire
`m()`, melee `n()`, hyper-shot search `l()`, ground calc `a(boolean)`,
render `a(Graphics)` all keep their obfuscated names (documented here).

## Enemy (from `d`)

Statics: `a`/`b`/`c`/`d` = per-type hitbox **x-offset / y-offset / width /
height** (from `/q`, the a1 build's `enemy_spr_box.bin`); `e`..`h` =
second per-type geometry group (weapon/fire offsets; from `/q`);
`i`->**`HP_BY_ANIM`** (27 anims), `j`->**`DAMAGE_BY_ANIM`** (damage dealt
to the player per anim), `k`->**`BOLTS_DROPPED_BY_ANIM`**,
`l`/`m`/`n`/`o` kept (small per-type tables; `n` = {1024, 512, 768, 0, 512}
looks like fixed-point speeds), `p`/`q`/`r`->**`ANIM_FRAMES` `[5][6][5]` /
`ANIM_FRAME_COUNTS` / `ANIM_FRAME_EXTRA`** (5 types x 6 anims, from `/p`).
Instance: `D`->`game`, `t`->`animKind` (the anim-set index, 0-27),
`u`->`bounceTimer` (+/-10 when the player bounces off its head),
`R`(Entity)->`kind` (type 0-4; 4 = exploded/dead), `s` kept (flag that
switches the renderer to the flattened variant). Types 0-2 walk/fly,
type 3 is the wall-crawler (uses Entity `subState` 1-3), boss level 12
uses types 1 and 4 as boss parts.

## Projectile (from `g`)

Statics renamed: `a`->**`DAMAGE_BY_TYPE`** (33), `b`->**`HALF_WIDTHS`**,
`c`->**`HALF_HEIGHTS`** (collision half-sizes), `d`->**`SPEEDS`**,
`e`->**`RENDER_MODES`** (values 8-11 select rotated/blipped drawing);
`f` = {3, 4, 6} kept (unconfirmed). Instance fields renamed: `g`->`type`,
`h`/`i`->`posX`/`posY`, `j`/`k`->`prevX`/`prevY` (also the decoy-drone
target for type 32), `l`/`m`->`prev2X`/`prev2Y` (trail), `n`/`o`->`vx`/`vy`,
`p`->`age`, `q`/`r`->`halfWidth`/`halfHeight` (copies of the type tables),
`s`->`sourceAnim` (damage class for `DAMAGE_BY_ANIM`), `t`->`facingRight`,
`u`->`spawnX`, `v`->`detonated`, `x`->`game`. Type 31 is the boss's
aiming crosshair marker; 30 the hit spark; 32 the decoy drone.

## LevelMap (from `c`)

`levelGrids` (old `d`, `byte[12][28][18]`) = all 12 grids, one per `n*`
file; `tiles` (old `f`, `byte[28][18]`) = the active grid;
`columnSolidMasks` (old `e`, `int[28]`) = bit per solid row per column
(built from tiles 4-13; every physics/collision helper reads it);
`gridCount` (old `g`); `game` (old `c`). Statics renamed:
`h`->**`PLATFORM_TILE_TYPES`** (12 entries, tiles 102-113),
`i`->**`ENEMY_TILE_TYPES`** (14 entries, tiles 114-127 + tile 0x80),
`j`->**`BOLT_TILE_TYPES`** (4 entries, tiles 43-46); `a`/`b` kept
(13-entry per-level tables; `b` selects the boss arena's 12 grids).

Tile legend (from the level parser `a(int, boolean)`):
0-3 background, **4-13 solid** (rendered from `a.png`, 22x14 cells),
14-35 solid specials/decoration, 36-42 + 77-96 foreground decoration,
43-46 titanium-bolt boxes, 47-55 enemy spawns (9 direct types),
56-61 hazard/death tiles, 62-69 zip-line endpoints (2 per line),
70-76/-119 background, 97-98 hyper-shot targets, 101 marker,
102-113 moving platforms, 114-127 enemies (14 types via the table),
-125 (raw 0x20) player start + camera, -124/-122/-127/-126 camera tiles.

## Game (from `f`)

Game's own single-letter fields/methods **all keep their obfuscated names**:
the obfuscator reused every single letter a-z/A-O as *both* a field and a
method (legal in Java, since fields and methods have separate namespaces),
so no single-letter member of `Game` can be renamed by any safe textual
rule. They are documented here instead:

| Old | Role |
|---|---|
| `a` | static: the `ratchetandclank` midlet instance |
| `b` | game state: 0 gameplay, 19 menu screens, 20 splash images |
| `c`/`d`/`e` | menu-music played / startup done / repaint requested |
| `f`/`g`/`h` | **statics**: boss shell HP (35), kernel HP (100), fire rate (22) -- from the manifest attributes |
| `i`/`j` | statics: invincibility cheat / `MIDlet-Spec-Code` flag (enables cheats 0/7/9 on keypad) |
| `k`/`l`/`m` | statics: menu scroll position / rows total / rows visible |
| `n`/`o`/`p`/`q` | statics: about/help/credits text vectors / message screen vector |
| `r`/`s` | **statics: camera x / y** (clamped to -488 / -124: the 616x252 level vs the 128x128 view) |
| `t`/`u`/`v`/`w`/`x`/`y` | statics: camera-pan mode & focus x, input lock flags, weapon-overlay item, ... |
| `A`/`B`/`C` | statics: language names / file suffixes / selected language |
| `D` | subscreen id (114 main menu, 115 name entry, 116 skin info, 117 confirm, 118/119/120 language) |
| `E` | the 14-char name-entry buffer (multi-tap via `KEYPAD_CHARS`) |
| `H` | static: the menu pages (`Vector` of `MenuItem[]`, from `/o`) |
| `I`/`J`/`K`/`L`/`M` | statics: menu scroll / current page / blink counter / visible rows / max extra rows |
| `N`/`O` | the `LevelMap` / the `java.util.Random` |
| `P`/`Q`/`R`/`S`/`T`/`U`/`V` | current sub-level / level (12 = boss) / camera start x,y / camera spawn x,y / checkpoint sub-level |
| `W`/`X`/`Y`/`Z` | **renamed**: `enemies` / `player` / `playerShots` / `enemyShots` |

Renamed 2-letter members (the full list is the mapping table in
`tools/rename_gm.py`, `GAME_MEMBERS`): the image fields (`tileSetImage`
= a.png, `enemySegmentImage` = b.png, `playerImage` = c.png, `actorImage`
= d.png, `titaniumBoltImage` = e.png, `weaponImage` = f.png,
`hudIconImage` = g.png, `portraitImage` = p.png, `smallSpriteImage` =
h.png, `splashImage`, `digitStripImage` = uc.png), the boss fight state
(`bossHp[5]` = 4 shells + kernel, `bossAim` 0-7, `bossShellState`,
`bossShellTimer`, `BOSS_SHELL_COL/ROW`), the titanium-bolt boxes
(`titaniumBoltX/Y/Type[20]`, `TITANIUM_BOLTS_FOR_RYNO`), the magnet
pickups (`pickupX/Y/Vx/Vy/Type[6]`), the moving platforms
(`platformX/Y/Dx/Dy/State[4]`), the zip lines (`zipX1/Y1/X2/Y2/Dx/Dy/BaseY`),
the exit (`exitTileX/Y`), the HUD/message state (`boltCount`,
`messageText/Portrait/StringId/Timer...`), the progress masks, and the
scoring block (labels confirmed against `m*.txt` strings 63-70/111:
`enemyKills`, `boltsCollected`, `shotsFired`, `enemyShotHits`, `boxesHit`,
`totalTitaniumBolts`, `totalKills`, `totalScore`, `baseScore`, `timeBonus`,
`PAR_TIME_MS` = 180000, ...).

Kept obfuscated 2-letter members (unconfirmed): `bA` (player-death
counter?), `bB`/`bG`/`bH` (intermediate score terms), `cs`/`ct`
(the name-entry info-screen vectors), `cu`/`cv` (their cursor rows),
`ca`, `cr` (previous menu selection). Renamed since the cross-build
pass: `bk`/`bl` -> **`INFOLINK_MESSAGE_TICKS`/`INFOLINK_MESSAGE_IDS`**
(the infolink tiles 77-96 show "Level N unlocked." strings 75-84 with
these ids/timers; advancing the message inside the `[75, 91]` bounds
completes the level -- see `BUILD_COMPARISON.md`).

## ratchetandclank (the MIDlet)

Renamed fields: `strings` (old `e`, the `m*.txt` table), `soundPlayer`
(old `f`), `soundEnabled` (old `g`), `saveBuffer` (old `h`, the 220-byte
record), `settings` (old `q`, 3 bytes), `SAVE_RECORD_IDS` (old `p`,
{1,2,3,4}), `saveSlotFlags`/`saveSlotTimes` (old `c`/`d`, per-slot
in-use/progress), `fontGlyphs` (old `r`, the `f2.v` glyph table),
`fontSpacing` (old `s`), `fontLineHeight` (old `o`).

Methods kept obfuscated (documented): `a()`/`b()` quit,
`c()`/`d()` RMS verify/rebuild, `a(int)`/`b(int)`/`c(int)`/`d(int)`
save-slot write/read/clear/read-raw, `e()`/`e(int)`/`a(byte,int)`
settings read/write, `a(String,int)` string-table loader (145 lines),
`b/c(String,int)` word-wrap loaders, `a(char,int)`/`a(...)` word-wrap
state machine, `a(String,int,int)` the **font loader** (`f2.v`), the
`a(Graphics,...)` fillRect font renderer and `a(String)`/`a(char)`
width queries. The static field `b` (a `Game` instance) is the save
serializer entry point (`b.a(byte[])`/`b.b(byte[])`).

## Vineflower artifacts fixed (compile blockers in the raw decompile)

These are decompiler bugs in `decompiled/`, fixed by `tools/rename_gm.py`
(behavior-preserving; see the script for the exact rewrites):

1. `f.java` constructor: `short[] var10000 = new byte[]{...}` -- dead
   code with an impossible assignment; retyped to `new short[]`.
2. `u()` (platform mover): local `var5` aliased both the `byte[]`
   platform-state array and the `short[]` platform position arrays --
   split into `var5` (short[]) + `varBd` (byte[]).
3. `l(int)` (boss shell hit): local `var10000` aliased both a `byte[]`
   and the `int[]` bossHp array -- split out as `varHp` (int[]).
4. `i(Graphics)` (boss render): `var7` held sprite offsets up to 140 and
   `var10000` the 16384 rotation flag -- retyped byte->int / byte->short
   (a `(byte)` cast would have *changed* the values).
5. `a.java` (Player): `var15` held `column * 22` values up to 594 --
   retyped byte->int (the `(byte)var15` cast at its use site proves it).

## Open questions

- `Entity.fieldW` (old `W`): never read anywhere -- dead field?
- `Entity.activeFlag` (old `Z`): set to 1 at spawn, never tested.
- `Game.bB`/`bG`/`bH`/`bA` (intermediate scoring terms) and
  `cs`/`ct`/`cu`/`cv`/`ca`/`cr` (name-entry screens, menu cursors):
  roles inferred but not confirmed.

## Settled by the cross-build comparison (see `BUILD_COMPARISON.md`)

- `Game.bk`/`bl` were the infolink tables -> renamed
  `INFOLINK_MESSAGE_TICKS`/`INFOLINK_MESSAGE_IDS`.
- `Player.q` was the post-ladder-jump grace timer -> renamed
  `ladderExitTimer`.
- The `t` asset is dead data (byte-identical in both builds that share
  this code, absent from a1, referenced by no code path anywhere).
- The `(a)` build is a 128x160 screen-port of the same revision; the
  a1 build is a newer, restructured MIDP-2.0 trial version (unlock-code
  registration UI) -- the full a1 class map is in `BUILD_COMPARISON.md`.


