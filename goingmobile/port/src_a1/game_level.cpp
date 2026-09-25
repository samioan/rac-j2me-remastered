#include <cstdio>
#include "screen.h"
#include <cstdlib>
// game_level.cpp -- Game's level-entry, spawn helpers, camera and in-level
// render pipeline, transcribed from src_a1/Game.java (see game.h for the
// obfuscated-name -> C++ overload map). Split out of game.cpp to keep that
// file's boot/menu-helper scope readable.
#include "game.h"
#include "canvasshell.h"
#include "midlet.h"
#include "intromanager.h"
#include "font.h"
#include <string>

bool Game::f = false, Game::z = false, Game::A = false, Game::B = false;
bool Game::dF = false, Game::dG = false, Game::dH = false, Game::dI = false;
jbyte Game::C = 0, Game::dy = 0, Game::dB = 0;
jint Game::D = 0, Game::E = 0, Game::dm = 0, Game::dn = 0, Game::do_ = 0, Game::dp = 0, Game::dq = 0;
jint Game::dr = 0, Game::ds = 0, Game::dt = 0, Game::du = 0, Game::dv = 0, Game::dz = 0, Game::dD = 0,
     Game::dE = 0;
jlong Game::dw = 0;

bool Game::a_(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
  auto within = [](int v, int lo, int len) { return v >= lo && v <= lo + len; };
  return (within(x2, x1, w1) || within(x1, x2, w2)) && (within(y2, y1, h1) || within(y1, y2, h2));
}

int Game::b_(int px, int py, int) {
  int floorY = 18 * tileHeight;
  int r = py / tileHeight;
  if ((LevelMap::columnSolidMasks[px / tileWidth] & (1 << (r + 1))) > 0) floorY = (r + 1) * tileHeight;
  return floorY;
}

void Game::a_(int col, int row) {
  for (int i2 = 0; i2 < 8; i2++) {
    if (bj[i2] == -1) {
      bj[i2] = (jshort)(col * tileWidth + (tileWidth >> 1));
      bk[i2] = (jshort)(row * tileHeight + (tileHeight >> 1));
      return;
    }
  }
}

void Game::a_(int col, int row, bool isStart, int idx) {
  jshort px = (jshort)(col * tileWidth + (tileWidth >> 1));
  jshort py = (jshort)(row * tileHeight + (tileHeight >> 1));
  if (isStart) { bc[idx] = px; bd[idx] = py; }
  else { be[idx] = px; bf[idx] = py; }
  if (bc[idx] != -1 && be[idx] != -1) {
    bh[idx] = (bf[idx] - bd[idx]) << 8;
    bg[idx] = (jshort)(be[idx] - bc[idx]);
    bi[idx] = (bd[idx] << 8) - bh[idx] * bc[idx] / bg[idx];
  }
}

void Game::a_(int col, int row, int rowKind, int spriteRow, int idx) {
  jshort room = X;
  if (cV) room = 0;
  int word = (idx + room * 50) >> 5;
  int bit = 1 << (idx - ((idx >> 5) << 5));
  if (bt[room] && rowKind > 0) bu[word] |= bit;
  if ((bu[word] & bit) != 0) {
    bn[idx] = (jbyte)rowKind;
    if (spriteRow == 2) bl[idx] = (jshort)(col * tileWidth + (aJ->getWidth() >> 1));
    else bl[idx] = (jshort)(col * tileWidth + spriteRow * aJ->getWidth());
    bm[idx] = (jshort)(row * tileHeight);
    bp[idx] = bm[idx];
    bo[idx] = 0;
  }
}

