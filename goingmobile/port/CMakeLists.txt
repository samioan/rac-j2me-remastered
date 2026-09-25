cmake_minimum_required(VERSION 3.20)
project(goingmobile_port C CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

if(MSVC)
  add_compile_options(/W4 /utf-8)
endif()

# --- building something other people can download ---------------------
# GOINGMOBILE_VERSION is what the launcher prints in its corner: a local build
# stays "-dev" (no update checks); the release workflow passes the git tag
# with its "goingmobile-v" prefix stripped.
set(GOINGMOBILE_VERSION "0.1.0-dev" CACHE STRING "Version string shown by the launcher")
set(GOINGMOBILE_UPDATE_OWNER "samioan" CACHE STRING "GitHub owner to check for updates")
set(GOINGMOBILE_UPDATE_REPO "rac-j2me-remastered" CACHE STRING "GitHub repo to check for updates")
set(GOINGMOBILE_UPDATE_TAG_PREFIX "goingmobile-v" CACHE STRING "Release tag prefix identifying a Going Mobile build")
# The shipping build: only the launcher and the a1 game.
option(GOINGMOBILE_DIST "Build only the shipped executables (launcher + a1 game)" OFF)
# Static CRT so "unzip and play" needs no Visual C++ redistributable.
if(MSVC)
  set(CMAKE_MSVC_RUNTIME_LIBRARY "MultiThreaded$<$<CONFIG:Debug>:Debug>")
endif()

# Milestone 3.1: the boot/splash/menu/save surface, ported behaviorally from
# goingmobile/src/ (phase 1/2's renamed reference tree) onto a raw Win32
# software-rendered window -- no SDL/D3D/GL dependency, matching the sibling
# tes-travels-decomp ports' platform layer. Window size is the original
# game's screen: 128x128 (Nokia Series 40 FullCanvas, confirmed in phase 1).
# Gameplay internals (b==0 simulation, LevelMap/Player/Enemy/Projectile
# behavior) are declared and stubbed; that's milestone 3.2.
if(NOT GOINGMOBILE_DIST)
add_executable(goingmobile_port WIN32
  src/main.cpp
  src/midp.cpp
  src/midlet.cpp
  src/game.cpp
  src/levelmap.cpp
  src/player.cpp
  src/enemy.cpp
  src/projectile.cpp
)
if(MSVC)
  target_compile_options(goingmobile_port PRIVATE /W4)
endif()
endif()

# Milestone 3.1a: a1's boot/splash/menu/save surface, re-based from
# goingmobile/src_a1/ (the target build's compile-checked phase-1 tree,
# ROADMAP.md's "Status") onto the same raw Win32 software-rendered window
# as milestone 3.1, resized to a1's own screen (176x220, not 128x128) and
# using a plain MIDP Canvas + CommandListener input model instead of the
# legacy build's FullCanvas keyPressed state machine. The scaffold slice
# (shim/font/midlet/canvasshell) plus the boot chain and splash sequence
# are real: Game::runBootStep (all 42 steps -- asset loading and
# constructing the Player/Enemy/LevelMap/Projectile pools) and
# IntroManager's splash/boot dispatch. Player/Enemy/LevelMap/Projectile
# are real for their data tables + constructor + asset loading, behavior
# stubbed (same precedent milestone 3.1 set for the legacy build). The
# interactive menu system (main menu, language select, save slots,
# credits/help/about, ...) is declared-and-stubbed still; that's the next
# slice of milestone 3.1a.
add_executable(goingmobile_port_a1 WIN32
  src_a1/main.cpp
  src_a1/midp.cpp
  src_a1/input.cpp
  src_a1/screen.cpp
  src_a1/game_interp.cpp
  src_a1/font.cpp
  src_a1/midlet.cpp
  src_a1/canvasshell.cpp
  src_a1/entity.cpp
  src_a1/player.cpp
  src_a1/enemy.cpp
  src_a1/levelmap.cpp
  src_a1/levelmap_enter.cpp
  src_a1/game_level.cpp
  src_a1/game_play.cpp
  src_a1/game_menus.cpp
  src_a1/game_flow.cpp
  src_a1/game_store.cpp
  src_a1/game_boss.cpp
  src_a1/game_wheel.cpp
  src_a1/game_tables.cpp
  src_a1/player_behavior.cpp
  src_a1/enemy_behavior.cpp
  src_a1/projectile.cpp
  src_a1/projectile_behavior.cpp
  src_a1/game.cpp
  src_a1/intromanager.cpp
)
if(MSVC)
  target_compile_options(goingmobile_port_a1 PRIVATE /W4)
endif()

# --- the launcher -------------------------------------------------------
# What a downloaded zip opens: picks and unpacks the user's own .jar, runs the
# game, and updates itself from GitHub releases. See src/launcher/.
# Artwork (assets/banner.png, goingmobile.ico) is generated from
# assets/banner_source.jpg by tools/make_banner.py and tools/make_icon.py.
add_library(stb_image STATIC third_party/stb/stb_image_impl.cpp)
target_include_directories(stb_image PUBLIC third_party/stb)
add_library(puff STATIC third_party/puff/puff.c)
target_include_directories(puff PUBLIC third_party/puff)
if(MSVC)
  target_compile_options(stb_image PRIVATE /W3)
  target_compile_options(puff PRIVATE /W3)
endif()
configure_file(src/launcher/version.h.in
               "${CMAKE_BINARY_DIR}/generated/launcher/version.h" @ONLY)
add_executable(goingmobile_launcher WIN32
  src/launcher/launcher_main.cpp
  src/launcher/banner.cpp
  src/launcher/install.cpp
  src/launcher/launcher_config.cpp
  src/launcher/update_check.cpp
  src/launcher/updater.cpp
  src/launcher/zip_reader.cpp
  src/platform/win32/exe_dir.cpp
  src/launcher/launcher.rc
)
set_target_properties(goingmobile_launcher PROPERTIES OUTPUT_NAME "GoingMobile")
target_include_directories(goingmobile_launcher PRIVATE src "${CMAKE_BINARY_DIR}/generated")
target_link_libraries(goingmobile_launcher PRIVATE stb_image puff comctl32 ole32 winhttp)
target_compile_definitions(goingmobile_launcher PRIVATE UNICODE _UNICODE NOMINMAX WIN32_LEAN_AND_MEAN)
if(MSVC)
  target_compile_options(goingmobile_launcher PRIVATE /W4)
  target_link_options(goingmobile_launcher PRIVATE /MANIFEST:NO)
endif()
