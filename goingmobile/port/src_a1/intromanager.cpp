// intromanager.cpp -- implementation of IntroManager (see intromanager.h),
// transcribed from src_a1/IntroManager.java's boot/splash slice.
#include "intromanager.h"
#include "midlet.h"
#include "game.h"
#include "canvasshell.h"
#include "font.h"
#include "screen.h"
#include <cstdio>

const jint IntroManager::CHEAT_CODE_KEYS[4] = {49, 51, 49, 51};
jint IntroManager::b = 0;
jbyte IntroManager::d = 0;
jbyte IntroManager::A = 0;
jbyte IntroManager::B = 0;
jint IntroManager::x = 0;
jint IntroManager::y = 0;

IntroManager::IntroManager(ratchetandclank* midlet) : midlet_(midlet) {
  c = true;
  midlet_->loadStrings();
  A = Game::tileWidth;
  B = Game::tileHeight;
  e = 0;
  D = 0;
  f = 3;
  g = new jint[8]();
  F = currentTimeMillis();
  G = 0;
  H = 0;
  // No MIDlet manifest API in the port (PORT_ROADMAP.md's "Decisions
  // carried through every milestone") -- j stays at its default (true);
  // the shipped a1.jar carries no Unlock-Code property anyway.
  splashImages_[0] = Image::createImage("/intro_publisher.png");
  splashImages_[1] = Image::createImage("/intro_handheld.png");
  splashImages_[2] = Image::createImage("/intro_ratchet.png");
}

void IntroManager::c_() {
  f = 3;
  D = 0;
  G = 0;
  midlet_->game->aQ = 3;
  Game::aF = nullptr;
  gc();
  Game::sleep(20);
  Game::aF = Image::createImage(Game::aR[3]);
  a_((jbyte)1);
}

// var1 (newGame) is unused in the original too -- see src_a1/IntroManager.java.
void IntroManager::a_(bool) {
  gc();
  Game::sleep(20);
  o = true;
  r = true;
}

void IntroManager::d_() {
  if (r) {
    p = true;
    midlet_->stopSoundHardOnLevelStart();
  } else {
    f_();
  }
}

void IntroManager::e_() {
  if (q) {
    q = false;
  } else {
    g_();
  }
  p = false;
}

void IntroManager::f_() {
  midlet_->stopSoundHardOnLevelStart();
  f = (jbyte)(f | 1);
  p = true;
  t = 0;
  Game::aF = nullptr;
  Game::aG = nullptr;
  Game::aH = nullptr;
  Game::aC = nullptr;
  Game::aM = nullptr;
  Game::aJ = nullptr;
  gc();
}

void IntroManager::g_() {
  if (p) {
    if (e > 0) s = 5;
    f = (jbyte)(f | 1);
    p = false;
    u = 4;
    if (Game::aG == nullptr) {
      Game::aG = new Image*[5];
      Game::aG[0] = Image::createImage("/en_disdro.png");
      Game::aG[1] = Image::createImage("/en_micbot.png");
      Game::aG[2] = Image::createImage("/en_patbot.png");
      Game::aG[3] = Image::createImage("/en_turret.png");
      Game::aG[4] = Image::createImage("/en_boar.png");
    }
    if (Game::aH == nullptr) Game::aH = Image::createImage("/ratcht.png");
    if (Game::aF == nullptr) {
      midlet_->game->aQ = 3;
      Game::aF = Image::createImage(Game::aR[3]);
    }
    if (Game::aC == nullptr) Game::aC = Image::createImage("/doors.png");
    if (Game::aM == nullptr) Game::aM = Image::createImage("/explod.png");
    if (Game::aJ == nullptr) Game::aJ = Image::createImage("/box.png");
    t = currentTimeMillis();
  }
}

void IntroManager::h_() {
  if (M <= 42) {
    if (midlet_->game == nullptr) {
      E = currentTimeMillis();
      midlet_->game = new Game(midlet_);
    }
    if (u > 0) {
      u = 0;
      M--;
    }
    midlet_->game->runBootStep((int)M);
    M++;
    if (M <= 42) return;
    f = 3;
  }

  if (p) {
    Game::sleep(100);
  } else if (u > 0 && --u > 0) {
    Game::sleep(10);
  } else {
    if (currentTimeMillis() - F > 33) {
      F = currentTimeMillis();
      if (s > 0 && --s == 0 && ratchetandclank::language != -1 && !midlet_->playMenuLoopSound(false)) {
        s = 2;
      }
      i_();
    } else if ((f & 1) > 0) {
      f = (jbyte)(f & -2);
    }
    if (o) o = false;
  }
}

void IntroManager::i_() {
  switch (e) {
    case 0:
      j_();
      return;
    case 1: {
      if (d <= 100) { d++; return; }
      Game* gm = midlet_->game;
      Player* p = gm->player;
      Enemy** en = gm->enemies;
      p->updateAnimation();
      for (int i2 = 0; i2 < 4; i2++) en[i2]->updateAnimation();
      f = (jbyte)(f | 1);
      p->posX += p->velX;
      for (int i2 = 0; i2 < 4; i2++) en[i2]->posX += en[i2]->velX;
      if ((p->posX >> 8) > 7 * A && p->facingRight) {
        p->facingRight = false;
        p->velX = (jshort)(p->velX * -1);
        en[0]->facingRight = false;
        en[0]->velX = (jshort)(en[0]->velX * -1);
        en[0]->posX = 12 * A << 8;
        for (int i2 = 1; i2 < 4; i2++) en[i2]->velX = -1536;
        return;
      }
      if ((en[0]->posX >> 8) < 0 && !en[0]->facingRight) {
        d = 0;
        p->facingRight = true;
        p->velX = (jshort)(p->velX * -1);
        p->posX = -30720;
        en[0]->velX = (jshort)(en[0]->velX * -1);
        en[0]->facingRight = true;
        en[0]->posX = -10752;
        for (int i2 = 1; i2 < 4; i2++) { en[i2]->posX = (8 + i2) * A << 8; en[i2]->velX = 0; }
        return;
      }
      return;
    }
    case 5: {
      Player* p = midlet_->game->player;
      f = (jbyte)(f | 1);
      p->updateAnimation();
      if (G == 3) {
        if (I > 70) {
          p->setAnimState(0);
          p->animRestart = 0;
          I = 0;
        } else if (I == 1 || I == 10 || I == 20) {
          p->setAnimState(8);
          p->animRestart = 1;
        }
        I++;
        return;
      }
      // Player::fire() and the Projectile update/render loop (weapons
      // 4..10's demo shots) are not transcribed yet -- only the counters
      // and pose reset run here.
      if (J < 3) {
        if (G > 3 && G < 11 && I > 10) {
          I = 0;
          J++;
          for (int i2 = 0; i2 < 8; i2++) if (p->ammo[i2] == 0) p->ammo[i2] = 30;
        }
      } else if (I > 50) {
        J = 0;
        I = 0;
      } else if (I == 1) {
        p->setAnimState(0);
        p->animRestart = 0;
      }
      I++;
      return;
    }
    default:
      return;
  }
}