void Game::a_(int col, int row, jbyte anim, int slot) {
  if (slot == -1) {
    slot = enemyPoolSize - 1;
    while (slot >= 0 && enemies[slot]->kind != -1) slot--;
    if (slot == -1) return;
  } else {
    jshort room = X;
    if (cV) room = 0;
    int word = (slot + room * 10) >> 5;
    int bit = 1 << (slot + room * 10 - (word << 5));
    if (bt[room]) bv[word] |= bit;
    if ((bv[word] & bit) == 0) return;
  }
  Enemy* en = enemies[slot];
  if (anim >= 0 && anim <= 2) en->kind = 1;
  else if (anim >= 3 && anim <= 5) en->kind = 0;
  else if (anim >= 6 && anim <= 13) en->kind = 2;
  else en->kind = 3;
  en->posX = (col * tileWidth + (tileWidth >> 1)) << 8;
  en->row = (jbyte)row;
  en->posInRow = en->velX = en->velY = 0;
  en->health = Enemy::HP_BY_ANIM[anim];
  en->ap = 0;
  en->facingRight = true;
  if (Z == 12 && player->posX < en->posX) en->facingRight = false;
  en->flattenedRenderFlag = false;
  en->wallCrawlFlipped = false;
  en->stateTimer = 0;
  en->attackPhase = 0;
  en->bounceTimer = 0;
  en->spawnBitIndex = (jbyte)slot;
  en->patrolTargetX = (en->kind == 1) ? 0 : en->posX;
  // Game.java also writes `enemies[i].tileWidth = 0` here for kind 1; tileWidth is
  // Enemy's STATIC tile width (divisor in x()/column math), so zeroing it would
  // crash the original too -- a field-name mix-up from decompilation, omitted.
  if (en->kind == 3) {
    en->facingRight = false;
    jshort t;
    if ((t = levelMap->getTile(col + 1, row)) >= 19 && t <= 34) en->ap = 3;
    if ((t = levelMap->getTile(col - 1, row)) >= 19 && t <= 34) en->ap = 2;
    if ((t = levelMap->getTile(col, row - 1)) >= 19 && t <= 34) { en->posInRow = 3072; en->ap = 1; }
  }
  en->animKind = anim;
  en->setAnimState(0);
  en->animRestart = 1;
  en->activeFlag = 1;
}

void Game::a_(jbyte col, jbyte row) {
  for (int i2 = 0; i2 < 3; i2++) {
    if (bF[i2] == -1) { bF[i2] = col; bG[i2] = row; return; }
  }
}

void Game::a_(jbyte col, jbyte row, int tile) {
  for (int i2 = 0; i2 < 3; i2++) {
    if (bC[i2] == 0) {
      switch (tile) {
        case -119: bC[i2] = -128; break;
        case 70: bC[i2] = 1; break;
        case 71: bC[i2] = 2; break;
        case 72: bC[i2] = 4; break;
        case 73: bC[i2] = 8; break;
        case 74: bC[i2] = 16; break;
        case 75: bC[i2] = 32; break;
        case 76: bC[i2] = 64; break;
        default: break;
      }
      bD[i2] = col;
      bE[i2] = (jbyte)(row - 1);
      return;
    }
  }
}

void Game::a_(int px, int py, int type, bool up, bool horiz, int dir, jbyte srcAnim) {
  for (int i2 = 9; i2 >= 0; i2--) {
    Projectile* pr = enemyProjectiles[i2];
    if (pr->type != -1) continue;
    pr->posX = px;
    pr->posY = py;
    pr->type = (jbyte)type;
    pr->age = 0;
    pr->sourceAnim = srcAnim;
    pr->halfWidth = Projectile::HALF_WIDTHS[type];
    pr->halfHeight = Projectile::HALF_HEIGHTS[type];
    int spd = Projectile::SPEEDS[type];
    if (dir != 0 && dir != 1) {
      if (horiz) { pr->vx = (jshort)(((dir == 3) ? -1 : 1) * spd << 8); pr->vy = 0; }
      else { pr->vx = 0; pr->vy = (jshort)((up ? -1 : 1) * spd << 8); }
    } else if (horiz) {
      pr->vx = 0;
      if (srcAnim != 17 && srcAnim != 20 && srcAnim != 23) pr->vy = (jshort)(((dir == 0) ? -1 : 1) * spd << 8);
      else pr->vy = (jshort)(((dir == 0) ? 1 : -1) * spd << 8);
    } else {
      pr->vx = (jshort)((up ? 1 : -1) * spd << 8);
      pr->vy = 0;
    }
    if (type == 32) {
      pr->prevX = px;
      pr->prevY = py;
      pr->prev2X = px;
      pr->prev2Y = py - ((4 * tileHeight) << 8);
    }
    return;
  }
}

void Game::c_(int px, int py, int kind) {
  px -= 9;
  for (int i2 = 11; i2 >= 0; i2--) {
    if (ck[i2] == -1) {
      if (kind == 0) ck[i2] = (jbyte)(abs_(random->nextInt()) % 2);
      else if (kind == 1) ck[i2] = 2;
      else ck[i2] = 3;
      cg[i2] = px << 8;
      ch[i2] = py << 8;
      ci[i2] = 0;
      cj[i2] = 768;
      return;
    }
  }
}

