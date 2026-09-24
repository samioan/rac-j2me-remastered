// player_behavior.cpp -- Player's physics/input-driven state machine,
// transcribed from src_a1/Player.java (tables, ctor, animation, render in
// player.cpp). Every `game_->xxx` is Game's own obfuscated member.
#include "player.h"
#include "game.h"
#include "midlet.h"

short Player::groundYAhead(bool tall) {
  int floorY = 17 * tileHeight;
  int thresh = 4 * tileHeight / 44;
  if (tall) thresh = 18 * tileHeight / 44;
  if (posInRow > (thresh << 8)) {
    if ((LevelMap::columnSolidMasks[columnRight()] & (1 << (row + 2))) > 0 ||
        (LevelMap::columnSolidMasks[columnLeft()] & (1 << (row + 2))) > 0)
      floorY = (row + 1) * tileHeight;
  } else if ((LevelMap::columnSolidMasks[columnRight()] & (1 << (row + 1))) > 0 ||
             (LevelMap::columnSolidMasks[columnLeft()] & (1 << (row + 1))) > 0) {
    floorY = row * tileHeight;
  }
  short platformFloor = game_->C_();
  short tileFloor = (short)game_->D_(floorY, platformFloor);
  short zipFloor = game_->B_();
  ledgeAhead = false;
  if (zipFloor < tileFloor) {
    ledgeAhead = true;
    return zipFloor;
  }
  return tileFloor;
}

bool Player::onLadderTop() {
  int px = posX >> 8;
  int col = px / tileWidth;
  int rem = px % tileWidth;
  if (rem < 10) {
    if (game_->levelMap->tiles[col - 1][row + 1] != 19) return false;
  } else if (rem > tileWidth - 10 && game_->levelMap->tiles[col + 1][row + 1] != 19) {
    return false;
  }
  return game_->levelMap->tiles[col][row + 1] == 19;
}

bool Player::wallOnLeft() {
  return !game_->levelMap->isWalkable(column() - 1, row + 1) && (((posX >> 8) - 8) % tileWidth) < 8;
}

bool Player::wallOnRight() {
  return !game_->levelMap->isWalkable(column() + 1, row + 1) && (((posX >> 8) + 8) % tileWidth) > tileWidth - 8;
}

void Player::zipRide() {
  if (game_->cv == -1) {
    setAnimState(2);
    game_->cv = facingRight ? -4 : -3;
    velY = 3584;
    velX = 0;
    jumpPhase = 0;
    game_->B_(0, -1);
  } else {
    posX = posX + zipVelX;
    posInRow = posInRow + zipVelY;
    wrapRow();
    if (y() + Game::K > zipEndY) {
      setAnimState(3);
      velX = 0;
      game_->cw = game_->cv = 0;
    }
  }
}

