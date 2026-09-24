// game_store.cpp -- Extras list (b==1), weapon store (b==11), purchase
// confirm (b==12), not-enough-bolts (b==13), challenge briefing (b==14),
// challenge reward/failure (b==15/19), and the challenge-room start/tick
// (Game.s(int)/A()). Transcribed from Game.java b(g,byte)/i/h/r/o/n/m(g),
// t/k/l/p/s/r(int,int), X(), Y().
#include "game.h"
#include "midlet.h"
#include "intromanager.h"
#include "font.h"
#include <string>

typedef ratchetandclank RC;

const jint Game::cl[12] = {200, 400, 400, 600, 600, 600, 800, 800, 800, 1200, 1200, 2000};

void Game::X_() {
  cu = 0;
  dk = 0;
  if ((player->ownedWeapons & 64) <= 0) {
    for (int k2 = 0; k2 < 11; k2++)
      if ((cL & (1 << (21 + k2))) != 0) cu++;
    dk = cu >= 8 ? 2 : (cu >= 4 ? 1 : 0);
  }
}

int Game::Y_() {
  int total = 0;
  int need[8];
  for (int k2 = 0; k2 < 8; k2++) {
    need[k2] = Player::AMMO_CAPACITY[k2 * 3 + player->weaponLevel[k2]] - player->ammo[k2];
    bool owned = (player->ownedWeapons & (1 << k2)) > 0;
    int mul = 0;
    switch (k2) {
      case 0: case 6: aZ[k2] = 0; continue;
      case 1: mul = 1; owned = true; break;
      case 2: mul = 1; break;
      case 3: case 4: mul = 3; break;
      case 5: mul = 2; break;
      case 7: mul = 5; break;
    }
    if (owned) {
      total += need[k2] * mul;
      aZ[k2] = need[k2] * mul;
    }
  }
  return total;
}

// Java IntroManager.a(g, stringIdx, number, x, y, anchor, selected)
int Game::numText(Graphics* g, int idx, int num, int x, int y, bool sel) {
  return midlet->introManager->a_(g, a_(RC::strings[idx], {std::to_string(num)}), x, y, 0, sel);
}

void Game::drawExtras(Graphics* g) {
  IntroManager* im = midlet->introManager;
  int tw = tileWidth + (tileWidth >> 1);
  int fw = aq->getWidth(), fh = aq->getHeight();
  int jx = (176 - fw) >> 1, top = 75;
  g->setClip(0, 0, 176, 220);
  g->setColor(0);
  g->fillRect(0, 0, 176, 220);
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  e_(g);
  im->a_(g, RC::strings[24]);
  int y = top + fh / 10;
  for (int k2 = 0; k2 < 12; k2++) {
    if ((cL & (1 << (20 + k2))) != 0) {
      y = im->a_(g, RC::strings[60 + k2], tw, y, 0, cu == k2);
      if (y >= top + fh - 2 * fh / 10) {
        if (k2 >= cu) break;
        e_(g);
        y = top + fh / 10;
      }
    }
  }
  if (cu != 11 && (cL & (1 << (cu + 21))) != 0) {
    int tx = jx + fw - 7, ty = top + fh - 20;
    g->fillTriangle(tx, ty, tx - 10, ty, tx - 5, ty + 10, -15222068);
  }
  if (cu != 0 && (cL & (1 << (cu + 19))) != 0) {
    int tx = jx + fw - 7, ty = top + 20;
    g->fillTriangle(tx, ty, tx - 10, ty, tx - 5, ty - 10, -15222068);
  }
  im->a_(g, 7, 8, this);
}

