// game_boss.cpp -- level-12 boss fight: intro cutscene (b==16, Game.b(byte)),
// boss AI/damage (M/L/o), boss render (F, c(g,byte)), win outro (b==24,
// Game.c(byte)), end-of-game stats (b==2) and the New-Game+ prompt (b==23).
// Transcribed from Game.java.
#include "game.h"
#include "screen.h"
#include "midlet.h"
#include "intromanager.h"
#include "font.h"
#include <string>

typedef ratchetandclank RC;

static const jbyte BOSS_NX[4] = {14, 14, 13, 13};
static const jbyte BOSS_NY[4] = {8, 9, 8, 9};

void Game::bossInit() {
  h = 0;
  i = 0;
  g = false;
  s = 0;
  u = 0;
  bK = 4;
  bI[0] = bI[1] = bI[2] = bI[3] = 0;
  bH[0] = bH[1] = bH[2] = bH[3] = 0;
  bJ[0] = bJ[1] = bJ[2] = bJ[3] = 100;
  bJ[4] = 200;
  bM = 0;
}

void Game::bossIntro(jbyte step) {
  Player* pl = player;
  switch (step) {
    case 0:
      pl->updateAnimation();
      d = true;
      pl->posX += 1536;
      if ((pl->posX >> 8) > 9 * tileWidth) {
        h++;
        pl->velY = -3584;
        pl->velX = 512;
        return;
      }
      break;
    case 1: {
      pl->updateAnimation();
      pl->setAnimState(2);
      d = true;
      pl->velY = (jshort)(pl->velY + 256);
      pl->posInRow = pl->posInRow + pl->velY;
      pl->posX = pl->posX + pl->velX;
      pl->wrapRow();
      short gy = pl->groundYAhead(false);
      if (pl->y() >= gy && pl->velY > 0) {
        pl->setAnimState(4);
        pl->posInRow = 0;
        pl->velY = -3584;
        h++;
        return;
      }
      break;
    }
    case 2: {
      if (i >= 4) {
        h++;
        pl->setAnimState(1);
        return;
      }
      pl->updateAnimation();
      pl->setAnimState(2);
      d = true;
      pl->velY = (jshort)(pl->velY + 256);
      pl->posInRow = pl->posInRow + pl->velY;
      pl->wrapRow();
      short gy = pl->groundYAhead(false);
      if (pl->y() >= gy && pl->velY > 0) {
        pl->setAnimState(4);
        pl->posInRow = 0;
        pl->velY = -3584;
        pl->animRestart = 0;
        i++;
        return;
      }
      break;
    }
    case 3:
      pl->updateAnimation();
      d = true;
      pl->posX += 1536;
      if ((pl->posX >> 8) > 11 * tileWidth + (tileWidth >> 1)) {
        h++;
        pl->setAnimState(0);
        pl->animRestart = 0;
        pl->updateAnimation();
      }
  }
}

void Game::bossEffect() {
  int x0 = tileWidth * 14 - 42;
  int w = 2 * tileWidth;
  int y0 = tileHeight * 9 - tileHeight;
  int hh = 2 * tileHeight;
  e_((x0 + abs_(random->nextInt()) % w) << 8, (y0 + abs_(random->nextInt()) % hh) << 8, 30);
}

