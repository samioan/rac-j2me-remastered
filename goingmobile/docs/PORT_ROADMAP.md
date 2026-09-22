# Port Roadmap

Tracks `port/`, the actual PC port -- as opposed to [`ROADMAP.md`](ROADMAP.md),
which tracks the reverse-engineering work that feeds it (now fully done:
phases 0-2, all 9 canonical-build classes renamed and cross-referenced in
[`../src/`](../src/), every asset format confirmed and tool-verified). Follows
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
  software-rendered Nokia `FullCanvas`, so a Win32 window blitting a manually
  computed 128x128 backbuffer reproduces that architecture directly with
  zero external dependencies, same rationale as the sibling ports.
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

## Milestones remaining

- [ ] **3.2 -- gameplay.** Fill every stub `game.h`'s "gameplay internals"
      section and `player.h`/`enemy.h`/`levelmap.h`/`projectile.h` declared
      in 3.1: `LevelMap`'s `/n<level>` parser, tile activation and the 22x14
      renderer; `Player`'s physics/animation/melee/weapon firing (loading
      `/r`); `Enemy`'s AI/animation (loading `/p`+`/q`); `Projectile`'s
      movement/collision/rendering; and `Game`'s own tick-loop internals
      (player/enemy/projectile collision, the boss fight, the gameplay HUD
      and overlay rendering). `docs/ASSET_FORMATS.md`'s confirmed,
      tool-verified formats (phase 2) are the reference for every loader.