void Player::swingUpdate() {
  if (swingPhase == 0) {
    short floorY = groundYAhead(true);
    velY = (jshort)(velY - 128);
    posInRow = posInRow - velY;
    wrapRow();
    if (y() >= floorY) {
      row = (jbyte)(floorY / tileHeight);
      posInRow = (jshort)(floorY % tileHeight);
    }
    swingCurX = swingCurX + swingStepX;
    swingCurY = swingCurY + swingStepY;
    if (swingCurY < swingTargetY ||
        (abs(swingCurX - swingTargetX) < 1280 && abs(swingCurY - swingTargetY) < 1280)) {
      if (grabTileType == 97) {
        swingPhase = 1;
      } else {
        swingPhase = 3;
        if (jumpPhase == -1) {
          velY = (jshort)(1320 * tileHeight / 44);
          swingPhase = 2;
        }
      }
      int ox = 9 * tileWidth / 44;
      if (!facingRight) ox = -ox;
      int oy = 6 * tileHeight / 44;
      int cx = ((posX >> 8) + ox) << 8;
      int cy = (row * tileHeight + (posInRow >> 8) + oy) << 8;
      swingStepX = (swingTargetX - cx) / 10;
      swingStepY = (swingTargetY - cy) / 10;
      return;
    }
  } else if (swingPhase == 1) {
    animFrame = 1;
    posX = posX + swingStepX;
    posInRow = posInRow + swingStepY;
    wrapRow();
    velY = 0;
    if ((y() << 8) < swingTargetY ||
        (abs((x() << 8) - swingTargetX) < 1280 && abs((y() << 8) - swingTargetY) < 1280)) {
      setAnimState(3);
      swingPhase = -2;
      swingEndTimer = 0;
      Game::A = false;
      game_->cw = game_->cv = 0;
      return;
    }
  } else if (swingPhase == 2) {
    animFrame = 1;
    velY = (jshort)(velY - 128);
    posInRow = posInRow - velY;
    wrapRow();
    if (velY < 0) {
      swingPhase = 3;
      return;
    }
  } else if (swingPhase == 3) {
    int dy = (swingTargetY >> 8) - y();
    int dx = (swingTargetX >> 8) - x();
    int reach = (tileWidth + (tileWidth >> 1)) * (tileWidth + (tileWidth >> 1)) +
                (tileHeight + (tileHeight >> 1)) * (tileHeight + (tileHeight >> 1));
    swingPhase = 5;
    velX = 0;
    velY = 0;
    if (dx * dx + dy * dy > reach) {
      velX = (jshort)(dx << 4);
      velY = (jshort)(dy << 4);
      swingPhase = 4;
      return;
    }
  } else {
    if (swingPhase == 4) {
      int dy = (swingTargetY >> 8) - y();
      int dx = (swingTargetX >> 8) - x();
      int reach = (tileWidth + (tileWidth >> 1)) * (tileWidth + (tileWidth >> 1)) +
                  (tileHeight + (tileHeight >> 1)) * (tileHeight + (tileHeight >> 1));
      if (dx * dx + dy * dy > reach) {
        posX = posX + velX;
        posInRow = posInRow + velY;
        wrapRow();
        return;
      }
      swingPhase = 5;
      velY = 0;
      velX = 0;
      return;
    }
    if (swingPhase == 5) {
      animFrame = 1;
      short stepX = 0, stepY = 0;
      int a = y() - (swingTargetX >> 8) + 6 * tileHeight / 44;
      int b = facingRight ? (swingTargetY >> 8) - (x() + 9) : x() - 9 - (swingTargetY >> 8);
      int slope;
      if (b == 0) slope = 2000;
      else slope = (a << 8) / b;
      if (slope < -1945 || slope > 1945) { stepX = 2880; stepY = 0; }
      else if (slope > 616) { stepX = 2781; stepY = 447; }
      else if (slope > 333) { stepX = 2494; stepY = 864; }
      else if (slope > -10) { stepX = 2036; stepY = 1222; }
      else if (slope > -334) { stepX = 2036; stepY = -1222; }
      else if (slope > -617) { stepX = 2494; stepY = -864; }
      else if (slope > -1946) { stepX = 2781; stepY = -447; }
      if (facingRight) posX += stepX; else posX -= stepX;
      posInRow += stepY;
      velY = 447;
      wrapRow();
      if (slope < 0 && slope > -150) {
        setAnimState(2);
        animRestart = 0;
        swingPhase = -1;
        swingEndTimer = 15;
        game_->cw = game_->cv = 0;
      }
    }
  }
}

void Player::startFall() {
  setAnimState(2);
  jumpPhase = 0;
  ap = 0;
}