void Game::d_(int col, int row, int kind) {
  for (int i2 = 3; i2 >= 0; i2--) {
    if (cq[i2] != -1) continue;
    cq[i2] = (jbyte)kind;
    if (kind != 0 && kind != 1) {
      if (kind == 2) {
        cm[i2] = (jshort)((col - 2) * tileWidth);
        cn[i2] = (jshort)(row * tileHeight);
        co[i2] = (jshort)(tileWidth << 1);
        cp[i2] = 0;
        return;
      }
      cm[i2] = (jshort)(col * tileWidth);
      cn[i2] = (jshort)((row - 2) * tileHeight);
      co[i2] = 0;
      cp[i2] = (jshort)(tileHeight << 1);
      return;
    }
    cm[i2] = (jshort)(col * tileWidth);
    cn[i2] = (jshort)(row * tileHeight);
    co[i2] = 0;
    cp[i2] = 0;
    return;
  }
}

void Game::e_(int px, int py, int type) {
  for (int i2 = 9; i2 >= 0; i2--) {
    Projectile* pr = playerProjectiles[i2];
    if (pr->type != -1) continue;
    if ((type >= 0 && type <= 14) || (type >= 18 && type <= 20)) {
      player->ammo[player->currentWeapon]--;
      e = true;
    }
    if ((type >= 9 && type <= 11) || (type >= 15 && type <= 17)) {
      pr->halfWidth = 84;
      pr->halfHeight = 11;
      px += ((player->facingRight ? 1 : -1) * 84) << 8;
    } else {
      pr->halfWidth = Projectile::HALF_WIDTHS[type];
      pr->halfHeight = Projectile::HALF_HEIGHTS[type];
    }
    pr->prev2X = pr->prevX = pr->posX = px;
    pr->prev2Y = pr->prevY = pr->posY = py;
    pr->type = (jbyte)type;
    pr->vx = ((player->facingRight ? 1 : -1) * Projectile::SPEEDS[type]) << 8;
    pr->vy = 0;
    pr->age = 0;
    pr->playerOffsetX = player->facingRight ? px - player->posX : player->posX - px;
    if (type >= 3 && type <= 5) pr->vy = 768;
    return;
  }
}

void Game::P_() {
  bool moving = false;
  for (jshort i2 = 0; i2 < 50; i2++) {
    if (bn[i2] == -1) continue;
    moving = true;
    jshort y0 = bm[i2];
    int r = y0 / tileHeight;
    int c2 = bl[i2] / tileWidth;
    jshort t;
    while (moving && ((t = levelMap->tiles[c2][r + 1]) < 19 || t > 27)) {
      for (jshort k2 = 0; k2 < 50; k2++) {
        if (bn[k2] != -1 && k2 != i2 && bl[k2] == bl[i2] && bm[k2] == y0 + tileHeight) { moving = false; break; }
      }
      if (moving) {
        bm[i2] = (jshort)(bm[i2] + tileHeight);
        y0 = (jshort)(y0 + tileHeight);
        r++;
      }
    }
  }
}

void Game::Q_() {
  P_();
  for (jshort i2 = 49; i2 >= 0; i2--) {
    if (bn[i2] == -1) continue;
    jshort y0 = bm[i2];
    bq[i2] = br[i2] = -1;
    for (jshort k2 = 49; k2 >= 0; k2--) {
      if (bn[k2] != -1 && bl[k2] == bl[i2]) {
        if (bm[k2] == y0 - tileHeight) { bq[i2] = k2; br[k2] = i2; }
        else if (bm[k2] == y0 + tileHeight) { br[i2] = k2; bq[k2] = i2; }
      }
    }
  }
}

void Game::f_() {
  for (int i2 = 11; i2 >= 0; i2--) { dL[i2] = false; dJ[i2] = 0; dK[i2] = 0; }
}

void Game::d_() {
  dp = 10000; dn = 0; dm = 0; do_ = 0; du = 0; dq = 0; dr = 0; dv = 0; dw = 0; dz = 0; dB = 0;
  dD = 0; dE = 0; dF = false; dG = false; dI = false; dH = false;
  f_();
}

int Game::e_() {
  int n2 = 0;
  for (int i2 = enemyPoolSize - 1; i2 >= 0; i2--)
    if (enemies[i2]->kind != 4 && enemies[i2]->kind != -1) n2++;
  return n2;
}

