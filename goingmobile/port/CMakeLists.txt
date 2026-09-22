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
