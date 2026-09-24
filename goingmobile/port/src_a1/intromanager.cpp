// intromanager.cpp -- implementation of IntroManager (see intromanager.h),
// transcribed from src_a1/IntroManager.java's boot/splash slice.
#include "intromanager.h"
#include "midlet.h"
#include "game.h"

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
    default:
      // Screens 1 (decorative main-menu background) and 5 (weapon-select
      // idle animation) drive live Player/Enemy update() calls -- next
      // slice, once those behavior methods are transcribed (3.2a-adjacent).
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
  if (state == 15) {
    // Unlock-code name-entry Form/TextField -- not modeled (unreachable on
    // a fresh/normal playthrough: only reached from the main menu when
    // isGameWon() is already true, see src_a1/IntroManager.java's c(int,int)).
  }
  if (state == 17) {
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
          case 0:
            b_(g_gfx);
            return;
          default:
            // Screens 1+ (main menu, language select, ...): not
            // transcribed yet -- next slice of milestone 3.1a.
            return;
        }
      }
    }
  }
}

void IntroManager::b_(int) {}
void IntroManager::a_(Command*, Displayable*) {}
void IntroManager::b_(Command*, Displayable*) {}