void Game::b_(int slot, int) { dJ[slot] = e_(); }

void Game::u_() {
  d_();
  bw = -1; bx = -1; by = -1; bT = -1; bU = -1; cJ = -1; cK = -1;
  cL = 1572865;
  player->ownedWeapons = 3;
  player->currentWeapon = 1;
  for (int i2 = 7; i2 >= 0; i2--) {
    player->N[i2] = 0;
    player->weaponLevel[i2] = 0;
    player->ammo[i2] = Player::INITIAL_AMMO[i2];
  }
  for (int i2 = 0; i2 < 10; i2++) bu[i2] = -1;
  for (int i2 = 0; i2 < 6; i2++) bt[i2] = false;
  for (int i2 = 0; i2 < 2; i2++) bv[i2] = -1;
  cu = 0;
}

void Game::f_(int level) {
  player->swingPhase = -2;
  cS = false;
  cD = false;
  bZ = true;
  o = 13;
  k = 0;
  dC = 0;
  ab = 1;
  aa = false;
  u_();
  if (level == 0) {
    Z = (jbyte)level;
    levelMap->loadLevelFile(level);
    X = LevelMap::currentSubGrid;
    di = 1;
    Y = X;
    levelMap->enterRoom(X, true);
    ad = 1;
    ae = 5;
    player->posX = (ad * tileWidth + (J >> 1)) << 8;
    player->row = (jbyte)(player->af = ae);
    player->animFrame = 1;
    player->animCounter = 0;
    player->animState = 0;
    player->animRestart = 0;
    player->posInRow = 0;
    player->kind = 0;
    player->activeFlag = 1;
    player->health = 20;
    cr = 0;
    ct = currentTimeMillis();
    b_((int)X, 6364);
    player->invulnTimer = 0;
    player->facingRight = true;
    d = true;
    aX = 0;
    player->setAnimState(0);
    player->animRestart = 0;
    ce = 1;
    cf = 18;
    player->ammo[1] = 0;
    x = -22;
    y = -32;
  }
  e = true;
  b = 17;
}

void Game::C_(int px, int py) {
  eb = false;
  int col = (px >> 8) / tileWidth;
  int row = (py >> 8) / tileHeight;
  if (!levelMap->isWalkable(col, row)) {
    if (levelMap->isWalkable(col, row - 1)) row--;
    else if (levelMap->isWalkable(col, row + 1)) row++;
    else if (levelMap->isWalkable(col + 1, row)) col++;
    else if (levelMap->isWalkable(col - 1, row)) col--;
    else return;
  }
  a_(col, row, (jbyte)1, -1);
}

void Game::updateCamera() {
  int cx = 0, cy = 0;
  jshort px = (jshort)(player->posX >> 8);
  jshort py = (jshort)(player->row * tileHeight + (player->posInRow >> 8));
  if (!z) {
    // Framing is relative to the original 176-wide window, centred in the wider view.
    cx = player->facingRight ? -(px - tileWidth - screen::offsetX()) : -(px + tileWidth - 176 - screen::offsetX());
  } else {
    cx = -(E - screen::width / 2);
    // The original also releases the hold when the player stands on the
    // ground (needs Player::groundYAhead, tick slice); z is never set true
    // yet, so only the distance test can run.
    if (px > E + (tileWidth >> 1) || px < E - (tileWidth >> 1)) z = false;
  }
  if (A) cy = -(py + K - 220);
  else cy = -(py + K + (tileHeight << 1) - 220);
  int dy2 = cy - y;
  y += dy2 >> 1;
  if (x > cx) { x -= 10; if (x < cx) x = cx; }
  else if (x < cx) { x += 10; if (x > cx) x = cx; }
  if (x > 0) x = 0;
  if (x < -(28 * tileWidth - screen::width)) x = -(28 * tileWidth - screen::width);
  if (y > 0) y = 0;
  if (y < -(18 * tileHeight - 220)) y = -(18 * tileHeight - 220);
}

// ---- in-level render pipeline ---------------------------------------------

void Game::a_(Graphics* g) {
  g->setClip(0, hudHeight, screen::width, 220 - hudHeight);
  for (int i2 = 0; i2 < 8; i2++) {
    if (bj[i2] == -1) return;
    int px = bj[i2] + x, py = bk[i2] + y;
    if (px < screen::width && py < 220 && px + 22 >= 0 && py + 22 >= 0) g->drawImage(au, px, py, 3);
  }
}

