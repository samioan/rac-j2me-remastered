cmake_minimum_required(VERSION 3.20)
project(goingmobile_port CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

if(MSVC)
  add_compile_options(/W4 /utf-8)
endif()

# Placeholder Win32 shell -- proves the toolchain (CMake + Ninja + MSVC)
# builds and runs on this machine. No game logic yet: that starts once
# ../docs/ROADMAP.md phase 1/2 (source read-through, asset formats) is
# done. Software-rendered raw Win32 (no SDL/D3D/GL dependency), matching
# the sibling tes-travels-decomp ports' platform layer. Window size is
# the original game's screen: 128x128 (Nokia Series 40 FullCanvas,
# confirmed in phase 1 -- a 20px HUD row over a 128x108 play area).
add_executable(goingmobile_port WIN32
  src/main.cpp
)
if(MSVC)
  target_compile_options(goingmobile_port PRIVATE /W4)
endif()
