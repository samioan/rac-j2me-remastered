// canvasshell.cpp -- implementation of CanvasShell (see canvasshell.h),
// transcribed from src_a1/CanvasShell.java.
#include "canvasshell.h"
#include "midlet.h"
#include "game.h"
#include "intromanager.h"
#include "font.h"
#include "screen.h"

Graphics* CanvasShell::directGraphics = nullptr;

CanvasShell::CanvasShell(ratchetandclank* midlet) : midlet_(midlet) {}

void CanvasShell::hideNotify() {
  if (canvasState == 0) {
    midlet_->stopSoundHard();
    canvasState = 1;
    if (midlet_->gameStarted) {
      if (midlet_->game) midlet_->game->pause();
    } else if (midlet_->introManager) {
      midlet_->introManager->d_();
    }
  }
}

void CanvasShell::showNotify() {
  if (canvasState == -1) {
    canvasState = 0;
    activate();
  } else {
    hideNotify();
  }
}

void CanvasShell::activate() {
  if (midlet_->gameStarted) {
    if (midlet_->game) {
      midlet_->game->resume();
      return;
    }
  } else if (midlet_->introManager) {
    midlet_->introManager->e_();
  }
}

void CanvasShell::paint(Graphics* g) {
  if (canvasState == 1) {
    g->setClip(0, 0, screen::width, 220);
    g->setColor(0);
    g->fillRect(0, 0, screen::width, 220);
    g->setColor(16777215);
    if (ratchetandclank::largeFont)
      ratchetandclank::largeFont->drawText(g, "Press * Key...", screen::width / 2, 110, 17);
  } else if (canvasState == 2) {
    g->setClip(0, 0, screen::width, 220);
    g->setColor(0);
    g->fillRect(0, 0, screen::width, 220);
  } else if (canvasState == 0) {
    directGraphics = g;
    screen::beginFrame();
    if (midlet_->gameStarted) {
      if (midlet_->game) midlet_->game->render(g);
    } else if (midlet_->introManager) {
      // Intro screens are 176x220 design layouts: centred between black side bars, unscaled.
      const int off = screen::offsetX();
      g->translate(off, 0);
      midlet_->introManager->a_(g);
      g->translate(-off, 0);
      screen::paintSideBars();  // painted last: sprites that walk off the 176 edge are cut off
    }
    directGraphics = nullptr;
  }
}

void CanvasShell::tick() {
  if (canvasState == 0) {
    if (midlet_->gameStarted) {
      if (midlet_->game) midlet_->game->tick();
    } else if (midlet_->introManager) {
      midlet_->introManager->h_();
    }
  } else if (canvasState == 2) {
    activate();
    canvasState = 0;
  }
}

void CanvasShell::keyPressed(int key) {
  if (canvasState == 1) {
    if (key == KEY_STAR) canvasState = 2;
  } else if (canvasState == 0) {
    if (midlet_->gameStarted) {
      if (midlet_->game) midlet_->game->keyPressed(key);
    } else if (midlet_->introManager) {
      midlet_->introManager->b_(key);
    }
  }
}

void CanvasShell::keyReleased(int key) {
  if (canvasState == 0 && midlet_->gameStarted && midlet_->game) {
    midlet_->game->keyReleased(key);
  }
}

void CanvasShell::commandAction(Command* cmd, Displayable* screen) {
  if (canvasState == 0) {
    if (!midlet_->gameStarted && midlet_->introManager) {
      midlet_->introManager->b_(cmd, screen);
    }
  } else if (canvasState == 1 && !midlet_->gameStarted && midlet_->introManager) {
    midlet_->introManager->a_(cmd, screen);
  }
}