void Game::drawWeaponBuy(Graphics* g) {
  IntroManager* im = midlet->introManager;
  int tw = tileWidth + (tileWidth >> 1);
  int rowH = aE->getHeight() / 12;
  int colW = aE->getWidth();
  int trayX = 52;
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  im->a_(g, RC::strings[26]);
  RC::currentFont = RC::smallFont;
  e_(g);
  int y = 85 + rowH + 4;
  if (cu != 7) player->currentWeapon = (jbyte)(cu + 1);
  player->render(g, 0, 0, -20);
  g->setClip(0, 0, 176, 220);
  if ((cu != 2 || !h_(3) || !h_(8)) && (cu != 3 || !h_(8))) {
    if (cu == 7) {
      y = im->a_(g, RC::strings[58], tw, y, 0, false);
      y = im->a_(g, RC::strings[295], tw, y, 0, true);
      y = numText(g, 56, aX, tw, y, false);
      y = numText(g, 57, player->Q, tw, y, false);
      if (player->Q <= 0) im->a_(g, RC::strings[257], tw, y, 0, false);
    } else if ((player->ownedWeapons & (1 << (cu + 1))) > 0) {
      y = im->a_(g, RC::strings[58], tw, y, 0, false);
      y = im->a_(g, RC::strings[49 + cu], tw, y, 0, true);
      y = numText(g, 56, aX, tw, y, false);
      y = numText(g, 57, aZ[cu + 1], tw, y, false);
      int cap = Player::AMMO_CAPACITY[3 * (cu + 1) + player->weaponLevel[cu + 1]];
      if (player->ammo[cu + 1] < cap) {
        String s = a_(RC::strings[255], {std::to_string(player->ammo[cu + 1]), std::to_string(cap)});
        im->a_(g, s, tw, y, 0, false);
      } else {
        im->a_(g, RC::strings[257], tw, y, 0, false);
      }
    } else {
      y += 15;
      y = im->a_(g, RC::strings[49 + cu], tw, y, 0, true);
      y = numText(g, 56, aX, tw, y, false);
      numText(g, 57, Player::h[cu], tw, y, false);
    }
  } else {
    y += 15;
    RC::currentFont = RC::smallFontAlias;
    g->setColor(12632256);
    y = a_(g, RC::strings[49 + cu], 88, y, 17, i_());
    RC::currentFont = RC::smallFont;
    y = a_(g, a_(RC::strings[56], {std::to_string(aX)}), 88, y, 17, i_());
    a_(g, RC::strings[253], 88, y, 17, i_());
  }

  int page = cu + 1;
  if (page == 7) {
    page--;
    if ((player->ownedWeapons & 32) == 0) page--;
  }
  if (cu == 7) {
    page--;
    if ((player->ownedWeapons & 128) == 0) page--;
    if ((player->ownedWeapons & 32) == 0) page--;
  }
  im->b_(g, page, 5 + ((player->ownedWeapons & 32) > 0 ? 1 : 0) + ((player->ownedWeapons & 128) > 0 ? 1 : 0));
  im->a_(g, 254, 8, this);
  g->setClip(0, 85, 176, rowH);
  if (dj > 6) {
    g->fillTriangle(42, 86, 37, 85 + (rowH >> 1), 42, 85 + rowH - 1, -14894388);
    g->drawTriangle(43, 85, 36, 85 + (rowH >> 1), 43, 85 + rowH, -14581353);
  }
  int x = trayX;
  for (int k2 = 0; k2 < 4 + ((player->ownedWeapons & 32) > 0 ? 1 : 0); k2++) {
    g->drawImage(aE, x, 85 - k2 * rowH, 0);
    x += colW;
  }
  if ((player->ownedWeapons & 32) <= 0) x += colW;
  if ((player->ownedWeapons & 128) > 0) g->drawImage(aE, x, 85 - 6 * rowH, 0);
  x += colW;
  g->drawImage(aE, x, 85 - 11 * rowH, 0);
  if (dj > 6) {
    g->fillTriangle(x + colW + 9, 86, x + colW + 9, 85 + rowH, x + colW + 15, 85 + (rowH >> 1), -14894388);
    g->drawTriangle(x + colW + 9, 85, x + colW + 9, 85 + rowH, x + colW + 16, 85 + (rowH >> 1), -14581353);
  }
  g->setColor(0xFFFFFF);
  if (cu > 5) g->drawRect(52 + (cu - 1) * colW, 86, colW, rowH - 2);
  else g->drawRect(52 + cu * colW, 86, colW, rowH - 2);
}