void Player::handleTileInteractions() {
  jbyte col = column();
  jbyte trow = (jbyte)((y() + Game::K) / tileHeight);
  if (swingEndTimer > 0) {
    game_->e = true;
    Game::A = true;
    if (--swingEndTimer <= 0) {
      swingPhase = -2;
      swingEndTimer = 0;
      Game::A = false;
    }
  }

  jshort t = game_->levelMap->getTile(col, trow);
  Game::B = false;
  if (t >= 56 && t <= 61) {
    swingPhase = -1;
    swingEndTimer = 10;
  } else if (t == 26) {
    velY = 3584;
    jumpPhase = 0;
    game_->ab = 1;
    animRestart = 1;
    setAnimState(2);
    game_->e_(posX, y() << 8, 30);
    if (!Game::f) health = (jbyte)(health - 4);
    swingPhase = -2;
    swingEndTimer = 0;
    Game::A = false;
    game_->e = true;
  } else if (t == -125) {
    Game::B = true;
    Game::D = 0;
    Game::C = -1;
  } else {
    for (jbyte k = (jbyte)(--trow + 2); k >= trow; k--) {
      t = game_->levelMap->getTile(col, k);
      if (t == 99 || t == 100) {
        game_->ah = game_->X;
        game_->ad = col;
        game_->ae = trow;
        game_->af = (col < 1) ? 0 : (jshort)(-tileWidth * (col - 1));
        game_->ag = (trow < 6) ? 0 : (jshort)(-tileHeight * (trow - 5));
        break;
      }
      if (t >= 77 && t <= 96 && game_->h_(t - 77)) {
        game_->cC = true;
        game_->cI = Game::cz[t - 77];
        game_->r_(Game::cA[t - 77]);
        game_->g_(t - 77);
        if (Game::cA[t - 77] == 112 && (game_->bw & 2) == 0) {
          game_->dC = 248;
          Game::dy = 16;
          return;
        }
        game_->dC = 233 + (t - 77);
        Game::dy = (jbyte)(t - 77);
        return;
      }
      if (t >= -118 && t <= -99 && game_->h_(t + 118 + 20)) {
        if (t + 118 == 18 && (ownedWeapons & 32) == 0) return;
        if (t + 118 != 19 || (!game_->h_(4) && !game_->h_(5) && !game_->h_(6) && !game_->h_(7) &&
                              !game_->h_(9) && !game_->h_(10))) {
          game_->cC = true;
          game_->cI = Game::cz[t + 118 + 20];
          game_->r_(Game::cA[t + 118 + 20]);
          game_->g_(t + 118 + 20);
          return;
        }
        return;
      }
      if (t == -94 && !game_->h_(11) && game_->h_(30)) {
        game_->cC = true;
        game_->cI = Game::cz[30];
        game_->r_(Game::cA[30]);
        game_->g_(30);
        return;
      }
    }
  }

  if (row >= 17 && game_->cT) {
    game_->cS = true;
    setAnimState(10);
    animRestart = 1;
  } else if (roomEdgeReached) {
    roomEdgeReached = false;
    if (columnRight() == 0 && !facingRight) { game_->j_(2); return; }
    if (columnLeft() >= 27 && facingRight) { game_->j_(5); return; }
    if (row >= 17 && velY <= 0) { game_->j_(6); return; }
    if (row == 0 && posInRow < 1280) game_->j_(1);
  }
}

