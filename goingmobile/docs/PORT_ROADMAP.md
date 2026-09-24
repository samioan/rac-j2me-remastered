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
      Scaffold slice done (this session, mirrors milestone 3.0's original
      scope applied to a1): a new `port/src_a1/` tree, built as a separate
      `goingmobile_port_a1` CMake target alongside the legacy build's
      (untouched, still `port/src/`). Real and building clean (`/W4`,
      zero errors) as of this slice: the MIDP/Nokia shim (`midp.h`/`.cpp`
      -- Graphics/Image/RecordStore/resource-loading carried over verbatim
      from milestone 3.1's, since that layer isn't build-specific; a1's
      own `SoundPlayer` re-modeled for 6 cues + a .wav/.mid mix instead of
      the legacy build's 7 all-.mid cues; screen resized to 176x220; new
      `Command`/`Displayable` placeholders for the `CommandListener` input
      model), `Font` (new standalone class, `font.h`/`.cpp`, transcribed
      from `Font.java`), `ratchetandclank`'s MIDlet lifecycle and RMS
      saves (`midlet.h`/`.cpp` -- **confirmed 214-byte game-slot records**,
      not the legacy build's 220-byte layout, verified by a real
      verify/rebuild/write/read round trip against the real Win32
      RecordStore shim), and `CanvasShell`'s input/paint dispatch
      (`canvasshell.h`/`.cpp`, the actual `Canvas`+`CommandListener`
      re-base point). Verified by building clean and launching
      `goingmobile_port_a1.exe`: the 176x220 window opens, the MIDlet
      boots (RMS store verify/rebuild, save-slot summaries, font/sound
      init), and it stays up with no crash.

      Declared and stubbed (real bodies still to come, matching the
      "declared and stubbed until built" pattern milestone 3.1 itself used
      for `Player`/`Enemy`/`LevelMap`/`Projectile`): `Game` and
      `IntroManager`, to just the call surface `CanvasShell`/
      `ratchetandclank` exercise (pause/resume/render/tick/keyPressed/
      keyReleased/writeSaveData/readSaveData on `Game`; paint/keyPressed/
      tick/hideNotify/activate/commandAction on `IntroManager`). Not
      started: the real boot-chain/splash/menu transcription from
      `Game.java`/`IntroManager.java` (a1 moved the legacy build's
      in-`Game` menu system entirely into `IntroManager` -- see
      `src_a1/README.md` -- so this is comparable in scope to milestone
      3.1's own ~1,500-line slice, maybe larger given `IntroManager.java`
      alone is 1,800 lines), the MIDP `Canvas` key-code mapping (kept the
      legacy build's Nokia values as a documented, not yet independently
      verified, assumption -- see `src_a1/midp.h`'s `KeyCode` comment), and
      real `Command`/`Displayable` construction for the menu screens.
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