void IntroManager::j_() {
  if (E != 0 && E + 2000 >= currentTimeMillis()) {
    if (splashImages_[D] == nullptr) {
      switch (D) {
        case 0:
          if (splashImages_[1] == nullptr) splashImages_[1] = Image::createImage("/intro_handheld.png");
          return;
        case 1:
          if (splashImages_[2] == nullptr) splashImages_[2] = Image::createImage("/intro_ratchet.png");
          return;
        default:
          return;
      }
    }
  } else {
    if (D == 0) {
      D = 1;
      f = (jbyte)(f | 1);
      E = currentTimeMillis();
      return;
    }
    if (D == 1) {
      D = 2;
      f = (jbyte)(f | 1);
      E = currentTimeMillis();
      return;
    }
    if (D == 2) {
      f = (jbyte)(f | 1);
      E = currentTimeMillis();
      splashImages_[2] = nullptr;
      splashImages_[1] = nullptr;
      splashImages_[0] = nullptr;
      gc();
      if (ratchetandclank::language >= 0) {
        a_((jbyte)1);
        return;
      }
      a_((jbyte)20);
      return;
    }
  }
}

void IntroManager::b_(Graphics* g_gfx) {
  if (D == 0) {
    f = (jbyte)(f | 1);
    if ((f & 1) != 0) {
      if (splashImages_[0] == nullptr) {
        splashImages_[0] = Image::createImage("/intro_publisher.png");
        Game::sleep(30);
      }
      g_gfx->setColor(0);
      g_gfx->fillRect(0, 0, 176, 220);
      g_gfx->drawImage(splashImages_[D], 88, (220 - splashImages_[D]->getHeight()) >> 1, 17);
      splashImages_[D] = nullptr;
      gc();
      Game::sleep(20);
    }
    g_gfx->setClip(0, 110, 176, 110);
    g_gfx->setColor(13369344);
    g_gfx->fillRect(39, 206, b * 100 / 95, 6);
    g_gfx->setColor(16777215);
    g_gfx->drawRect(38, 206, 102, 6);
  } else if (splashImages_[D] != nullptr) {
    int color = 0;
    if (D == 1) color = 16777215;
    g_gfx->setClip(0, 0, 176, 220);
    g_gfx->setColor(color);
    g_gfx->fillRect(0, 0, 176, 220);
    g_gfx->setClip(88 - (splashImages_[D]->getWidth() >> 1), 110 - (splashImages_[D]->getHeight() >> 1),
                    splashImages_[D]->getWidth(), splashImages_[D]->getHeight());
    g_gfx->drawImage(splashImages_[D], 88 - (splashImages_[D]->getWidth() >> 1),
                      110 - (splashImages_[D]->getHeight() >> 1), 20);
    splashImages_[0] = nullptr;
    splashImages_[D] = nullptr;
  }
}

// weight is unused in the original too -- see src_a1/IntroManager.java.
void IntroManager::d_(int) {
  b = (M - 4) * 100 / 39;
  gc();
  Game::sleep(10);
}

void IntroManager::a_(jbyte state) {
  f = 3;
  if (state == 1) {
    Game* gm = midlet_->game;
    gm->dX = -1;
    Player* p = gm->player;
    p->facingRight = true;
    d = 90;
    p->currentWeapon = 1;
    p->setAnimState(1);
    p->animRestart = 0;
    p->row = 6;
    p->posX = -30720;
    p->velY = 0;
    p->velX = 1536;
    p->invulnTimer = 0;
    p->posInRow = -2048;
    for (int i2 = 0; i2 < 4; i2++) {
      Enemy* en = gm->enemies[i2];
      en->kind = 1;
      en->animKind = 0;
      en->facingRight = (i2 == 0);
      en->wallCrawlFlipped = false;
      en->setAnimState(1);
      en->animRestart = 0;
      en->row = 6;
      en->posX = (i2 == 0) ? -10752 : (8 + i2) * A << 8;
      en->velY = 0;
      en->velX = (i2 == 0) ? 1536 : 0;
      en->health = 1;
      en->activeFlag = 1;
      en->ap = 0;
      en->posInRow = -2048;
    }
    s = 3;
  }
  if (state == 5) {
    Game* gm = midlet_->game;
    for (int i2 = 0; i2 < Game::enemyPoolSize; i2++) gm->enemies[i2]->kind = -1;
    Player* p = gm->player;
    p->facingRight = true;
    p->currentWeapon = 0;
    p->setAnimState(8);
    p->animRestart = 1;
    p->activeFlag = 1;
    p->posX = 22528;
    p->velY = 0;
    p->velX = 0;
    p->invulnTimer = 0;
    p->row = 2;
    p->posInRow = -1280;
    p->ownedWeapons = -1;
    I = 0;
  }
  if (state == 15) {
    n = currentTimeMillis();
    errUntil_ = 0;
    // Unlock-code name-entry Form/TextField -- not modeled (unreachable on
    // a fresh/normal playthrough: only reached from the main menu when
    // isGameWon() is already true, see src_a1/IntroManager.java's c(int,int)).
  }
  if (state == 17) {
    computeCodes_(m, 0);
    computeCodes_(m, 1);
    // a(this.m, 0); a(this.m, 1); -- cheat-hash helper, depends on the
    // Form-entered name above; skipped alongside it.
  }
  e = state;
  G = 0;
  if (state == 20) {
    G = (ratchetandclank::language == -1) ? 0 : (jbyte)ratchetandclank::language;
  }
  if (state == 4 || state == 6 || state == 11) {
    midlet_->refreshSaveSlotSummaries();
  }
}

void IntroManager::a_(Graphics* g_gfx) {
  if (u <= 0) {
    if (e == 0 || (Game::aG != nullptr && Game::aH != nullptr && Game::aF != nullptr)) {
      g_gfx->setClip(0, 0, 176, 220);
      if (o) {
        g_gfx->setColor(0);
        g_gfx->fillRect(0, 0, 176, 220);
      } else {
        switch (e) {
          case 0: b_(g_gfx); return;
          case 1: d_(g_gfx); return;
          case 2: h_(g_gfx); return;
          case 4: e_(g_gfx); return;
          case 5: a_(g_gfx, (int)G); return;
          case 6: k_(g_gfx); return;
          case 7: l_(g_gfx); return;
          case 8: b_(g_gfx, G); return;
          case 9: j_(g_gfx); return;
          case 10:
            g_gfx->setClip(0, 0, 176, 220);
            g_gfx->setColor(0);
            g_gfx->fillRect(0, 0, 176, 220);
            return;
          case 11: c_(g_gfx); return;
          case 15: nameEntry_(g_gfx); return;
          case 17: codesResult_(g_gfx); return;
          case 16: g_(g_gfx); return;
          case 20: i_(g_gfx); return;
          default:
            // 3/12/13/14/18/19: unused by IntroManager itself in the
            // original too. 15/17: the unlock-code Form/TextField entry
            // and its post-submit screen -- unreachable (see header note).
            return;
        }
      }
    }
  }
}