void Player::tick() {
  Game* gm = game_;
  gm->d = true;
  if (velY <= -terminalVelocityY) velY = (jshort)(-terminalVelocityY);

  if (animState == 10 && animRestart == 2) {
    gm->w_();
    if (gm->cS) gm->cS = false;
    Game::dm++;
  } else if (animState == 10) {
    gm->cw = gm->cv = 0;
    gm->cx &= -129;
  } else if (health <= 0 && animState != 10) {
    gm->midlet->playSoundIfEnabled(0);
    health = 0;
    animRestart = 1;
    setAnimState(10);
    Game::dm++;
  } else if (swingPhase >= 0) {
    swingUpdate();
    updatePickupMagnet();
  } else if (animState == 6) {
    zipRide();
    updatePickupMagnet();
    handleTileInteractions();
  } else {
    if (animState == 11) {
      gm->cw = gm->cv = 0;
      velX = 0;
      swingTargetType = 1;
      meleeHit();
    } else if (animState == 9) {
      if (++animHold > 6) {
        if (jumpPhase == -1) {
          setAnimState(0);
        } else {
          velX = 0;
          velY = 0;
          setAnimState(3);
          jumpPhase = 0;
        }
      } else {
        gm->cv = 0;
      }
    }

    if (gm->cv == -4 || gm->cw == -4 || (animState == 8 && facingRight)) {
      if (animState == 4) gm->cv = 0;
      if (!facingRight || animState != 1) {
        if (jumpPhase == 2) currentWalkSpeed = 768;
        else if (animState == 8) currentWalkSpeed = 384;
        else if (jumpPhase > -1) currentWalkSpeed = 1536;
        else {
          setAnimState(1);
          currentWalkSpeed = 1536;
          animRestart = 0;
        }
        velX = currentWalkSpeed;
        facingRight = true;
      }
    } else if (gm->cv == -3 || gm->cw == -3 || (animState == 8 && !facingRight)) {
      if (animState == 4) gm->cv = 0;
      if (facingRight || animState != 1) {
        if (jumpPhase == 2) currentWalkSpeed = 768;
        else if (animState == 8) currentWalkSpeed = 384;
        else if (jumpPhase > -1) currentWalkSpeed = 1536;
        else {
          setAnimState(1);
          currentWalkSpeed = 1536;
          animRestart = 0;
        }
        velX = (jshort)(-currentWalkSpeed);
        facingRight = false;
      }
    } else if (gm->cv == 0) {
      if (jumpPhase == 2) {
        velX = (jshort)(facingRight ? 768 : -768);
      } else if (jumpPhase >= 0) {
        if (velX > 0) velX = (jshort)(velX - 196);
        else if (velX < 0) velX = (jshort)(velX + 196);
        if (velX <= 196 && velX >= -196) velX = 0;
      } else {
        velX = 0;
      }
      if (animState == 1) setAnimState(0);
    }

    if (gm->cv == -1) {
      if (jumpPhase == -1) {
        velY = 3584;
        jumpPhase++;
      } else if (jumpPhase == 0) {
        if (Game::z) {
          velY = 3584;
        } else {
          velY = 3072;
          animRestart = 1;
          setAnimState(13);
        }
        jumpPhase++;
      }
      gm->B_(0, -1);
    }

    if (animState == 4 && jumpPhase >= 0) {
      if (animCounter > 2) setAnimState(2);
      animRestart = 0;
    } else {
      if (velX != 0) {
        ae = facingRight ? columnRight() : columnLeft();
        if (posInRow < currentWalkSpeed) af = row;
        else af = (jbyte)(row + 1);

        if (ae >= 0 && ae <= 27 && gm->levelMap->isWalkable(ae, af) && (row < 17 || (row == 17 && posInRow == 0))) {
          int savedX = posX;
          posX = posX + velX;
          ae = facingRight ? columnRight() : columnLeft();
          if (gm->G_() != -1) {
            posX = posX - velX * 3 / 2;
            if (!gm->levelMap->isWalkable((posX >> 8) / tileWidth, af)) posX = savedX;
          } else if (gm->D_()) {
            posX = posX - velX;
          } else if (!gm->levelMap->isWalkable(ae, af)) {
            posX = posX - velX;
          }
        } else {
          velX = 0;
          if (facingRight) {
            if (columnRight() == ae) posX = (ae * tileWidth - 8 - 1) << 8;
          } else if (columnLeft() == ae) {
            posX = (ae * tileWidth + tileWidth + 8 + 1) << 8;
          }
        }
        if (ae >= 27) roomEdgeReached = true;
        if (ae == 0) roomEdgeReached = true;
      }

      if (hyperCastTimer >= 0 && animState != 11) updateHyperCastTimer();

      short floorY;
      if ((floorY = groundYAhead(false)) - y() > (tileHeight >> 1)) ledgeAhead = false;

      if (ledgeSnapPending && !ledgeAhead) {
        ledgeSnapPending = false;
        if (jumpPhase == -1) posInRow = 0;
      }

      if (ledgeAhead && jumpPhase == -1) {
        ledgeSnapPending = ledgeAhead;
        row = (jbyte)(floorY / tileHeight);
        posInRow = (jshort)((floorY % tileHeight) << 8);
        posX = posX + (ledgeSnapOffsetX << 8);
        velY = 0;
        if (!gm->levelMap->isWalkable(columnLeft(), row)) posX = (y() + 1) << 8;
        else if (!gm->levelMap->isWalkable(columnRight(), row)) posX = (y() - 1) << 8;
      } else {
        if ((jumpPhase >= 0 || x() < floorY) && ap == 0) {
          if (jumpPhase == 2) velY = -768;
          else velY = (jshort)(velY - 384);

          posInRow = posInRow - velY;
          if (velY > 0 && (!gm->levelMap->isWalkable(columnRight(), row) || !gm->levelMap->isWalkable(columnLeft(), row))) {
            posInRow = posInRow + velY;
            velY = 0;
          }

          if (posInRow < 0) {
            posInRow = posInRow + (tileHeight << 8);
            row--;
            if (row < 0) {
              row = 0;
              posInRow = 0;
            }
            if (row == 0) roomEdgeReached = true;
          } else if (posInRow > (tileHeight << 8)) {
            posInRow = posInRow - (tileHeight << 8);
            row++;
          }

          if (jumpPhase == -1) jumpPhase = 0;

          if (animState != 11 && animState != 14) {
            if (jumpPhase == 0) setAnimState(2);
            if (gm->cv == -3 || !facingRight) facingRight = false;
            else if (gm->cv == -4 || facingRight) facingRight = true;

            if (velY <= 0 && jumpPhase == 1 && !Game::z && animState == 12) jumpPhase = 2;
            else if (velY <= -1024 && jumpPhase != 2) setAnimState(3);
          }
        }

        if (ap == 0) {
          if (animState != 11 && velY < 0 && animState != 10) {
            for (int i2 = 0; i2 < Game::enemyPoolSize; i2++) {
              Enemy* en = gm->enemies[i2];
              if (en->animState != 2 && gm->k_(i2) && en->y() > x()) {
                setAnimState(3);
                if (y() > en->x()) {
                  en->bounceTimer = -10;
                  if (gm->levelMap->isWalkable((posX >> 8) / tileWidth + 1, row + 1) &&
                      gm->levelMap->isWalkable((posX >> 8) / tileWidth, row + 1)) {
                    velY = 0;
                    velX = 0;
                    jumpPhase = 0;
                    if (gm->levelMap->isWalkable((posX >> 8) / tileWidth + 1, row)) posX += 510;
                    if (!gm->levelMap->isWalkable((posX >> 8) / tileWidth + 1, row)) posX -= 510;
                  } else {
                    jumpPhase = -1;
                  }
                } else {
                  en->bounceTimer = 10;
                  if (gm->levelMap->isWalkable((posX >> 8) / tileWidth - 1, row + 1) &&
                      gm->levelMap->isWalkable((posX >> 8) / tileWidth, row + 1)) {
                    velY = 0;
                    velX = 0;
                    jumpPhase = 0;
                    if (gm->levelMap->isWalkable((posX >> 8) / tileWidth - 1, row)) posX -= 510;
                    if (!gm->levelMap->isWalkable((posX >> 8) / tileWidth - 1, row)) posX += 510;
                  } else {
                    jumpPhase = -1;
                  }
                }
              }
            }
          }

          if (x() >= floorY && velY < 0) {
            row = (jbyte)(floorY / tileHeight);
            posInRow = (jshort)((floorY % tileHeight) << 8);
            roomEdgeReached = true;
            velY = 0;
            jumpPhase = -1;
            if (animState == 3 || animState == 2 || (animState >= 11 && animState <= 14)) {
              if (jumpPhase == 2) jumpPhase = 1;
              setAnimState(4);
              animRestart = 1;
            }
          }
        }
      }

      if (animState != 10) {
        updatePickupMagnet();
        if (zipGrabRetryDelay == 0) {
          int px = posX >> 8;
          if (jumpPhase >= 0 && velY < 0) {
            for (int i2 = 3; i2 >= 0; i2--) {
              bool rightward = false;
              if (gm->bc[i2] != -1 &&
                  (((rightward = gm->be[i2] - gm->bc[i2] > 0) && px > gm->bc[i2] && px < gm->be[i2]) ||
                   (!rightward && px < gm->bc[i2] && px > gm->be[i2]))) {
                int lineY = gm->bh[i2] * px / gm->bg[i2] + gm->bi[i2];
                int gap = lineY - ((y() + (Game::K >> 1)) << 8);
                if (abs(gap) < -velY) {
                  lineY -= Game::K << 8;
                  row = (jbyte)((lineY >> 8) / tileHeight);
                  posInRow = (jshort)(((lineY >> 8) % tileHeight) << 8);
                  jumpPhase = -1;
                  setAnimState(6);
                  velX = velY = 0;
                  facingRight = rightward;
                  int steps = (abs(gm->be[i2] - gm->bc[i2]) << 8) / 2304;
                  zipVelX = (gm->bg[i2] << 8) / steps;
                  zipVelY = gm->bh[i2] / steps;
                  zipEndY = gm->bf[i2];
                  break;
                }
              }
            }
          }
        } else {
          zipGrabRetryDelay--;
        }
        handleTileInteractions();
      }
    }
  }
}

