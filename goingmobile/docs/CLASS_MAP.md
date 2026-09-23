# Going Mobile -- class map (phase 1)

> **Target changed 2026-09-22** to `roms/RAC-GoingMobile-a1.jar` -- see
> `ROADMAP.md`. This file's original content (below, "Class mapping"
> onward) is the **legacy build's** complete phase-1 map, kept as-is: it's
> a reference/pattern library for a1's read-through (shared physics/tile/
> font conventions per `BUILD_COMPARISON.md`), not itself the a1 mapping.
> a1's own phase-1 progress is tracked in a new section right below,
> **"a1 class map (in progress)"**, output to `../src_a1/` instead of
> `../src/`. Read that section first if you're working on a1.

## a1 class map (in progress)

Class-level mapping (from `BUILD_COMPARISON.md`'s cross-build comparison):
`a`=Font (standalone, not MIDlet-embedded like the legacy build),
`b`=Player, `c`=SoundPlayer, `d`=LevelMap, `e`=CanvasShell (`extends
Canvas implements CommandListener`, not `FullCanvas`), `f`=Enemy,
`g`=IntroManager (splash + unlock-code UI), `h`=Game (the engine, 195KB,
the big one), `i`=Entity, `j`=Projectile, `ratchetandclank`=MIDlet (name
kept). **Policy**: class-level renames are applied in every file as soon
as they're confirmed (here, straight from the table above), even in
classes whose own member-level phase-1 pass hasn't happened yet -- same
approach the legacy build's `tools/rename_gm.py` used (one pass, every
cross-file reference included). So e.g. `Entity.java` already says
`Game.F`/`Game.G`, not `h.F`/`h.G`, well before `Game` itself is done;
only *members* of an unfinished class stay obfuscated (documented at the
call site, or in that class's own not-yet-written section here). Output
tree: `../src_a1/`, mirroring `../src/`'s layout and hand-written-vs-
generated split once a renamer script exists (none yet -- current files
below were renamed by hand, not by `tools/rename_gm.py`, since a1's
structure differs enough from the legacy build's that the existing
script's per-file qualifier tables don't apply).

### Entity (from a1's `i`) -- done, in `../src_a1/Entity.java`

Same three methods as the legacy build's Entity (`column()`/
`setAnimState(byte)`/`wrapRow()`), confirmed structurally identical, plus
cross-checks against `b.java` (Player)/`f.java` (Enemy)/`h.java`/`g.java`
use sites. 17 fields total (same count as the legacy build's Entity), 14
renamed:

| Old | New | Evidence |
|---|---|---|
| `Z` | `kind` | `f.java` sets `-1` at spawn (same latent bug as the legacy build's Enemy) then indexes every per-type table 0-4; `b.java` also reads it (always effectively 0 for Player) |
| `aa` | `row` | `i.v()` (`wrapRow`) increments/decrements it on `ag` overflow |
| `ab` | `posX` | `i.u()` (`column`) derives column from it; `b.java` adds `ad` (velX) to it every tick |
| `ac` | `velY` | `b.java` subtracts it from `ag` (posInRow) every tick, clamped against a terminal-fall constant |
| `ad` | `velX` | `b.java` adds it to `ab` (posX) every tick |
| `ag` | `posInRow` | `i.v()`/`i.u()` (wrapRow/column math) |
| `ah` | `activeFlag` | set to `1` at spawn sites in `h.java`/`g.java` (int here, byte in the legacy build) |
| `ai` | `health` | initialized to 20 in `b.java`'s ctor, decremented by damage tables, `<=0` triggers the death anim |
| `aj` | `animFrame` | zeroed by `setAnimState`; short here (byte in the legacy build) |
| `ak` | `animCounter` | zeroed by `setAnimState`; short here (byte in the legacy build) |
| `al` | `animState` | set by `setAnimState`; indexes every per-anim table |
| `am` | `animRestart` | **tentative**: same 0/1/2-valued, set-alongside-death-anim shape as the legacy build's field, not independently confirmed |
| `an` | `animHold` | **tentative**: incremented/reset alongside anim-state changes, same shape as the legacy build's field, not independently confirmed |
| `ao` | `facingRight` | the only `boolean` field, matching the legacy build's |

Kept obfuscated (role not confirmed): `ae`/`af` (assigned from
column/row-helper method calls and read back immediately -- look like
transient collision-check scratch, not persistent per-object state like
the legacy build's `fieldW`/`spawnRow`), `ap` (zeroed at spawn/reset,
tested `== 0` in `b.java` physics, no other role evidence yet).

### SoundPlayer (from a1's `c`) -- done, in `../src_a1/SoundPlayer.java`

Same MMAPI background-thread design as the legacy build's SoundPlayer, but
6 cues instead of 7 (no PORT/KILLEN-equivalent) and mixed formats: `death`/
`bubble`/`shoot`/`msound`/`box` are `.wav`, only `menu` is `.mid` --
confirms `BUILD_COMPARISON.md`'s ".wav sound variants" note. One method
(`a(boolean)`, renamed `playMenuLoop` provisionally) forces the menu cue
to loop regardless of its unused boolean argument -- signature suggests a
listener/callback adapter; not confirmed. `playerUpdate`'s `endOfMedia`
check evaluates but discards its result (a real no-op in this build, not
a decompiler artifact) -- loop-end cleanup instead happens inline in
`run()`'s `getState() == 400` branch, unlike the legacy build which relied
on `playerUpdate` setting a pending-cleanup sentinel.

### Font (from a1's `a`) -- done, in `../src_a1/Font.java`

Unlike the legacy build (font embedded directly in `ratchetandclank`), a1
factors it into its own class, instantiated **twice**: confirmed at
`decompiled_a1/h.java:714,716` -- `ratchetandclank.h = new a("/f2.v", 10,
1)` and `ratchetandclank.f = new a("/f3.v", 13, 1)` (line height 10 vs
13, both spacing 1). `f2.v` is the same byte-identical asset as the
legacy build's; `f3.v` is new to a1. Same `fillRect`-glyph format and
same `charToGlyphIndex` two-range mapping (32-127 -> 0-95, 160-255 ->
96-191) as the legacy build's font -- this is the one class so far
that's a near-verbatim carry-over of legacy logic into a1's smaller
obfuscation pass, just repackaged as a standalone, dual-instantiated
class. All fields/methods renamed with high confidence (structural +
call-site evidence, no open questions).

### CanvasShell (from a1's `e`) -- done, in `../src_a1/CanvasShell.java`

The `Canvas`+`CommandListener` shell mentioned in `BUILD_COMPARISON.md`
-- confirms that write-up's note directly (a1 drops `FullCanvas` for a
real MIDP `Canvas`). Delegates every callback to one of two controller
objects on the MIDlet depending on a boolean gate: `ratchetandclank.b`
(type `g`, the intro/unlock manager) while not yet playing, or
`ratchetandclank.c` (type `h`, `Game`) once playing. **Field-letter note:
a1's MIDlet uses `b`/`c` for these in the *opposite* sense of what the
"a1 class map" table above implies from the legacy build's convention --
confirmed directly against `ratchetandclank.java`'s own declarations
(`public g b; public h c;`)**, so don't assume the legacy build's
letter-to-role habits carry over. Also pre-confirms one `ratchetandclank`
member ahead of that class's own phase-1: `public boolean i = false;` ->
`gameStarted` (defaults false = intro/unlock flow active), verified
directly in `ratchetandclank.java`. `ratchetandclank.b()`/`.c` clash
the same way `Game`'s legacy single-letter members did (field `b` +
method `b()` on the same class) -- left obfuscated here, to be settled
when `ratchetandclank.java` itself gets its phase-1 pass.