// keyPressed dispatch -- translates the raw key into the -1..-5 "action"
// codes CanvasShell's numeric-keypad path uses (see midp.h's KeyCode
// note) and routes to the current screen's input handler.
void IntroManager::b_(int key) {
  if (u > 0) return;
  if (t > 0 && currentTimeMillis() - t < 1000) return;
  int action;
  if (key == 50) action = -1;
  else if (key == 56) action = -2;
  else if (key == 52) action = -3;
  else if (key == 54) action = -4;
  else if (key == 53) action = -5;
  else action = key;

  switch (e) {
    case 0:
      if (midlet_->game != nullptr) {
        E = 0;
        f = (jbyte)(f | 1);
      }
      return;
    case 1: c_(key, action); return;
    case 2: e_(key, action); return;
    case 4: d_(key, action); return;
    case 5: h_(key, action); return;
    case 6: k_(key, action); return;
    case 7: j_(key, action); return;
    case 8: g_(key, action); return;
    case 9: i_(key, action); return;
    case 11: l_(key, action); return;
    case 15: nameKey_(key, action); return;
    case 16: b_(key, action); return;
    case 17: codesKey_(key, action); return;
    case 20: f_(key, action); return;
    default:
      // 3/10/12/13/14/18/19 unused; 15/17 unreachable (see header note).
      return;
  }
}

// --- shared widgets/helpers ------------------------------------------------

jbyte IntroManager::a_(jbyte cur, jbyte lowerBound, jbyte resetTo) {
  return cur > lowerBound ? (jbyte)(cur - 1) : resetTo;
}

jbyte IntroManager::b_(jbyte cur, jbyte upperBound, jbyte resetTo) {
  return cur < upperBound ? (jbyte)(cur + 1) : resetTo;
}

int IntroManager::b_() { return A - ((176 / A + 1) * A - 176) / 2; }

String IntroManager::c_(int ms) {
  int sec = ms / 1000;
  int totalMin = ms / 60000;
  int hr = totalMin / 60;
  int min = totalMin % 60;
  sec %= 60;
  String out = std::to_string(hr);
  out += (min < 10 ? ":0" : ":");
  out += std::to_string(min);
  out += (sec < 10 ? ":0" : ":");
  out += std::to_string(sec);
  return out;
}

int IntroManager::a_(Graphics* g_gfx, const String& text, int x, int y, int, bool selected, int color) {
  ratchetandclank::currentFont = selected ? ratchetandclank::smallFontAlias : ratchetandclank::smallFont;
  if (selected) i = text;
  g_gfx->setColor(color);
  int width = Game::ap ? Game::ap->getWidth() - 10 : 0;
  return midlet_->game->a_(g_gfx, text, x, y, 17, width);
}

int IntroManager::a_(Graphics* g_gfx, const String& text, int x, int y, int anchor, bool selected) {
  return a_(g_gfx, text, x, y, anchor, selected, selected ? 1175023 : 14474495);
}

int IntroManager::b_(Graphics* g_gfx, const String& text, int x, int y, int anchor, bool selected) {
  return a_(g_gfx, text, x, y, anchor, selected, selected ? 16777215 : 14474495);
}

int IntroManager::a_(Graphics* g_gfx, jbyte slotIndex, int x, int y, int anchor, bool selected) {
  if (midlet_->saveSlotFlags[slotIndex] == 0) {
    String label = std::to_string(slotIndex + 1) + ratchetandclank::strings[31];
    return b_(g_gfx, label, x, y, 17, selected);
  }
  String label = Game::a_(ratchetandclank::strings[32],
                           {std::to_string(slotIndex + 1), c_(midlet_->saveSlotTimes[slotIndex])});
  return a_(g_gfx, label, x, y, anchor, selected);
}

int IntroManager::a_(Graphics* g_gfx, const String& title) {
  ratchetandclank::currentFont = ratchetandclank::largeFont;
  g_gfx->setColor(14474495);
  return midlet_->game->a_(g_gfx, title, 88, 5, 17, 176 - 2 * b_() - 10);
}

// caller is unused, matching the original's dead Object parameter.
void IntroManager::a_(Graphics* g_gfx, int leftStringIdx, int rightStringIdx, void*) {
  g_gfx->setClip(0, 0, 176, 220);
  ratchetandclank::currentFont = ratchetandclank::largeFont;
  int y = 220 - ratchetandclank::largeFont->lineHeight;
  if (leftStringIdx >= 0) {
    const String& s = ratchetandclank::strings[leftStringIdx];
    g_gfx->setColor(14474495);
    ratchetandclank::currentFont->drawText(g_gfx, s, 1, y + 1, 20);
  }
  if (rightStringIdx >= 0) {
    const String& s = ratchetandclank::strings[rightStringIdx];
    int w = ratchetandclank::largeFont->textWidth(s);
    g_gfx->setColor(14474495);
    ratchetandclank::currentFont->drawText(g_gfx, s, 176 - w - 1, y + 1, 20);
  }
}

// Selection bracket drawn around the currently-highlighted item at (x,y),
// using the cached label `i` (set by the a_(...,selected=true) draw call
// that just ran) to measure its height via a 1x1-clipped "invisible" draw.
void IntroManager::a_(Graphics* g_gfx, int x, int y) {
  g_gfx->setClip(0, 219, 1, 1);
  int h2 = a_(g_gfx, i, 0, 0, 17, true, 1175023);
  int apW = Game::ap ? Game::ap->getWidth() : 0;
  int apH = Game::ap ? Game::ap->getHeight() : 0;
  int x5 = (176 - apW) / 2;
  int third = apH / 3;
  g_gfx->setClip(x5, y - 3, apW, third);
  if (Game::ap) g_gfx->drawImage(Game::ap, x5, y - 3, 20);
  int y7 = y + third - 3;
  for (int step = third; y7 < y + h2 - third + 3; y7 += step) {
    if (y7 + step > y + h2 - third + 3) step -= y7 + step - (y + h2 - third + 3);
    g_gfx->setClip(x5, y7, apW, step);
    if (Game::ap) g_gfx->drawImage(Game::ap, x5, y7 - third, 20);
  }
  g_gfx->setClip(x5, y + h2 - third + 3, apW, third);
  if (Game::ap) g_gfx->drawImage(Game::ap, x5, y + h2 - third + 3 - 2 * third - 1, 20);
  g_gfx->setClip(0, 0, 176, 220);
  a_(g_gfx, i, x, y - 1, 17, false, 1175023);
}

