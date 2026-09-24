// levelmap.cpp -- implementation of LevelMap (see levelmap.h), transcribed
// from src_a1/LevelMap.java's static tables and constructor.
#include "levelmap.h"
#include "game.h"

const jbyte LevelMap::PLATFORM_TILE_TYPES[12] = {15, 15, 16, 17, 18, 18, 19, 20, 21, 21, 22, 23};
const jbyte LevelMap::ENEMY_TILE_TYPES[14] = {3, 4, 5, 0, 1, 2, 6, 7, 7, 9, 10, 10, 12, 13};
const jbyte LevelMap::BOLT_TILE_TYPES[4] = {0, 2, 1, 3};

const std::vector<std::vector<jbyte>> LevelMap::LEVEL_ROOM_TABLES = {
    {1, -1, -1, -1, -1, 0, 2},
    {6, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, 5, 3, -1, -1, -1, 4, 0, 1, 2, 3, 4, 5, 0},
    {4, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, -1, 2, 0, 1, 2, 3, 1},
    {5, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, -1, 3, 4, 0, 1, 2, 3, 2},
    {6, -1, -1, 1, -1, -1, 2, 3, 0, 1, -1, 4, -1, -1, 4, -1, 1, 3, -1, 5, 2, -1, -1, -1, 4, 0, 1, 3, 2, 4, 5, 2},
    {5, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, -1, 3, 2, 1, 0, 3, 4, 2},
    {5, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, -1, 3, 0, 1, 2, 3, 4, 0},
    {3, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, -1, 1, 0, 2, 1, 0},
    {4, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, -1, 2, 0, 1, 2, 3, 1},
    {6, -1, 1, 2, -1, 0, -1, 3, -1, -1, 3, 4, 0, 2, -1, -1, 1, -1, -1, 5, 2, -1, -1, -1, 4, 0, 4, 3, 5, 1, 2, 1},
    {6, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, 5, 3, -1, -1, -1, 4, 0, 1, 2, 3, 5, 4, 2},
    {12, -1, 1, 8, -1, 0, 2, 9, -1, 1, 3, 10, -1, 2, 4, 11, -1, 3, 5, -1, -1, 4, 6, -1, -1, 5, 7, -1, -1, 6, -1, -1,
     -1, -1, 9, -1, 0, 8, 10, -1, 1, 9, 11, -1, 2, 10, -1, -1, 3, 0, 1, 2, 3, 0, 4, 1, 2, 5, 3, 4, 5, 1},
    {1, -1, -1, -1, -1, 0, 2},
};

jbyte LevelMap::currentSubGrid = 0;
jbyte LevelMap::startSubGrid = 0;
jbyte LevelMap::subGridCount = 0;
jbyte LevelMap::tileWidth = 0;
jbyte LevelMap::tileHeight = 0;
jbyte LevelMap::tileAnimCounter = 0;
jbyte LevelMap::subGrids[12][28][18] = {};
jint LevelMap::columnSolidMasks[28] = {};
jbyte LevelMap::subGridNeighbors[12][4] = {};
jbyte LevelMap::subGridIndexMap[12] = {};

LevelMap::LevelMap(Game* game) : game_(game) {
  tileWidth = Game::tileWidth;
  tileHeight = Game::tileHeight;
  tileAnimCounter = 0;
  for (auto& row : subGridNeighbors) for (auto& v : row) v = 0;
  for (auto& v : subGridIndexMap) v = 0;
  for (auto& grid : subGrids) for (auto& row : grid) for (auto& v : row) v = 0;
  for (auto& v : columnSolidMasks) v = 0;
  for (auto& row : tiles) for (auto& v : row) v = 0;
}
