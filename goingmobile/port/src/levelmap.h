// levelmap.h -- port of goingmobile/src/LevelMap.java (class c).
// Milestone 3.1: field/static surface + constructor; parsing/render -> 3.2.
#pragma once
#include "midp.h"

class Game;

class LevelMap {
 public:
  // Bare letters, no trailing underscore -- see player.h for why (data
  // members can't share a name with the a_() behavior methods below).
  static const jbyte a[13];
  static const jbyte b[13];
  static jbyte levelGrids[12][28][18];      // tiles[grid][col][row]
  static jint columnSolidMasks[28];
  jbyte tiles[28][18];
  static jbyte gridCount;
  static const jbyte PLATFORM_TILE_TYPES[12];
  static const jbyte ENEMY_TILE_TYPES[14];
  static const jbyte BOLT_TILE_TYPES[4];

  Game* game = nullptr;

  explicit LevelMap(Game* g) : game(g) {}

  jshort a_(int var1, int var2);            // (3.2 stub)
  void a_(int level);                       // load /n<level> (3.2 stub)
  void a_(int grid, bool spawn);            // activate grid (3.2 stub)
  void a_(Graphics* g);                     // tile renderer (3.2 stub)
};