// Bordered wallpaper backdrop (Game::aF tiled around the screen edges) --
// drawn first by every menu screen. Java's manipulation constant 8192
// (Nokia FLIP_HORIZONTAL) maps to this port's MANIP_MIRROR.
void IntroManager::a_(Graphics* g_gfx, jbyte) {
  g_gfx->setClip(0, 0, 176, 220);
  g_gfx->setColor(0);
  g_gfx->fillRect(0, 0, 176, 220);
  if (!Game::aF) return;
  int fw = Game::aF->getWidth();
  int fh = Game::aF->getHeight();
  if (fw <= 0 || fh <= 0) return;
  int x0 = (176 % fw == 0) ? 0 : -(((176 / fw + 1) * fw - 176) / 2);
  int hTile = fh / 16;

  midlet_->game->a_(g_gfx, x0, 0, fw, hTile);
  g_gfx->drawImage(Game::aF, x0, 0, 0);
  midlet_->game->a_(g_gfx, x0, hTile, fw, hTile);
  g_gfx->drawImage(Game::aF, x0, hTile, 0);
  midlet_->game->a_(g_gfx, 176 - x0 - fw, 0, fw, hTile);
  CanvasShell::directGraphics->drawImageManip(Game::aF, 176 - x0 - fw, 0, 20, MANIP_MIRROR);
  midlet_->game->a_(g_gfx, 176 - x0 - fw, hTile, fw, hTile);
  CanvasShell::directGraphics->drawImageManip(Game::aF, 176 - x0 - fw, hTile, 20, MANIP_MIRROR);
  midlet_->game->a_(g_gfx, x0, 2 * hTile, fw, hTile);
  g_gfx->drawImage(Game::aF, x0, hTile, 0);
  midlet_->game->a_(g_gfx, 176 - x0 - fw, 2 * hTile, fw, hTile);
  CanvasShell::directGraphics->drawImageManip(Game::aF, 176 - x0 - fw, hTile, 20, MANIP_MIRROR);

  int step = hTile;
  for (int y = 3 * step; y < 220 - hTile; y += step) {
    if (y + step > 220 - hTile) step = 220 - hTile - y;
    midlet_->game->a_(g_gfx, x0, y, fw, step);
    g_gfx->drawImage(Game::aF, x0, y - 2 * hTile, 0);
    midlet_->game->a_(g_gfx, 176 - x0 - fw, y, fw, step);
    CanvasShell::directGraphics->drawImageManip(Game::aF, 176 - x0 - fw, y - 2 * hTile, 20, MANIP_MIRROR);
  }

  midlet_->game->a_(g_gfx, x0, 220 - hTile, fw, hTile);
  g_gfx->drawImage(Game::aF, x0, 220 - hTile - 3 * hTile, 0);
  midlet_->game->a_(g_gfx, 176 - x0 - fw, 220 - hTile, fw, hTile);
  CanvasShell::directGraphics->drawImageManip(Game::aF, 176 - x0 - fw, 220 - 4 * hTile, 20, MANIP_MIRROR);

  for (int x = x0 + fw; x < 176 - x0 - fw; x += fw) {
    midlet_->game->a_(g_gfx, x, 220 - hTile, fw, hTile);
    g_gfx->drawImage(Game::aF, x, 220 - hTile - 4 * hTile, 0);
  }
  g_gfx->setClip(0, 0, 176, 220);
}

// "Page X/Y" footer, shared by the weapon store and the credits pager.
void IntroManager::b_(Graphics* g_gfx, int page, int total) {
  String s = Game::a_(ratchetandclank::strings[36], {std::to_string(page), std::to_string(total)});
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  a_(g_gfx, s, 88, 220 - ratchetandclank::currentFont->lineHeight + 1, 17, false);
}

void IntroManager::recordKeyForCheat(int key) {
  recentKeyHistory_[0] = recentKeyHistory_[1];
  recentKeyHistory_[1] = recentKeyHistory_[2];
  recentKeyHistory_[2] = recentKeyHistory_[3];
  recentKeyHistory_[3] = key;
}

bool IntroManager::checkCheatCode() {
  for (int i2 = 0; i2 < 4; i2++) {
    if (recentKeyHistory_[i2] != CHEAT_CODE_KEYS[i2]) return false;
  }
  return true;
}

// --- state 1: main menu -----------------------------------------------------

void IntroManager::d_(Graphics* g_gfx) {
  a_(g_gfx, (jbyte)0);
  {
    Game* gm = midlet_->game;
    gm->player->render(g_gfx, gm->player->activeFlag, 0, 11);
    gm->enemies[0]->render(g_gfx, 0, gm->enemies[0]->activeFlag, 0, 11);
    for (int i2 = 1; i2 < 4; i2++) gm->enemies[i2]->render(g_gfx, 1, gm->enemies[1]->activeFlag, 0, 11);
  }
  g_gfx->setClip(0, 0, 176, 220);
  if (Game::as) g_gfx->drawImage(Game::as, 88, 4, 17);
  int y = 4 + (Game::as ? Game::as->getHeight() : 0) + 4;
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  int apH = Game::ap ? Game::ap->getHeight() : 0;
  int y0 = g[0] = apH / 2 + y;
  int y1 = g[1] = apH / 2 + a_(g_gfx, ratchetandclank::strings[1], 88, y0, 0, G == 0);
  int y2 = g[2] = apH / 2 + a_(g_gfx, ratchetandclank::strings[2], 88, y1, 0, G == 1);
  int idx = 3;
  int last = y2;
  if (j) {
    last = apH / 2 + a_(g_gfx, ratchetandclank::strings[286], 88, y2, 0, G == 2);
    g[idx++] = last;
  }
  int yA = apH / 2 + a_(g_gfx, ratchetandclank::strings[3], 88, last, 0, (G == 2 && !j) || (G == 3 && j));
  g[idx++] = yA;
  int yB = apH / 2 + a_(g_gfx, ratchetandclank::strings[4], 88, yA, 0, (G == 3 && !j) || (G == 4 && j));
  g[idx++] = yB;
  int yC = apH / 2 + a_(g_gfx, ratchetandclank::strings[5], 88, yB, 0, (G == 4 && !j) || (G == 5 && j));
  g[idx] = yC;
  a_(g_gfx, ratchetandclank::strings[6], 88, yC, 0, (G == 5 && !j) || (G == 6 && j));
  a_(g_gfx, 0 + A + (A >> 1), g[G]);
  a_(g_gfx, 7, -1, this);
}

void IntroManager::c_(int key, int action) {
  if (action == -1 || key == 50) {
    G = a_(G, (jbyte)0, (jbyte)(j ? 6 : 5));
    f = (jbyte)(f | 3);
  } else if (action == -2 || key == 56) {
    G = b_(G, (jbyte)(j ? 6 : 5), (jbyte)0);
    f = (jbyte)(f | 3);
  } else if (key == -8) {
    f = (jbyte)(f | 3);
    a_((jbyte)9);
  } else if (key == 53 || action == -5 || key == -6) {
    if (G == 0) { a_((jbyte)11); return; }
    if (G == 1) { a_((jbyte)4); return; }
    if (G == 2 && j) {
      if (midlet_->isGameWon()) a_((jbyte)15); else a_((jbyte)16);
      return;
    }
    if ((G == 2 && !j) || (G == 3 && j)) { a_((jbyte)2); return; }
    if ((G == 3 && !j) || (G == 4 && j)) { a_((jbyte)5); return; }
    if ((G == 4 && !j) || (G == 5 && j)) {
      for (int i2 = 0; i2 < 4; i2++) recentKeyHistory_[i2] = 52;
      a_((jbyte)8);
      return;
    }
    if ((G == 5 && !j) || (G == 6 && j)) a_((jbyte)9);
  }
}

// --- state 2: options (sound / delete-save / language) ---------------------

