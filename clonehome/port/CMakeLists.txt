cmake_minimum_required(VERSION 3.20)
project(clonehome_port CXX C)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

if(MSVC)
  add_compile_options(/W4 /utf-8)
endif()

add_library(stb_image STATIC third_party/stb/stb_image_impl.cpp)
target_include_directories(stb_image PUBLIC third_party/stb)
if(MSVC)
  target_compile_options(stb_image PRIVATE /W3)
endif()

# Bank/atlas/anim-set/string loaders for the original RP1..RP3 files.
add_library(ch_assets STATIC src/assets.cpp src/gfx.cpp)
target_include_directories(ch_assets PUBLIC src)
target_link_libraries(ch_assets PUBLIC stb_image)

# Checks every resource against the formats in ../docs/ASSET_FORMATS.md.
add_executable(clonehome_selftest src/selftest.cpp)
target_link_libraries(clonehome_selftest PRIVATE ch_assets)

# Software-rendered raw Win32 shell (no SDL/D3D/GL dependency), at the
# original 240x320 SEMC resolution. Game logic to come.
add_executable(clonehome_port WIN32 src/main.cpp)
target_link_libraries(clonehome_port PRIVATE ch_assets shell32)
