// game.cpp -- implementation of Game (see game.h): the real constructor,
// boot chain (runBootStep, all 42 steps, transcribed from
// src_a1/Game.java), t()'s mapData.txt parser, and sleep(). Everything
// else is still a no-op stub (see game.h's header note).
#include "game.h"
#include "canvasshell.h"
#include "midlet.h"
#include "intromanager.h"
#include "font.h"
#include <algorithm>
#include <cstddef>
#include <cstdlib>
#include <cctype>
#include <vector>

const String Game::aR[4] = {"/bg_agclnk.png", "/bg_arena.png", "/bg_os.png", "/bg_menu.png"};

jbyte Game::tileWidth = 42;
jbyte Game::tileHeight = 28;
jbyte Game::hudHeight = 20;
jbyte Game::J = 44;
jbyte Game::K = 44;
jbyte Game::L = -16;
jint Game::x = 0;
jint Game::y = 0;
jbyte Game::enemyPoolSize = 10;
jbyte Game::bb = Game::enemyPoolSize;

Image* Game::am = nullptr; Image* Game::an = nullptr; Image* Game::ao = nullptr; Image* Game::ap = nullptr;
Image* Game::aq = nullptr; Image* Game::ar = nullptr; Image* Game::as = nullptr; Image* Game::at = nullptr;
Image* Game::au = nullptr; Image* Game::av = nullptr; Image* Game::aw = nullptr; Image* Game::ax = nullptr;
Image* Game::ay = nullptr; Image* Game::az = nullptr; Image* Game::aA = nullptr; Image* Game::aB = nullptr;
Image* Game::aC = nullptr; Image* Game::aD = nullptr; Image* Game::aE = nullptr; Image* Game::aF = nullptr;
Image** Game::aG = nullptr; Image* Game::aH = nullptr; Image* Game::aI = nullptr; Image* Game::aJ = nullptr;
Image* Game::aK = nullptr; Image* Game::aL = nullptr; Image* Game::aM = nullptr; Image* Game::aN = nullptr;
Image* Game::aO = nullptr; Image* Game::aP = nullptr;
jbyte Game::dk = 0;
jint Game::dA = 0;
jshort Game::v = 0;
jshort Game::w = 0;
jint* Game::dJ = nullptr;
jint* Game::dK = nullptr;
bool* Game::dL = nullptr;

Game::Game(ratchetandclank* midlet_in) : midlet(midlet_in) {}

void Game::sleep(int ms) { sleepMs(ms); }

String Game::readResourceText(const String& path) {
  ByteArray raw = loadResource(path);
  String text;
  text.resize(raw.size());
  for (size_t i = 0; i < raw.size(); i++) text[i] = (char)std::to_integer<uint8_t>(raw[i]);
  return text;
}

// Parses mapData.txt (world-map data, tool-verified in phase 2, see
// ASSET_FORMATS.md) into dd[62][3] and de[43][2].
void Game::t() {
  String text = readResourceText("/mapData.txt");
  size_t pos = 0;
  for (int i = 0; i < 62; i++) {
    for (int j = 0; j < 3; j++) {
      size_t comma = text.find(',', pos);
      if (comma == String::npos) comma = text.size();
      dd[i][j] = (jshort)atoi(trim(text.substr(pos, comma - pos)).c_str());
      pos = comma + 1;
    }
    size_t nl = text.find('\n', pos);
    pos = (nl == String::npos) ? text.size() : nl + 1;
  }
  for (int i = 0; i < 43; i++) {
    for (int j = 0; j < 2; j++) {
      size_t comma = text.find(',', pos);
      if (comma == String::npos) comma = text.size();
      de[i][j] = (jbyte)atoi(trim(text.substr(pos, comma - pos)).c_str());
      pos = comma + 1;
    }
    size_t nl = text.find('\n', pos);
    pos = (nl == String::npos) ? text.size() : nl + 1;
  }
}

