// game_flow.cpp -- level flow between levels: results screens (b==18),
// world-map level select (b==3), level-end menu (b==21), info page (b==10),
// and level loading. Transcribed from Game.java a(byte)/a(Graphics,byte)/
// j(Graphics)/k(Graphics)/t(Graphics)/m,n,o,x(int,int)/a(int,short)/W().
#include "game.h"
#include "midlet.h"
#include "intromanager.h"
#include "font.h"
#include <string>

typedef ratchetandclank RC;

const jbyte Game::N[20] = {6, 0, 0, 1, 2, 4, 5, 3, 13, 13, 9, 10, 9, 16, 12, 11, 8, 11, 19, 18};
const jbyte Game::O[20] = {2, 3, 4, 7, 5, 6, 0, 5, 16, 12, 11, 17, 14, 10, 15, 13, 13, 15, 4, 18};
const jbyte Game::P[20] = {19, 0, 18, 2, 12, 4, 5, 4, 7, 10, 3, 8, 11, 1, 15, 6, 6, 16, 9, 13};
const jbyte Game::Q[20] = {1, 13, 3, 10, 7, 7, 16, 8, 11, 18, 9, 12, 4, 19, 12, 14, 17, 14, 2, 0};
const jbyte Game::T[13] = {4, 3, 2, 3, 4, 4, 2, 3, 3, 2, 0, 0, 0};
const jlong Game::dx[17] = {160000LL, 120000LL, 160000LL, 130000LL, 160000LL, 130000LL, 100000LL, 140000LL,
                            220000LL, 180000LL, 180000LL, 110000LL, 140000LL, 140000LL, 300000LL, 150000LL, 30000LL};
const jbyte Game::cQ[20] = {1, 1, 2, 2, 3, 3, 3, 4, 4, 5, 6, 6, 7, 8, 9, 9, 10, 12, 11, 13};
const jshort Game::cR[20] = {0, 2, 0, 1, 0, 2, 4, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0};
const jbyte Game::df[16] = {2, 18, 4, 7, 5, 1, 13, 10, 9, 12, 6, 15, 11, 8, 16, 17};
const jbyte Game::dg[16] = {1, 1, 2, 1, 3, 8, 1, 2, 3, 3, 6, 2, 5, 2, 1, 2};
const jbyte Game::dh[16][8] = {
    {0}, {1}, {2, 3}, {4}, {5, 6, 7}, {8, 9, 10, 11, 12, 13, 14, 15}, {16}, {17, 18},
    {19, 20, 21}, {22, 23, 24}, {25, 26, 27, 28, 29, 30}, {31, 32}, {33, 34, 35, 36, 37}, {38, 39}, {40}, {41, 42}};

int Game::i_() { return aq->getWidth() - 20; }

int Game::i_(int level) {
  int n = 0;
  if (level > 0 && level <= 5) {
    int base = (level - 1) * 6;
    for (int k2 = 0; k2 < 6; k2++)
      if ((bT & (1 << (base + k2))) == 0) n++;
  } else if (level >= 6 && level <= 10) {
    int base = (level - 6) * 6;
    for (int k2 = 0; k2 < 6; k2++)
      if ((bU & (1 << (base + k2))) == 0) n++;
  }
  return n;
}

void Game::a_(jbyte page) {
  switch (page) {
    case 0:
      return;
    case 1:
      if (!dI && dD != 0 && dD == dE) dF = true;
      if (do_ == 0) dG = true;
      for (int k2 = 11; k2 >= 0; k2--)
        if (dJ[k2] > 0) dn = dn + dJ[k2];
      if (du == do_ && do_ > 0 && dn == 0) dH = true;
      dp = do_ * 100;
      dz = dz + dp;
      ds = dq * 1;
      dt = dr * 1000;
      dz = dz + ds;
      dz = dz + dt;
      if (dw < dx[dy]) dv = (int)((dx[dy] - dw) / 1000LL * 10LL);
      dz = dz + dv;
      return;
    case 2:
      if (dG) dz += 100000;
      if (dF) dz += 100000;
      if (dH) dz += 10000;
  }
}

