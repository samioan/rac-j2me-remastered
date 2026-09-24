// game_wheel.cpp -- in-game weapon wheel (b==22): Game.q(Graphics),
// a(Graphics,int,int)/b(Graphics,int,int) outline/fill helpers, v(int,int).
#include "game.h"
#include "midlet.h"
#include "font.h"
#include <string>

typedef ratchetandclank RC;

const jbyte Game::R[8] = {0, 2, 3, 5, 1, 7, 4, 6};
const jbyte Game::S[8] = {0, 4, 1, 2, 6, 3, 7, 5};
const jshort Game::dM[7][2] = {{24, 62}, {70, 42}, {117, 62}, {11, 112}, {128, 112}, {43, 154}, {97, 154}};
static const jbyte dN[8] = {0, 0, 10, 26, 36, 36, 26, 10};
static const jbyte dO[8] = {26, 10, 0, 0, 10, 26, 36, 36};
static const jbyte dP[8] = {3, 3, 10, 26, 33, 33, 26, 10};
static const jbyte dQ[8] = {26, 10, 3, 3, 10, 26, 33, 33};
static const jbyte dR[8] = {2, 2, 11, 26, 35, 35, 26, 11};
static const jbyte dS[8] = {25, 11, 2, 2, 11, 25, 34, 34};

void Game::wheelOutline(Graphics* g, int ox, int oy) {
  g->setColor(1240814);
  for (int k2 = 0; k2 < 7; k2++) {
    g->drawLine(dN[k2] + ox, dO[k2] + oy, dN[k2 + 1] + ox, dO[k2 + 1] + oy);
    g->drawLine(dP[k2] + ox, dQ[k2] + oy, dP[k2 + 1] + ox, dQ[k2 + 1] + oy);
  }
  g->drawLine(dN[7] + ox, dO[7] + oy, dN[0] + ox, dO[0] + oy);
  g->drawLine(dP[7] + ox, dQ[7] + oy, dP[0] + ox, dQ[0] + oy);
}

void Game::wheelFill(Graphics* g, int ox, int oy) {
  int xs[8], ys[8];
  for (int k2 = 0; k2 < 8; k2++) {
    xs[k2] = dR[k2] + ox;
    ys[k2] = dS[k2] + oy;
  }
  g->fillPolygon(xs, ys, 8, -14330774);
}

void Game::drawWheel(Graphics* g) {
  int half = hudHeight >> 1;
  g->setClip(0, 0, 176, 220);
  g->setColor(0);
  RC::currentFont = RC::smallFont;
  int cy = 110 + half;
  if (Z == 11) cy -= half;
  g->drawImage(aB, 88, cy, 3);
  g->setColor(2446442);
  int top = cy - 116;
  for (int k2 = 1; k2 <= 7; k2++)
    if ((player->ownedWeapons & (1 << k2)) == 0) wheelFill(g, dM[k2 - 1][0], top + dM[k2 - 1][1]);
  if (player->currentWeapon > 0)
    wheelOutline(g, dM[player->currentWeapon - 1][0], top + dM[player->currentWeapon - 1][1]);
  int y0 = 110 - 3 * RC::currentFont->lineHeight / 2 + half;
  g->setColor(0xFFFFFF);
  int w = 2 * aB->getWidth() / 4;
  y0 = a_(g, RC::strings[48 + player->currentWeapon], 88, y0, 17, w);
  y0 = a_(g, RC::strings[310] + " " + std::to_string(player->weaponLevel[player->currentWeapon] + 1), 88, y0, 17, w);
  if (player->currentWeapon != 6)
    a_(g, RC::strings[232] + ": " + std::to_string(player->ammo[player->currentWeapon]), 88, y0, 17, w);
}

void Game::wheelKey(int key, int action) {
  int n = 0;
  if (key != 54 && key != 50 && action != -4 && action != -1) {
    if (key == 52 || key == 56 || action == -3 || action == -2) {
      player->currentWeapon = S[player->currentWeapon];
      while ((player->ownedWeapons & (1 << player->currentWeapon)) == 0 && n < 8) {
        player->currentWeapon = S[player->currentWeapon];
        n++;
      }
    } else if (action == -5 || key == 53) {
      ec = cu = 0;
      ct = currentTimeMillis();
      b = 0;
      e = true;
      updateCamera();
    }
  } else {
    player->currentWeapon = R[player->currentWeapon];
    while ((player->ownedWeapons & (1 << player->currentWeapon)) == 0 && n < 8) {
      player->currentWeapon = R[player->currentWeapon];
      n++;
    }
  }
}