### ratchetandclank (a1's MIDlet) -- done, in `../src_a1/ratchetandclank.java`

Same RMS/settings/string-table role as the legacy build's MIDlet, high
confidence throughout (the RMS-quadruplet methods, settings byte layout,
and string loader are near-verbatim carry-overs, just different letters
and a 214- vs 220-byte save record). Two real findings:

- **Four static `Font` fields, not two**: `e`/`f`/`g`/`h` on the MIDlet.
  Only `h`->`smallFont` (`/f2.v`) and `f`->`largeFont` (`/f3.v`) are
  actually constructed (`new a(...)`, confirmed at `h.java:714,716`);
  `g`->`smallFontAlias` is set once, right between those two
  constructions (`h.java:715`, `ratchetandclank.g = ratchetandclank.h;`)
  and never reassigned anywhere else in any of the three decompiled
  trees -- so it's a permanent alias of `smallFont`. `e`->`currentFont`
  is the pointer actually passed to draw calls, reassigned between the
  other three throughout `g.java` (the intro/unlock/menu rendering). One
  conditional at `g.java:1185` picks between `smallFontAlias` and
  `smallFont` based on a boolean -- since they're always equal, that
  branch is dead code (same flavor of finding as the legacy build's `t`
  dead asset).
- **CanvasShell's `b()`/`c()` no-arg methods (here `stopSoundHard()`/
  `stopSoundHardOnLevelStart()`) have byte-identical decompiled bodies**
  (both just `this.soundPlayer.haltPlayer();`) -- kept as two separate
  renamed methods rather than merged, since the original bytecode had
  them as genuinely distinct obfuscated names; documented as a decompiler-
  confirmed duplicate, not a rename mistake.