void Player::hyperShotSearch() {
  if (facingRight) {
    int c0 = columnRight();
    if ((posX >> 8) % tileWidth > (tileWidth >> 1)) c0++;
    for (int c = c0; c < c0 + 3; c++)
      for (int r = row; r >= row - 3; r--) {
        jshort t;
        if (c >= 0 && r >= 0 && ((t = game_->levelMap->getTile(c, r)) == 97 || t == 98)) game_->a_(c, r, (int)t);
      }
  } else {
    int c0 = columnLeft();
    if ((posX >> 8) % tileWidth < (tileWidth >> 1)) c0--;
    for (int c = c0; c > c0 - 3; c--)
      for (int r = row; r >= row - 3; r--) {
        jshort t;
        if (c >= 0 && r >= 0 && ((t = game_->levelMap->getTile(c, r)) == 97 || t == 98)) game_->a_(c, r, (int)t);
      }
  }
}

void Player::fire() {
  jbyte level = weaponLevel[currentWeapon];
  if (animState != 6 && animState != 7 && animState != 12 && animState != 13) {
    setAnimState(7);
    animRestart = 0;
  }
  if (ammo[currentWeapon] > 0) {
    if (game_->midlet->gameStarted) game_->midlet->playSoundIfEnabled(2);
    int sx = posX + (facingRight ? 4608 : -4608);
    int sy = (y() << 8) + 2048;
    switch (currentWeapon) {
      case 0: return;
      case 1: game_->e_(sx, sy, 0 + level); return;
      case 2: game_->e_(sx, sy, 3 + level); return;
      case 3: game_->e_(sx, sy, 6 + level); return;
      case 4:
        game_->e_(sx, sy, 9 + level);
        if (ammo[currentWeapon] > 0 && level == 2) game_->e_(sx, sy, 9 + level);
        return;
      case 5: game_->e_(sx, sy + ammo[currentWeapon] % 2 * 1024, 12 + level); return;
      case 6: game_->e_(sx, sy, 15 + level); return;
      case 7: game_->e_(sx, sy, 18 + level); return;
      default: return;
    }
  }
}

