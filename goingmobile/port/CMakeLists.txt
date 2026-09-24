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
# legacy build's FullCanvas keyPressed state machine. This first slice is
# scaffold-scoped (mirrors milestone 3.0's original scope, applied to a1):
# the shim (midp), font, MIDlet lifecycle + 214-byte RMS saves (midlet),
# and canvas shell/input dispatch (canvasshell) are real; Game and
# IntroManager (a1's engine + menu/splash controller -- IntroManager
# absorbed the legacy build's in-Game menu system) are declared and
# stubbed to the call surface CanvasShell/ratchetandclank exercise. The
# real boot-chain/menu transcription (comparable in scope to milestone
# 3.1's ~1500 lines) is still to come.
add_executable(goingmobile_port_a1 WIN32
  src_a1/main.cpp
  src_a1/midp.cpp
  src_a1/font.cpp
  src_a1/midlet.cpp
  src_a1/canvasshell.cpp
  src_a1/game.cpp
  src_a1/intromanager.cpp
)
if(MSVC)
  target_compile_options(goingmobile_port_a1 PRIVATE /W4)
endif()