void Game::runBootStep(int step) {
  switch (step) {
    case 4:
      ratchetandclank::smallFont = new Font("/f2.v", 10, 1);
      ratchetandclank::smallFontAlias = ratchetandclank::smallFont;
      ratchetandclank::largeFont = new Font("/f3.v", 13, 1);
      ratchetandclank::currentFont = ratchetandclank::smallFontAlias;
      midlet->introManager->d_(2);
      return;
    case 5:
      random = new JRandom();
      dk = 0;
      aG = new Image*[5];
      aG[0] = Image::createImage("/en_disdro.png");
      midlet->introManager->d_(2);
      return;
    case 6: aG[1] = Image::createImage("/en_micbot.png"); midlet->introManager->d_(2); return;
    case 7: aG[2] = Image::createImage("/en_patbot.png"); midlet->introManager->d_(2); return;
    case 8: aG[3] = Image::createImage("/en_turret.png"); midlet->introManager->d_(2); return;
    case 9: aG[4] = Image::createImage("/en_boar.png"); midlet->introManager->d_(2); return;
    case 10:
      am = Image::createImage("/arrows.png");
      at = Image::createImage("/prtrts.png");
      midlet->introManager->d_(2);
      return;
    case 11: an = Image::createImage("/hud.png"); midlet->introManager->d_(2); return;
    case 12: aq = Image::createImage("/wpnhud.png"); midlet->introManager->d_(2); return;
    case 13: ap = Image::createImage("/menuhl.png"); midlet->introManager->d_(2); return;
    case 14: ao = Image::createImage("/diabox.png"); midlet->introManager->d_(2); return;
    case 15: aA = Image::createImage("/bltctr.png"); midlet->introManager->d_(2); return;
    case 16: aE = Image::createImage("/icons.png"); midlet->introManager->d_(2); return;
    case 17: aC = Image::createImage("/doors.png"); midlet->introManager->d_(2); return;
    case 18: aM = Image::createImage("/explod.png"); midlet->introManager->d_(2); return;
    case 19: aJ = Image::createImage("/box.png"); midlet->introManager->d_(2); return;
    case 20: aI = Image::createImage("/weapon.png"); midlet->introManager->d_(2); return;
    case 21: aL = Image::createImage("/bolts.png"); midlet->introManager->d_(2); return;
    case 22: aD = Image::createImage("/pltfrm.png"); midlet->introManager->d_(2); return;
    case 23: aK = Image::createImage("/strtpt.png"); midlet->introManager->d_(2); return;
    case 24: aN = Image::createImage("/cnnbse.png"); midlet->introManager->d_(2); return;
    case 25: aO = Image::createImage("/cnnprj.png"); midlet->introManager->d_(2); return;
    case 26: aP = Image::createImage("/cnnbrl.png"); midlet->introManager->d_(2); return;
    case 27: av = Image::createImage("/maxmil.png"); midlet->introManager->d_(2); return;
    case 28: ar = Image::createImage("/mpicns.png"); midlet->introManager->d_(2); return;
    case 29: ay = Image::createImage("/flmbot.png"); midlet->introManager->d_(2); return;
    case 30: midlet->introManager->d_(2); return;
    case 31: aB = Image::createImage("/wpnmnu.png"); midlet->introManager->d_(2); return;
    case 32: au = Image::createImage("/spike.png"); midlet->introManager->d_(2); return;
    case 33: aw = Image::createImage("/payola.png"); midlet->introManager->d_(2); return;
    case 34: ax = Image::createImage("/bncbot.png"); midlet->introManager->d_(2); return;
    case 35: as = Image::createImage("/menuhd.png"); midlet->introManager->d_(2); return;
    case 36: aH = Image::createImage("/ratcht.png"); midlet->introManager->d_(2); return;
    case 37: aF = Image::createImage(aR[3]); midlet->introManager->d_(2); return;
    case 38: {
      I = 0;
      while (I < 220 - tileHeight * 2) I = I + tileHeight;
      I += 12;
      r = false;
      dA = 0;
      v = (jshort)(28 * -tileWidth + 176);
      w = (jshort)(18 * -tileHeight + 220);
      if (10 > hudHeight) hudHeight = 16;
      levelMap = new LevelMap(this);
      midlet->introManager->d_(7);
      return;
    }
    case 39:
      player = new Player(this);
      player->loadAssets();
      midlet->introManager->d_(5);
      return;
    case 40: {
      enemies = new Enemy*[enemyPoolSize];
      for (int i = enemyPoolSize - 1; i >= 0; i--) enemies[i] = new Enemy(this);
      enemies[0]->loadAssets();
      midlet->introManager->d_(5);
      playerProjectiles = new Projectile*[10];
      for (int i = 9; i >= 0; i--) playerProjectiles[i] = new Projectile(this);
      enemyProjectiles = new Projectile*[10];
      for (int i = 9; i >= 0; i--) enemyProjectiles[i] = new Projectile(this);
      midlet->introManager->d_(2);
      return;
    }
    case 41:
      bH = new jbyte[4](); bI = new jbyte[4](); bJ = new jint[5]();
      bF = new jbyte[3](); bG = new jbyte[3](); bC = new jbyte[3]();
      bD = new jbyte[3](); bE = new jbyte[3]();
      bc = new jshort[4](); bd = new jshort[4](); be = new jshort[4]();
      bf = new jshort[4](); bg = new jshort[4]();
      bh = new jint[4](); bi = new jint[4]();
      bj = new jshort[8](); bk = new jshort[8]();
      bl = new jshort[50](); bm = new jshort[50](); bn = new jbyte[50]();
      bo = new jshort[50](); bp = new jshort[50]();
      bu = new jint[10]();
      bq = new jshort[50](); bs = new bool[50](); br = new jshort[50]();
      bt = new bool[6]();
      bv = new jint[2]();
      cg = new jint[12](); ch = new jint[12](); ck = new jbyte[12]();
      ci = new jshort[12](); cj = new jshort[12]();
      cm = new jshort[4](); cn = new jshort[4](); co = new jshort[4]();
      cp = new jshort[4](); cq = new jbyte[4]();
      dJ = new jint[12](); dK = new jint[12](); dL = new bool[12]();
      dc = new jint[13]();
      aZ = new jint[8]();
      midlet->introManager->d_(5);
      return;
    case 42:
      t();
      aQ = 3;
      midlet->introManager->d_(5);
      return;
    default:
      return;
  }
}