void Game::bossOutro(jbyte step) {
  Player* pl = player;
  switch (step) {
    case 0:
      bossEffect();
      pl->updateAnimation();
      if (pl->jumpPhase != -1) {
        short gy = pl->groundYAhead(false);
        d = true;
        pl->posX = pl->posX + pl->velX;
        pl->velY = (jshort)(pl->velY + 256);
        pl->posInRow = pl->posInRow + pl->velY;
        pl->wrapRow();
        if (pl->y() >= gy) {
          pl->setAnimState(4);
          pl->posInRow = 0;
          pl->jumpPhase = -1;
        }
      } else {
        if (pl->row <= 8 && (pl->posX >> 8) < 13 * tileWidth) {
          if ((pl->posX >> 8) > 11 * tileWidth + (tileWidth >> 1)) {
            pl->setAnimState(0);
            pl->animRestart = 0;
            s = 4;
            return;
          }
          pl->setAnimState(1);
          pl->animRestart = 0;
          s = 3;
          return;
        }
        if ((pl->posX >> 8) < 10 * tileWidth + (tileWidth >> 1)) {
          s = 2;
          pl->velY = -3584;
          return;
        }
        pl->setAnimState(1);
        pl->animRestart = 0;
        pl->facingRight = false;
        pl->velX = 0;
        pl->velY = 0;
        bossT = false;
        s++;
      }
      bossEffect();
      return;
    case 1: {
      short gy = pl->groundYAhead(false);
      pl->updateAnimation();
      d = true;
      if (bossT) {
        gy = pl->groundYAhead(false);
        d = true;
        pl->posX = pl->posX + pl->velX;
        pl->velY = (jshort)(pl->velY + 256);
        pl->posInRow = pl->posInRow + pl->velY;
        pl->wrapRow();
        if (pl->y() >= gy) {
          pl->setAnimState(1);
          pl->posInRow = 0;
          bossT = false;
        }
      } else {
        if (pl->y() < gy) {
          pl->setAnimState(3);
          bossT = true;
        }
        pl->posX -= 1536;
        if ((pl->posX >> 8) < 10 * tileWidth + (tileWidth >> 1)) {
          pl->setAnimState(0);
          s++;
          pl->velY = -3584;
        }
      }
      bossEffect();
      return;
    }
    case 2: {
      pl->updateAnimation();
      pl->setAnimState(2);
      d = true;
      pl->velY = (jshort)(pl->velY + 256);
      pl->posInRow = pl->posInRow + pl->velY;
      pl->wrapRow();
      short gy = pl->groundYAhead(false);
      if (pl->y() >= gy && pl->velY > 0) {
        pl->setAnimState(4);
        pl->posInRow = 0;
        pl->velY = -3584;
        pl->animRestart = 0;
        if (pl->row <= 8) {
          s++;
          pl->facingRight = true;
          pl->setAnimState(1);
        }
      }
      bossEffect();
      return;
    }
    case 3:
      pl->updateAnimation();
      d = true;
      pl->posX += 1536;
      if ((pl->posX >> 8) > 11 * tileWidth + (tileWidth >> 1)) {
        pl->setAnimState(0);
        pl->animRestart = 0;
        s++;
      }
      bossEffect();
      return;
    case 4: {
      if (u < 50) {
        u++;
        bossEffect();
        return;
      }
      bJ[4] = 0;
      int room = LevelMap::subGridIndexMap[X];
      LevelMap::subGrids[room][14][8] = -126;
      LevelMap::subGrids[room][14][9] = -125;
      levelMap->tiles[14][9] = 22;
      levelMap->tiles[13][9] = 22;
      levelMap->tiles[12][9] = 20;
      levelMap->tiles[15][9] = 21;
      LevelMap::columnSolidMasks[12] |= 512;
      LevelMap::columnSolidMasks[13] |= 512;
      LevelMap::columnSolidMasks[14] |= 512;
      LevelMap::columnSolidMasks[15] |= 512;
      s++;
      return;
    }
    case 5:
      cC = true;
      cI = cz[32];
      r_(cA[32]);
      pl->velY = -3172;
      pl->velX = 512;
      s++;
      return;
    case 6:
      if (!cD) {
        pl->updateAnimation();
        pl->setAnimState(2);
        d = true;
        pl->velY = (jshort)(pl->velY + 256);
        pl->posInRow = pl->posInRow + pl->velY;
        pl->posX = pl->posX + pl->velX;
        pl->wrapRow();
        short gy = pl->groundYAhead(false);
        if (pl->y() >= gy && pl->velY > 0) {
          pl->setAnimState(1);
          pl->animRestart = 0;
          pl->posInRow = 0;
          pl->velY = -3584;
          s++;
          return;
        }
      }
      break;
    case 7:
      pl->updateAnimation();
      d = true;
      pl->posX += 1536;
      if ((pl->posX >> 8) > 14 * tileWidth + (tileWidth >> 1)) {
        pl->setAnimState(0);
        s++;
        return;
      }
      break;
    case 8:
      p = 0;
      y_();
      if (az == nullptr) az = Image::createImage("/clank.png");
      dA = dA + dz;
      b = 2;
      pl->facingRight = true;
      pl->currentWeapon = 1;
      pl->setAnimState(0);
      pl->animRestart = 0;
      pl->row = 1;
      pl->posInRow = 1792;
      pl->posX = 22528;
      pl->velY = 0;
      pl->velX = 0;
      pl->invulnTimer = 0;
      s++;
  }
}