void IntroManager::h_(Graphics* g_gfx) {
  a_(g_gfx, (jbyte)0);
  g_gfx->setClip(0, 0, 176, 220);
  midlet_->game->e_(g_gfx);
  a_(g_gfx, ratchetandclank::strings[27]);
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  int apH = Game::ap ? Game::ap->getHeight() : 0;
  int y0 = g[0] = apH / 2 + 100;
  int y1 = g[1] = apH / 2 + a_(g_gfx, midlet_->soundEnabled ? ratchetandclank::strings[28] : ratchetandclank::strings[29],
                               0 + A + (A >> 1), y0, 0, G == 0);
  int y2 = apH / 2 + a_(g_gfx, ratchetandclank::strings[33], 0 + A + (A >> 1), y1, 0, G == 1);
  g[2] = y2;
  int y3 = g[3] = apH / 2 + a_(g_gfx, ratchetandclank::strings[314], 88, y2, 17, G == 2);
  a_(g_gfx, screen::modeLabel(), 0 + A + (A >> 1), y3, 0, G == 3);
  a_(g_gfx, 7, 8, this);
  a_(g_gfx, 0 + A + (A >> 1), g[G]);
}

void IntroManager::e_(int key, int action) {
  if (key == -7 || key == -8) {
    a_((jbyte)1);
    midlet_->writeSoundAndLanguageSettings((jbyte)(midlet_->soundEnabled ? 1 : 0));
  } else if (action == -1 || key == 50) {
    G = a_(G, (jbyte)0, (jbyte)3);
    f = (jbyte)(f | 3);
  } else if (G == 3 && (action == -3 || action == -4 || key == 52 || key == 54)) {
    screen::cycleMode((action == -3 || key == 52) ? -1 : 1);
    f = (jbyte)(f | 3);
  } else if (action != -2 && key != 56) {
    if (key == 53 || action == -5 || key == -6) {
      if (G == 3) { screen::cycleMode(1); f = (jbyte)(f | 3); return; }
      if (G == 0) {
        if (midlet_->soundEnabled) {
          s = 0;
          midlet_->soundEnabled = false;
          midlet_->stopSoundHardOnLevelStart();
        } else {
          s = 15;
          midlet_->soundEnabled = true;
        }
        f = (jbyte)(f | 3);
        return;
      }
      if (G == 1) { a_((jbyte)6); return; }
      if (G == 2 && c) a_((jbyte)20);
    }
  } else {
    G = b_(G, (jbyte)3, (jbyte)0);
    f = (jbyte)(f | 3);
  }
}

// --- state 4: save-write picker ---------------------------------------------

void IntroManager::e_(Graphics* g_gfx) {
  a_(g_gfx, (jbyte)0);
  g_gfx->setClip(0, 0, 176, 220);
  midlet_->game->e_(g_gfx);
  a_(g_gfx, ratchetandclank::strings[2]);
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  int apH = Game::ap ? Game::ap->getHeight() : 0;
  int y0 = g[0] = apH / 2 + 100;
  int y1 = g[1] = apH / 2 + a_(g_gfx, (jbyte)0, 0 + A + (A >> 1), y0, 0, G == 0);
  int y2 = g[2] = apH / 2 + a_(g_gfx, (jbyte)1, 0 + A + (A >> 1), y1, 0, G == 1);
  a_(g_gfx, (jbyte)2, 0 + A + (A >> 1), y2, 0, G == 2);
  a_(g_gfx, 7, 8, this);
  a_(g_gfx, 0 + A + (A >> 1), g[G]);
}

void IntroManager::d_(int key, int action) {
  if (action == -1 || key == 50) {
    G = a_(G, (jbyte)0, (jbyte)2);
    f = (jbyte)(f | 3);
  } else if (action == -2 || key == 56) {
    G = b_(G, (jbyte)2, (jbyte)0);
    f = (jbyte)(f | 3);
  } else if (key != 53 && action != -5 && key != -6) {
    if (key == -7 || key == -8) a_((jbyte)1);
  } else if (midlet_->saveSlotFlags[G] != 0) {
    // Game.java (phase-1 tree) calls midlet.writeSaveSlot(G) here, which would overwrite
    // the chosen slot from the empty menu-time Game state; "Load Game" can only mean
    // continueGame(slot) (nothing else in the tree calls it), so it is used instead.
    midlet_->continueGame(G);
  }
}

// --- state 5: weapon store ---------------------------------------------------

void IntroManager::a_(Graphics* g_gfx, int weaponIndex) {
  int apqW = Game::aq ? Game::aq->getWidth() : 0;
  int x4 = 88 - apqW / 2 + 10;
  int x5 = 88 + apqW / 2 - 10;
  int aJw = Game::aJ ? Game::aJ->getWidth() : 0;
  int aJh8 = Game::aJ ? (Game::aJ->getHeight() >> 3) : 0;

  a_(g_gfx, (jbyte)0);
  g_gfx->setClip(0, 0, 176, 220);
  midlet_->game->e_(g_gfx);
  int y = a_(g_gfx, ratchetandclank::strings[4]);
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  if (weaponIndex < 3) {
    a_(g_gfx, ratchetandclank::strings[76], 0 + (A >> 1), y, 0, false);
  } else if (weaponIndex < 11) {
    a_(g_gfx, ratchetandclank::strings[262], 88, y, 17, false);
  } else {
    a_(g_gfx, ratchetandclank::strings[271], 88, y, 17, false);
  }

  y = midlet_->game->k_() - 3;
  int lh = ratchetandclank::currentFont->lineHeight;
  static const int kNameStr[11] = {-1, -1, -1, 48, 49, 50, 51, 52, 53, 54, 55};
  static const int kDescStr[11] = {77, 258, 82, 263, 264, 265, 266, 267, 268, 269, 270};
  if (weaponIndex >= 0 && weaponIndex <= 2) {
    y += lh;
    if (weaponIndex == 0) {
      y = midlet_->game->a_(g_gfx, ratchetandclank::strings[77], x4, y, 0, x5);
      y = midlet_->game->a_(g_gfx, ratchetandclank::strings[78], x4, y, 0, x5);
      y = midlet_->game->a_(g_gfx, ratchetandclank::strings[79], x4, y, 0, x5);
      midlet_->game->a_(g_gfx, ratchetandclank::strings[80], x4, y, 0, x5);
    } else if (weaponIndex == 1) {
      y = midlet_->game->a_(g_gfx, ratchetandclank::strings[258], x4, y, 0, x5);
      y = midlet_->game->a_(g_gfx, ratchetandclank::strings[259], x4, y, 0, x5);
      midlet_->game->a_(g_gfx, ratchetandclank::strings[81], x4, y, 0, x5);
    } else {
      y = midlet_->game->a_(g_gfx, ratchetandclank::strings[82], x4, y, 0, x5);
      y = midlet_->game->a_(g_gfx, ratchetandclank::strings[83], x4, y, 0, x5);
      midlet_->game->a_(g_gfx, ratchetandclank::strings[260], x4, y, 0, x5);
    }
  } else if (weaponIndex >= 3 && weaponIndex <= 10) {
    y += lh;
    y = a_(g_gfx, ratchetandclank::strings[kNameStr[weaponIndex]], 0 + (A >> 1), y, 0, false) + lh;
    midlet_->game->a_(g_gfx, ratchetandclank::strings[kDescStr[weaponIndex]], x4, y, 0, x5);
  } else if (weaponIndex >= 11 && weaponIndex <= 13) {
    int shift = (weaponIndex - 11) * aJh8;
    g_gfx->setClip((176 - aJw) >> 1, 2 * B - (B >> 1) - 2, aJw, aJh8);
    if (Game::aJ) g_gfx->drawImage(Game::aJ, (176 - aJw) >> 1, 2 * B - (B >> 1) - 2 - shift, 0);
    g_gfx->setClip(0, 0, 176, 220);
    y += lh;
    int nameIdx = 272 + (weaponIndex - 11);
    int descIdx = 275 + (weaponIndex - 11);
    y = a_(g_gfx, ratchetandclank::strings[nameIdx], 0 + (A >> 1), y, 0, false) + lh;
    midlet_->game->a_(g_gfx, ratchetandclank::strings[descIdx], x4, y, 0, x5);
  }

  b_(g_gfx, weaponIndex + 1, 14);
  a_(g_gfx, 9, 8, this);
  // Projectile::render() (weapons 4..10's demo shots) not transcribed yet.
  if (weaponIndex > 2 && weaponIndex < 11) {
    midlet_->game->player->render(g_gfx, midlet_->game->player->activeFlag, 0, -4);
  }
}