// --- menu-screen rendering helpers (see game.h's note on these) -----------

void Game::a_(Graphics* g, int x, int y, int w, int h) {
  g->setClip(x, y, w, h);
}

bool Game::h_(int bit) {
  if (bit >= 0 && bit <= 19) return (cJ & (1 << bit)) != 0;
  return (bit < 20 || bit > 39) ? false : (cK & (1 << (bit - 20))) != 0;
}

bool Game::f_(int level, int room) {
  int idx, set;
  if (level > 0 && level <= 5) { idx = (level - 1) * 6 + room; set = bT; }
  else if (level >= 6 && level <= 10) { idx = (level - 6) * 6 + room; set = bU; }
  else return false;
  return (set & (1 << idx)) != 0;
}

void Game::d_(int level, int room) {
  int idx, set;
  if (level > 0 && level <= 5) { idx = (level - 1) * 6 + room; set = bx; }
  else if (level >= 6 && level <= 10) { idx = (level - 5) * 6 + room; set = by; }
  else { bw = (jbyte)(bw | 1); return; }
  if ((set & (1 << idx)) == 0) bw = (jbyte)(bw & 254);
  else bw = (jbyte)(bw | 1);
}

void Game::x_() {
  if (aQ != LevelMap::startSubGrid) {
    aQ = LevelMap::startSubGrid;
    aF = Image::createImage(aR[aQ]);
  }
}

void Game::b_(Graphics* g, int x, int y, int w, int h) {
  if (x < 0) { w -= -x; x = 0; }
  if (y < hudHeight) { h -= hudHeight - y; y = hudHeight; }
  if (x + w > 176) w = 176 - x;
  if (y + h > 220) h = 220 - y;
  g->setClip(x, y, w, h);
}

void Game::p_(int idx) {
  if (idx == bb) {
    aS = player->kind;
    aT = player->animState;
    aU = player->animFrame;
    aW = player->facingRight ? 0 : 5;
  } else if (idx >= 0) {
    Enemy* e = enemies[idx];
    aS = e->kind;
    aT = e->animState;
    aU = e->animFrame;
    if (e->ap != 0) {
      if (e->ap == 1) aW = e->facingRight ? 4 : 2;
      else if (e->ap == 2) aW = e->facingRight ? 6 : 3;
      else if (e->ap == 3) aW = e->facingRight ? 1 : 7;
    } else {
      aW = e->facingRight ? 0 : 5;
    }
  }
  if (aS >= 0) {
    if (idx == bb) {
      aV = Player::ANIM_FRAMES[aS][aT][aU];
      return;
    }
    aV = Enemy::ANIM_FRAMES[aS][aT][aU];
    M = (jbyte)(aG[aS]->getHeight() / K);
  }
}

