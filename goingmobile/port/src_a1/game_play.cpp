// game_play.cpp -- Game's per-tick gameplay loop, input handling, collision
// helpers, room transitions and hint/dialogue panel, transcribed from
// src_a1/Game.java (see game.h for the obfuscated-name -> C++ overload map).
#include "game.h"
#include "canvasshell.h"
#include "midlet.h"
#include "intromanager.h"
#include "font.h"
#include <algorithm>
#include <string>

jbyte Game::bR = 30;
jbyte Game::bS = 60;

// ---- collision / scan helpers ----------------------------------------------

int Game::G_() {
  short px = player->x();
  int py = player->y() + L;
  for (int i2 = enemyPoolSize - 1; i2 >= 0; i2--) {
    Enemy* en = enemies[i2];
    jbyte k = en->kind;
    if (k != -1 && k != 4 && abs_(en->posX - player->posX) <= (tileWidth << 8) &&
        abs_(en->row - player->row) <= 3 && en->animState != 5) {
      short ex = en->x(), ey = en->y();
      if (a_(ex - Enemy::HITBOX_X_OFFSETS[k], ey + Enemy::HITBOX_Y_OFFSETS[k], Enemy::HITBOX_WIDTHS[k],
             Enemy::HITBOX_HEIGHTS[k], px - 11, py + 7, 18, 37))
        return i2;
    }
  }
  return -1;
}

bool Game::D_() {
  int w = aJ->getWidth();
  int h2 = aJ->getHeight() >> 3;
  int px = player->posX >> 8;
  short py = player->y();
  int hh = tileHeight - 1;
  for (int i2 = 49; i2 >= 0; i2--) {
    if (bn[i2] >= 0 && abs_(bl[i2] - px) <= tileWidth && abs_(bm[i2] - py) <= tileHeight &&
        a_(bl[i2], bm[i2], w, h2, px - 8, py, 16, hh))
      return true;
  }
  return false;
}

bool Game::E_() {
  int w = aJ->getWidth();
  int px = player->posX >> 8;
  short py = player->y();
  int mid = 0;
  for (int i2 = 49; i2 >= 0; i2--) {
    if (bn[i2] >= 0 && ((mid = bl[i2] + (w >> 1)) <= px || player->facingRight) &&
        (mid >= px || !player->facingRight) && abs_(mid - px) <= tileWidth && bm[i2] == py)
      return true;
  }
  return false;
}

bool Game::F_() {
  for (int i2 = enemyPoolSize - 1; i2 >= 0; i2--) {
    Enemy* en = enemies[i2];
    if (en->kind != -1 && en->kind != 4 && (en->posX <= player->posX || player->facingRight) &&
        (en->posX >= player->posX || !player->facingRight) && abs_(en->posX - player->posX) <= (tileWidth << 8) &&
        en->animState != 5 && player->row == en->row)
      return true;
  }
  return false;
}

bool Game::k_(int idx) {
  short px = player->x();
  int py = player->y() + L;
  Enemy* en = enemies[idx];
  jbyte k = en->kind;
  if (k != -1 && k != 4) {
    if (abs_(en->posX - player->posX) <= (tileWidth << 8) && abs_(en->row - player->row) <= 3 && en->animState != 5) {
      short ex = en->x(), ey = en->y();
      return a_(ex - Enemy::HITBOX_X_OFFSETS[k], ey + Enemy::HITBOX_Y_OFFSETS[k], Enemy::HITBOX_WIDTHS[k],
                Enemy::HITBOX_HEIGHTS[k], px - 11, py + 7, 18, 37);
    }
  }
  return false;
}

short Game::B_() {
  short best = (short)(18 * tileHeight);
  int v2 = 0;
  if (player->zipGrabRetryDelay != 0) return best;
  for (int i2 = 3; i2 >= 0; i2--) {
    if (cq[i2] != -1 && cm[i2] + co[i2] <= (player->posX >> 8) + 8 &&
        cm[i2] + co[i2] + tileWidth >= (player->posX >> 8) - 8 && (v2 = cn[i2] + cp[i2]) < best &&
        v2 > player->y()) {
      best = (short)v2;
      Player::hudHeight = 0;
      if (cq[i2] == 0) Player::hudHeight = 2;
      else if (cq[i2] == 2) Player::hudHeight = -2;
    }
  }
  return (short)(best - tileHeight);
}

short Game::C_() {
  short best = (short)(18 * tileHeight);
  short py = player->y();
  int px = player->posX >> 8;
  int w = aJ->getWidth();
  for (int i2 = 49; i2 >= 0; i2--) {
    if (bn[i2] != -1 && bm[i2] >= py && bl[i2] <= px + 8 && bl[i2] + w >= px - 8 && bm[i2] < best) best = bm[i2];
  }
  return (short)(best - tileHeight);
}

void Game::a_(int col, int row, int) {
  player->swingTargetX = ((col * tileWidth + (tileWidth >> 1) + (player->facingRight ? -5 : 5))) << 8;
  player->swingTargetY = (row * tileHeight + (tileHeight >> 1) - 2) << 8;
  int ox = 4 * tileWidth / 44;
  if (!player->facingRight) ox = -ox;
  int oy = 10 * tileHeight / 44;
  player->swingCurX = ((player->posX >> 8) + ox) << 8;
  player->swingCurY = (player->row * tileHeight + (player->posInRow >> 8) + oy) << 8;
  player->swingStepX = (player->swingTargetX - player->swingCurX) / 6;
  player->swingStepY = (player->swingTargetY - player->swingCurY) / 6;
  if (player->swingTargetY < player->swingCurY) {
    // Game.java writes `player.tileWidth = (byte)var3`, which is Player's STATIC tile
    // width (see the matching Enemy note in game_level.cpp) -- omitted; the tile id
    // (var3) picks the grab type in the original's renamed field, grabTileType.
    player->swingPhase = 0;
    player->setAnimState(5);
    player->animRestart = 2;
  }
}

// ---- secrets / bitsets / hint messages -------------------------------------