void Game::drawResults(Graphics* g, jbyte page) {
  IntroManager* im = midlet->introManager;
  int tw = i_();
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  e_(g);
  im->a_(g, RC::strings[296]);
  g->setColor(0xFFFFFF);
  RC::currentFont = RC::smallFont;
  int y0 = 87;
  switch (page) {
    case 0:
      a_(g, RC::strings[dC], 18, 107, 17, tw);
      break;
    case 1:
      g->setClip(0, 0, 176, 220);
      y0 = a_(g, RC::strings[297] + " " + std::to_string(do_), 88, 85, 17, tw);
      y0 = a_(g, RC::strings[298] + " " + std::to_string(dq), 88, y0, 17, tw);
      y0 = a_(g, RC::strings[299] + " " + std::to_string(dr), 88, y0, 17, tw);
      y0 = a_(g, RC::strings[300] + " " + im->c_((int)dw), 88, y0, 17, tw);
      a_(g, RC::strings[301] + " " + im->c_((int)dx[dy]), 88, y0, 17, tw);
      break;
    case 2:
      if (dG) y0 = im->a_(g, RC::strings[302], 88, 87, 17, false);
      if (dF) y0 = im->a_(g, RC::strings[303], 88, y0, 17, false);
      if (dH) im->a_(g, RC::strings[304], 88, y0, 17, false);
  }
  if (page > 0) im->a_(g, RC::strings[305] + " " + std::to_string(dz), 88, 166, 17, true);
  im->a_(g, 9, -1, this);
}