void Player::meleeHit() {
  Game* gm = game_;
  if (!(animState < 4 || animState == 11)) return;
  gm->m_(-1);
  if (animState != 11) {
    animRestart = 1;
    setAnimState(8);
  }
  int swx = (posX >> 8) + (facingRight ? SWING_X_OFFSET[swingTargetType]
                                       : -SWING_X_OFFSET[swingTargetType] - SWING_WIDTH[swingTargetType]);
  int swy = y() + SWING_Y_OFFSET[swingTargetType];
  int sww = SWING_WIDTH[swingTargetType], swh = SWING_HEIGHT[swingTargetType];

  for (int i2 = 2; i2 >= 0; i2--) {
    if (gm->bF[i2] == -1) continue;
    int bx = gm->bF[i2] * tileWidth + ((tileWidth - 19) >> 1);
    int by = gm->bG[i2] * tileHeight;
    if (abs(bx - x()) <= (3 * tileWidth >> 1) && abs(by - y()) <= tileHeight &&
        gm->a_(bx, by, 19, 19, swx, swy, sww, swh)) {
      int t = gm->levelMap->getTile(gm->bF[i2], gm->bG[i2]) - 36;
      if (t == 2 && (gm->bw & (1 << t)) > 0) {
        gm->cC = true;
        gm->di = 32;
        gm->cL |= 32;
        gm->cI = Game::cz[33];
        gm->r_(Game::cA[33]);
        gm->dC = 256;
        Game::dy = 15;
      }
      gm->bw = (jbyte)(gm->bw & ~(1 << t));
      if (t == 0) gm->e_(gm->Z, gm->X);
    }
  }

  for (int i2 = Game::enemyPoolSize - 1; i2 >= 0; i2--) {
    Enemy* en = gm->enemies[i2];
    jbyte k = en->kind;
    if (k == -1 || en->animState == 5) continue;
    int ex = en->x() - Enemy::HITBOX_X_OFFSETS[k];
    int ey = en->y() + Enemy::HITBOX_Y_OFFSETS[k];
    int ew = Enemy::HITBOX_WIDTHS[k], eh = Enemy::HITBOX_HEIGHTS[k];
    if (abs(ex - x()) <= (3 * tileWidth >> 1) && abs(ey - y()) <= tileHeight &&
        gm->a_(ex, ey, ew, eh, swx, swy, sww, swh) && (!en->flattenedRenderFlag || animState != 11)) {
      if (animState == 11) en->flattenedRenderFlag = true;
      if (animState == 11) en->health = (jbyte)(en->health - MELEE_DAMAGE_BY_LEVEL[weaponLevel[1]]);
      else en->health = (jbyte)(en->health - MELEE_DAMAGE_BY_LEVEL[weaponLevel[0]]);

      if (en->animState != 2) {
        if (en->posX > posX) en->bounceTimer = 10;
        else en->bounceTimer = -10;
      }

      if (en->health <= 0) {
        jshort room = gm->X;
        if (gm->cV) room = 0;
        int word = (i2 + room * 10) >> 5;
        gm->bv[word] &= ~(1 << (i2 + room * 10 - (word << 5)));
        en->setAnimState(5);
        en->animRestart = 1;
        if (k != 4) {
          Game::do_++;
          if (gm->aa && ++gm->ab > 10) gm->ab = 10;
          if (gm->Z != 0) {
            for (int b = 0; b < Enemy::BOLTS_DROPPED_BY_ANIM[en->animKind]; b++) gm->c_(en->x(), en->y(), 0);
          }
        }
      } else if (en->animState != 2) {
        en->setAnimState(4);
        en->animHold = 0;
      }
    }
  }

  if (jumpPhase == -1) gm->cv = 0;
}