void Game::g_(int bit) {
  if (bit >= 0 && bit <= 19) {
    cJ &= ~(1 << bit);
    if ((bit == 4 || bit == 5 || bit == 6 || bit == 7 || bit == 9 || bit == 10) && !h_(4) && !h_(5) && !h_(6) &&
        !h_(7) && !h_(9) && !h_(10)) {
      cJ |= 8;
      return;
    }
  } else if (bit >= 20 && bit <= 39) {
    cK &= ~(1 << (bit - 20));
  }
}

void Game::e_(int level, int room) {
  if (level > 0 && level <= 5) bx &= ~(1 << ((level - 1) * 6 + room));
  else if (level >= 6 && level <= 10) by &= ~(1 << ((level - 5) * 6 + room));
}

int Game::v_() {
  int n2 = 0;
  for (int i2 = (bS >> 1) - 1; i2 >= 0; i2--) {
    if ((bT & (1 << i2)) == 0) n2++;
    if ((bU & (1 << i2)) == 0) n2++;
  }
  return n2;
}

void Game::g_(int level, int room) {
  if (level > 0 && level <= 5) bT &= ~(1 << ((level - 1) * 6 + room));
  else if (level >= 6 && level <= 10) bU &= ~(1 << ((level - 6) * 6 + room));
  if (v_() >= bR) {
    cC = false;
    r_(72);
    player->ownedWeapons = (jbyte)(player->ownedWeapons | 128);
    da = (jbyte)(da | 128);
  } else if (v_() == 1) {
    cC = false;
    r_(74);
  } else {
    cC = false;
    r_(227);
  }
}

void Game::r_(int idx) {
  using RC = ratchetandclank;
  player->invulnTimer = 0;
  cF = 0;
  cH = idx;
  bool anyBlaster = h_(4) || h_(5) || h_(6) || h_(7) || h_(9) || h_(10);
  bool allBlasters = h_(4) && h_(5) && h_(6) && h_(7) && h_(9) && h_(10);
  bool grp = (idx == 125 || idx == 126 || idx == 127 || idx == 128 || idx == 132 || idx == 133);
  if (idx == 112 && !anyBlaster) {
    cI = cz[16];
    cH = 150;
    cE = RC::strings[150];
  } else if (grp && allBlasters) {
    cI = cz[17];
    cH = 156;
    cE = RC::strings[156];
  } else if (grp) {
    int n2 = 1;
    if (!h_(4)) n2++;
    if (!h_(5)) n2++;
    if (!h_(6)) n2++;
    if (!h_(7)) n2++;
    if (!h_(9)) n2++;
    if (!h_(10)) n2++;
    cE = a_(RC::strings[idx], {std::to_string(n2)});
  } else if (idx == 227) {
    cE = a_(RC::strings[idx], {std::to_string(v_()), std::to_string(bR)});
  } else {
    cE = RC::strings[idx];
  }
  if (cE.empty()) cF = -1;
  cD = true;
}

void Game::S_() {
  cD = false;
  if (cH == 104) { di = 524288; cL |= 4; }
  else if (cH == 106) { di = 16; cL |= 16; cL |= 262144; }
  else if (cH == 110) { di = 128; cL |= 128; }
  else if (cH == 112) { di = 10; cL |= 10; bw = (jbyte)(bw & -3); }
  else if (cH == 150) { cL |= 32768; di = 32768; player->ownedWeapons = (jbyte)(player->ownedWeapons | 32); }
  else if (cH == 125) { di &= -3; di |= 8192; cL |= 8192; }
  else if (cH == 126) { di &= -9; di |= 1024; cL |= 1024; }
  else if (cH == 127) {
    di &= -1025;
    if ((cJ & 128) == 0) { di |= 512; cL |= 512; }
  } else if (cH == 128) {
    di &= -8193;
    if ((cJ & 64) == 0) { di |= 512; cL |= 512; }
  } else if (cH == 129) { di = 20480; cL |= 4096; cL |= 16384; }
  else if (cH == 132) {
    di &= -4097;
    if (!h_(4) && !h_(5) && !h_(6) && !h_(7) && !h_(9) && !h_(10)) { cL |= 64; di = 64; }
  } else if (cH == 133) {
    di &= -16385;
    if (!h_(4) && !h_(5) && !h_(6) && !h_(7) && !h_(9) && !h_(10)) { cL |= 64; di = 64; }
  } else if (cH == 134) { cL |= 2048; di = 2048; bw = (jbyte)(bw & -9); }
  else if (cH == 135) { cL |= 256; di = 256; bw = (jbyte)(bw & -129); }
  else if (cH == 140) { di = 65536; cL |= 65536; }
  else if (cH == 144) { di = 131072; cL |= 131072; }
  else if (cH == 156) {
    if (!h_(4)) { di &= -3; di |= 8192; cL |= 8192; }
    else { di &= -9; di |= 1024; cL |= 1024; }
  }

  if (--cI > 0) {
    r_(cH + 1);
  } else {
    cC = false;
    cI = 0;
    if (cH >= 104 && cH <= 156 + cz[17] && Z != 0) {
      // Results/summary screen (Game.a(dB) + state 18): not ported yet.
      y_();
      b = 18;
    }
    if (cH + 1 == cA[33] + cz[33]) {
      y_();
      b = 18;
    }
    if (cH + 1 == cA[31] + cz[31]) g = true;
    if (cH + 1 == cA[32] + cz[32]) {
      player->setAnimState(1);
      player->animRestart = 0;
    }
  }
}

