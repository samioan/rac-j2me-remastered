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

      **Menu-screens slice** (third session): `IntroManager::b_(int)`
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

      **Still stubbed** (needs `Player`/`Enemy` behavior methods --
      `render()`/`updateAnimation()`/`fire()` -- which don't exist yet,
      same milestone-3.2a boundary as `Game`'s own tick/render): the
      main menu's decorative Player+4xEnemy walk-in animation, the
      weapon store's decorative player pose/projectile render, and the
      idle-tick cases that would drive both (`IntroManager::i_()`'s
      screen-1/5 branches stay no-ops). Real `Command`/`Displayable`
      construction (the soft-key `Command` objects `CanvasShell`'s
      `commandAction` would receive) is also not started -- the port
      drives everything through raw key codes instead, which is
      sufficient for a numeric-keypad-equipped input path but not for a
      softkey-label-driven one. The MIDP `Canvas` key-code mapping
      (Nokia negative codes assumed to reach a plain `Canvas` the same
      way they reach `FullCanvas`) remains an unverified assumption.
      Legacy build's `game.h`/`.cpp` etc. stay in `port/src/` as a
      transcription reference, not deleted.

- [ ] **3.2a -- gameplay.** Same scope as the legacy build's stubbed 3.2
      (`LevelMap`'s level parser and 22x14-equivalent tile renderer at
      a1's resolution, `Player`/`Enemy`/`Projectile` physics/AI/rendering,
      `Game`'s tick-loop internals, boss fight, HUD), transcribed from
      a1's `h`/`b`/`f`/`j`/`d` instead of the legacy build's `f`/`a`/`d`/
      `g`/`c`, and reading a1's asset formats (`ROADMAP.md`'s phase 2 for
      a1: `enemy.bin`/`player.bin` state scripts, `mapData.txt`, the
      13-level `level*.bin` set) once confirmed and tool-verified there.
