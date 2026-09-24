// levelmap.h -- port of goingmobile/src_a1/LevelMap.java (class d), scoped
// to milestone 3.1a's boot-chain needs: the compiled-in room tables and
// the constructor (real). Tile parsing/rendering/level-file loading (see
// src_a1/LevelMap.java) is not transcribed yet; that's milestone 3.2a.
#pragma once
#include "midp.h"

class Game;

class LevelMap {
 public:
  static jbyte currentSubGrid;
  static jbyte startSubGrid;
  static jbyte subGridCount;
  static jbyte tileWidth;
  static jbyte tileHeight;
  static jbyte tileAnimCounter;
  static const jbyte PLATFORM_TILE_TYPES[12];
  static const jbyte ENEMY_TILE_TYPES[14];
  static const jbyte BOLT_TILE_TYPES[4];

  // 13 entries (level0..level12); each row is [roomCount, roomCount*4
  // neighbor bytes, roomCount index-map bytes, startSubGrid] packed flat,
  // ragged (variable length per level) -- see src_a1/LevelMap.java.
  static const std::vector<std::vector<jbyte>> LEVEL_ROOM_TABLES;

  // subGrids[12][28][18], columnSolidMasks[28], subGridNeighbors[12][4],
  // subGridIndexMap[12] -- fixed dims in the Java, kept as plain statics.
  static jbyte subGrids[12][28][18];
  static jint columnSolidMasks[28];
  static jbyte subGridNeighbors[12][4];
  static jbyte subGridIndexMap[12];

  jbyte tiles[28][18] = {};

  explicit LevelMap(Game* game);

  jshort getTile(int col, int row) const;
  void loadRoomTable(int level);
  void loadLevelFile(int level);
  void computeColumnSolidMasks();
  void enterRoom(int room, bool placePlayer);
  void render(Graphics* g);
  bool isWalkable(int col, int row) const;
  // enterRoom() spawns enemies/platforms/pickups through Game's still-
  // undeciphered spawn helpers -- lands with Game's phase-1 (3.2a).

 private:
  Game* game_;
};