void Game::H_(Graphics* g) {
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  int panelH = 42;
  bool portrait = cC && cH >= 104 && cH - 104 < 109 && cB[cH - 104] != -1;
  if (portrait && 42 < at->getHeight() / 5 + 12) panelH = at->getHeight() / 5 + 12;
  int top = 220 - panelH;
  g->setClip(0, 0, 176, 220);
  g->setColor(16777215);
  g->fillRect(0, top, 176, panelH);
  if (portrait) {
    int ph = at->getHeight() / 5;
    int pw = at->getWidth();
    g->setColor((52 << 16) | (86 << 8) | 92);
    top += (panelH - ph) >> 1;
    g->fillRect(6 + pw, top, 176 - pw - 12, 2);
    g->fillRect(168, top, 2, ph);
    g->fillRect(6 + pw, top + ph - 2, 176 - pw - 12, 2);
    g->setClip(6, top, at->getWidth(), ph);
    g->drawImage(at, 6, top - ph * cB[cH - 104], 20);
  }
}

int Game::a_(Graphics* g, int offset, const String& text, int x0, int y0, int anchor, int width) {
  Font* small = ratchetandclank::smallFont;
  int len = (int)text.size();
  int lineW = 0;
  int wordW = 0;
  int wordStart = offset;
  int pos = offset;
  bool started = false;
  while (true) {
    wordW = 0;
    int prevEnd = pos - 1;
    bool atEnd = false, overflow = false;
    int dash = -1;
    while (true) {
      if (pos >= len) { atEnd = true; break; }
      char c = text[(size_t)pos];
      if (c == ' ') {
        wordW += small->charWidth(c);
        pos++;
        if (lineW + wordW > width && !started) { overflow = true; break; }
        started = true;
        break;
      }
      if (c == '-') dash = pos;
      int cw = small->charWidth(c);
      if (lineW + wordW + cw > width && !started) { overflow = true; break; }
      wordW += cw;
      pos++;
    }
    if (lineW + wordW > width || atEnd || overflow) {
      if (text[(size_t)wordStart] == ' ') wordStart++;
      if (lineW + wordW > width) {
        if (!overflow) pos = prevEnd;
        else if (dash >= 0) pos = dash + 1;
      } else if (overflow && !started && dash >= 0) {
        pos = dash + 1;
      }
      if (pos - wordStart > 0)
        ratchetandclank::currentFont->drawTextRange(g, text.c_str(), len, wordStart, pos - wordStart,
                                                    (anchor & 1) > 0 ? 88 : x0, y0, anchor);
      return pos + (overflow ? 0 : 1);
    }
    lineW += wordW;
  }
}

int Game::a_(Graphics* g, int offset) {
  int x0 = 6;
  jbyte anchor = 17;
  bool portrait = cC && cH >= 104 && cH - 104 < 109 && cB[cH - 104] != -1;
  if (portrait) {
    x0 = at->getWidth() + 6 + 3;
    anchor = 20;
  }
  g->setColor(0);
  g->setClip(0, 0, 176, 220);
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  int total = (int)cE.size();
  int y0;
  if (portrait) {
    int ph = std::max(at->getHeight() / 5, 30);
    y0 = 220 - (((ph - 30) >> 1) + 30 + 6);
  } else {
    y0 = 184;
  }
  if ((offset = a_(g, offset, cE, x0, y0, anchor, 176 - x0 - 6)) >= total) return -1;
  if ((offset = a_(g, offset, cE, x0, y0 + 10, anchor, 176 - x0 - 6)) >= total) return -1;
  int next = a_(g, offset, cE, x0, y0 + 20, anchor, 176 - x0 - 6 - 7);
  return next >= total ? -1 : next;
}

// ---- room transitions / respawn --------------------------------------------

void Game::j_(int edge) {
  switch (edge) {
    case 1: ed = 6; Y = LevelMap::subGridNeighbors[X][0]; break;
    case 2: ed = 5; Y = LevelMap::subGridNeighbors[X][3]; break;
    case 5: ed = 2; Y = LevelMap::subGridNeighbors[X][2]; break;
    case 6: ed = 1; Y = LevelMap::subGridNeighbors[X][1]; break;
    default: break;
  }
  if (Y != -1) {
    X = Y;
    V_();
  }
}

void Game::V_() {
  m = true;
  b_((int)X, 7455);
  levelMap->enterRoom(X, false);
  switch (ed) {
    case 1: player->row = (jbyte)(player->af = 1); y = 0; break;
    case 2: player->posX = (1 * tileWidth + (J >> 1)) << 8; x = 0; break;
    case 5: player->posX = (26 * tileWidth + (J >> 1)) << 8; x = v; break;
    case 6: player->row = (jbyte)(player->af = 16); y = w; break;
    default: break;
  }
  m = false;
}

void Game::w_() {
  player->swingPhase = -2;
  for (int i2 = 0; i2 < 6; i2++) bt[i2] = true;
  X = ah;
  b_((int)X, 6911);
  levelMap->enterRoom(ah, false);
  player->invulnTimer = 10;
  player->posX = (ad * tileWidth + (J >> 1)) << 8;
  player->row = ae;
  x = af;
  y = ag;
  player->posInRow = 0;
  player->health = 20;
  player->velX = player->velY = 0;
  player->setAnimState(0);
  player->animRestart = 0;
  e = true;
  if (Z == 12) {
    bI[0] = bI[1] = bI[2] = bI[3] = 0;
    bH[0] = bH[1] = bH[2] = bH[3] = 0;
    bJ[0] = bJ[1] = bJ[2] = bJ[3] = 100;
    bJ[4] = 200;
  }
}

// ---- input ----------------------------------------------------------------

void Game::keyPressed(int key) {
  if (m || U > 0) return;
  if (dW > 0 && currentTimeMillis() - dW < 1000) return;
  int action;
  if (key == 50) action = -1;
  else if (key == 56) action = -2;
  else if (key == 52) action = -3;
  else if (key == 54) action = -4;
  else if (key == 53) action = -5;
  else action = key;
  d = true;
  switch (b) {
    case 0: case 16: case 17: case 24:
      A_(key, action);
      return;
    case 4:
      // Placeholder for the pause menu (Game.h(int,int), not ported yet):
      // any key resumes play so the player is never trapped in state 4.
      b = c;
      z_();
      e = true;
      return;
    default:
      return;  // other UI states (store/results/game-over/...) not ported yet
  }
}

