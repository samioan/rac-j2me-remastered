# Port Roadmap

> **Target changed 2026-09-22**: the port target is now
> `roms/RAC-GoingMobile-a1.jar` (176x220), not the original
> `RAC-GoingMobile.jar` (128x128) -- see `ROADMAP.md`'s target-change note.
> Milestones 3.0-3.1 below were built against the *original* build, whose
> reverse-engineering (phases 0-2) is fully done (`../src/`,
> `CLASS_MAP.md`'s canonical section). That port code and the design
> decisions under it are kept as a reference/pattern -- Win32+GDI,
> `Game`/`Player`/`Enemy`/`LevelMap`/`Projectile` structure, the MIDP/RMS
> shim -- but it is not the shipping port, since a1 is a different
> revision (different screen size, engine split, asset formats) and its
> own phase 1/2 (`ROADMAP.md`'s "Status") haven't started. Once a1's
> phase 1 is far enough along, the port resumes as milestone **3.1a**
> (re-base the 3.1 boot/menu/save slice onto a1's classes and 176x220
> screen) before 3.2 (gameplay) is attempted for a1.

Tracks `port/`, the actual PC port -- as opposed to [`ROADMAP.md`](ROADMAP.md),
which tracks the reverse-engineering work that feeds it. Follows
the sibling `tes-travels-decomp`/`shadowkey-decomp` projects' precedent (their
own `docs/PORT_ROADMAP.md`): milestone by milestone, each one landing a real,
buildable/runnable slice.

## Decisions carried through every milestone

- **Behavioral reimplementation, not byte-exact recompilation** (per
  `ROADMAP.md`'s phase-3 note), but *transcribed* as literally as C++ allows
  from `../src/`'s renamed-where-confident Java -- not a from-scratch
  rewrite. Obfuscated methods that never got a confident phase-1 rename keep
  their single-letter name with a trailing `_` (`a()` -> `a_()`, `b()` ->
  `b_()`, ...); renamed members keep their `CLASS_MAP.md` name.
- **Obfuscated *data* members never get the trailing `_`.** C++, unlike
  Java, can't have a field and a method share one name in the same class --
  Java's `Player.a` (a static table) and `Player.a()` (a method) coexist
  fine, but their direct C++ transliteration doesn't. The convention (see
  `game.h`'s own `a`/`b`/`f`/`g`/... static fields vs. its `a_()`/`b_()`/...
  methods) is: methods always keep the `_`, obfuscated data fields never do.
  `player.h`/`levelmap.h` initially got this wrong (every static table was
  named with the same `_` suffix as the behavior methods reading it) and
  didn't compile; fixed in 3.1 once a real build actually exercised them.
- **C++17, CMake + Ninja, MSVC**, matching the sibling ports' toolchain and
  this repo's existing `port/` scaffold (`build.bat`).
- **Raw Win32 + GDI (`StretchDIBits`), no SDL2/D3D/GL** -- the original is a
  software-rendered canvas, so a Win32 window blitting a manually computed
  backbuffer reproduces that architecture directly with zero external
  dependencies, same rationale as the sibling ports. Backbuffer size
  matches the target build's screen: 128x128 for milestones 3.0-3.1
  (legacy build), **176x220 for a1** (milestone 3.1a onward).
- **Assets stay out of the repo.** The port reads resources (`/n1`, `/r`,
  `/f2.v`, `.png`s, ...) from a configurable data directory at runtime
  (`setDataDir`/`resourcePath` in `midp.h`), defaulting to a handful of
  candidate paths relative to the exe that all resolve to this project's
  existing `tools/extract_jar.py` output (`../extracted/`) -- nothing
  copyrighted is copied into git.
- **No MIDlet manifest API in the port.** `ratchetandclank.getAppProperty`
  calls (the `MIDlet-Spec-Code`/`MIDlet-Version` lookups) have no PC
  equivalent; ported call sites either fall back to the property's likely
  absent-in-the-real-build value (`t_()`'s spec-code check) or substitute the
  confirmed retail version string ("1.0.9", per `ROADMAP.md`'s "The target"
  section) where the original would have shown it (the about-screen "??"
  placeholder in `f_()`'s menu handler).

## Milestones done

- [x] **3.0 -- scaffold** (`32a186e`, "Phase 0: project setup"). CMake +
      Ninja + MSVC project proves the toolchain: opens a blank 128x128 Win32
      window. No tick loop, no backbuffer, no game logic.

- [x] **3.1 -- boot/splash/menu/save surface** (this session). The complete
      non-gameplay slice of the game: MIDlet lifecycle, RMS save records,
      the `.txt` string-table/word-wrap/bitmap-font pipeline
      (`midlet.h`/`.cpp`), the MIDP/Nokia-UI shim (`midp.h`/`.cpp` --
      `Graphics`/`Image`/`RecordStore`/`SoundPlayer`, plus a real Win32
      window + GDI presentation + input dispatch in a new `platform`
      namespace), and `Game`'s entire boot chain, splash sequence, every
      menu page, name entry, high scores, the store, and save/load
      (`game.h`/`.cpp`). `Player`/`Enemy`/`LevelMap`/`Projectile` ship their
      complete static data tables and constructor-faithful initialization;
      their behavior methods (physics, AI, rendering, the level-file parser)
      are declared and stubbed, deferred to 3.2 alongside `Game`'s own
      b==0 (in-level) simulation methods (also declared + stubbed here so
      the boot/menu code that calls into them links).

      This milestone picked up mid-flight: `game.cpp` had stopped
      mid-generation (a literal `//__GAME_CPP_APPEND__` marker at EOF,
      roughly a third of `game.h`'s declared boot/menu methods missing --
      `paint`/`keyPressed`/`keyReleased`/`run_`/the whole `f_()` menu-input
      handler/save serialize-deserialize/the splash-to-menu chain/etc.), and
      nothing in `port/` had ever actually been built -- `CMakeLists.txt`
      still only compiled the placeholder `main.cpp`. The remaining ~1,500
      lines of boot/menu/save logic were transcribed from `../src/Game.java`
      to close out the milestone as scoped in the file's own header
      comments, then the project was wired up (`CMakeLists.txt`, a real
      `main.cpp` boot loop) and iterated against the real MSVC build until
      it linked clean.

      **Real bugs found while getting this to actually compile and run**
      (none were caught by the earlier read-through phases, since nothing
      had been built yet):
      1. `Enemy`'s constructor was a no-op, leaving every pool slot's `kind`
         at `Entity`'s default (0 == "alive, type 0") instead of the
         original's explicit `-1` ("inactive slot") -- `Game::l_()`'s
         live-enemy count would have read 5 enemies alive at boot instead
         of 0. Fixed to match `Enemy(Game)`'s real field inits.
      2. `Player::c` (renamed from `c_`, see below) was declared `[99]` in
         `player.h` but defined `[90]` in `player.cpp` -- a hard redefinition
         error. Cross-checked against `Player.java`'s literal array (90
         elements, lines 9-100): the header was wrong, not the definition.
      3. `ratchetandclank`'s font code had two distinct bugs from the same
         root cause: `a_(char)` (char width) and a private `a_(char)`
         (char-to-glyph-index) were declared as static/non-static overloads
         of the identical signature -- illegal in C++, and a `Player.java`
         line-442-equivalent method that doesn't actually exist as two
         separate Java methods (there's only one `a(char[], String, int)`,
         doing the position-lookup *and* the index conversion together, and
         a single public `a(char)` that calls it with both source args
         null so the "index" parameter is read as the char value directly).
         The C++ port's 4-argument position-lookup helper had also never
         actually done the char-to-glyph-index conversion it names -- it
         returned the raw char code, meaning every render call was
         indexing `fontGlyphs` (192 entries) by character code (up to 255)
         instead of glyph index. Fixed by merging both into the one real
         Java method's actual behavior.
      4. Several straight typos of the same shape: `midlet.cpp` called
         `a(...)`/`b->a(...)`/`b->b(...)` where it meant `a_(...)`/
         `b->a_(...)`/`b->b_(...)` (missing the obfuscated-method
         underscore) -- silently resolves to nothing or a different
         overload rather than failing loudly in a dynamically-obfuscated
         reading, but is a hard "not a function" error in C++.
      5. `Game::u_()` (the moving-platform tick, called from `v_()`) was
         declared and called but never defined at all -- a link error, not
         a compile error, so nothing upstream of the linker step would have
         caught it.
      6. `midp.cpp` `#define WIN32_LEAN_AND_MEAN` before `#include
         <gdiplus.h>` -- GDI+'s headers need `IStream`/`PROPID` from
         `<objidl.h>`, which `WIN32_LEAN_AND_MEAN` strips out of
         `<windows.h>`. Fixed by including `<objidl.h>` explicitly first.

      **A gap in `game.h`'s own method inventory**, found while
      cross-referencing every "ported" method against `Game.java`:
      `keyReleased()` calls a no-arg `i()` (clears the charge-shot state,
      releases the player's attack) that game.h's gameplay-stubs section
      never declared at all. Small enough to port for real rather than stub
      (`Game::i_()`), since `keyReleased` needed it either way.

      Verified by building clean with zero errors via `port/build.bat`
      (CMake + Ninja + MSVC, `/W4`) and launching the real
      `goingmobile_port.exe` against the real extracted canonical-build
      assets: the window opens, the boot chain runs (font/save-record/
      settings/sound init, then the sony/hhg/logo splash chain), and it
      stays up and responsive with no crash. No gameplay to verify yet by
      design -- `b==0` (in-level) is still fully stubbed.

## Milestones remaining (re-based onto a1)

3.2 (legacy-build gameplay) is superseded, not pursued -- 3.1 already
proved the boot/menu/save architecture works, so the next real work is
re-basing onto a1 rather than finishing the legacy build's gameplay.

- [ ] **3.1a -- re-base boot/splash/menu/save onto a1.** In progress.

      **Scaffold slice** (first session): a new `port/src_a1/` tree, built
      as a separate `goingmobile_port_a1` CMake target alongside the
      legacy build's (untouched, still `port/src/`). The MIDP/Nokia shim
      (`midp.h`/`.cpp`), `Font` (new standalone class, a1 factors it out
      of the MIDlet), `ratchetandclank`'s MIDlet lifecycle and RMS saves
      (**confirmed 214-byte game-slot records**, not the legacy build's
      220-byte layout), and `CanvasShell`'s input/paint dispatch (the
      actual `Canvas`+`CommandListener` re-base point) are real.

      **Boot chain + splash slice** (second session): reaching even the
      splash screen turned out to need much more than `Game`/
      `IntroManager` stubs -- `Game::runBootStep()` (all 42 steps) loads
      30+ image assets and constructs real `Player`/`Enemy`/`LevelMap`/
      `Projectile` objects (their static data tables, constructors, and
      `loadAssets()`/`player.bin`/`enemy.bin`/`enemy_spr_box.bin` parsers
      are now real and building clean, `/W4` zero errors -- behavior
      methods stay declared-and-stubbed, same precedent milestone 3.1 set
      for the legacy build). `IntroManager`'s boot dispatcher and splash
      sequencer (3 logos with a progress bar, `Game::t()`'s `mapData.txt`
      parse) are transcribed and **visually verified** by screenshotting
      the running `goingmobile_port_a1.exe`: the real "Ratchet & Clank
      Going Mobile" splash logo renders correctly, matching the original
      asset. No crash.

      **Structural finding correcting `src_a1/README.md`'s framing**:
      `IntroManager` owns the *pre-game* flow (splash, main menu, language/
      sound settings, save slots, credits/help/about, the unlock-code UI),
      but `Game` independently has its own ~20-screen state machine (field
      `b`, `keyPressed`/`keyReleased`/`d(Graphics)` et al.) for the
      *in-level* UI -- pause menu, weapon store, results, game over --
      reached only once gameplay starts. That slice is gameplay-adjacent
      and stays deferred to milestone 3.2a, not 3.1a.

      **Menu-screens slice** (third session; later verification pass
      screenshotted every reachable screen -- slot pickers, skin
      message, Help/About pagers, exit confirm, language -- and fixed
      `Game::a_(Graphics*,String,x,y,anchor,width)` to a line-for-line
      transcription of Game.java's word-wrap, which centers at x=88
      when anchor has the HCENTER bit): `IntroManager::b_(int)`
      (keyPressed) is real, and the paint dispatch now covers 11 of the
      ~20 screens -- main menu, options (sound/delete-save/language),
      save-write picker, weapon store, delete-save picker, a shared
      Yes/No confirm, the credits/help/about pager, exit-game confirm,
      new-game slot picker, "Get Ratchet Skin" message, and language
      select (states 1/2/4/5/6/7/8/9/11/16/20) -- with real per-screen
      input handlers, the Konami-style cheat-code recorder, and every
      shared widget (selection-highlight bracket, softkey hint row,
      bordered wallpaper backdrop, save-slot label, page-indicator
      footer). **Visually verified** across four distinct screens
      (language select, main menu, options, delete-save picker) by
      screenshotting the running exe and driving it with real key input
      (English selects into the main menu; New Game/Load Game/Get Ratchet
      Skin/Settings/Help/About/Exit all render with correct labels and
      selection highlight; Settings -> Clear Save reaches the 3-slot
      picker showing "1.(empty)" etc). States 15/17 (the unlock-code
      Form/TextField entry and its post-submit screen) stay unreachable/
      untranscribed -- gated behind `isGameWon()`, and the shipped a1.jar
      carries no Unlock-Code manifest attribute either. States 3/10/12/
      13/14/18/19 are unused by `IntroManager` itself (case 10 just blanks
      the screen).

      Two things were deliberately given SIMPLIFIED (not stubbed, not
      pixel-exact) bodies rather than reverse-engineered ones, since
      `Game`'s own phase-1 read-through is still deferred to 3.2a (see
      `game.h`'s note): `Game::e_()` (full-screen backdrop -- tiles
      `Game::aF` across the whole canvas instead of whatever the real
      compositing does) and `Game::k_()` (a content-area top-y constant).
      `Game::a_(Graphics*,String,...)` (word-wrap paragraph draw) and
      `Game::a_(String, {...})` (`%N` placeholder substitution) ARE exact
      -- generic MIDP text layout and pure string logic, no `Game`
      internals needed. One interesting confirmed-by-transcription
      finding: the shared text-draw helper's `anchor` parameter is
      **dead** in the original (`IntroManager.java`'s 7-arg `a(Graphics,
      String,...)` core always hardcodes `HCENTER|TOP` regardless of what
      callers pass) -- invisible at the screen-center x values every
      other screen uses, but visible on the language-select screen (which
      passes x=0), where each item's text is centered *on* x=0 and so
      shows only its trailing characters. Transcribed faithfully, not
      "fixed."

      **Sprite render slice** (fourth session): `Player::updateAnimation`/
      `resetAttackAnim`/`render`, `Enemy::updateAnimation`/`render`, and
      Game's `b`/`p`/`c`/`a(g,x,y,int)` sprite helpers are real, plus the
      main-menu (state 1) and weapon-store (state 5) entry setup and idle
      ticks. `Graphics::drawImageManip` now translates Nokia DirectGraphics
      codes (90/180/270/8192/16384 and sums) and its MIRROR_ROT_90 case was
      fixed. Verified: enemies render on the main menu, the store shows the
      player with a held weapon. Not yet: `Player::fire()` and
      `Projectile` update/render (weapon demo shots), and the main-menu
      walk-in was not caught mid-pass in screenshots.

      **Still stubbed**: Real `Command`/`Displayable`
      construction (the soft-key `Command` objects `CanvasShell`'s
      `commandAction` would receive) is also not started -- the port
      drives everything through raw key codes instead, which is
      sufficient for a numeric-keypad-equipped input path but not for a
      softkey-label-driven one. The MIDP `Canvas` key-code mapping
      (Nokia negative codes assumed to reach a plain `Canvas` the same
      way they reach `FullCanvas`) remains an unverified assumption.
      Legacy build's `game.h`/`.cpp` etc. stay in `port/src/` as a
      transcription reference, not deleted.

- [ ] **3.2a -- gameplay.** In progress. **Level-render slice** (fifth
      session): New Game starts level 0 and renders it (LevelMap load/
      enterRoom/render, Game spawn helpers, camera, HUD, Projectile).
      **Playable slice** (sixth session): `Player::tick`/physics/fire/
      melee/zip-ride/pickups, `Enemy::tick`/AI/attacks, `Game::tick` (the
      `b == 0` gameplay body, collisions, moving platforms, projectiles),
      keyPressed/keyReleased, room-edge transitions, respawn, the hint/
      dialogue panel (`cz`/`cA`/`cB` tables generated from Game.java), and
      level 0's intro cutscene (`Game.d(byte)`; the decompiled tick shows
      `this.abs(this.k)` at its call site -- a decompiler artifact, treated
      as `d(k)`). Screenshot/keyboard-verified: dialogue paging with
      portraits, Clank's drop, the zip-line ride, HUD, running left/right
      and jumping. Two decompiled writes to a STATIC `tileWidth` through an
      instance (`enemies[i].tileWidth = 0`, `player.tileWidth = ..`) would
      zero the tile divisor and crash the original, so they are omitted.
      Pause menu (game_menus.cpp, verified by screenshot): Esc/Backspace
      opens `Game.d(g)` (Continue / Settings / Main Menu / Exit Game, with the
      scrolling mission-objective ticker), sound toggle (b==5), quit-to-menu
      (b==8, -> returnToIntro) and quit-to-desktop (b==7) confirms. Game.e(g)
      is now exact (wpnhud frame at y=75) instead of the old tiled backdrop.
      Level flow (game_flow.cpp): results pages (b==18, `Game.a(byte)` score
      maths), Save/Replay menu (b==21), "Game Saved" info (b==10), world-map
      level select (b==3, `Game.j(g)`/`m(key)`) and `Game.a(int,short)` level
      load, so Enter on a map node loads that level (level 1 verified to
      render; screens verified via a temporary state-jump hook, since removed).
      Store/challenge slice (game_store.cpp): Extras list (b==1, map node 18),
      weapon store + purchase confirm + not-enough-bolts (b==11/12/13, node
      19), and challenge arena briefing/start/tick/reward/failure (b==14/15/19,
      `Game.s(int)`/`A()`); screens verified via a temporary state-jump hook
      (removed). The Player sprite overlaps the store text column as drawn by
      `Player::render(g,0,0,-20)`; not compared against the original.
      Boss slice (game_boss.cpp): level-12 boss intro cutscene (b==16), boss
      AI/damage/render (Game.M/L/o/F, `c(g,byte)`), win outro (b==24), "You
      Win!" stats (b==2) and the Challenge Mode prompt (b==23 -> `Game.O()`).
      Verified by screenshots (intro dialogue, rotating cannon firing, outro,
      stats, prompt) using temporary env-var hooks that jumped to level 12 /
      forced the win, since removed. Boss damage from real player shots was
      not exercised.
      Bug fix found while testing: Player.java's landing checks read
      `this.x() < floor` / `this.x() >= floor` (a decompile x/y swap); with
      the literal transcription the player fell through floors in some rooms.
      player_behavior.cpp now uses `y()` there (lines "jumpPhase >= 0 ||
      y() < floorY" and "y() >= floorY && velY < 0"). Other `x()`/`y()`
      pairs in Player.java that look swapped (enemy-stomp check, line ~730 of
      Player.java) were left literal and may need the same treatment.
      Weapon wheel + save/load slice: in-game weapon wheel (b==22, `#` key;
      the port maps Tab to `#`), `Game.writeSaveData/readSaveData` (214-byte
      slot format) and the dU load path. Verified by screenshots (wheel cycling
      and closing; a slot written on level load then listed in Load Game and
      loaded to the level-select map). The phase-1 IntroManager Load Game
      handler calls `writeSaveSlot(G)` where only `continueGame(G)` makes
      sense (nothing else calls continueGame), so the port calls continueGame.
      Sound: already real since the boot slice (MCI-backed `SoundPlayer`,
      menu.mid looped, five .wav cues). Verified this slice: menu.mid opens
      and plays (MCI rc=0), all five .wav cues open. Every Game/Player
      `playSoundIfEnabled` call site in the Java tree is ported except the one
      in Game's unused own slot picker (b==9). Not audibly compared.
      Controls (`input.cpp`): keyboard + XInput gamepad map to logical actions, which are
      translated to the original handset key codes per context (gameplay / menu+dialogue /
      text entry). Keyboard: A/D or arrows move, W/Up/Space jump, J/X/LCtrl fire, K/C melee,
      Q/E/Tab weapon wheel, left mouse = fire (which melees on its own when an enemy is adjacent), right mouse = weapon wheel (Back in menus), Esc/P pause, Enter confirm, Backspace back. Gamepad: stick/D-pad
      move, A jump, X/RT fire, B/LT melee, Y/bumpers wheel, Start pause, Back = back. In menus
      Space/J/Enter/A confirm and Esc/K/Backspace/B go back. Bindings are two tables at the
      top of `input.cpp`, the hook for a remapping screen. Gamepad path not hardware-tested.
      Display: starts in borderless fullscreen (`--windowed` opts out); F11 /
      Alt+Enter toggles it; the
      176x220 canvas is fit with its aspect ratio kept (black bars) via
      `computeViewport()` in `midp.cpp`, which only reads the canvas size --
      the hook where a widescreen mode (wider logical canvas) plugs in.
      Widescreen (`screen.h/.cpp`): Settings > Resolution (main menu and pause
      menu) cycles Original / Auto / 4:3 / 16:10 / 16:9 / 21:9, saved in
      `%LOCALAPPDATA%ac-gm-port-a1\display.cfg`. Height stays 220; the canvas
      width becomes `screen::width`. World rendering, culling, camera and the HUD bar
      (hud.png tiled, never stretched) use the full width; every 176x220 design
      screen (menus, dialogue text, cutscene overlays) is drawn unscaled at
      `screen::offsetX()` between black bars. Menu wallpaper: its edge columns are continued outward (`screen::captureEdges/paintSideBars`).
      FPS (Settings > FPS: Original/60/90/120/144/165/240/Unlimited): logic stays on the
      fixed ~30 Hz step; extra frames are drawn interpolated (`game_interp.cpp`
      blends camera, player, enemy and projectile positions at render time, skipping
      jumps > 48 px). Only live gameplay (b == 0) is drawn between ticks.
      Tutorial fixes (level 0 was not completable): (1) `Enemy.groundYAhead`
      scales the found row by tileHeight and then tests `r >= 18`, always
      true, so every enemy's ground was the map bottom and it fell through
      the floor; the row test now precedes the scale. (2) `src_a1/Enemy.java`
      names the static copy of `Game.H` "hudHeight" and uses it in `y()`,
      `attackBlockedByPlatform` and the AI's dy, but the raw decompile
      (`decompiled_a1/f.java`) uses `Game.K` (44) there; only the render
      clip uses `H`. With hudHeight the enemy sprite/hitbox sat 24px below
      the floor and Ratchet's melee rectangle missed it, so it could never
      be killed and the post-kill cutscene never fired. Fixed in enemy.cpp/
      enemy_behavior.cpp. (3) Player.java's enemy-stomp check
      (`enemy.y() > this.x()`, `this.y() > enemy.x()`) is the same x/y swap
      as the landing check; now `en->y() > y()` and `x() > en->x()`.
      Verified by playing level 0 through: enemy on the floor, melee kill,
      Clank cutscene, zip-line ride and landing.
      Further src_a1 naming errors found by diffing against the raw decompile
      (`decompiled_a1/b.java` = Player, `h.java` = Game): Player's own
      `b()`/`c()` are x/y, so `updatePickupMagnet`'s reference y and the
      ledge-snap wall pushes had x/y swapped (fixed); Game.B() writes the
      platform-carry field `Player.H` (an instance short, i.e.
      `ledgeSnapOffsetX`), not the static hudHeight copy (fixed). The
      tutorial's teleporter Down-press calls `Game.a(C, D)`, which was still
      a stub; it is now `Game::exitLevel(-1)` (save, reload menu backdrop,
      world map), so level 0 now ends at the level-select map. Verified by
      playing: tutorial -> map -> Circuit Circuit; crates, bolts, Lancer
      pickup, wrench kill (`/` = melee; Enter only melees on a lock/enemy or
      with no ammo), wall-jump shaft, grind rails, hint panels.
      Remaining for 3.2a: an end-to-end playthrough of levels 1-11 (only
      the first section of level 1 and level 12 have been exercised) to shake out further
      transcription bugs of this kind (src_a1's renamed fields can be wrong;
      cross-check against decompiled_a1 when behaviour looks off), Game's
      own unused slot picker (b==6/9), and a pixel/behaviour comparison
      against the original.
      Original scope: Same scope as the legacy build's stubbed 3.2
      (`LevelMap`'s level parser and 22x14-equivalent tile renderer at
      a1's resolution, `Player`/`Enemy`/`Projectile` physics/AI/rendering,
      `Game`'s tick-loop internals, boss fight, HUD), transcribed from
      a1's `h`/`b`/`f`/`j`/`d` instead of the legacy build's `f`/`a`/`d`/
      `g`/`c`, and reading a1's asset formats (`ROADMAP.md`'s phase 2 for
      a1: `enemy.bin`/`player.bin` state scripts, `mapData.txt`, the
      13-level `level*.bin` set) once confirmed and tool-verified there.