void Game::drawWorldMap(Graphics* g) {
  IntroManager* im = midlet->introManager;
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  im->a_(g, RC::strings[12]);
  int scale = 176 < 220 ? 176 : 220;
  scale = scale * 1024 / 176;
  short maxY = -1000, minY = 1000;
  for (int k2 = 0; k2 <= 19; k2++) {
    if (minY > dd[k2][2]) minY = dd[k2][2];
    if (maxY < dd[k2][2]) maxY = dd[k2][2];
  }
  int offY = (205 - (maxY - minY) * scale / 1024) / 2 - 3 * minY * scale / 1024 / 2;
  g->drawImage(ao, (176 - ao->getWidth()) / 2, 220 - tileHeight - ao->getHeight() + 8, 0);
  im->a_(g, RC::strings[14 + cQ[cu] - 1], (176 - ao->getWidth()) / 2, 220 - tileHeight - ao->getHeight() + 12, 0, true);

  for (int n = 0; n < 16; n++) {
    if ((cL & (1 << df[n])) == 0) continue;
    for (int k2 = 0; k2 < dg[n]; k2++) {
      jbyte a1 = de[dh[n][k2]][0], a2 = de[dh[n][k2]][1];
      int x1 = dd[a1][1] * scale / 1024, y1 = dd[a1][2] * scale / 1024 + offY;
      int x2 = dd[a2][1] * scale / 1024, y2 = dd[a2][2] * scale / 1024 + offY;
      g->setColor((20 << 16) | (186 << 8) | 204);
      g->drawLine(x1, y1, x2, y2);
      g->setColor((36 << 16) | (86 << 8) | 100);
      if (dd[a1][1] == dd[a2][1]) {
        g->drawLine(x1 - 1, y1, x2 - 1, y2);
        g->drawLine(x1 + 1, y1, x2 + 1, y2);
      } else {
        g->drawLine(x1, y1 - 1, x2, y2 - 1);
        g->drawLine(x1, y1 + 1, x2, y2 + 1);
      }
    }
  }

  for (int n = 0; n <= 19; n++) {
    short id = dd[n][0];
    int px = dd[n][1] * scale / 1024, py = dd[n][2] * scale / 1024 + offY;
    if ((id >= 0 && id <= 16) || ((id >= 17 && id <= 18) || (id == 19 && cL != 1572865)) && (cL & (1 << id)) > 0) {
      int frame = 1;
      if (id >= 0 && id <= 16 && (cL & (1 << id)) > 0) frame = 0;
      if (id == 17) frame = 6;
      else if (id == 18) frame = 2;
      else if (id == 19) frame = 4;
      if (id == 17 && dj > 10) frame++;
      if ((di & (1 << id)) > 0 && dj > 5) {
        g->setColor(0xFF0000);
        g->setClip(0, 0, 176, 220);
        if (id != 19 && id != 17) g->fillArc(px + 4, py + 4, 11, 11, 0, 360);
        else g->fillArc(px - 1, py - 1, 21, 21, 0, 360);
      }
      g->setClip(px, py, 19, 19);
      g->drawImage(ar, px, py - frame * 19, 0);
      if (cu == id) {
        g->setColor(0xFFFFFF);
        g->setClip(0, 0, 176, 220);
        if (id != 19 && id != 17 && id != 18) {
          g->drawLine(px + 4, py + 4, px + 15, py + 4);
          g->drawLine(px + 4, py + 15, px + 15, py + 15);
          g->drawLine(px + 4, py + 15, px + 4, py + 4);
          g->drawLine(px + 15, py + 15, px + 15, py + 4);
        } else {
          g->drawLine(px, py, px + 8, py);
          g->drawLine(px, py, px, py + 8);
          g->drawLine(px + 19, py, px + 11, py);
          g->drawLine(px + 19, py, px + 19, py + 8);
          g->drawLine(px, py + 19, px + 8, py + 19);
          g->drawLine(px, py + 19, px, py + 11);
          g->drawLine(px + 19, py + 19, px + 11, py + 19);
          g->drawLine(px + 19, py + 19, px + 19, py + 11);
        }
      }
    }
  }

  g->setClip(0, 0, 176, 220);
  if (cQ[cu] < 11) {
    RC::currentFont = RC::smallFont;
    jbyte total = T[cQ[cu] - 1];
    g->drawImage(aA, im->b_() / 2, 120 + offY, 0);
    g->setColor(1370860);
    RC::currentFont->drawText(g, std::to_string(i_(cQ[cu])) + "/" + std::to_string(total), 7 + im->b_() / 2, 137 + offY, 0);
  }
  im->a_(g, 7, -1, this);
}

void Game::drawInfo(Graphics* g) {
  IntroManager* im = midlet->introManager;
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  e_(g);
  im->a_(g, RC::strings[43]);
  RC::currentFont = RC::smallFont;
  a_(g, RC::strings[285], 88, 110, 17, i_());
  im->a_(g, 9, -1, this);
}

void Game::drawLevelEnd(Graphics* g) {
  IntroManager* im = midlet->introManager;
  int tw = tileWidth + (tileWidth >> 1);
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  e_(g);
  im->a_(g, RC::strings[281]);
  RC::currentFont = RC::smallFont;
  dc[0] = 100;
  int y1 = dc[1] = im->a_(g, RC::strings[40], tw, 100, 0, cu == 0) + 5;
  im->a_(g, RC::strings[280], tw, y1, 0, cu == 1);
  im->a_(g, 7, -1, this);
  im->a_(g, tileWidth >> 1, dc[cu]);
}

void Game::W_() {
  cu = 0;
  for (int k2 = 0; k2 < 19; k2++) {
    if ((di & (1 << k2)) > 0) return;
    cu++;
  }
}