void Game::drawBuyConfirm(Graphics* g) {
  IntroManager* im = midlet->introManager;
  int tw = tileWidth + (tileWidth >> 1);
  int y = 0;
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  e_(g);
  im->a_(g, RC::strings[47]);
  RC::currentFont = RC::smallFont;
  if (ec != 7) player->currentWeapon = (jbyte)(ec + 1);
  player->render(g, 0, 0, -20);
  g->setClip(0, 0, 176, 220);
  if (ec == 7) {
    y = im->a_(g, RC::strings[58], tw, 85, 0, false);
    y = im->a_(g, RC::strings[295], tw, y, 0, true);
    y = numText(g, 56, aX, tw, y, false);
    y = numText(g, 57, player->Q, tw, y, false);
  } else if ((player->ownedWeapons & (1 << (ec + 1))) != 0) {
    y = im->a_(g, RC::strings[58], tw, 85, 0, false);
    y = im->a_(g, RC::strings[49 + ec], tw, y, 0, true);
    y = numText(g, 56, aX, tw, y, false);
    y = numText(g, 57, aZ[ec + 1], tw, y, false);
  } else {
    y = 85 + RC::currentFont->lineHeight;
    y = im->a_(g, RC::strings[49 + ec], tw, y, 0, true);
    y = numText(g, 56, aX, tw, y, false);
    y = numText(g, 57, Player::h[ec], tw, y, false);
  }
  int y1 = dc[0] = y + RC::currentFont->lineHeight / 2;
  int y2 = dc[1] = im->a_(g, RC::strings[11], tw, y1, 0, cu == 0) + 3;
  im->a_(g, RC::strings[10], tw, y2, 0, cu == 1);
  RC::currentFont = RC::smallFontAlias;
  im->a_(g, tw, dc[cu]);
  im->a_(g, 7, 8, this);
}

void Game::drawNoFunds(Graphics* g) {
  IntroManager* im = midlet->introManager;
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  e_(g);
  im->a_(g, RC::strings[59]);
  RC::currentFont = RC::smallFont;
  im->a_(g, -1, 8, this);
}

void Game::drawChallengeIntro(Graphics* g) {
  IntroManager* im = midlet->introManager;
  int w = i_();
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  e_(g);
  im->a_(g, RC::strings[24]);
  int y = 87;
  RC::currentFont = RC::smallFont;
  if (cW) y = a_(g, RC::strings[228], (176 - w) / 2, 87, 0, w);
  if (cX) y = a_(g, RC::strings[230], (176 - w) / 2, y, 0, w);
  y = a_(g, RC::strings[229], (176 - w) / 2, y, 0, w);
  if (cZ) {
    cE = a_(RC::strings[231], {RC::strings[player->currentWeapon + 48]});
    y = a_(g, cE, (176 - w) / 2, y, 0, w);
  }
  cE = a_(RC::strings[249], {std::to_string(cl[X])});
  a_(g, cE, (176 - w) / 2, y, 0, w);
  im->a_(g, 9, 8, this);
}

void Game::drawChallengeEnd(Graphics* g) {
  IntroManager* im = midlet->introManager;
  int w = i_();
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  e_(g);
  im->a_(g, RC::strings[75]);
  RC::currentFont = RC::smallFont;
  int y;
  if (X < 11) y = a_(g, RC::strings[251], (176 - w) / 2, 87, 0, w);
  else y = a_(g, RC::strings[252], (176 - w) / 2, 87, 0, w);
  cE = a_(RC::strings[250], {std::to_string(cl[X])});
  a_(g, cE, (176 - w) / 2, y, 0, w);
  im->a_(g, 9, -1, this);
}

void Game::drawChallengeFail(Graphics* g) {
  IntroManager* im = midlet->introManager;
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  e_(g);
  im->a_(g, RC::strings[308]);
  RC::currentFont = RC::smallFont;
  a_(g, RC::strings[309], 88, 131, 33, i_());
  im->a_(g, 9, -1, this);
}