void IntroManager::h_(int key, int action) {
  if (key != 52 && action != -3 && key != -7) {
    if (key != 54 && action != -4 && key != -6) {
      if (key == 53 || action == -5) a_((jbyte)1);
    } else if (G >= 13) {
      a_((jbyte)1);
    } else {
      G++;
      if (G > 3 && G < 11) midlet_->game->player->currentWeapon++;
      else if (G == 3) { midlet_->game->player->currentWeapon = 0; midlet_->game->player->setAnimState((jbyte)8); midlet_->game->player->animRestart = 1; }
      else if (G == 11) midlet_->game->player->currentWeapon = 8;
      for (int i2 = 9; i2 >= 0; i2--) midlet_->game->playerProjectiles[i2]->reset();
      J = 0;
      I = 0;
      f = (jbyte)(f | 3);
    }
  } else if (G <= 0) {
    a_((jbyte)1);
  } else {
    G--;
    if (G > 3 && G < 11) midlet_->game->player->currentWeapon--;
    else if (G == 3) { midlet_->game->player->currentWeapon = 0; midlet_->game->player->setAnimState((jbyte)8); midlet_->game->player->animRestart = 1; }
    else if (G == 11) midlet_->game->player->currentWeapon = 8;
    for (int i2 = 9; i2 >= 0; i2--) midlet_->game->playerProjectiles[i2]->reset();
    J = 0;
    I = 0;
    f = (jbyte)(f | 3);
  }
}

// --- state 6: delete-save picker --------------------------------------------

void IntroManager::k_(Graphics* g_gfx) {
  a_(g_gfx, (jbyte)0);
  g_gfx->setClip(0, 0, 176, 220);
  midlet_->game->e_(g_gfx);
  a_(g_gfx, ratchetandclank::strings[33]);
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  int apH = Game::ap ? Game::ap->getHeight() : 0;
  int y0 = g[0] = apH / 2 + 100;
  int y1 = g[1] = apH / 2 + a_(g_gfx, (jbyte)0, 0 + A + (A >> 1), y0, 0, G == 0);
  int y2 = g[2] = apH / 2 + a_(g_gfx, (jbyte)1, 0 + A + (A >> 1), y1, 0, G == 1);
  a_(g_gfx, (jbyte)2, 0 + A + (A >> 1), y2, 0, G == 2);
  a_(g_gfx, 7, 8, this);
  a_(g_gfx, 0 + A + (A >> 1), g[G]);
}

void IntroManager::k_(int key, int action) {
  if (key == -7 || key == -8) {
    a_((jbyte)2);
  } else if (action == -1 || key == 50) {
    G = a_(G, (jbyte)0, (jbyte)2);
    f = (jbyte)(f | 3);
  } else if (action != -2 && key != 56) {
    if (key == 53 || action == -5 || key == -6) {
      if (midlet_->saveSlotFlags[G] == 0) return;
      H = G;
      h = false;
      a_((jbyte)7);
    }
  } else {
    G = b_(G, (jbyte)2, (jbyte)0);
    f = (jbyte)(f | 3);
  }
}

// --- state 7: shared Yes/No confirm -----------------------------------------

void IntroManager::l_(Graphics* g_gfx) {
  a_(g_gfx, (jbyte)0);
  g_gfx->setClip(0, 0, 176, 220);
  midlet_->game->e_(g_gfx);
  ratchetandclank::currentFont = ratchetandclank::largeFont;
  g_gfx->setColor(14474495);
  a_(g_gfx, ratchetandclank::strings[34]);
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  int apH = Game::ap ? Game::ap->getHeight() : 0;
  int y0 = g[0] = apH / 2 + 100;
  int y1 = g[1] = apH / 2 + a_(g_gfx, ratchetandclank::strings[11], 0 + A + (A >> 1), y0, 0, G == 0);
  a_(g_gfx, ratchetandclank::strings[10], 0 + A + (A >> 1), y1, 0, G == 1);
  a_(g_gfx, 7, 8, this);
  a_(g_gfx, 0 + A + (A >> 1), g[G]);
}

void IntroManager::j_(int key, int action) {
  if (key != -7 && key != -8) {
    if (action == -1 || key == 50) {
      G = a_(G, (jbyte)0, (jbyte)1);
      f = (jbyte)(f | 3);
    } else if (action != -2 && key != 56) {
      if (key == 53 || action == -5 || key == -6) {
        if (G == 0) {
          if (h) { a_((jbyte)11); return; }
          a_((jbyte)6);
          return;
        }
        if (G == 1) {
          if (h) {
            midlet_->game->cu = H;
            H = -1;
            midlet_->startNewGame(0);
            return;
          }
          midlet_->clearSaveSlot(H);
          H = -1;
          midlet_->refreshSaveSlotSummaries();
          f = (jbyte)(f | 3);
          a_((jbyte)6);
        }
      }
    } else {
      G = b_(G, (jbyte)1, (jbyte)0);
      f = (jbyte)(f | 3);
    }
  } else if (h) {
    a_((jbyte)11);
  } else {
    a_((jbyte)6);
  }
}

// --- state 8: credits/help/about pager --------------------------------------