The **game-flow orchestration methods are tentative**
(`startNewGame`/`continueGame`/`returnToIntro`/`playSoundIfEnabled`):
named from call shape (what they call and when), not confirmed against
`Game`'s (`h`) or `IntroManager`'s (`g`) own confirmed behavior, since
neither has had its phase-1 pass yet. Revisit once `g`/`h` are done --
in particular confirm `Game.c(int,int)`, `Game.m()`, and
`IntroManager.a(boolean)`/`.c()`.

**Retroactive fix to `SoundPlayer.java`**: its hard-stop method (a1's
`c.a()`, no-arg) was initially renamed `stopPlayer()` and marked
*private* -- wrong, since `ratchetandclank` calls it directly from
outside the class. Renamed `haltPlayer()` and made `public`, matching
the real (confirmed-by-compile-requirement) visibility.

### LevelMap (from a1's `d`) -- done, in `../src_a1/LevelMap.java`

Same core shape as the legacy build's LevelMap (28x18 byte-per-cell
grids, a `columnSolidMasks` bitmask, `PLATFORM_TILE_TYPES`/
`ENEMY_TILE_TYPES`/`BOLT_TILE_TYPES` tables that are an **exact 12/14/4-
entry match** to the legacy build's equivalents, tile ranges 43-46/
102-113/114-127 lining up too) plus a new layer: each level
(`/level0.bin`..`/level12.bin`) is split into up to 12 "rooms" (sub-
grids), with a compiled-in per-level table (`LEVEL_ROOM_TABLES`, 13
entries) describing room count, a room-to-room neighbor/connectivity
table, and a room-id-to-file-index map. This is **not** the `mapData.txt`
asset (that's loaded separately by `Game`, confirmed at
`decompiled_a1/h.java:2648` -- a world-map screen, unrelated to
`LEVEL_ROOM_TABLES`).

Only `LevelMap`'s own members are renamed; every `this.game.xxx` in the
giant room-activation method is Game's own obfuscated member (~30 of
them touched -- enemy pool, projectiles, moving platforms, pickups, zip
lines, exit tile, infolink state), intentionally left alone since `Game`
hasn't had its phase-1 pass. The tile legend (which id does what) is
**tentative** -- read off this method's own branches, not yet
cross-checked with a tool-verified parser the way the legacy build's
`tools/parse_gm.py levels` validated its tile legend; worth a dedicated
pass once enough of `Game` is confirmed to write an a1 equivalent of
that tool.

### Projectile (from a1's `j`) -- done, in `../src_a1/Projectile.java`