void Game::b_(Graphics* g) {
  if (bX == -1) return;
  int px = bX * tileWidth + x + ((tileWidth - 19) >> 1);
  int py = bY * tileHeight + y + ((tileHeight - 19) >> 1);
  if (px < screen::width && px + 19 >= 0 && py < 220 && py + 19 >= 0) {
    b_(g, px, py, 19, 19);
    g->drawImage(aL, px, py - 475, 0);
  }
}

void Game::c_(Graphics* g) {
  if (cc == -1) return;
  int w = ax->getWidth();
  int h2 = ax->getHeight() / 2;
  int px = cc * tileWidth + x;
  int py = cd * tileHeight + y + L;
  if (px < screen::width && px + w >= 0 && py < 220 && py + h2 >= 0 && h2 > 0) {
    b_(g, px, py, w, h2);
    g->drawImage(ax, px, py - ce * h2, 0);
  }
}

void Game::D_(Graphics* g) {
  int w = aw->getWidth(), h2 = aw->getHeight();
  int px = ca * tileWidth + x, py = cb * tileHeight + y;
  if (px < screen::width && px + w >= 0 && py < 220 && py + h2 >= 0 && h2 > 0) {
    b_(g, px, py, w, h2);
    g->drawImage(aw, px, py, 0);
  }
}

void Game::z_(Graphics* g) {
  int ox = x, oy = y + L;
  static const int cols[5] = {15658734, 12303291, 8947848, 5592405, 2236962};
  for (int i2 = 3; i2 >= 0; i2--) {
    if (bc[i2] == -1) continue;
    int x1 = bc[i2] + ox, y1 = bd[i2] + oy, x2 = be[i2] + ox, y2 = bf[i2] + oy;
    if ((x1 <= screen::width || x2 <= screen::width) && (x1 >= 0 || x2 >= 0) && (y1 <= 220 || y2 <= 220) && (y1 >= 0 || y2 >= 0)) {
      g->setClip(0, hudHeight, screen::width, 220 - hudHeight);
      for (int k2 = 0; k2 < 5; k2++) {
        g->setColor(cols[k2]);
        g->drawLine(x1, y1 - 2 + k2, x2, y2 - 2 + k2);
      }
    }
  }
}

void Game::A_(Graphics* g) {
  int tw = tileWidth, th = tileHeight;
  for (int i2 = 3; i2 >= 0; i2--) {
    if (cq[i2] < 0) continue;
    int px = cm[i2] + co[i2] + x, py = cn[i2] + cp[i2] + y;
    if (px < screen::width && px + tw >= 0 && py < 220 && py + th >= 0 && th > 0) {
      b_(g, px, py, tw, th);
      g->drawImage(aD, px, py - aQ * tileHeight, 0);
    }
  }
}

void Game::B_(Graphics* g) {
  int w = aJ->getWidth(), h2 = aJ->getHeight() >> 3;
  for (int i2 = 0; i2 < 50; i2++) {
    if (bn[i2] < 0) continue;
    int px = bl[i2] + x, py = bm[i2] + bo[i2] + y;
    if (px < screen::width && px + w >= 0 && py < 220 && py + h2 >= 0 && h2 > 0) {
      b_(g, px, py, w, h2);
      g->drawImage(aJ, px, py - bn[i2] * h2, 0);
    }
  }
}

void Game::C_(Graphics* g) {
  for (int i2 = 11; i2 >= 0; i2--) {
    if (ck[i2] < 0) continue;
    int px = (cg[i2] >> 8) + x, py = (ch[i2] >> 8) + y;
    if (px < screen::width && px + 19 >= 0 && py < 220 && py + 19 >= 0) {
      b_(g, px, py, 19, 19);
      g->drawImage(aL, px, py - ck[i2] * 19, 0);
    }
  }
}

void Game::E_(Graphics* g) {
  for (int i2 = enemyPoolSize - 1; i2 >= 0; i2--)
    if (enemies[i2]->kind != -1) enemies[i2]->render(g, i2, enemies[i2]->activeFlag, x, y);
  player->render(g, player->activeFlag, x, y);
  for (int i2 = 9; i2 >= 0; i2--) enemyProjectiles[i2]->render(g);
  for (int i2 = 9; i2 >= 0; i2--) playerProjectiles[i2]->render(g);
}

