// levelmap.cpp -- implementation of LevelMap (see levelmap.h), transcribed
// from src_a1/LevelMap.java's static tables and constructor.
#include "levelmap.h"
#include "screen.h"
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

jshort LevelMap::getTile(int col, int row) const {
  return (col >= 0 && col < 28 && row >= 0 && row < 18)
             ? (jshort)subGrids[(int)subGridIndexMap[game_->X]][col][row] : (jshort)-1;
}

void LevelMap::loadRoomTable(int level) {
  const std::vector<jbyte>& t = LEVEL_ROOM_TABLES[(size_t)level];
  size_t p = 0;
  subGridCount = t[p++];
  for (int i = 0; i < subGridCount; i++)
    for (int k = 0; k < 4; k++) subGridNeighbors[i][k] = t[p++];
  for (int i = 0; i < subGridCount; i++) subGridIndexMap[i] = t[p++];
  currentSubGrid = 0;
  startSubGrid = t[p];
}

void LevelMap::loadLevelFile(int level) {
  loadRoomTable(level);
  game_->x_();
  ByteArray data = loadResource("/level" + std::to_string(level) + ".bin");
  size_t pos = 0;
  for (int r = 0; r < subGridCount; r++)
    for (int c = 0; c < 28; c++)
      for (int k = 0; k < 18; k++) {
        int v = pos < data.size() ? (int)std::to_integer<uint8_t>(data[pos++]) : -1;
        subGrids[r][c][k] = (jbyte)(v - 32);
      }
}

void LevelMap::computeColumnSolidMasks() {
  for (int c = 27; c >= 0; c--) {
    columnSolidMasks[c] = 0;
    for (int r = 0; r < 18; r++) {
      int v = tiles[c][r];
      if (v >= 19 && v <= 34) columnSolidMasks[c] |= 1 << r;
    }
  }
}

bool LevelMap::isWalkable(int col, int row) const {
  if (col >= 0 && col < 28 && row >= 0 && row < 18) {
    jbyte t = tiles[col][row];
    int a = getTile(col, row), b = getTile(col, row + 1);
    int ma = 1 << (a - 70), mb = 1 << (b - 70);
    if ((a < 70 || a > 76 || (game_->bw & ma) == 0) && (b < 70 || b > 76 || (game_->bw & mb) == 0)) {
      if ((a == -119 || b == -119) && (game_->bw & 128) != 0) return false;
      return t >= 0 && t <= 19;
    }
    return false;
  }
  return false;
}

void LevelMap::render(Graphics* g) {
  int camX = Game::x, camY = Game::y;
  int c0 = (jbyte)(-camX / tileWidth), r0 = (jbyte)(-camY / tileHeight);
  int c1 = c0, r1 = r0;
  int x0 = c0 * tileWidth + camX, y0 = r0 * tileHeight + camY;
  int px = x0, py = y0;
  c1 = (jbyte)(c1 + screen::width / tileWidth + 2);
  r1 = (jbyte)(r1 + 220 / tileHeight + 2);
  if (c1 > 28) c1 = 28;
  if (r1 > 18) r1 = 18;
  if (game_->advanceAnim && ++tileAnimCounter >= 12) tileAnimCounter = 0;
  for (int c = c0; c < c1; c++) {
    for (int r = r0; r < r1; r++) {
      if (c >= 0 && r >= 0 && px < screen::width && py < 220) {
        int t = tiles[c][r];
        if (t <= 34) {
          if (tileAnimCounter >> 1 > 2) {
            if (t == 15 || t == 17) t++;
            else if (t == 16 || t == 18) t--;
          }
          if (game_->aQ != 0 && t == 26) t = (tileAnimCounter < 6) ? 13 : 14;
          game_->b_(g, px, py, tileWidth, tileHeight);
          g->drawImage(Game::aF, px, py - t * tileHeight, 0);
        } else {
          g->setColor(0);
          g->fillRect(px, py, tileWidth, tileHeight);
        }
        int o = getTile(c, r);
        if (o > -127 && o <= -125) {
          int w = Game::aK->getWidth();
          int th = tileHeight;
          int idx = o + 126;
          if (tileAnimCounter >> 1 > 2) idx += 2;
          game_->b_(g, px + ((tileWidth - w) >> 1), py, w, th);
          g->drawImage(Game::aK, px + ((tileWidth - w) >> 1), py - th * idx, 0);
        } else if (o == 97 || o == 98) {
          int yy = py + ((tileHeight - 19) >> 1);
          game_->b_(g, px + ((tileWidth - 19) >> 1), yy, 19, 19);
          g->drawImage(Game::aL, px + ((tileWidth - 19) >> 1), yy - 209, 0);
        } else if (o == 35 && game_->f_(game_->Z, game_->X)) {
          game_->b_(g, px + ((tileWidth - 19) >> 1), py, 19, 19);
          g->drawImage(Game::aL, px + ((tileWidth - 19) >> 1), py - 76, 0);
        } else if (o >= 77 && o <= 96) {
          int w = Game::aJ->getWidth();
          int hh = Game::aJ->getHeight() >> 3;
          int yy = py + tileHeight - hh;
          int sh = game_->h_(o - 77) ? 3 * hh : 4 * hh;
          if (o >= 81 && o <= 87 && o != 85) sh = game_->h_(o - 77) ? 7 * hh : 8 * hh;
          if (o == 80) sh += 2 * hh;
          game_->b_(g, px + ((tileWidth - w) >> 1), yy, w, hh);
          g->drawImage(Game::aJ, px + ((tileWidth - w) >> 1), yy - sh, 0);
        }
      }
      py += tileHeight;
    }
    py = y0;
    px += tileWidth;
  }
}
