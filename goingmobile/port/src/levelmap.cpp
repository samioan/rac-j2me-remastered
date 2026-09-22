// levelmap.cpp -- LevelMap static data + stubs (milestone 3.2 fills
// the /n<level> parser, spawn logic and the 22x14 tile renderer).
#include "levelmap.h"
#include "game.h"

const jbyte LevelMap::a[13] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
const jbyte LevelMap::b[13] = {0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 0, 1, 2};
jbyte LevelMap::levelGrids[12][28][18] = {};
jint LevelMap::columnSolidMasks[28] = {};
jbyte LevelMap::gridCount = 0;
const jbyte LevelMap::PLATFORM_TILE_TYPES[12] = {15, 15, 16, 17, 18, 18, 19, 20, 21, 21, 22, 23};
const jbyte LevelMap::ENEMY_TILE_TYPES[14] = {3, 4, 5, 0, 1, 2, 6, 7, 7, 9, 10, 10, 12, 13};
const jbyte LevelMap::BOLT_TILE_TYPES[4] = {0, 2, 1, 3};

jshort LevelMap::a_(int, int) { return 0; }
void LevelMap::a_(int) {}
void LevelMap::a_(int, bool) {}
void LevelMap::a_(Graphics*) {}