void Game::keyReleased(int key) {
  if (m || U > 0) return;
  if (key == 50) key = -1;
  else if (key == 52 || key == 49) key = -3;
  else if (key == 54 || key == 51) key = -4;
  else if (key == 56) key = -2;
  B_(key, key);
}

void Game::A_(int key, int action) {
  if (cD) {
    if (action == -5 || key == 53) {
      if (cF >= 0) cF = cG;
      if (cF < 0) {
        S_();
        cG = 0;
      }
    }
    return;
  }
  if (b == 16 || b == 17 || b == 24) return;

  if (key == -6 || key == -7) {
    cu = 0;
    c = b;
    b = 4;
    y_();
    return;
  }
  if (player->animState == 10) {
    cv = cw = 0;
    cx &= -129;
    cy = 0;
    return;
  }

  if (action == -1) {
    if (player->animState == 11) return;
    jbyte wallKick = 0;
    if (player->swingPhase == -1 && player->animState != 12) {
      player->hyperShotSearch();
    } else if (player->jumpPhase >= 0) {
      if (player->wallOnLeft() && !player->facingRight && ea != -1) {
        if (!z) {
          z = true;
          E = player->x() - 8 + (tileWidth >> 1);
        }
        wallKick = -1;
        ea = -1;
        player->velX = 2500;
        player->jumpPhase = 0;
        player->facingRight = true;
        player->setAnimState(4);
        cw = cv = 0;
      } else if (player->wallOnRight() && player->facingRight && ea != 1) {
        if (!z) {
          z = true;
          E = player->x() + 8 - (tileWidth >> 1);
        }
        wallKick = 1;
        ea = 1;
        player->velX = -2500;
        player->jumpPhase = 0;
        player->facingRight = false;
        player->setAnimState(4);
        cw = cv = 0;
      }
    }
    if (player->jumpPhase < 1 || wallKick != 0) {
      cv = action;
      return;
    }
  } else if (action == -2) {
    if (player->animState == 5) return;
    if (player->jumpPhase == 2) {
      player->setAnimState(3);
      player->velX = 0;
      player->velY = 0;
      player->jumpPhase = 1;
      if ((player->ownedWeapons & 1) > 0) {
        cw = 0;
        player->setAnimState(14);
        player->animRestart = 1;
        player->swingTargetType = 1;
        return;
      }
    } else {
      if ((player->ownedWeapons & 1) > 0 && player->jumpPhase != -1 && player->animState != 11 &&
          player->animState != 14) {
        cw = 0;
        player->setAnimState(14);
        player->animRestart = 1;
        player->swingTargetType = 1;
        return;
      }
      if (B) {
        // Level exit (Game.a(int,boolean) with C=-1): results flow not ported yet.
        return;
      }
      if (player->ledgeAhead) {
        player->ledgeAhead = false;
        player->row++;
        player->startFall();
        player->zipGrabRetryDelay = 10;
        if ((player->ownedWeapons & 1) > 0 && player->animState != 14) {
          cw = 0;
          player->setAnimState(14);
          player->animRestart = 1;
          player->swingTargetType = 1;
          return;
        }
      } else if (player->onLadderTop() && player->velY == 0 && player->animState != 14 && player->animState != 11) {
        player->startFall();
        player->row++;
        player->posInRow = tileHeight - 2;
        if ((player->ownedWeapons & 1) > 0) {
          cw = 0;
          player->setAnimState(14);
          player->animRestart = 1;
          player->swingTargetType = 1;
          return;
        }
      }
    }
  } else if (action == -3) {
    cw = cv = action;
    if (z && player->x() < E + (tileWidth >> 1) && player->velX > 0) {
      cw = cv = 0;
      return;
    }
  } else if (action == -4) {
    cw = cv = action;
    if (z && player->x() > E - (tileWidth >> 1) && player->velX < 0) {
      cw = cv = 0;
      return;
    }
  } else if (key == 53 || action == -5) {
    jbyte col = player->column();
    jbyte trow = (jbyte)((player->y() + K) / tileHeight);
    short tileAbove = levelMap->getTile(col, trow - 1);
    int mask = 1 << (tileAbove - 36);
    if ((player->ownedWeapons & 1) > 0 && player->jumpPhase == -1 && player->animState != 11 &&
        player->animState != 14 && player->animState != 8 &&
        (F_() || E_() || player->ammo[player->currentWeapon] <= 0 ||
         ((bw & mask) != 0 && tileAbove >= 36 && tileAbove <= 42))) {
      cw = 0;
      player->swingTargetType = 0;
      player->meleeHit();
      return;
    }
    if (player->ammo[player->currentWeapon] > 0 && cx == 0) {
      player->fire();
      cx = 129;
      dD++;
      cy = currentTimeMillis();
      return;
    }
  } else if (key == 42) {
    cw = 0;
    if ((player->ownedWeapons & 1) > 0 && player->jumpPhase == -1) {
      player->swingTargetType = 0;
      player->meleeHit();
      return;
    }
  } else if (key == 35) {
    cu = 0;
    b = 22;
    if (player->currentWeapon != 0) {
      if (++player->currentWeapon > 7) player->currentWeapon = 1;
      while ((player->ownedWeapons & (1 << player->currentWeapon)) == 0) {
        if (++player->currentWeapon > 7) player->currentWeapon = 1;
      }
      e = true;
      return;
    }
  } else if (key == 49 || key == 51) {
    if (!z && player->animState != 11 && player->swingPhase < 0 && player->animState != 5) {
      if (player->animState != 12) player->setAnimState(0);
      if (player->jumpPhase < 0) {
        player->velY = 3584;
        player->jumpPhase++;
      } else if (player->jumpPhase < 1) {
        player->velY = 3072;
        player->jumpPhase++;
        player->animRestart = 1;
        player->setAnimState(13);
      }
      cv = (key == 49) ? -3 : -4;
    }
  }
}