void Game::extrasKey(int key, int action) {
  if (action != -2 && key != 56) {
    if (action != -1 && key != 50) {
      if (key != 53 && action != -5 && key != -6) {
        if (key == -7) {
          ec = cu = 0;
          W_();
          b = 3;
          if ((player->ownedWeapons & (1 << player->currentWeapon)) == 0 || player->currentWeapon == 0)
            player->currentWeapon = 1;
          for (int k2 = 0; k2 < 8; k2++) player->ammo[k2] = player->T[k2];
          midlet->playSoundIfEnabled(3);
        }
      } else {
        da = player->ownedWeapons;
        cV = true;
        cW = cX = cZ = false;
        cY = 0;
        switch (cu) {
          case 0: case 1: case 3: case 8: o = 7; break;
          case 2: case 5:
            cZ = true; player->ownedWeapons = 2; player->currentWeapon = 1; o = 12; break;
          case 4:
            cZ = true; player->ownedWeapons = 1; player->currentWeapon = 0; o = 8; break;
          case 6: case 10: cW = true; o = 9; break;
          case 7: case 9: cX = true; o = 10; break;
          case 11:
            cZ = true; player->ownedWeapons = 64; player->currentWeapon = 6; o = 11; break;
        }
        X = (jshort)cu;
        ec = (jbyte)cu;
        b = 14;
      }
    } else if (cu != 0) {
      cu--;
      dk = cu >= 8 ? 2 : (cu >= 4 ? 1 : 0);
    }
  } else if ((cL & (1 << (cu + 21))) != 0 && cu != 11) {
    cu++;
    dk = cu >= 8 ? 2 : (cu >= 4 ? 1 : 0);
  }
}

void Game::weaponBuyKey(int key, int action) {
  if (key == -7) {
    midlet->playSoundIfEnabled(3);
    ec = cu = 0;
    W_();
    player->currentWeapon = cU;
    b = 3;
    return;
  }
  auto skipInvalid = [&](int dir) {
    while ((cu > 3 && (player->ownedWeapons & (1 << (cu + 1))) == 0) || cu == 5) {
      if (cu == 7) return false;
      cu += dir;
      if (cu > 7) cu = 0;
      if (cu < 0) cu = 7;
    }
    return true;
  };
  if (key != 52 && action != -3) {
    if (key == 54 || action == -4) {
      if (++cu > 7) cu = 0;
      skipInvalid(1);
    } else if (key == 53 || action == -5 || key == -6) {
      midlet->playSoundIfEnabled(3);
      if (cu != 2 || !h_(3)) {
        if (cu == 3 && h_(8)) return;
        if (cu == 7 && player->Q <= 0) return;
        if (cu != 7 && (player->ownedWeapons & (1 << (cu + 1))) == 0) {
          b = 12;
          ec = (jbyte)cu;
          cu = 0;
          return;
        }
        if (cu != 7 && player->ammo[cu + 1] >= Player::AMMO_CAPACITY[3 * (cu + 1) + player->weaponLevel[cu + 1]]) return;
        b = 12;
        ec = (jbyte)cu;
        cu = 0;
      }
    }
  } else {
    if (--cu < 0) cu = 7;
    skipInvalid(-1);
  }
}