void Game::loadLevel(int level, jshort room) {
  m = true;
  bz = bw; bA = bx; bB = by;
  bV = bT; bW = bU;
  cM = cJ; cN = cK; cO = cL;
  dl = di;
  aY = aX;
  player->R = player->currentWeapon;
  da = player->ownedWeapons;
  cP = room;
  for (int k2 = 0; k2 < 8; k2++) {
    player->S[k2] = player->N[k2];
    player->U[k2] = player->weaponLevel[k2];
    player->T[k2] = player->ammo[k2];
  }
  player->swingPhase = -2;
  ab = 1;
  cS = false;
  cD = false;
  for (int k2 = 0; k2 < 10; k2++) bu[k2] = -1;
  for (int k2 = 0; k2 < 2; k2++) bv[k2] = -1;
  dC = 0;
  ct = currentTimeMillis();
  Z = (jbyte)level;
  levelMap->loadLevelFile(level);
  player->facingRight = true;
  X = room;
  Y = X;
  player->invulnTimer = 10;
  d = true;
  b_((int)X, 6541);
  levelMap->enterRoom(X, true);
  if (Z == 12) bossInit();
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
  ct = currentTimeMillis();
  e = true;
  cu = 0;
  x = af;
  y = ag;
  d_();
  cv = cw = 0;
  b = 0;
  updateCamera();
  m = false;
  if (level == 1) midlet->writeSaveSlot(ac);
}

void Game::resultsKey(int key, int action) {
  if (key == 53 || action == -5 || key == -6) {
    dB++;
    if (dB == 3 || (dB == 2 && !dG && !dF)) {
      ec = cu = 0;
      b = 21;
      dB = 0;
      return;
    }
    a_(dB);
  }
}

void Game::mapKey(int key, int action) {
  jbyte prev = (jbyte)cu;
  const jbyte* nav = nullptr;
  if (action == -1 || key == 50) nav = N;
  else if (action == -2 || key == 56) nav = O;
  else if (action == -4 || key == 54) nav = Q;
  else if (action == -3 || key == 52) nav = P;
  if (nav) {
    cu = nav[cu];
    while ((cL & (1 << cu)) == 0) cu = nav[cu];
    if (cu == 19 && cL == 1572865) cu = prev;
    return;
  }
  if (!(key == 53 || action == -5 || key == -6)) return;
  if (cu == 19) {
    if (cL != 1572865) {
      player->facingRight = true;
      player->setAnimState(0);
      player->animRestart = 0;
      player->row = 2;
      player->posX = 22528;
      player->velY = 0;
      player->velX = 0;
      player->invulnTimer = 0;
      player->posInRow = 0;
      b = 11;
      cU = player->currentWeapon;
      player->Q = Y_();
      cu = 0;
    }
    return;
  } else if (cu == 18) {
    bv[0] = -1;
    bv[1] = -1;
    X_();
    b = 1;
    for (int k2 = 0; k2 < 8; k2++) player->T[k2] = player->ammo[k2];
    return;
  }
  if (cu == 0 && h_(0)) o = 0;
  else if (cu == 1 && h_(4)) o = 5;
  else if (cu == 2 && h_(1)) o = 0;
  else if (cu == 3 && h_(5)) o = 5;
  else if (cu == 4 && h_(2)) o = 1;
  else if (cu == 5 && (bw & 2) != 0) o = 3;
  else if (cu == 6 && h_(3)) o = 3;
  else if (cu == 7 && (bw & 4) != 0) o = 2;
  else if (cu == 8 && h_(13)) o = 0;
  else if (cu == 9 && h_(8)) o = 0;
  else if (cu == 10 && h_(8)) o = 5;
  else if (cu == 11 && h_(12)) o = 0;
  else if (cu == 12 && h_(9)) o = 5;
  else if (cu == 13 && h_(7)) o = 5;
  else if (cu == 14 && h_(10)) o = 5;
  else if (cu == 15 && h_(11)) o = 4;
  else if (cu == 16 && h_(14)) o = 0;
  else if (cu == 17) o = 6;
  else o = -1;
  aQ = 3;
  loadLevel(cQ[cu], cR[cu]);
  cu = 0;
}