void Game::bossFire() {
  eb = true;
  for (int k2 = 9; k2 >= 0; k2--) {
    Projectile* pr = enemyProjectiles[k2];
    if (pr->type != -1) continue;
    pr->posX = (tileWidth * 14) << 8;
    pr->posY = (tileHeight * 9) << 8;
    pr->type = 31;
    pr->age = 0;
    pr->sourceAnim = 25;
    pr->halfWidth = Projectile::HALF_WIDTHS[31];
    pr->halfHeight = Projectile::HALF_HEIGHTS[31];
    if (bK == 0 || bK == 1 || bK == 7) pr->vx = Projectile::SPEEDS[31] << 8;
    else if (bK != 2 && bK != 6) pr->vx = -(Projectile::SPEEDS[31] << 8);
    else pr->vx = 0;
    if (bK != 2 && bK != 1 && bK != 3) {
      if (bK != 0 && bK != 4) pr->vy = Projectile::SPEEDS[31] << 8;
      else pr->vy = 0;
    } else {
      pr->vy = -(Projectile::SPEEDS[31] << 8);
    }
    return;
  }
}

void Game::bossHit(int part) {
  int w = tileWidth, hgt = tileHeight;
  if (bJ[part] <= 0) {
    bJ[part] = 0;
    return;
  }
  int bx0, by0;
  if (part == 4) {
    w = tileWidth << 1;
    hgt = tileHeight << 1;
    bx0 = BOSS_NX[2] * tileWidth;
    by0 = BOSS_NY[2] * tileHeight;
  } else {
    bx0 = BOSS_NX[part] * tileWidth;
    by0 = BOSS_NY[part] * tileHeight;
  }
  if (bx0 + w + x >= 0 && bx0 + x <= screen::width && by0 + hgt + y >= 0 && by0 + hgt * 0 + y <= 220) {
    for (int k2 = 9; k2 >= 0; k2--) {
      Projectile* pr = playerProjectiles[k2];
      jbyte t2 = pr->type;
      if (pr->type >= 0 && t2 != 30 && t2 != 15 && t2 != 16 && t2 != 17) {
        int hw = pr->halfWidth, hh = pr->halfHeight;
        int px = (pr->posX >> 8) - hw, py = (pr->posY >> 8) - hh;
        if (a_(bx0, by0, w, hgt, px, py, hw * 2, hh * 2)) {
          bJ[part] = bJ[part] - Projectile::DAMAGE_BY_TYPE[t2];
          if (part == 4) {
            bI[0] = bI[1] = bI[2] = bI[3] = 5;
            bH[0] = bH[1] = bH[2] = bH[3] = 3;
          } else {
            bI[part] = 5;
            bH[part] = 1;
          }
          pr->detonate(false);
          return;
        }
      }
    }
  }
}

void Game::bossUpdate() {
  if (bJ[4] <= 0) {
    midlet->markGameWon();
    Z = 100;
    cu = 0;
    b = 24;
    bJ[4] = 1;
    player->velY = 256;
    player->facingRight = true;
    for (int k2 = 0; k2 < enemyPoolSize; k2++) enemies[k2]->kind = -1;
    for (int k2 = 9; k2 >= 0; k2--) enemyProjectiles[k2]->type = -1;
    for (int k2 = 11; k2 >= 0; k2--) ck[k2] = -1;
    return;
  }
  short px = player->x();
  int py = player->y() + L + (K >> 1);
  int cx = tileWidth * 14, cy = tileHeight * 9;
  int dx2 = px - cx, dy2 = py - cy;
  int th = tileHeight;
  if (bM <= 2 && !eb) {
    if (dx2 > th) {
      if (dy2 > th) bK = 7;
      else if (dy2 < -th) bK = 1;
      else bK = 0;
    } else if (dx2 < -th) {
      if (dy2 > th) bK = 5;
      else if (dy2 < -th) bK = 3;
      else bK = 4;
    } else {
      bK = 2;
      if (dy2 > 0) bK = 6;
    }
  }
  if (++bL > 30) {
    bL = 0;
    if (++bM > 12) bM = 0;
    if (bM > 2) {
      bL = 7;
      if (++bK > 7) bK = 0;
    }
    bossFire();
  }
  for (int k2 = 0; k2 < 8; k2++) {
    int part = k2 % 4;
    if (bH[part] != 0 && bJ[part] > 0) {
      if (--bI[part] < 0) bH[part] = 0;
    } else if (bH[part] != 2 && bJ[part] <= 0 && --bI[part] < 0) {
      bH[part] = 2;
    }
    bossHit(part);
  }
  if (bJ[0] + bJ[1] + bJ[2] + bJ[3] == 0) bossHit(4);
}