void Game::buyConfirmKey(int key, int action) {
  IntroManager* im = midlet->introManager;
  if (action == -2 || key == 56) {
    cu = im->b_((jbyte)cu, (jbyte)1, (jbyte)0);
  } else if (action == -1 || key == 50) {
    cu = im->a_((jbyte)cu, (jbyte)0, (jbyte)1);
  } else if (key != 53 && action != -5 && key != -6) {
    if (key == -7) {
      midlet->playSoundIfEnabled(3);
      cu = ec;
      b = 11;
      player->Q = Y_();
    }
  } else {
    midlet->playSoundIfEnabled(3);
    if (cu == 1) {
      if (ec == 7) {
        if (aX < player->Q) { cu = 0; b = 13; return; }
        aX -= player->Q;
        for (int k2 = 1; k2 < 8; k2++)
          if ((player->ownedWeapons & (1 << k2)) > 0)
            player->ammo[k2] = Player::AMMO_CAPACITY[k2 * 3 + player->weaponLevel[k2]];
      } else if ((player->ownedWeapons & (1 << (ec + 1))) == 0) {
        if (aX < Player::h[ec]) { cu = 0; b = 13; return; }
        aX -= Player::h[ec];
        player->ownedWeapons = (jbyte)(player->ownedWeapons | (1 << (ec + 1)));
      } else {
        if (aX < aZ[ec + 1]) { cu = 0; b = 13; return; }
        aX -= aZ[ec + 1];
        int cap = Player::AMMO_CAPACITY[(ec + 1) * 3 + player->weaponLevel[ec + 1]];
        player->ammo[ec + 1] = (jshort)(player->ammo[ec + 1] + cap);
        if (player->ammo[ec + 1] > cap) player->ammo[ec + 1] = (jshort)cap;
      }
    }
    cu = ec;
    b = 11;
    player->Q = Y_();
  }
}

void Game::noFundsKey(int, int) {
  cu = 0;
  b = 11;
  player->Q = Y_();
  midlet->playSoundIfEnabled(3);
}

void Game::challengeIntroKey(int key, int action) {
  if (key != 53 && action != -5 && key != -6) {
    if (key == -7) {
      X_();
      b = 1;
      e = true;
      d = true;
      player->ownedWeapons = da;
      cV = false;
    }
  } else {
    startChallenge(ec);
    e = true;
    d = true;
  }
}

void Game::challengeEndKey(int key, int action) {
  if (key == 53 || action == -5 || key == -6) {
    bv[0] = -1;
    bv[1] = -1;
    y_();
    if (b == 19) {
      X_();
      b = 1;
    } else if (b == 15) {
      c = b;
      b = 10;
      midlet->writeSaveSlot(ac);
    }
    e = true;
    d = true;
  }
}

void Game::startChallenge(int room) {
  if (room < 0 || room > 11) return;
  cS = false;
  cD = false;
  cu = 0;
  Z = 11;
  aQ = 3;
  levelMap->loadLevelFile(11);
  b_((int)X, 7229);
  for (int k2 = 0; k2 < 10; k2++) bu[k2] = -1;
  for (int k2 = 0; k2 < 6; k2++) bt[k2] = false;
  for (int k2 = 0; k2 < 2; k2++) bv[k2] = -1;
  levelMap->enterRoom(room, true);
  aQ = 1;
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
  player->facingRight = true;
  d = true;
  x = af;
  y = ag;
  for (int k2 = 0; k2 < 8; k2++) player->ammo[k2] = Player::AMMO_CAPACITY[k2 * 3 + player->weaponLevel[k2]];
  b = 0;
  updateCamera();
}

void Game::A_() {
  if (cX && player->health != 20) {
    player->health = 0;
    return;
  }
  if (cW && cY++ > 80) {
    if (!f) player->health--;
    cY = 0;
    e = true;
  }
  if (cZ && player->ownedWeapons != 1 && player->ammo[player->currentWeapon] <= 0) {
    y_();
    b = 19;
  } else {
    for (int k2 = enemyPoolSize - 1; k2 >= 0; k2--)
      if (enemies[k2]->health > 0 && enemies[k2]->kind != 4 && enemies[k2]->kind != -1) return;
  }
  cW = false;
  cX = false;
  cY = 0;
  for (int k2 = 0; k2 < 10; k2++) bu[k2] = -1;
  for (int k2 = 0; k2 < 2; k2++) bv[k2] = -1;
  cu = 0;
  cV = false;
  player->ownedWeapons = da;
  if (b != 19) {
    y_();
    b = 15;
    aX = aX + cl[X];
    if (aX > 999999) aX = 999999;
    if (X < 11) {
      cL = cL | (1 << (21 + X));
      return;
    }
    player->ownedWeapons = (jbyte)(player->ownedWeapons | 64);
  }
}