void Game::B_(int key, int action) {
  if (key == 53 || action == -5) {
    cx &= -129;
    if (player->animState != 8) player->resetAttackAnim();
    cy = 0;
  } else if ((cx & 128) != 0 && cy != 0 && currentTimeMillis() - cy > 5000) {
    cx &= -129;
    if (player->animState != 8) player->resetAttackAnim();
    cy = 0;
  }
  if (action == cv) cv = 0;
  if (action == cw) cw = 0;
}

// ---- projectile vs platform / enemy collision -------------------------------

void Game::n_(int slot) {
  jshort above = bq[slot];
  jshort carry = 0;
  bool wasMoving = false;
  jshort room = X;
  if (cV) room = 0;
  bu[(slot + room * 50) >> 5] &= ~(1 << (slot - ((slot >> 5) << 5)));
  if (bs[slot] || (above != -1 && bs[above])) wasMoving = true;
  for (; above != -1; above = bq[above]) {
    bs[above] = true;
    if (wasMoving) {
      if (br[above] == slot) {
        carry = bp[above];
        bp[above] = bp[slot];
      } else {
        jshort tmp = bp[above];
        bp[above] = carry;
        carry = tmp;
      }
    } else {
      bp[above] = bm[br[above]];
    }
  }
  if (bq[slot] != -1) br[bq[slot]] = br[slot];
  if (br[slot] != -1) bq[br[slot]] = bq[slot];
  bq[slot] = -1;
  br[slot] = -1;
}

void Game::l_(int idx) {
  int w = aJ->getWidth();
  int h2 = aJ->getHeight() >> 3;
  Projectile* pr = enemyProjectiles[idx];
  jbyte type = pr->type;
  if (type != -1 && type != 30 && (type < 15 || type > 17)) {
    int px = (pr->posX >> 8) - pr->halfWidth;
    int py = (pr->posY >> 8) - pr->halfHeight;
    int pw = pr->halfWidth << 1, ph = pr->halfHeight << 1;
    for (int i2 = 49; i2 >= 0; i2--) {
      if (bn[i2] != -1 && bl[i2] + x <= 176 && bl[i2] + w + x >= 0 && bm[i2] + y <= 220 && bm[i2] + h2 + y >= 0 &&
          a_(bl[i2], bm[i2], w, h2, px, py, pw, ph))
        pr->detonate(true);
    }
  }
}

void Game::m_(int idx) {
  int w = aJ->getWidth();
  int h2 = aJ->getHeight() >> 3;
  if (idx == -1) {
    int swx = (player->posX >> 8) + (player->facingRight ? Player::SWING_X_OFFSET[player->swingTargetType]
                                                          : -Player::SWING_X_OFFSET[player->swingTargetType] -
                                                                Player::SWING_WIDTH[player->swingTargetType]);
    int swy = player->y() + Player::SWING_Y_OFFSET[player->swingTargetType];
    for (int i2 = 49; i2 >= 0; i2--) {
      if (bn[i2] != -1 && abs_(bl[i2] - (player->posX >> 8)) <= (3 * tileWidth >> 1) &&
          abs_(bm[i2] - player->y()) <= tileHeight * 2 &&
          a_(bl[i2], bm[i2] + bo[i2], aJ->getWidth(), aJ->getHeight() >> 2, swx, swy,
             Player::SWING_WIDTH[player->swingTargetType], Player::SWING_HEIGHT[player->swingTargetType])) {
        midlet->playSoundIfEnabled(4);
        n_(i2);
        dE++;
        if (bn[i2] == 0) {
          c_(bl[i2] + (w >> 1), bm[i2] + bo[i2], 0);
          c_(bl[i2] + (w >> 1), bm[i2] + bo[i2], 0);
        } else {
          c_(bl[i2] + (w >> 1), bm[i2] + bo[i2], bn[i2]);
        }
        e_((bl[i2] + (w >> 1)) << 8, (bm[i2] + (h2 >> 1) + bo[i2]) << 8, 30);
        bn[i2] = -1;
      }
    }
    return;
  }
  Projectile* pr = playerProjectiles[idx];
  jbyte type = pr->type;
  if (type != -1 && type != 30 && (type < 15 || type > 17)) {
    int px = (pr->posX >> 8) - pr->halfWidth;
    int py = (pr->posY >> 8) - pr->halfHeight;
    int pw = pr->halfWidth << 1, ph = pr->halfHeight << 1;
    for (int i2 = 49; i2 >= 0; i2--) {
      if (bn[i2] != -1 && bl[i2] + x <= 176 && bl[i2] + w + x >= 0 && bm[i2] + y <= 220 && bm[i2] + h2 + y >= 0 &&
          a_(bl[i2], bm[i2] + bo[i2], w, h2, px, py, pw, ph)) {
        n_(i2);
        dE++;
        if (bn[i2] == 0) {
          c_(bl[i2] + (w >> 1), bm[i2] + bo[i2], 0);
          c_(bl[i2] + (w >> 1), bm[i2] + bo[i2], 0);
        } else {
          c_(bl[i2] + (w >> 1), bm[i2] + bo[i2], bn[i2]);
        }
        e_((bl[i2] + (w >> 1)) << 8, (bm[i2] + (h2 >> 1) + bo[i2]) << 8, 30);
        bn[i2] = -1;
        pr->detonate(false);
      }
    }
  }
}

// ---- per-tick world updates ---------------------------------------------------

void Game::H_() {
  int fall = tileHeight >> 3;
  for (int i2 = 11; i2 >= 0; i2--) {
    if (ck[i2] != -1) {
      ch[i2] = ch[i2] + (fall << 8);
      int floorY = b_((cg[i2] >> 8) + 9, ch[i2] >> 8, tileHeight - 19);
      if ((ch[i2] >> 8) + 19 >= floorY) ch[i2] = (floorY - 19) << 8;
    }
  }
}

void Game::I_() {
  if (bP != -1) {
    int px = bP * tileWidth;
    int py = bQ * tileHeight;
    if (a_(player->x() - 11, player->y() + L + 7, 18, 37, px, py, 19, 19)) {
      bP = bQ = -1;
      g_((int)Z, (int)X);
      dr++;
    }
  }
}