void Game::bossRender(Graphics* g) {
  if (bJ[4] <= 0) return;
  short manip = 0;
  int x2 = tileWidth * 14 + x;
  int y2 = tileHeight * 9 + y;
  int cw = aP->getWidth();
  int ch = aP->getHeight() >> 1;
  int cx, cy;
  int bw2 = aN->getWidth(), bh2 = aN->getHeight();
  switch (bK) {
    case 0:
      x2 += bw2 - cw / 2; y2 -= ch / 2; cx = x2; cy = y2; break;
    case 1:
      x2 += (bw2 >> 1) - (bw2 >> 3); y2 -= (bh2 >> 3) + 2 * ch - (bh2 >> 5); cx = x2; cy = y2 + ch; break;
    case 2:
      manip = 90; x2 -= ch / 2; y2 -= (bh2 >> 2) + cw / 2; cx = x2; cy = y2; break;
    case 3:
      manip = 8192; x2 -= (bw2 >> 1) + cw - (bw2 >> 3); y2 -= (bh2 >> 3) + 2 * ch - (bh2 >> 5); cx = x2; cy = y2 + ch; break;
    case 4:
      manip = 8192; x2 -= bw2 + cw / 2; y2 -= ch / 2; cx = x2; cy = y2; break;
    case 5:
      manip = 180; x2 -= (bw2 >> 1) + cw - (bw2 >> 3); y2 += (bh2 >> 3) - (bh2 >> 5); cx = x2; cy = y2; break;
    case 6:
      manip = 270; x2 -= ch + ch / 2; y2 += (bh2 >> 2) - cw / 2; cx = x2 + cw; cy = y2; break;
    default:
      manip = 16384; x2 += (bw2 >> 1) - (bw2 >> 3); y2 += (bh2 >> 3) - (bh2 >> 5); cx = x2; cy = y2;
  }
  if (cx < screen::width && cy < 220 && cx + cw > 0 && cy + ch > 0) {
    b_(g, cx, cy, cw, ch);
    if (manip) g->drawImageManip(aP, x2, y2, 20, manip);
    else g->drawImage(aP, x2, y2, 20);
  }
  int pw = bw2, ph = bh2 >> 2;
  for (int k2 = 0; k2 < 4; k2++) {
    int px2 = tileWidth * 14 + x, py2 = tileHeight * 9 + y;
    int qx = px2, qy = py2;
    int mm = 0;
    switch (k2) {
      case 0: py2 -= (bh2 >> 2) + ph * bH[k2]; qy -= ph; break;
      case 1: mm = 16384; py2 -= 3 * (bh2 >> 2) - ph * bH[k2]; break;
      case 2: mm = 8192; px2 -= bw2; py2 -= (bh2 >> 2) + ph * bH[k2]; qx -= pw; qy -= ph; break;
      case 3: mm = 180; px2 -= bw2; py2 -= 3 * (bh2 >> 2) - ph * bH[k2]; qx -= pw; break;
    }
    if (qx + pw >= 0 && qx <= screen::width && qy + ph >= 0 && qy <= 220) {
      b_(g, qx, qy, pw, ph);
      if (mm) g->drawImageManip(aN, px2, py2, 20, mm);
      else g->drawImage(aN, px2, py2, 20);
    }
  }
}

void Game::bossMaxRender(Graphics* g, jbyte frame) {
  int w = av->getWidth();
  int fh = av->getHeight() / 5;
  int x2 = tileWidth * 14 + x;
  int y2 = tileHeight * 7 + y;
  player->updateAnimation();
  if (x2 - 3 < screen::width && y2 < 220 && x2 - 3 + w >= 0 && y2 + fh >= 0) {
    g->setClip(x2 - (tileWidth >> 1), y2, w, fh);
    g->drawImage(av, x2 - 3, y2 - fh * frame, 17);
  }
  if (!cD && frame == 4 && !this->g) {
    cI = cz[31];
    cC = true;
    r_(cA[31]);
  }
}