void IntroManager::b_(Graphics* g_gfx, int page) {
  a_(g_gfx, (jbyte)0);
  g_gfx->setClip(0, 0, 176, 220);
  int y = a_(g_gfx, ratchetandclank::strings[5]);
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  int lh = ratchetandclank::currentFont->lineHeight;
  y += lh;
  int width = 176 - 2 * b_();
  if (page == 0) {
    midlet_->game->a_(g_gfx, ratchetandclank::strings[284], 88, y, 17, width);
  } else if (page == 1) {
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[86], 88, y, 17, width);
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[87], 176, y, 17, width) + lh * 2;
    midlet_->game->a_(g_gfx, ratchetandclank::strings[88], 88, y, 17, width);
  } else if (page == 2) {
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[89], 88, y, 17, width);
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[90], 88, y, 17, width) + lh;
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[91], 88, y, 17, width);
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[92], 88, y, 17, width) + lh;
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[93], 88, y, 17, width);
    midlet_->game->a_(g_gfx, ratchetandclank::strings[94], 88, y, 17, width);
  } else if (page == 3) {
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[95], 88, y, 17, width);
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[96], 88, y, 17, width) + lh;
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[97], 88, y, 17, width);
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[98], 88, y, 17, width) + lh;
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[99], 88, y, 17, width);
    midlet_->game->a_(g_gfx, ratchetandclank::strings[100], 88, y, 17, width);
  } else if (page == 4) {
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[101], 88, y, 17, width);
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[102], 176, y, 17, width);
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[103], 88, y, 17, width) + lh * 2;
    y = midlet_->game->a_(g_gfx, ratchetandclank::strings[84], 88, y, 17, width);
    String versionText = ratchetandclank::strings[85];
    size_t qq = versionText.find("??");
    if (qq != String::npos) {
      String ver = midlet_->getAppProperty("MIDlet-Version");
      if (!ver.empty()) versionText = versionText.substr(0, qq) + ver;
    }
    midlet_->game->a_(g_gfx, versionText, 88, y, 17, width);
  }
  b_(g_gfx, page + 1, 5);
  a_(g_gfx, 9, 8, this);
}

void IntroManager::g_(int key, int action) {
  if (key != 52 && action != -3 && key != -7) {
    if (key != 54 && action != -4 && key != -6) {
      if (key == 53 || action == -5) a_((jbyte)1);
    } else if (G < 4) {
      G++;
      f = (jbyte)(f | 3);
    } else {
      a_((jbyte)1);
    }
  } else if (G > 0) {
    G--;
    f = (jbyte)(f | 3);
  } else {
    a_((jbyte)1);
  }

  if (!midlet_->game->r) {
    recordKeyForCheat(key);
    midlet_->game->r = checkCheatCode();
  }
}

// --- state 9: exit-game confirm ---------------------------------------------

void IntroManager::j_(Graphics* g_gfx) {
  a_(g_gfx, (jbyte)0);
  g_gfx->setClip(0, 0, 176, 220);
  midlet_->game->e_(g_gfx);
  a_(g_gfx, ratchetandclank::strings[35]);
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  int apH = Game::ap ? Game::ap->getHeight() : 0;
  int y0 = g[0] = apH / 2 + 100;
  int y1 = g[1] = apH / 2 + a_(g_gfx, ratchetandclank::strings[11], 0 + A + (A >> 1), y0, 0, G == 0);
  a_(g_gfx, ratchetandclank::strings[10], 0 + A + (A >> 1), y1, 0, G == 1);
  a_(g_gfx, 7, 8, this);
  a_(g_gfx, 0 + A + (A >> 1), g[G]);
}

void IntroManager::i_(int key, int action) {
  if (key == -7 || key == -8) {
    a_((jbyte)1);
  } else if (action == -2 || key == 56) {
    G = a_(G, (jbyte)0, (jbyte)1);
    f = (jbyte)(f | 3);
  } else if (action != -1 && key != 50) {
    if (key == 53 || action == -5 || key == -6) {
      if (G == 0) { a_((jbyte)1); return; }
      if (G == 1) {
        midlet_->stopSoundHardOnLevelStart();
        midlet_->notifyDestroyed();
      }
    }
  } else {
    G = b_(G, (jbyte)1, (jbyte)0);
    f = (jbyte)(f | 3);
  }
}

// --- state 11: new-game slot picker -----------------------------------------

void IntroManager::c_(Graphics* g_gfx) {
  a_(g_gfx, (jbyte)0);
  g_gfx->setClip(0, 0, 176, 220);
  midlet_->game->e_(g_gfx);
  a_(g_gfx, ratchetandclank::strings[283]);
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  int apH = Game::ap ? Game::ap->getHeight() : 0;
  int y0 = g[0] = apH / 2 + 100;
  int y1 = g[1] = apH / 2 + a_(g_gfx, (jbyte)0, 0 + A + (A >> 1), y0, 0, G == 0);
  int y2 = g[2] = apH / 2 + a_(g_gfx, (jbyte)1, 0 + A + (A >> 1), y1, 0, G == 1);
  a_(g_gfx, (jbyte)2, 0 + A + (A >> 1), y2, 0, G == 2);
  a_(g_gfx, 7, 8, this);
  a_(g_gfx, A >> 1, g[G]);
}

void IntroManager::l_(int key, int action) {
  if (key == -7 || key == -8) {
    a_((jbyte)1);
  } else if (action == -1 || key == 50) {
    G = a_(G, (jbyte)0, (jbyte)2);
    f = (jbyte)(f | 3);
  } else if (action != -2 && key != 56) {
    if (key == 53 || action == -5 || key == -6) {
      if (midlet_->saveSlotFlags[G] == 0) {
        midlet_->game->cu = G;
        midlet_->startNewGame(0);
        return;
      }
      H = G;
      h = true;
      a_((jbyte)7);
    }
  } else {
    G = b_(G, (jbyte)2, (jbyte)0);
    f = (jbyte)(f | 3);
  }
}

// --- state 16: "get Ratchet skin" message -----------------------------------

void IntroManager::g_(Graphics* g_gfx) {
  a_(g_gfx, (jbyte)0);
  g_gfx->setClip(0, 0, 176, 220);
  int y = a_(g_gfx, ratchetandclank::strings[286]);
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  y += 5;
  y = midlet_->game->a_(g_gfx, ratchetandclank::strings[287], b_(), y, 17, 176 - 2 * b_());
  y += 5;
  midlet_->game->a_(g_gfx, ratchetandclank::strings[288], b_(), y, 17, 176 - 2 * b_());
  a_(g_gfx, -1, 8, this);
}

void IntroManager::b_(int key, int) {
  if (key == -7 || key == -8) a_((jbyte)1);
}

// --- state 20: language select ----------------------------------------------

void IntroManager::i_(Graphics* g_gfx) {
  a_(g_gfx, (jbyte)0);
  String title = (ratchetandclank::language == -1)
                     ? ratchetandclank::strings[7] + " " + ratchetandclank::strings[314]
                     : ratchetandclank::strings[314];
  int y = a_(g_gfx, title);
  y += 28;
  g[0] = y;
  int apH = Game::ap ? Game::ap->getHeight() : 0;
  int y1 = g[1] = apH / 2 + a_(g_gfx, ratchetandclank::strings[315], 0, y, 0, G == 0);
  int y2 = g[2] = apH / 2 + a_(g_gfx, ratchetandclank::strings[316], 0, y1, 0, G == 1);
  int y3 = g[3] = apH / 2 + a_(g_gfx, ratchetandclank::strings[317], 0, y2, 0, G == 2);
  int y4 = g[4] = apH / 2 + a_(g_gfx, ratchetandclank::strings[318], 0, y3, 0, G == 3);
  a_(g_gfx, ratchetandclank::strings[319], 0, y4, 0, G == 4);
  a_(g_gfx, 7, ratchetandclank::language == -1 ? -1 : 8, this);
  a_(g_gfx, 0, g[G]);
}