void Game::J_() {
  int w = aJ->getWidth();
  int h2 = aJ->getHeight() >> 3;
  int hitW = 8;
  short py = player->y();
  int hitWide = 16;
  int hitH = K;
  for (int i2 = 49; i2 >= 0; i2--) {
    int px = player->posX >> 8;
    if (bs[i2] && bn[i2] != -1) {
      if (br[i2] == -1 || !bs[br[i2]]) {
        if (a_(bl[i2], bm[i2] + bo[i2], w, h2, px - hitW, py, hitWide, hitH)) {
          if (bn[i2] == 0) {
            c_(bl[i2] + (w >> 1), bm[i2] + bo[i2], 0);
            c_(bl[i2] + (w >> 1), bm[i2] + bo[i2], 0);
          } else {
            c_(bl[i2] + (w >> 1), bm[i2] + bo[i2], bn[i2]);
          }
          n_(i2);
          e_((bl[i2] + (w >> 1)) << 8, (bm[i2] + (h2 >> 1) + bo[i2]) << 8, 30);
          bn[i2] = -1;
          continue;
        }
        hitW = 12;
        hitWide = 24;
        for (int k2 = enemyPoolSize - 1; k2 >= 0; k2--) {
          if (enemies[k2]->kind >= 0) {
            px = enemies[k2]->posX >> 8;
            py = enemies[k2]->y();
            if (px + 12 >= bl[i2] && px - 12 <= bl[i2] + w &&
                a_(bl[i2], bm[i2] + bo[i2], w, h2, px - 12, py, 24, hitH)) {
              n_(i2);
              bs[i2] = false;
              if (bn[i2] == 0) {
                c_(bl[i2] + (w >> 1), bm[i2] + bo[i2], 0);
                c_(bl[i2] + (w >> 1), bm[i2] + bo[i2], 0);
              } else {
                c_(bl[i2] + (w >> 1), bm[i2] + bo[i2], bn[i2]);
              }
              e_((bl[i2] + (w >> 1)) << 8, (bm[i2] + (h2 >> 1) + bo[i2]) << 8, 30);
              bn[i2] = -1;
              break;
            }
          }
        }
      }
      bo[i2] = (jshort)(bo[i2] + 4);
      if (bn[i2] != -1 && bo[i2] + bm[i2] >= bp[i2]) {
        bm[i2] = bp[i2];
        bo[i2] = 0;
        bs[i2] = false;
      }
    }
  }
}

void Game::K_() {
  if (ca != -1) {
    int h2 = aw->getHeight();
    int w = aw->getWidth();
    int px = ca * tileWidth;
    int py = cb * tileHeight;
    if (a_(player->x() - 11, player->y() + L + 7, 18, 37, px, py, h2, w)) {
      ca = cb = -1;
      cC = true;
      cI = cz[11];
      r_(cA[11]);
      g_(11);
      dC = 261;
      dy = 11;
    }
  }
}

void Game::b_() {
  if (bX != -1) {
    int px = bX * tileWidth;
    int py = bY * tileHeight;
    if (a_(player->x() - 11, player->y() + L + 7, 18, 37, px, py, 19, 19)) {
      bX = bY = -1;
      bZ = false;
      cC = true;
      cI = cz[34];
      r_(cA[34]);
    }
  }
}

void Game::c_() {
  cf--;
  if (cf == 0) cf = 36;
  if (ce == 1) {
    if (cf % 18 == 0) { ce = 0; return; }
  } else if (cf % 36 == 0) {
    ce = 1;
  }
}

void Game::l_() {
  int w2 = tileWidth << 1;
  int h2 = tileHeight << 1;
  for (int i2 = 3; i2 >= 0; i2--) {
    if (cq[i2] == -1) continue;
    switch (cq[i2]) {
      case 0:
        co[i2] = (jshort)(co[i2] + 2);
        if (co[i2] > w2) { co[i2] = (jshort)(co[i2] - 2); cq[i2] = (jbyte)(cq[i2] + 2); }
        break;
      case 1:
        cp[i2] = (jshort)(cp[i2] + 2);
        if (cp[i2] > h2) { cp[i2] = (jshort)(cp[i2] - 2); cq[i2] = (jbyte)(cq[i2] + 2); }
        break;
      case 2:
        co[i2] = (jshort)(co[i2] - 2);
        if (co[i2] < 0) { co[i2] = (jshort)(co[i2] + 2); cq[i2] = (jbyte)(cq[i2] - 2); }
        break;
      case 3:
        cp[i2] = (jshort)(cp[i2] - 2);
        if (cp[i2] < 0) { cp[i2] = (jshort)(cp[i2] + 2); cq[i2] = (jbyte)(cq[i2] - 2); }
        break;
      default: break;
    }
  }
}

void Game::a_() {
  short px = player->x();
  int py = player->y() + L;
  int half = au->getWidth() >> 1;
  for (int i2 = 0; i2 < 8; i2++) {
    if (bj[i2] == -1) return;
    short hx = bj[i2];
    short hy = bk[i2];
    if (a_(px - 11, py + 7, 18, 37, hx - half + 10, hy - half, (half << 1) - 20, half << 1)) {
      player->velX = 0;
      player->velY = (jshort)(-Player::currentWalkSpeed);
      player->setAnimState(3);
      player->zipGrabRetryDelay = 10;
      player->jumpPhase = 0;
      return;
    }
  }
}