void Game::drawEndStats(Graphics* g) {
  IntroManager* im = midlet->introManager;
  g->setClip(0, 0, 176, 220);
  g->setColor(0);
  g->fillRect(0, 0, 176, 220);
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  int y0 = im->a_(g, RC::strings[75]) + (RC::currentFont->lineHeight >> 1);
  g->setClip(66, y0, 44, 44);
  g->drawImage(az, 66, y0 - p * 44, 0);
  g->setClip(0, 0, 176, 220);
  e_(g);
  g->setColor(0xFFFFFF);
  RC::currentFont = RC::smallFont;
  y0 = im->a_(g, RC::strings[306] + " " + im->c_(cr), 88, 85, 17, false);
  y0 = im->a_(g, RC::strings[299] + " " + std::to_string(v_()), 88, y0, 17, false);
  y0 += 5;
  im->a_(g, RC::strings[307] + " " + std::to_string(dA), 88, y0, 17, true);
  im->a_(g, 9, -1, this);
}

void Game::drawRestartPrompt(Graphics* g) {
  IntroManager* im = midlet->introManager;
  int tw = tileWidth + (tileWidth >> 1);
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  e_(g);
  im->a_(g, RC::strings[282]);
  RC::currentFont = RC::smallFont;
  dc[0] = 100;
  int y1 = dc[1] = ap->getHeight() / 2 + im->a_(g, RC::strings[11], tw, 100, 0, cu == 0);
  im->a_(g, RC::strings[10], tw, y1, 0, cu == 1);
  im->a_(g, 7, -1, this);
  im->a_(g, tileWidth >> 1, dc[cu]);
}

void Game::endStatsKey(int key, int action) {
  if (key == 53 || action == -5 || key == -6) {
    az = nullptr;
    sleep(20);
    if (!aa) {
      cu = 1;
      b = 23;
      return;
    }
    dZ = true;
  }
}

void Game::restartKey(int key, int action) {
  IntroManager* im = midlet->introManager;
  if (action == -1 || key == 50) {
    cu = im->a_((jbyte)cu, (jbyte)0, (jbyte)1);
  } else if (action != -2 && key != 56) {
    if (key == 53 || action == -5 || key == -6) {
      midlet->playSoundIfEnabled(3);
      if (cu == 0) {
        dZ = true;
        if (dA > 0) return;
      } else if (cu == 1) {
        restartGame();
      }
    }
  } else {
    cu = im->b_((jbyte)cu, (jbyte)1, (jbyte)0);
  }
}

void Game::restartGame() {
  if (aa) {
    dZ = true;
    return;
  }
  o = -1;
  player->swingPhase = -2;
  dC = 0;
  cS = false;
  Z = 1;
  bZ = false;
  aQ = 3;
  levelMap->loadLevelFile(Z);
  X = LevelMap::currentSubGrid;
  di = 1;
  Y = X;
  levelMap->enterRoom(X, true);
  player->posX = (ad * tileWidth + (J >> 1)) << 8;
  player->row = (jbyte)(player->af = ae);
  player->animFrame = 1;
  player->animCounter = 0;
  player->animState = 0;
  player->animRestart = 0;
  player->posInRow = 0;
  player->jumpPhase = -1;
  player->kind = 0;
  player->activeFlag = 1;
  player->health = 20;
  x = af;
  y = ag;
  ct = currentTimeMillis();
  b_((int)X, 11337);
  player->invulnTimer = 0;
  player->facingRight = true;
  d = true;
  e = true;
  player->setAnimState(0);
  player->animRestart = 0;
  ce = 1;
  cf = 18;
  player->ownedWeapons = (jbyte)(player->ownedWeapons & -33);
  aa = true;
  bw = -1;
  bx = -1;
  by = -1;
  cJ = -1;
  cK = -1;
  cK &= -221;
  cL = 1572865;
  player->currentWeapon = 1;
  b = 0;
  cv = cw = 0;
  updateCamera();
}