void Game::levelEndKey(int key, int action) {
  IntroManager* im = midlet->introManager;
  if (action == -1 || key == 50) {
    cu = im->a_((jbyte)cu, (jbyte)0, (jbyte)1);
  } else if (action != -2 && key != 56) {
    if (key == 53 || action == -5 || key == -6) {
      midlet->playSoundIfEnabled(3);
      if (cu == 0) {
        c = b;
        b = 10;
        dA = dA + dz;
        midlet->writeSaveSlot(ac);
        return;
      }
      if (cu == 1) {
        bw = bz; bx = bA; by = bB;
        bT = bV; bU = bW;
        cJ = cM; cK = cN; cL = cO;
        di = dl;
        aX = aY;
        player->currentWeapon = player->R;
        player->ownedWeapons = da;
        for (int k2 = 0; k2 < 8; k2++) {
          player->N[k2] = player->S[k2];
          player->weaponLevel[k2] = player->U[k2];
          player->ammo[k2] = player->T[k2];
        }
        if (Z == 1) bZ = true;
        aQ = 3;
        loadLevel(Z, cP);
      }
    }
  } else {
    cu = im->b_((jbyte)cu, (jbyte)1, (jbyte)0);
  }
}

void Game::infoKey(int key, int action) {
  if (key == 53 || action == -5 || key == -6) {
    ec = cu = 0;
    if (c == 21) {
      W_();
      b = 3;
    }
    // c == 15 (weapon store return) not ported yet.
    midlet->playSoundIfEnabled(3);
  }
}

void Game::writeSaveData(jbyte* buf) {
  buf[0] = (jbyte)(Z == 0 ? 2 : 1);
  midlet->writeInt(cL, buf, 1);
  midlet->writeInt(bT, buf, 5);
  midlet->writeInt(bU, buf, 9);
  midlet->writeInt(cJ, buf, 13);
  buf[17] = player->ownedWeapons;
  for (int k2 = 0; k2 < 8; k2++) {
    buf[18 + k2] = player->weaponLevel[k2];
    midlet->writeShort(player->N[k2], buf, 26 + k2 * 2);
    midlet->writeShort(player->ammo[k2], buf, 42 + k2 * 2);
  }
  midlet->writeInt(aX, buf, 58);
  buf[62] = bw;
  midlet->writeInt(bx, buf, 63);
  midlet->writeInt(by, buf, 67);
  midlet->writeInt(cr, buf, 195);
  buf[199] = (jbyte)(bZ ? 1 : 0);
  midlet->writeInt(di, buf, 200);
  midlet->writeInt(cK, buf, 204);
  midlet->writeInt(dA, buf, 208);
  buf[212] = (jbyte)(aa ? 1 : 0);
  buf[213] = ac;
}

void Game::readSaveData(const jbyte* buf) {
  if (buf[0] == 2) {
    f_(0);
    return;
  }
  cL = midlet->readInt(buf, 1);
  bT = midlet->readInt(buf, 5);
  bU = midlet->readInt(buf, 9);
  cJ = midlet->readInt(buf, 13);
  player->ownedWeapons = buf[17];
  for (int k2 = 0; k2 < 8; k2++) {
    player->weaponLevel[k2] = buf[18 + k2];
    player->N[k2] = midlet->readShort(buf, 26 + k2 * 2);
    player->ammo[k2] = midlet->readShort(buf, 42 + k2 * 2);
  }
  aX = midlet->readInt(buf, 58);
  bw = buf[62];
  bx = midlet->readInt(buf, 63);
  by = midlet->readInt(buf, 67);
  cr = midlet->readInt(buf, 195);
  bZ = buf[199] == 1;
  di = midlet->readInt(buf, 200);
  cK = midlet->readInt(buf, 204);
  dA = midlet->readInt(buf, 208);
  aa = buf[212] != 0;
  ac = buf[213];
  ec = cu = 0;
  W_();
  b = 3;
}
