// levelmap_enter.cpp -- LevelMap::enterRoom, transcribed from
// src_a1/LevelMap.java (walks a room's 28x18 raw-tile grid, translating
// tile ids into render state and spawning entities through Game's helpers).
#include "levelmap.h"
#include "game.h"

void LevelMap::enterRoom(int room, bool placePlayer) {
  Game* gm = game_;
  int platformIdx = 0;   // Java var3: moving-platform slot counter
  int enemyIdx = 0;      // Java var4: enemy/pickup slot counter
  bool copyRaw = false;  // Java var7
  bool copyLift = false; // Java var8
  bool copyRun = false;  // Java var9

  gm->X = (jshort)room;
  gm->d_(gm->Z, room);

  for (int i2 = 3; i2 >= 0; i2--) gm->cq[i2] = -1;
  for (int i2 = 49; i2 >= 0; i2--) {
    gm->bq[i2] = gm->br[i2] = -1;
    gm->bo[i2] = 0;
    gm->bs[i2] = false;
    gm->bn[i2] = -1;
  }
  for (int i2 = Game::enemyPoolSize - 1; i2 >= 0; i2--) gm->enemies[i2]->kind = -1;
  gm->bP = -1;
  gm->bQ = -1;
  for (int i2 = 3; i2 >= 0; i2--) { gm->bc[i2] = -1; gm->be[i2] = -1; }
  for (int i2 = 11; i2 >= 0; i2--) gm->ck[i2] = -1;
  for (int i2 = 2; i2 >= 0; i2--) { gm->bC[i2] = 0; gm->bF[i2] = -1; }
  for (int i2 = 9; i2 >= 0; i2--) gm->enemyProjectiles[i2]->type = -1;
  for (int i2 = 9; i2 >= 0; i2--) gm->playerProjectiles[i2]->type = -1;
  gm->cc = -1;
  gm->cd = -1;
  gm->ca = -1;
  gm->cb = -1;
  gm->bX = -1;
  gm->bY = -1;
  gm->cT = true;
  if (subGridNeighbors[room][1] != -1) gm->cT = false;
  for (int i2 = 7; i2 >= 0; i2--) gm->bj[i2] = -1;

  for (int c2 = 27; c2 >= 0; c2--) {
    for (int r2 = 17; r2 >= 0; r2--) {
      jshort t = getTile(c2, r2);
      if (copyRaw) {
        tiles[c2][r2 + 1] = (jbyte)t;
      } else if (copyLift) {
        if (t % 2 == 0 && t < 12) tiles[c2][r2 + 1] = (jbyte)(t + 1);
        else tiles[c2][r2 + 1] = (jbyte)t;
      }
      if (copyRun) {
        jshort v = t;
        if (tiles[c2][r2 + 2] >= 19) {
          for (int k = 1; v >= 47 && v <= 55; k++) v = getTile(c2, r2 - k);
          if (v % 2 == 0 && v < 12) tiles[c2][r2 + 1] = (jbyte)(v + 1);
          else tiles[c2][r2 + 1] = (jbyte)v;
        } else if (v >= 47 && v <= 55) {
          for (int k = 1; v >= 47 && v <= 55; k++) v = getTile(c2, r2 - k);
          tiles[c2][r2 + 1] = (jbyte)v;
        }
      }
      copyRaw = false;
      copyLift = false;
      copyRun = false;

      if (t >= 47 && t <= 55) {
        copyRaw = true;
        copyRun = true;
        switch (t) {
          case 47: gm->a_(c2, r2, 0, 0, platformIdx); break;
          case 48: gm->a_(c2, r2, 2, 0, platformIdx); break;
          case 49: gm->a_(c2, r2, 1, 0, platformIdx); break;
          case 50: gm->a_(c2, r2, 0, 0, platformIdx++); gm->a_(c2, r2, 0, 1, platformIdx); break;
          case 51: gm->a_(c2, r2, 0, 0, platformIdx++); gm->a_(c2, r2, 2, 1, platformIdx); break;
          case 52: gm->a_(c2, r2, 0, 0, platformIdx++); gm->a_(c2, r2, 1, 1, platformIdx); break;
          case 53: gm->a_(c2, r2, 0, 2, platformIdx); break;
          case 54: gm->a_(c2, r2, 2, 2, platformIdx); break;
          case 55: gm->a_(c2, r2, 1, 2, platformIdx); break;
          default: break;
        }
        platformIdx++;
      } else if (t == -97 || t == -96 || t == -95) {
        tiles[c2][r2] = 1;
      } else if (t == -98) {
        tiles[c2][r2] = 0;
        gm->a_(c2, r2);
      } else if (t == 35) {
        if (gm->f_(gm->Z, gm->X)) { gm->bP = (jbyte)c2; gm->bQ = (jbyte)r2; }
        copyRaw = true;
      } else if (t >= -118 && t <= -99) {
        copyLift = true;
      } else if (t == -93) {
        if (gm->h_(11)) { gm->ca = (jbyte)c2; gm->cb = (jbyte)r2; }
        copyLift = true;
      } else if (t == -94) {
        gm->cc = (jbyte)c2;
        gm->cd = (jbyte)r2;
        copyLift = true;
      } else if (t == -92) {
        if (gm->bZ) { gm->bX = (jbyte)c2; gm->bY = (jbyte)r2; }
        copyLift = true;
      } else if (t == 99) {
        copyLift = true;
      } else if (t == 100) {
        tiles[c2][r2] = tiles[c2 + 1][r2];
      } else if (t == 56) {
        tiles[c2][r2] = 27;
      } else if (t == 57) {
        tiles[c2][r2] = 22;
      } else if (t == 58) {
        tiles[c2][r2] = 20;
      } else if (t == 59) {
        tiles[c2][r2] = 21;
      } else if (t == 60 || t == 61) {
        tiles[c2][r2] = 22;
      } else if (t == -125) {
        if (placePlayer) {
          gm->ah = (jshort)(jbyte)room;
          gm->ad = (jbyte)c2;
          gm->ae = (jbyte)(r2 - 1);
          gm->af = (c2 < 1) ? 0 : (jshort)(-tileWidth * (c2 - 1));
          gm->ag = (r2 < 6) ? 0 : (jshort)(-tileHeight * (r2 - 5));
        }
        tiles[c2][r2] = 22;
      } else if (t == -127) {
        tiles[c2][r2] = 0;
      } else if (t == -126) {
        tiles[c2][r2] = 1;
      } else if (t >= 62 && t <= 69) {
        copyRaw = true;
        int k = t - 62;
        gm->a_(c2, r2, k % 2 == 0, k >> 1);
      } else if (t == 97 || t == 98) {
        copyRaw = true;
      } else if (t == -124) {
        copyLift = true;
      } else if (t >= 43 && t <= 46) {
        copyRaw = true;
        if (getTile(c2, r2 + 1) >= 19) { copyLift = true; copyRaw = false; }
        gm->d_(c2, r2, BOLT_TILE_TYPES[t - 43]);
      } else if (t >= 114 && t <= 127) {
        copyLift = true;
        gm->a_(c2, r2, ENEMY_TILE_TYPES[t - 114], enemyIdx++);
      } else if (t == -128) {
        copyLift = true;
        gm->a_(c2, r2, (jbyte)13, enemyIdx++);
      } else if (t >= 102 && t <= 113) {
        if ((t - 102) % 2 == 0) copyRaw = true;
        else tiles[c2][r2] = tiles[c2 + 1][r2];
        gm->a_(c2, r2, PLATFORM_TILE_TYPES[t - 102], enemyIdx++);
      } else if ((t < 70 || t > 76) && t != -119) {
        if (t >= 36 && t <= 42) {
          copyLift = true;
          gm->a_((jbyte)c2, (jbyte)r2);
        } else if (t >= 77 && t <= 96) {
          copyLift = true;
        } else if (t == 101) {
          copyRaw = true;
          int px = c2 * tileWidth + (tileWidth >> 1);
          int py = (r2 + 1) * tileHeight + (tileHeight >> 1);
          gm->a_(px << 8, py << 8, 32, true, true, 0, (jbyte)26);
        } else if (t > 34) {
          copyRaw = true;
        } else {
          tiles[c2][r2] = (jbyte)t;
        }
      } else {
        tiles[c2][r2] = 1;
        gm->a_((jbyte)c2, (jbyte)r2, (int)t);
      }
    }
  }

  computeColumnSolidMasks();
  gm->Q_();
  if (gm->cV) gm->bt[0] = false;
  else gm->bt[gm->X] = false;
}
