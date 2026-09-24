// game_menus.cpp -- Game's in-level pause menu (b==4) and its sub-screens:
// sound toggle (b==5), quit-to-desktop confirm (b==7), quit-to-menu
// confirm (b==8). Transcribed from Game.java d(Graphics)/f(Graphics)/
// v(Graphics)/u(Graphics) and h/i/y/z(int,int).
#include "game.h"
#include "midlet.h"
#include "intromanager.h"
#include "font.h"

typedef ratchetandclank RC;

void Game::e_(Graphics* g) {
  if (aq) g->drawImage(aq, (176 - aq->getWidth()) >> 1, 75, 0);
}

void Game::d_(Graphics* g) {
  IntroManager* im = midlet->introManager;
  int tw = tileWidth + (tileWidth >> 1);
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  int y0 = im->a_(g, RC::strings[38]);
  RC::currentFont = RC::smallFont;
  y0 += RC::smallFont->lineHeight;
  int half = ap->getHeight() / 2;
  int y1 = dc[0] = half + y0;
  int y2 = dc[1] = half + im->a_(g, RC::strings[39], tw, y1, 0, cu == 0);
  int y3 = dc[2] = half + im->a_(g, RC::strings[3], tw, y2, 0, cu == 1);
  int y4 = dc[3] = half + im->a_(g, RC::strings[41], tw, y3, 0, cu == 2);
  im->a_(g, RC::strings[42], tw, y4, 0, cu == 3);
  im->a_(g, 7, 8, this);
  im->a_(g, tileWidth >> 1, dc[cu]);
  g->setColor(0xFFFFFF);
  a_(g, RC::strings[213 + o], I - 10, 20, 156);
}

int Game::a_(Graphics* g, const String& text, int y, int left, int right) {
  int lh = RC::currentFont->lineHeight;
  int w = RC::currentFont->textWidth(text);
  g->setClip(left, 0, right - left, 220);
  RC::currentFont->drawText(g, text, l, y, 20);
  l -= 2;
  if (l + w <= left) l = right;
  return lh;
}

void Game::f_(Graphics* g) {
  IntroManager* im = midlet->introManager;
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  e_(g);
  im->a_(g, RC::strings[27]);
  RC::currentFont = RC::smallFont;
  dc[0] = 100;
  im->a_(g, midlet->soundEnabled ? RC::strings[28] : RC::strings[29], tileWidth + (tileWidth >> 1), 100, 0, cu == 0);
  im->a_(g, 7, 8, this);
  im->a_(g, tileWidth >> 1, dc[cu]);
}

// Yes/No confirm screen shared by state 7 (title 46) and state 8 (title 45).
void Game::confirmScreen(Graphics* g, int titleIdx) {
  Game* gm = this;
  IntroManager* im = midlet->introManager;
  int tw = Game::tileWidth + (Game::tileWidth >> 1);
  im->a_(g, (jbyte)0);
  g->setClip(0, 0, 176, 220);
  gm->e_(g);
  im->a_(g, RC::strings[titleIdx]);
  RC::currentFont = RC::smallFont;
  gm->dc[0] = 100;
  int y1 = gm->dc[1] = Game::ap->getHeight() / 2 + im->a_(g, RC::strings[11], tw, 100, 0, gm->cu == 0);
  im->a_(g, RC::strings[10], tw, y1, 0, gm->cu == 1);
  im->a_(g, 7, 8, gm);
  im->a_(g, Game::tileWidth >> 1, gm->dc[gm->cu]);
}

void Game::v_(Graphics* g) { confirmScreen(g, 46); }
void Game::u_(Graphics* g) { confirmScreen(g, 45); }

void Game::h_(int key, int action) {
  IntroManager* im = midlet->introManager;
  if (action == -1 || key == 50) {
    cu = im->a_((jbyte)cu, (jbyte)0, (jbyte)3);
  } else if (action != -2 && key != 56) {
    if (key == 53 || action == -5 || key == -6 || key == -7) {
      if (key == -7) cu = 0;
      midlet->playSoundIfEnabled(3);
      if (cu != 0) {
        if (cu == 1) b = 5;
        else if (cu == 2) b = 8;
        else if (cu == 3) b = 7;
      } else {
        if (c == 0 || c == 17 || c == 16 || c == 24) {
          ct = currentTimeMillis();
          z_();
        }
        b = c;
        d = true;
        if (b == 0) e = true;
      }
      cu = 0;
    }
  } else {
    cu = im->b_((jbyte)cu, (jbyte)3, (jbyte)0);
  }
}

void Game::i_(int key, int action) {
  if (key != 53 && action != -5 && key != -6) {
    if (key == -7) {
      cu = 0;
      b = 4;
      midlet->playSoundIfEnabled(3);
      midlet->writeSoundAndLanguageSettings((jbyte)(midlet->soundEnabled ? 1 : 0));
    }
  } else if (cu == 0) {
    midlet->soundEnabled = !midlet->soundEnabled;
    midlet->writeSoundAndLanguageSettings((jbyte)(midlet->soundEnabled ? 1 : 0));
  }
}

// Shared body of y(int,int) (state 8, quit to menu) and z(int,int) (state 7,
// quit to desktop); they differ only in what "Yes" does.
void Game::confirmKey(int key, int action, bool toDesktop) {
  IntroManager* im = midlet->introManager;
  if (action == -1 || key == 50) {
    cu = im->a_((jbyte)cu, (jbyte)0, (jbyte)1);
  } else if (action != -2 && key != 56) {
    if (key != 53 && action != -5 && key != -6) {
      if (key == -7) {
        if (!toDesktop) midlet->playSoundIfEnabled(3);
        cu = 0;
        b = 4;
        if (toDesktop) midlet->playSoundIfEnabled(3);
      }
    } else {
      if (cu == 0) {
        b = 4;
        cu = 0;
        midlet->playSoundIfEnabled(3);
        return;
      }
      if (cu == 1) {
        cV = true;
        player->ownedWeapons = da;
        player->currentWeapon = 1;
        cV = false;
        if (Z != 0) midlet->writeSaveSlot(ac);
        if (toDesktop) {
          midlet->stopSoundHardOnLevelStart();
          midlet->notifyDestroyed();
        } else {
          dZ = true;
        }
        return;
      }
    }
  } else {
    cu = im->b_((jbyte)cu, (jbyte)1, (jbyte)0);
  }
}
