cmake_minimum_required(VERSION 3.20)
project(goingmobile_port CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

if(MSVC)
  add_compile_options(/W4 /utf-8)
endif()

# Milestone 3.1: the boot/splash/menu/save surface, ported behaviorally from
# goingmobile/src/ (phase 1/2's renamed reference tree) onto a raw Win32
# software-rendered window -- no SDL/D3D/GL dependency, matching the sibling
# tes-travels-decomp ports' platform layer. Window size is the original
# game's screen: 128x128 (Nokia Series 40 FullCanvas, confirmed in phase 1).
# Gameplay internals (b==0 simulation, LevelMap/Player/Enemy/Projectile
# behavior) are declared and stubbed; that's milestone 3.2.
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