void Player::updateHyperCastTimer() {
  if (++hyperCastTimer > 15) {
    hyperCastTimer = -1;
    resetAttackAnim();
  }
}

void Player::updatePickupMagnet() {
  Game* gm = game_;
  int refY = x() + 7 + 37;
  for (int i2 = 11; i2 >= 0; i2--) {
    jbyte type = gm->ck[i2];
    int px = gm->cg[i2];
    jshort vel = gm->ci[i2];
    if (type != -1 && (health != 20 || type != 2)) {
      int range = (tileWidth * 5) << 7;
      if (vel != 0 || (abs(px - posX) <= range && abs((gm->ch[i2] >> 8) - refY) <= Game::K)) {
        if (px > posX) vel = (jshort)(vel - 256);
        else vel = (jshort)(vel + 256);

        if ((px = px + vel) >= ((tileWidth * 27 + (tileWidth >> 1)) << 8)) px = (tileWidth * 27 + (tileWidth >> 1)) << 8;

        int dist;
        if ((dist = abs(px - posX)) <= (tileWidth << 6)) {
          gm->e = true;
          if (type <= 1) {
            gm->aX = gm->aX + 10 * gm->ab;
            Game::dq = Game::dq + 10 * gm->ab;
            if (gm->aX > 999999) gm->aX = 999999;
          } else if (type == 2) {
            health = (jbyte)(health + 4);
            if (health > 20) health = 20;
          } else {
            refillAmmo(1);
          }
          gm->ck[i2] = -1;
          return;
        }

        int dyv = gm->ch[i2] - ((y() + (tileHeight >> 1)) << 8);
        if (dist != 0) gm->ch[i2] = gm->ch[i2] + dyv * vel * (vel >= 0 ? -1 : 1) / dist;
        gm->cg[i2] = px;
        gm->ci[i2] = vel;
      }
    }
  }
}

void Player::refillAmmo(int units) {
  int best = 1;
  short lowest = 9999;
  for (int w = 1; w < 8; w++) {
    if (w != 6 && (ownedWeapons & (1 << w)) > 0 && ammo[w] < lowest &&
        ammo[w] != AMMO_CAPACITY[w * 3 + weaponLevel[w]]) {
      lowest = ammo[w];
      best = w;
    }
  }
  ammo[best] = (jshort)(ammo[best] + units * AMMO_PER_PICKUP[best]);
  if (ammo[best] > AMMO_CAPACITY[best * 3 + weaponLevel[best]]) ammo[best] = AMMO_CAPACITY[best * 3 + weaponLevel[best]];
}