Same two-10-slot-pool design as the legacy build (confirmed by the two
spawn methods on `Game`: `e(int,int,int)` at `h.java:5500` for enemy
shots -> `Game.ak[]`, `a(int,int,int,boolean,boolean,int,byte)` at
`h.java:5533` for player shots -> `Game.al[]`), same 33 types, same
five-table shape (`DAMAGE_BY_TYPE`/`HALF_WIDTHS`/`HALF_HEIGHTS`/`SPEEDS`/
`RENDER_MODES`) -- confirms `BUILD_COMPARISON.md`'s "evolved tuning"
note (`RENDER_MODES` here really does use 13-24 with -1 sentinels vs.
the legacy build's 8-11). `halfWidth`/`halfHeight`/`sourceAnim`/
`playerOffsetX` all confirmed by tracing their spawn-time assignment in
`h.java` (not just from this file alone -- the first class here where
that cross-file spawn-site check was needed to pin a field down).

One field-reuse finding worth flagging for anyone reading `update()`:
the decoy drone (type 32) reuses `vx`/`prevY`/`prev2Y` for a bounce
countdown and min/max Y bounds -- unrelated to those fields' meaning for
every other type (velocity, trail history). Documented inline; caught a
transcription slip during this pass (`prev2Y` mistyped as `prev2X`,
fixed before commit) -- worth double-checking this kind of per-type
field reuse carefully in `Player`/`Enemy` too, since `Entity` already
has a few fields whose role shifts by subclass.

`homingSeek(int)` (from `a(int)`) has a parameter that's never read in
the original method body -- genuinely unused/vestigial, not a
transcription gap; kept for signature fidelity and documented.

`Enemy.b`/`Enemy.d` are referenced here (in the homing-seek center-y
calculation) as class-renamed-only -- strong circumstantial evidence
they're Enemy's per-type y-offset/height hitbox pair (mirroring the
legacy build's `Enemy` statics), but left obfuscated pending Enemy's own
phase-1.

### Enemy (from a1's `f`) -- done, in `../src_a1/Enemy.java`

Same 5-slot pool / 5-type shape as the legacy build, and the hitbox
table split confirms `BUILD_COMPARISON.md`'s note that canonical `/q` is
byte-identical to a1's `enemy_spr_box.bin`: `HITBOX_*`/`ATTACK_*` (both
x/y/w/h groups) load from there, `ANIM_FRAMES`/`ANIM_FRAME_COUNTS`/
`ANIM_FRAME_EXTRA` from `/enemy.bin` -- same three-table shape as the
legacy build's `p`/`q`/`r` from `/p`, just `[5][8][5]` instead of
`[5][6][5]`. `HP_BY_ANIM`/`DAMAGE_BY_ANIM`/`BOLTS_DROPPED_BY_ANIM`/
`ATTACK_WINDUP_TICKS` all confirmed by grepping *other* files (`Game`
sets spawned enemies' health from `HP_BY_ANIM`, `Player`/`Game` loop
`BOLTS_DROPPED_BY_ANIM[kind]` times on death) -- the first class here
where pinning a table down needed cross-file evidence beyond its own
source.

**Self-review caught two real mistakes before this was done**, both from
the same failure mode (assuming an obfuscated member's role by analogy
to the legacy build or to *this* class's own confirmed fields, instead
of checking what class actually owns it):

1. Called `Player`'s own `b()`/`c()` methods (accessed as
   `this.game.aj.b()`/`.c()`, `aj` = `Game`'s player field) `.y()`/`.x()`
   as if they'd already been renamed -- they're Enemy's *own* method
   names being renamed here, not inherited, and `Player` hasn't had its
   phase-1 pass yet. Also renamed two more of `Player`'s own
   not-yet-confirmed fields (`E`, `s`) to `invulnTimer`/`specialTimer` by
   analogy to the legacy build's `Player` field roles, without checking
   a1's actual `Player` source. All reverted to obfuscated
   (`this.game.aj.b()`/`.c()`/`.E`/`.s`).
2. Wrote `this.game.kind` in two spots -- confusing `Game`'s own
   obfuscated field `Z` with `Entity.kind` (same letter, unrelated
   field: `Entity.kind` is confirmed because `Enemy`/`Player` *extend*
   `Entity`, but `Game` does not). Reverted to `this.game.Z`.

**Lesson for `Player`/`IntroManager`/`Game`'s own passes**: a field
letter matching an already-confirmed name elsewhere is not evidence by
itself -- only inheritance (via `extends`) carries a confirmed member
name across files; every other same-letter member needs its own
evidence from the class that actually declares it.

Two more findings, both documented inline: a genuinely-unreachable
`else if` branch in `rangedAttack()` (`animKind == 7 || 10 || 13` is
already caught by the branch above it -- source-faithful, likely a
copy-paste bug in the original, not touched) and a dead static
initializer (`static { byte[] var10000 = new byte[]{10,10,10,10,10}; }`,
never stored anywhere -- same flavor as the legacy build's Vineflower
artifacts).

### Player (from a1's `b`) -- done, in `../src_a1/Player.java`

The densest class so far (41KB, 6 fields declared but never referenced
anywhere in this file -- `h`/`N`/`Q`/`R`/`S`/`T`/`U` -- presumably read
or written by `Game`, left obfuscated). Confirmed statics via exact-value
or exact-formula matches to the legacy build's already-documented Player
tables: `AMMO_CAPACITY` (`weapon*3+level` indexing, matches the legacy
build's `d` byte-for-byte in shape), `INITIAL_AMMO`/`AMMO_PER_PICKUP`
(byte-for-byte identical values to the legacy build's `e`/`f`).
`Player.x()`/`.y()` (pixel position) are confirmed to be the **opposite
letters of Enemy's** (`Enemy.b()`=y, `.c()`=x; `Player.b()`=x, `.c()`=y)
-- the Enemy.java header's warning about not assuming cross-class letter
consistency paid off immediately here.

**Self-review found and fixed a serious bug before this was done**, more
severe than Enemy.java's: an early draft collapsed three genuinely
distinct fields -- `s` (jumpPhase), `t` (a separate, rarely-touched
zip-grab-retry-delay field, confirmed distinct from the *method* `t()` =
`updatePickupMagnet()`, itself a field/method letter collision like
every other class here), and `u`/`v` (the real swing-phase state machine
and its countdown) -- into a single misleadingly-named field
(`ziplineCooldown`), because superficially-similar `if (this.X == N)`
branches *looked* like one state machine on a skim. This silently
swapped which field `handleTileInteractions()` (`m()`) read/wrote in
several branches. **Caught by systematically grepping the original for
every bare `this.<letter>` field access (no call parens) and building a
ground-truth table before trusting any rename** -- the same technique is
worth applying up front for `IntroManager`/`Game`, rather than as a
recovery step. After the fix, every other field in this file (`w`/`x`/
`y`/`z`/`A`/`B`/`C`/`D`/`E`/`F`/`G`/`H`/`L`/`M`/`O`/`P`/`V`/`W`/`X`/`Y`)
was independently re-verified against that same ground-truth grep and
checked out correct on the first pass.

One cross-class finding worth flagging for `Game`'s own phase-1:
`Player.fire()` calls `Game.e(int,int,int)` to spawn projectiles -- the
*same* method `Projectile.java`'s header speculated (from a single
`h.java` call site) was the enemy-shot-specific spawner writing to
`Game.ak[]`. It evidently isn't enemy-only; `Projectile.java`'s header
comment should be corrected once `Game`'s phase-1 clarifies what it
actually does.

### IntroManager (from a1's `g`) -- done, in `../src_a1/IntroManager.java`

**Scoping decision, different from every class so far**: at 58KB with
dozens of densely-interacting single-letter fields across a 1754-line
menu/splash/unlock-code state machine, this file's *own* members
(`b`/`c`/`d`/`e`/`f`/`g`/`h`/`i`/`j`/`k`/`l`/`m`/`n`/`o`/`p`/`q`/`r`/`s`/
`t`/`u`/`v`/`w`/`x`/`y`/`A`/`B`/`D`/`E`/`F`/`G`/`H`/`I`/`J`/`M`, and most
of the per-screen render/input-handler methods) are **left obfuscated on
purpose** -- the same treatment the legacy build gave its own `Game`
class. Attempting a full member-level rename here, at this size and
density, risked repeating `Player.java`'s `u`/`v`/`t` mix-up at a much
larger scale. What *is* renamed: the class itself, cross-references to
every already-confirmed member of other classes (`Game`/
`ratchetandclank`/`Projectile`/`CanvasShell`), and a handful of
IntroManager's own unambiguous fields (`splashImages`, `display`,
`midlet`, `recentKeyHistory`, `CHEAT_CODE_KEYS`).

**Produced mechanically** (a small Python find/replace script over
`decompiled_a1/g.java`, not hand-transcribed) to avoid manual-copy
transcription errors at this size -- then verified with the same
bare-`this.<letter>` grep discipline as `Player.java`'s recovery, applied
*before* trusting the output this time rather than after. Caught three
more real bugs this way: (1) the script's `h.` -> `Game.` substitution
ran before the `ratchetandclank.h` -> `ratchetandclank.smallFont`
substitution, so the two literal occurrences of `ratchetandclank.h.a`
(a `Font` field access, `.a` = `Font.lineHeight`) got corrupted into
`ratchetandclank.Game.a` -- fixed to `ratchetandclank.smallFont.
lineHeight`; (2) a decorative-background-projectile method
(`a(Projectile)`) had its parameter *type* renamed by the script but not
its field accesses (`var1.f`/`.g`/`.h`/`.m`/`.n`/`.o`) or method calls
(`var1.a()`, `var1.a(1)`) to `Projectile`'s already-confirmed names --
fixed by hand to `.type`/`.posX`/`.posY`/`.vx`/`.vy`/`.age`/`.reset()`/
`.homingSeek(1)`; (3) one `this.midlet.c != null` null-check used `!=`
where the script's regex only covered `==`, leaving a stray
`this.midlet.c` -- fixed to `this.midlet.game`. **Lesson for `Game`'s
phase-1** (also large enough that scripted substitution may be worth
it): run the substitution, then grep the *output* for every remaining
bare single-letter access before considering it done, and specifically
check every "type gets renamed but field accesses through it don't"
spot -- that failure mode didn't show up in the earlier hand-written
files because there was no type-only rename step to miss.

**What this class actually is**, confirmed by reading the whole file:
the MIDlet's pre-game controller. A splash sequence (3 logo images), a
step-by-step boot sequence that constructs `Game` over ~39 ticks
(calling an unidentified `Game.a(int)` init-step method once per tick),
the entire menu system (~20 screens: main menu, language, sound toggle,
new-game slot picker, delete-save picker, a shared save-slot confirm
screen, credits/help/about, and the trial's unlock-code `Form`/
`TextField` UI), a decorative animated background of real `Projectile`
instances during the menu, and a Konami-style 4-key cheat sequence
unlocking `Game.r` -- almost certainly the same "invincibility cheat"
role as the legacy build's `Game.i`/`Game.j`. **Structural finding**:
the legacy build's menu system lived *inside* `Game`/`f`; in a1 it moved
entirely into this separate class, meaning a1's `Game` is presumably
gameplay-only. Worth confirming once `Game`'s own phase-1 is done.

### Not started yet

`h` (Game, 195KB) is the only remaining class -- larger than the legacy
build's entire `f.java` engine class (127KB) by itself. Given
`IntroManager` apparently absorbed the legacy build's menu system, a1's
`Game` may turn out to be *smaller* in scope than its raw size suggests
(more gameplay ticks/state, less menu code) -- or the size difference is
something else entirely; no assumption either way until it's read.
`Player.java`'s lesson (full `this.<letter>` ground-truth grep before
trusting any bare-field rename) and `IntroManager.java`'s lesson (if
using scripted substitution, re-grep the *output*, and check every
type-only rename for missed field/method accesses through it) both
apply -- `Game` is large enough that a mixed approach (scripted safe
renames + hand verification, like `IntroManager`) is probably the right
call rather than fully hand-transcribing 195KB.

---

## Legacy build's class map (done, reference only)

`src/` is the phase-1 renamed reference tree for the **legacy build**
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

Tile legend (from the level parser `a(int, boolean)`; tool-verified by
`tools/parse_gm.py levels`):
0-3 background, **4-13 solid** (rendered from `a.png`, 22x14 cells),
14-35 solid specials (35 = the level exit), 36-42 foreground decoration,
43-46 titanium-bolt boxes, 47-55 enemy spawns (types 0/2/1; tiles 50-52
also drop a bolt box), 56-61 hazards, 62-69 zip-line endpoints,
70-76 background, 77-96 infolink tiles, 97-98 hyper-shot targets,
99-101 markers, 102-113 moving platforms, 114-127 + 0x80 enemies (14
types via the table), 0xA3 player start, 0xA1/0xA2/0xA4..0xC4 camera and
marker tiles. Note: the shipped level set is **n1..n10 + n12** -- level
11 is skipped by the level select (which maps 11 -> the boss 12), so
the `Q == 11` 12-grid read is dead code in this build, and
`LevelMap`'s special-tile continuation indexes `tiles[x][y+1]`,
`[y+2]`/`[x+1]` -- a latent out-of-bounds the shipped levels never
trigger (the port must bounds-check).

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