void Game::q_(int idx) {
  Enemy* en = enemies[idx];
  if (en->kind == -1 || en->animState == 5) return;
  int weapon = 0;
  short ex = en->x();
  short ey = en->y();
  jbyte k = en->kind;
  jbyte hx = Enemy::HITBOX_X_OFFSETS[k], hy = Enemy::HITBOX_Y_OFFSETS[k];
  jbyte hw = Enemy::HITBOX_WIDTHS[k], hh = Enemy::HITBOX_HEIGHTS[k];
  if (!(ex - hx + hw + x >= 0 && ex - hx + x <= 176 && ey + hy + hh + y >= 0 && ey + hy + y <= 220)) return;
  for (int i2 = 9; i2 >= 0; i2--) {
    Projectile* pr = playerProjectiles[i2];
    jbyte pt = pr->type;
    if (pr->type >= 0 && pt != 30 &&
        (pr->isActive() || (pt >= 9 && pt <= 11) || (pt >= 15 && pt <= 17) || pt == 2 || (pt >= 21 && pt <= 29))) {
      int px = (pr->posX >> 8) - pr->halfWidth;
      int py = (pr->posY >> 8) - pr->halfHeight;
      if (a_(ex - hx, ey + hy, hw, hh, px, py, pr->halfWidth << 1, pr->halfHeight << 1)) {
        weapon = pr->weaponIndex();
        if ((k != 4 && k != 3) || weapon != 6) {
          if (pt >= 9 && pt <= 11) {
            int dist;
            if ((dist = abs_(player->x() - ex)) < tileWidth)
              en->health = (jbyte)(en->health - Projectile::DAMAGE_BY_TYPE[pt]);
            else if (dist >= tileWidth * 2 && pt <= 10)
              en->health = (jbyte)(en->health - (Projectile::DAMAGE_BY_TYPE[pt] >> 2));
            else
              en->health = (jbyte)(en->health - (Projectile::DAMAGE_BY_TYPE[pt] >> 1));
          } else {
            en->health = (jbyte)(en->health - Projectile::DAMAGE_BY_TYPE[pt]);
          }
          pr->detonate(false);
          e_(ex << 8, (ey << 8) + (K << 7), 30);
          dE++;
          if (en->health <= 0 && en->animState != 5) {
            jshort room = X;
            if (cV) room = 0;
            int word = (idx + room * 10) >> 5;
            bv[word] &= ~(1 << (idx + room * 10 - (word << 5)));
            if (weapon == 6 && k != 4) {
              du++;
              for (int b2 = 0; b2 < Enemy::BOLTS_DROPPED_BY_ANIM[en->animKind]; b2++)
                c_((short)(en->posX >> 8), en->y(), 0);
              en->kind = 4;
              en->animKind = 24;
              en->health = Enemy::HP_BY_ANIM[24];
              if (player->weaponLevel[6] < 2) player->N[6]++;
              if (player->N[6] >= 20) {
                player->weaponLevel[6]++;
                player->N[6] = 0;
                cC = false;
                r_(73);
                do_++;
                if (aa && ++ab > 10) ab = 10;
                dK[X]++;
                return;
              }
            } else {
              do_++;
              if (aa && ++ab > 10) ab = 10;
              dK[X]++;
              en->setAnimState(5);
              en->animRestart = 1;
              if (k != 4) {
                for (int b2 = 0; b2 < Enemy::BOLTS_DROPPED_BY_ANIM[en->animKind]; b2++)
                  c_((short)(en->posX >> 8), en->y(), 0);
                if (player->weaponLevel[weapon] < 2) player->N[weapon]++;
                if (player->N[weapon] >= 20) {
                  player->weaponLevel[weapon]++;
                  player->N[weapon] = 0;
                  cC = false;
                  r_(73);
                  return;
                }
              }
            }
          } else if (en->animState != 2) {
            en->setAnimState(4);
            en->animHold = 0;
          }
          return;
        }
      }
    }
  }
}

void Game::R_() {
  short px = player->x();
  int py = player->y() + L;
  for (int i2 = 9; i2 >= 0; i2--) {
    if (player->animState == 10) return;
    Projectile* pr = enemyProjectiles[i2];
    jbyte type = pr->type;
    if (type != -1 && type != 30 && pr->isActive()) {
      int hx = (pr->posX >> 8) - pr->halfWidth;
      int hy = (pr->posY >> 8) - pr->halfHeight;
      if (a_(px - 11, py + 7, 18, 37, hx, hy, pr->halfWidth << 1, pr->halfHeight << 1)) {
        jbyte src = pr->sourceAnim;
        if (type != 3 && type != 6 && type != 18) {
          if (!f) player->health = (jbyte)(player->health - Enemy::DAMAGE_BY_ANIM[src]);
          ab = 1;
          dI = true;
          e = true;
          player->setAnimState(9);
          player->animHold = 0;
          if (player->jumpPhase == 2) player->jumpPhase = 1;
          e_(px << 8, (py << 8) + (K << 7), 30);
        }
        if (type == 32) player->invulnTimer = 10;
        pr->detonate(true);
      }
    }
  }
}

void Game::T_() {
  if (b == 0) {
    int charge = cx & 63;
    jbyte level = player->weaponLevel[player->currentWeapon];
    if (charge > 0) {
      cx++;
      if (charge >= Player::CHARGE_TICKS[level][player->currentWeapon]) {
        if ((cx & 128) != 0) {
          cx = 129;
          player->fire();
        } else {
          cx = 0;
        }
      }
    }
  }
  if (b == 11) {
    if ((di & 524288) > 0) di = 4;
  } else if (b == 16 && h == 4) {
    ee++;
    if (ee > 20) {
      if (g && j == 0) {
        b = 0;
        e = true;
        updateCamera();
      }
      if (g) j--;
      else j++;
      if (j > 4) j = 4;
      ee = 0;
    }
    d = true;
  }
  if (b != 0) {
    if (dj++ > 12) dj = 0;
    d = true;
  }
}