void Game::G_(Graphics* g) {
  int h4 = aC->getHeight() >> 2;
  int w = aC->getWidth();
  int shift = 0;
  for (int i2 = 0; i2 < 3; i2++) {
    if (bC[i2] == -128 || (bC[i2] != 0 && (bw & bC[i2]) != 0)) {
      int px = bD[i2] * tileWidth + x + ((tileWidth - w) >> 1);
      int py = bE[i2] * tileHeight + y;
      if (px + w >= 0 && px < screen::width && py + h4 >= 0 && py < 220) {
        if (bC[i2] >= 2 && bC[i2] <= 64) shift = h4;
        else if (bC[i2] == -128) {
          shift = h4 * 3;
          if ((bw & -128) != 0) shift = h4 << 1;
        }
        if (h4 > 0) {
          b_(g, px, py, w, h4);
          g->drawImage(aC, px, py - shift, 0);
        }
      }
    }
  }
  for (int i2 = 0; i2 < 3; i2++) {
    if (bF[i2] == -1) continue;
    int t = levelMap->getTile(bF[i2], bG[i2]) - 36;
    int mask = 1 << t;
    int px = bF[i2] * tileWidth + x + ((tileWidth - 19) >> 1);
    int py = bG[i2] * tileHeight + y + tileHeight - 19;
    if (px + 19 >= 0 && px < screen::width && py + 38 >= 0 && py < 220) {
      b_(g, px, py, 19, 38);
      g->drawImage(aL, px, py - (((bw & mask) == 0) ? 38 : 0) - 133, 0);
    }
  }
}

void Game::x_(Graphics* g) {
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  const int sw = screen::width;
  g->setClip(0, 0, sw, hudHeight);
  g->setColor(0);
  g->fillRect(0, 0, sw, hudHeight);
  int right = sw - (tileWidth >> 1) + 2 - 5;
  int h12 = aE->getHeight() / 12;
  int w = aE->getWidth();
  if (player->health < 0) player->health = 0;
  int lost = 20 - player->health;
  int half = h12 >> 1;
  // hud.png is 176 wide: columns 0..116 (weapon slot + health pill), a flat strip that repeats
  // unchanged over columns 117..158, and the right cap at 159..175. Wider screens keep the left
  // and right pieces at their native size and tile the flat strip between them.
  if (sw == 176) {
    g->drawImage(an, 0, 0, 0);
  } else {
    g->setClip(0, 0, 117, hudHeight);
    g->drawImage(an, 0, 0, 0);
    for (int sx = 117; sx < sw - 17; sx += 42) {
      g->setClip(sx, 0, std::min(42, sw - 17 - sx), hudHeight);
      g->drawImage(an, sx - 117, 0, 0);
    }
    g->setClip(sw - 17, 0, 17, hudHeight);
    g->drawImage(an, sw - 176, 0, 0);
    g->setClip(0, 0, sw, hudHeight);
  }
  g->setColor(0xFFFFFF);
  jbyte wpn = player->currentWeapon;
  if (wpn != 6 && wpn != 0)
    ratchetandclank::currentFont->drawText(g, std::to_string(player->ammo[wpn]), 3 + w + 6, 4, 0);
  ratchetandclank::currentFont->drawText(g, std::to_string(aX), right, 4, 24);
  g->setColor((37 << 16) | (84 << 8) | 106);
  for (int k2 = 0; k2 < lost; k2++) {
    int dx = (k2 >> 1) * half;
    int dy2 = (k2 % 2) * half;
    g->fillRect(51 + dx, 4 + dy2, half, half);
  }
  if (player->currentWeapon != 0) {
    a_(g, 3, 4, w, h12);
    g->drawImage(aE, 3, 4 - (player->currentWeapon - 1) * h12, 0);
  }
}

void Game::y_(Graphics* g) {
  if (A) {
    int h12 = aE->getHeight() / 12;
    int w = aE->getWidth();
    int ax = screen::offsetX() + ((176 - w) >> 1);
    g->setClip(ax, hudHeight, w, h12);
    g->drawImage(aE, ax, hudHeight - 7 * h12, 0);
  }
}