void IntroManager::f_(int key, int action) {
  if (key != -7 && key != -8) {
    if (action == -1 || key == 50) {
      G = a_(G, (jbyte)0, (jbyte)4);
      f = (jbyte)(f | 3);
      return;
    }
    if (action == -2 || key == 56) {
      G = b_(G, (jbyte)4, (jbyte)0);
      f = (jbyte)(f | 3);
      return;
    }
    if ((key == 53 || action == -5 || key == -6) && G != ratchetandclank::language) {
      bool firstTime = ratchetandclank::language == -1;
      ratchetandclank::language = G;
      midlet_->writeSoundAndLanguageSettings((jbyte)(midlet_->soundEnabled ? 1 : 0));
      gc();
      Game::sleep(20);
      midlet_->loadStrings();
      f = (jbyte)(f | 3);
      if (firstTime) a_((jbyte)1);
    }
  } else if (ratchetandclank::language != -1) {
    a_((jbyte)2);
  }
}

void IntroManager::a_(Command*, Displayable*) {}
void IntroManager::b_(Command*, Displayable*) {}

// --- states 15/17: "Get Ratchet Skin" name entry and codes -------------------

void IntroManager::textInput(int ch) {
  if (e != 15 || errUntil_ > currentTimeMillis()) return;
  if (ch >= 32 && ch < 127 && m.size() < 15) m += (char)ch;
}

void IntroManager::nameEntry_(Graphics* g_gfx) {
  a_(g_gfx, (jbyte)0);
  g_gfx->setClip(0, 0, 176, 220);
  int y = a_(g_gfx, ratchetandclank::strings[286]);
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  y += 5;
  g_gfx->setColor(14474495);
  y = midlet_->game->a_(g_gfx, ratchetandclank::strings[289], b_(), y, 17, 176 - 2 * b_());
  y += 8;
  int boxX = b_(), boxW = 176 - 2 * b_(), boxH = ratchetandclank::currentFont->lineHeight + 8;
  g_gfx->setColor(0);
  g_gfx->fillRect(boxX, y, boxW, boxH);
  g_gfx->setColor(1175023);
  g_gfx->drawRect(boxX, y, boxW - 1, boxH - 1);
  g_gfx->setColor(16777215);
  String shown = m;
  if ((currentTimeMillis() / 500) % 2 == 0) shown += "_";
  ratchetandclank::currentFont->drawText(g_gfx, shown, boxX + 4, y + 4, 20);
  a_(g_gfx, 39, 8, this);
  if (errUntil_ > currentTimeMillis()) {
    g_gfx->setClip(0, 0, 176, 220);
    g_gfx->setColor(0);
    g_gfx->fillRect(8, 70, 160, 80);
    g_gfx->setColor(1175023);
    g_gfx->drawRect(8, 70, 159, 79);
    ratchetandclank::currentFont = ratchetandclank::largeFont;
    g_gfx->setColor(14474495);
    int ey = midlet_->game->a_(g_gfx, errTitle_, 88, 76, 17, 148);
    ratchetandclank::currentFont = ratchetandclank::smallFont;
    midlet_->game->a_(g_gfx, errBody_, 88, ey + 4, 17, 148);
  }
}

void IntroManager::nameKey_(int key, int) {
  if (errUntil_ > currentTimeMillis()) return;
  if (n != 0 && currentTimeMillis() - n < 1000) return;
  // Printable keys arrive through textInput(); only the command keys matter here.
  if (key == KEY_FIRE || key == KEY_SOFT_LEFT) {
    String trimmed = trim(m);
    if (trimmed.size() < 4) {
      errTitle_ = ratchetandclank::strings[292];
      errBody_ = ratchetandclank::strings[293];
      errUntil_ = currentTimeMillis() + 1500;
      return;
    }
    if (trimmed.size() >= 16) {
      errTitle_ = ratchetandclank::strings[292];
      errBody_ = ratchetandclank::strings[294];
      errUntil_ = currentTimeMillis() + 1500;
      return;
    }
    m = trimmed;
    a_((jbyte)17);
  } else if (key == KEY_SOFT_RIGHT) {
    // Right soft key ("Back"): deletes a character, and leaves the screen once the field is empty.
    if (!m.empty()) m.pop_back();
    else a_((jbyte)1);
  }
}

int IntroManager::codeGroup_(int nibble) {
  if (nibble >= 0 && nibble <= 3) return 0;
  if (nibble >= 4 && nibble <= 7) return 1;
  return (nibble >= 8 && nibble <= 11) ? 2 : 3;
}

void IntroManager::computeCodes_(const String& name, int which) {
  String up = toUpperCase(name);
  int sum = 0;
  for (int i = 0; i < (int)up.size(); i++) sum += (int)(unsigned char)up[(size_t)i] * (i + 1);
  int32_t value = (int32_t)((uint32_t)sum * (uint32_t)v[which]);
  for (int k = 0; k < 8; k++) {
    int shift = k * 4;
    int32_t mask = (int32_t)(15u << shift);
    w[which][k] = codeGroup_((value & mask) >> shift);
  }
}

int IntroManager::codeRow_(Graphics* g_gfx, int which, int y) {
  int width = Game::aq ? Game::aq->getWidth() : 150;
  y = midlet_->game->a_(g_gfx, ratchetandclank::strings[which == 0 ? 312 : 313], -1, y, 17, width);
  int x = 33;
  for (int k = 0; k < 8; k++, x += 14) {
    g_gfx->setClip(x, y, 12, 12);
    g_gfx->drawImage(Game::am, x - w[which][k] * 12, y, 20);
  }
  g_gfx->setClip(0, 0, 176, 220);
  return y + 14;
}

void IntroManager::codesResult_(Graphics* g_gfx) {
  a_(g_gfx, (jbyte)0);
  g_gfx->setClip(0, 0, 176, 220);
  int y = a_(g_gfx, ratchetandclank::strings[286]);
  ratchetandclank::currentFont = ratchetandclank::smallFont;
  y += ratchetandclank::smallFont->lineHeight / 2;
  y = a_(g_gfx, ratchetandclank::strings[290], -1, y, 17, false);
  y = a_(g_gfx, m, -1, y, 17, false) + ratchetandclank::currentFont->lineHeight / 2;
  ratchetandclank::currentFont = ratchetandclank::largeFont;
  g_gfx->setColor(14474495);
  y = codeRow_(g_gfx, 1, y) + ratchetandclank::currentFont->lineHeight / 2;
  y = codeRow_(g_gfx, 0, y) + ratchetandclank::currentFont->lineHeight / 2;
  a_(g_gfx, ratchetandclank::strings[288], -1, y, 17, false);
  a_(g_gfx, 39, 8, this);
}

void IntroManager::codesKey_(int key, int) {
  if (key == KEY_SOFT_RIGHT || key == -8) a_((jbyte)15);
  else if (key == KEY_SOFT_LEFT || key == KEY_FIRE) a_((jbyte)1);
}