// Game.d(byte): the level-0 intro cutscene, one step per tick, driven by
// `k`. (The decompiled tick() shows `this.abs(this.k)` at this call site --
// a decompiler artifact; d(byte) has no other caller.)
void Game::d_(jbyte step) {
  Player* pl = player;
  switch (step) {
    case 0:
      k++;
      return;
    case 1:
      cC = true;
      cI = cz[20];
      r_(cA[20]);
      k++;
      return;
    case 2:
      if (!cD) {
        a_(3, 2, (jbyte)0, -1);
        enemies[9]->facingRight = false;
        k++;
      }
      return;
    case 3: {
      Enemy* clank = enemies[9];
      clank->updateAnimation();
      d = true;
      int floorY = clank->groundYAhead(false);
      clank->velY = (jshort)(clank->velY + 256);
      clank->posInRow = clank->posInRow + clank->velY;
      clank->wrapRow();
      if (clank->y() + 2 * tileHeight >= floorY && clank->velY > 0) {
        clank->setAnimState(0);
        clank->animRestart = 0;
        b = 0;
        cC = true;
        cI = cz[21];
        r_(cA[21]);
        e = true;
        k++;
        updateCamera();
      }
      return;
    }
    case 4:
      pl->updateAnimation();
      d = true;
      pl->posX += 1536;
      if ((pl->posX >> 8) > 18 * tileWidth - (tileWidth >> 3)) {
        pl->setAnimState(0);
        pl->velX = 1280;
        pl->posInRow = 0;
        pl->velY = -3584;
        k++;
      }
      return;
    case 5:
      cC = true;
      cI = cz[15];
      r_(cA[15]);
      k++;
      return;
    case 6:
      if (cD) {
        pl->setAnimState(0);
        return;
      }
      pl->updateAnimation();
      pl->setAnimState(2);
      d = true;
      pl->velY = (jshort)(pl->velY + 384);
      pl->posInRow = pl->posInRow + pl->velY;
      pl->posX = pl->posX + pl->velX;
      pl->wrapRow();
      if (pl->y() >= 5 * tileHeight + (tileHeight >> 1) && pl->velY > 0) {
        pl->setAnimState(6);
        int steps = (abs_(be[0] - bc[0]) << 8) / 2304;
        pl->zipVelX = (bg[0] << 8) / steps;
        pl->zipVelY = bh[0] / steps;
        pl->zipEndY = bf[0];
        k++;
      }
      return;
    case 7:
      pl->zipRide();
      if (pl->posX >= ((tileWidth * 25) << 8)) {
        k++;
        pl->velY = 0;
        pl->velX = 0;
        pl->setAnimState(3);
      }
      return;
    case 8: {
      pl->updateAnimation();
      d = true;
      short floorY = pl->groundYAhead(false);
      pl->velY = (jshort)(pl->velY + 256);
      if (pl->y() + pl->velY > (floorY << 8)) pl->velY = (jshort)((floorY << 8) - pl->y());
      pl->posInRow = pl->posInRow + pl->velY;
      pl->wrapRow();
      if (pl->y() >= floorY && pl->velY > 0) {
        pl->setAnimState(4);
        pl->posInRow = 0;
        pl->velY = 0;
        pl->velX = 0;
        k++;
      }
      return;
    }
    case 9:
      pl->setAnimState(0);
      e = true;
      a_(3, 2, (jbyte)0, -1);
      ct = currentTimeMillis();
      b = 0;
      k++;
      updateCamera();
      return;
    default:
      return;
  }
}

// ---- the tick -----------------------------------------------------------------

void Game::tick() {
  if (player->velY == 0) ea = 0;

  if (dT != -1) {
    f_(dT);
    dT = -1;
    d = true;
  } else if (dU != -1) {
    // Loading a saved slot (midlet.readSaveSlot) is not ported yet.
    dU = -1;
    d = true;
  }

  if (dV) {
    sleep(100);
    return;
  }
  if (U > 0 && --U > 0) sleep(10);

  cs = currentTimeMillis();
  if (cs - ct > 33) {
    dY = (int)(cs - ct);
    T_();
    if (b == 17) {
      d_(k);
      if (!cD) updateCamera();
    }
    // b == 24 / b == 16 (boss-fight scripting: Game.c(byte)/b(byte)) not ported yet.

    if (b == 2) {
      q++;
      if (q == 25) q = 80;
      if (q > 84) {
        q = 80;
        if (p < 15) p++;
        else p--;
      }
    } else {
      q = 0;
    }
    if (b == 11 || b == 12) player->updateAnimation();
    if (b == 3 || b == 1) dw = 0;

    if (b == 0 && !cD) {
      dw = dw + (cs - ct);
      cr = (int)(cr + (cs - ct));
      H_();
      J_();
      l_();
      c_();
      K_();
      b_();
      player->tick();
      player->updateAnimation();
      if (player->invulnTimer <= 0) R_();
      else player->invulnTimer--;
      updateCamera();
      I_();
      a_();

      for (int i2 = 9; i2 >= 0; i2--) {
        playerProjectiles[i2]->update(false);
        if (playerProjectiles[i2]->type != -1) m_(i2);
      }
      for (int i2 = 9; i2 >= 0; i2--) {
        enemyProjectiles[i2]->update(true);
        if (enemyProjectiles[i2]->type != -1) l_(i2);
      }

      db = 0;
      for (int i2 = enemyPoolSize - 1; i2 >= 0; i2--) {
        Enemy* en = enemies[i2];
        if (en->kind == -1) continue;
        if (Z == 12 && (en->kind == 1 || en->kind == 4)) {
          if (en->stateTimer > 140) {
            en->kind = -1;
            continue;
          }
          en->stateTimer++;
          if (en->stateTimer == 1) {
            en->setAnimState(6);
            en->animRestart = 1;
          } else if (en->stateTimer == 137) {
            en->setAnimState(7);
            en->animRestart = 1;
          }
        }
        if (en->animState != 5 && player->animState != 10) q_(i2);
        en->tick();
        en->updateAnimation();
        if (Z == 11 && en->kind != 4) db++;
      }

      if (Z == 0 && enemies[9]->kind == -1 && player->y() >= player->groundYAhead(false)) {
        b = 17;
        e = false;
        bC[0] = 0;
        player->setAnimState(1);
        player->animRestart = 0;
        player->facingRight = true;
      }
      // Z == 12 (boss room) and cV (challenge rooms) branches: not ported yet.
    }
    ct = cs;
  } else if (d) {
    d = false;
  }

  if (dZ) {
    dZ = false;
    midlet->returnToIntro();
  }
}