void Game::renderImpl(Graphics* g) {
  if (U > 0 || m) return;
  const int sw = screen::width, off = screen::offsetX();
  if (dT != -1 || dU != -1 || dZ) {
    g->setClip(0, 0, sw, 220);
    g->setColor(0);
    g->fillRect(0, 0, sw, 220);
    d = true;
    return;
  }
  if (aF == nullptr) return;
  // Screens laid out in 176x220 design coordinates: drawn centred, never scaled. The weapon
  // wheel (22) is an overlay on the frozen world, so its side areas are left alone.
  switch (b) {
    case 1: case 2: case 23: case 3: case 11: case 12: case 13: case 14: case 15: case 19:
    case 10: case 18: case 21: case 4: case 5: case 7: case 8: case 22:
      g->translate(off, 0);
      switch (b) {
        case 1: drawExtras(g); break;
        case 2: drawEndStats(g); break;
        case 23: drawRestartPrompt(g); break;
        case 3: drawWorldMap(g); break;
        case 11: drawWeaponBuy(g); break;
        case 12: drawBuyConfirm(g); break;
        case 13: drawNoFunds(g); break;
        case 14: drawChallengeIntro(g); break;
        case 15: drawChallengeEnd(g); break;
        case 19: drawChallengeFail(g); break;
        case 10: drawInfo(g); break;
        case 18: drawResults(g, dB); break;
        case 21: drawLevelEnd(g); break;
        case 4: d_(g); break;
        case 5: f_(g); break;
        case 7: v_(g); break;
        case 8: u_(g); break;
        case 22: drawWheel(g); break;
      }
      g->translate(-off, 0);
      if (b != 22) screen::paintSideBars();
      return;
    case 6: case 9:
      return;  // Game's own pause/store/results/game-over screens: later 3.2a slice
    default:
      break;
  }
  if (cS) player->invulnTimer = 1;
  levelMap->render(g);
  G_(g);
  z_(g);
  B_(g);
  C_(g);
  A_(g);
  if (bX != -1) b_(g);
  if (ca != -1) D_(g);
  if (cc != -1) c_(g);
  E_(g);
  a_(g);
  if (b == 16 && h == 4) bossMaxRender(g, j);
  if (Z == 12) bossRender(g);
  if (b != 16 && b != 17 && b != 24) {
    y_(g);
  } else {
    if (b == 24) {
      bossRender(g);
      for (int k2 = 9; k2 >= 0; k2--) playerProjectiles[k2]->render(g);
    }
    g->setClip(0, 0, sw, hudHeight);
    g->setColor(0);
    g->fillRect(0, 0, sw, hudHeight);
    g->setClip(0, 220 - hudHeight, sw, hudHeight);
    g->setColor(0);
    g->fillRect(0, 220 - hudHeight, sw, hudHeight);
    g->translate(off, 0);
    midlet->introManager->a_(g, -1, -1, this);
    g->translate(-off, 0);
  }
  if (Z == 11) {
    g->setClip(0, 220 - hudHeight, sw, hudHeight);
    g->setColor(0);
    g->fillRect(0, 220 - hudHeight, sw, hudHeight);
    ratchetandclank::currentFont = ratchetandclank::smallFont;
    g->setColor(1882828);
    ratchetandclank::currentFont->drawText(g, ratchetandclank::strings[311] + " " + std::to_string(db), sw / 2, 220 - hudHeight + 3, 17);
  }
  if (e) {
    x_(g);
    e = false;
  }
  if (cD) {
    g->translate(off, 0);
    H_(g);
    cG = a_(g, cF);
    g->translate(-off, 0);
  }
  if (dX != -1) {
    cu = dX;
    g->translate(off, 0);
    drawWheel(g);
    g->translate(-off, 0);
    b = 22;
    dX = -1;
  }
}

void Game::c_(int level, int slot) {
  dT = level;
  dU = slot;
}

void Game::m_() {
  sleep(20);
  c = b;
  b = 4;
  cv = 0;
  cu = 0;
}

void Game::n_() {
  if (dV) return;
  dV = true;
  midlet->stopSoundSoft();
  d = true;
  dW = 0;
  cv = 0;
  cw = 0;
  if (b == 0 || b == 17 || b == 16 || b == 24 || b == 22) {
    if (b == 22) {
      dX = cu;
      b = 0;
    }
    cu = 0;
    c = b;
    b = 4;
    aF = nullptr;
  }
}

void Game::y_() { aF = Image::createImage(aR[3]); }

void Game::z_() { aF = Image::createImage(aR[aQ]); }

void Game::o_() {
  if (!dV) return;
  U = 4;
  if (aF == nullptr) y_();
  d = true;
  dV = false;
  dW = currentTimeMillis();
}

void Game::pause() { n_(); }

void Game::resume() {
  o_();
  dV = false;
}