void Game::c_(Graphics* g, int idx, int x, int y, int yOffset) {
  p_(idx);
  int flip = enemies[idx]->wallCrawlFlipped ? (K << 1) : 0;
  Graphics* dg = CanvasShell::directGraphics;
  Image* im = aG[aS];
  switch (aW) {
    case 1: dg->drawImageManip(im, x - aV * K - flip, y - yOffset, 20, 90); break;
    case 2: dg->drawImageManip(im, x, y - (M - 1 - aV) * K - yOffset + flip, 20, 180); break;
    case 3: dg->drawImageManip(im, x - (M - 1 - aV) * K + flip, y - yOffset, 20, 270); break;
    case 4: dg->drawImageManip(im, x, y - (M - 1 - aV) * K - yOffset + flip, 20, 16384); break;
    case 5: dg->drawImageManip(im, x, y - aV * K - yOffset - flip, 20, 8192); break;
    case 6: dg->drawImageManip(im, x - (M - 1 - aV) * K + flip, y - yOffset, 20, 8282); break;
    case 7: dg->drawImageManip(im, x - aV * K - flip, y - yOffset, 20, 16474); break;
    default: break;
  }
}

void Game::a_(Graphics* g, int x, int y, int yOffset) {
  p_(bb);
  CanvasShell::directGraphics->drawImageManip(aH, x, y - yOffset, 20, 8192);
}

int Game::a_(Graphics* g, const String& text, int x, int y, int anchor, int width) {
  Font* font = ratchetandclank::currentFont;
  int var12 = font->lineHeight;
  int len = (int)text.size();
  int var7;
  if ((anchor & 1) > 0) { x = 0; var7 = 0; } else { var7 = x; }
  int var10 = 0, var11 = 0;
  bool var15 = false;
  do {
    int var8 = 0;
    bool var16 = false, var17 = false;
    int var18 = -1;
    int var9 = var11;
    while (true) {
      if (var11 >= len) { var16 = true; break; }
      char c = text[var11];
      if (c == '.' || c == '/') var18 = var11;
      if (c == ' ') {
        var8 += font->charWidth(c);
        var11++;
        if (var7 + var8 > width && !var15) { var17 = true; break; }
        var15 = true;
        break;
      }
      int cw = font->charWidth(c);
      if (var7 + var8 + cw > width && !var15) { var17 = true; break; }
      var8 += cw;
      var11++;
    }
    if (var7 + var8 <= width && !var16 && !var17) {
      var7 += var8;
    } else {
      if (var7 + var8 > width) {
        if (!var17) var11 = var9;
        else if (var18 > 0) var11 = var18;
      } else if (var17 && var18 > 0) {
        var11 = var18;
      }
      if (var11 - var10 > 0) {
        font->drawTextRange(g, text.c_str(), len, var10, var11 - var10,
                            (anchor & 1) > 0 ? 88 : x, y, anchor);
      }
      y += var12;
      var7 = x;
      var15 = false;
      var10 = var11;
    }
  } while (var11 < len);
  return y;
}

String Game::a_(const String& fmt, std::initializer_list<String> args) {
  std::vector<String> argv(args);
  String out;
  for (size_t i = 0; i < fmt.size(); i++) {
    if (fmt[i] == '%' && i + 1 < fmt.size() && std::isdigit((unsigned char)fmt[i + 1])) {
      int idx = fmt[i + 1] - '0';
      if (idx >= 0 && (size_t)idx < argv.size()) out += argv[(size_t)idx];
      i++;
    } else {
      out += fmt[i];
    }
  }
  return out;
}

int Game::k_() { return hudHeight + 20; }

void Game::writeSaveData(jbyte*) {}
void Game::readSaveData(const jbyte*) {}
