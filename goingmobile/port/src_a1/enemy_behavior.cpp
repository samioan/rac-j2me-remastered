// enemy_behavior.cpp -- Enemy's AI/physics, transcribed from
// src_a1/Enemy.java (tables, constructor, animation and render are in
// enemy.cpp).
#include "enemy.h"
#include "screen.h"
#include "game.h"

int Enemy::groundYAhead(bool sampleTile) {
  int col = (posX >> 8) / tileWidth;
  int platW = Game::aJ->getWidth();
  if (sampleTile) tileAhead = game_->levelMap->getTile(col, row);

  // Java tests `r >= 18` after `r *= tileHeight`, which is always true and
  // drops every enemy to the map bottom; the row test must precede the scale.
  int r;
  for (r = row + 1; r < 18; r++) {
    if ((LevelMap::columnSolidMasks[col] & (1 << r)) > 0) break;
  }
  r = (r >= 18) ? 18 * tileHeight : r * tileHeight;

  int px = posX >> 8;
  for (int i2 = 0; i2 < 50; i2++) {
    if (game_->bn[i2] != -1 && game_->bl[i2] <= px + 8 && game_->bl[i2] + platW >= px - 8 &&
        game_->bm[i2] < r && game_->bm[i2] > row * tileHeight) {
      r = game_->bm[i2];
    }
  }
  return r;
}

bool Enemy::attackBlockedByPlatform() {
  int w = Game::aJ->getWidth();
  int h2 = Game::aJ->getHeight() >> 3;
  int tw = tileWidth, th = tileHeight;
  int px = posX >> 8;
  int py = y() + Game::K - tileHeight;
  int hh = th - 1;
  for (int i2 = 49; i2 >= 0; i2--) {
    if (game_->bn[i2] >= 0 && abs(game_->bl[i2] - px) <= tw && game_->bm[i2] == py &&
        game_->a_(game_->bl[i2], game_->bm[i2], w, h2, px - 8, py, 16, hh)) {
      return true;
    }
  }
  return false;
}

void Enemy::updateWalk() {
  int groundRow = groundYAhead(true) / tileHeight;
  invalidTileAhead = false;
  wallAhead = false;
  edgeAhead = false;
  Player* pl = game_->player;
  if ((row + 1 < groundRow && kind != 3) || animState == 2) {
    setAnimState(2);
    velY = (jshort)(velY + 256);
    posInRow = posInRow + velY;
    wrapRow();
    if (row + 1 == groundRow && velY > 0) {
      setAnimState(0);
      velY = 0;
      posInRow = 0;
    }
    if (game_->a_(x() - HITBOX_X_OFFSETS[kind], y() + HITBOX_Y_OFFSETS[kind], HITBOX_WIDTHS[kind],
                  HITBOX_HEIGHTS[kind], pl->x() - 11, pl->y() + Game::L + 7, 18, 37)) {
      velX = 0;
      if (velY > 0) {
        velY = (jshort)(-velY / 2);
        if (pl->x() < x()) { velX = 510; facingRight = false; }
        else { velX = -510; facingRight = true; }
      } else {
        velY = (jshort)(-velY);
      }
    }
  }

  if (velX != 0) {
    ae = (jbyte)(facingRight ? columnRight() : columnLeft());
    af = row;
    int saved = posX;
    posX = posX + (facingRight ? (tileWidth << 7) : -(tileWidth << 7));
    int nextRow = groundYAhead(false) / tileHeight;
    edgeAhead = false;
    if (nextRow > groundRow) edgeAhead = true;
    if (bounceTimer != 0 && edgeAhead) velX = 0;
    posX = saved;
    if (ae > 0 && ae < 27 && game_->levelMap->isWalkable(ae, af)) {
      posX = posX + velX;
      if (attackBlockedByPlatform()) {
        posX = posX - velX;
        if (animState != 2) {
          wallAhead = true;
          return;
        }
      }
    } else {
      invalidTileAhead = true;
    }
  }
}

void Enemy::startWalk() {
  if (animState != 1) setAnimState(1);
  animRestart = 0;
  velX = (jshort)(facingRight ? SPEED_BY_TYPE[kind] : -SPEED_BY_TYPE[kind]);
}

void Enemy::meleeAttack() {
  if (animState == 3) return;
  Player* pl = game_->player;
  if (pl->animState == 10) return;
  facingRight = false;
  if (pl->x() - x() > 0) facingRight = true;
  if (--attackCooldown < 0) {
    attackCooldown = ATTACK_COOLDOWN_BY_TYPE[kind];
    if (game_->Z != 0) setAnimState(3);
    if (pl->invulnTimer > 0) return;
    if (game_->a_(pl->x() - 11, pl->y() + 7 + Game::L, 18, 37,
                  x() + (facingRight ? ATTACK_X_OFFSETS[kind] : -ATTACK_X_OFFSETS[kind] - ATTACK_WIDTHS[kind]),
                  y() + ATTACK_Y_OFFSETS[kind], ATTACK_WIDTHS[kind], ATTACK_HEIGHTS[kind]) &&
        game_->Z != 0) {
      if (!Game::f) pl->health = (jbyte)(pl->health - DAMAGE_BY_ANIM[animKind]);
      game_->ab = 1;
      pl->setAnimState(9);
      pl->animHold = 0;
      if (pl->jumpPhase == 2) pl->jumpPhase = 1;
      game_->e = true;
    }
  }
}

void Enemy::rangedAttack() {
  if (--attackCooldown < 0) {
    setAnimState(3);
    animRestart = 1;
    int sx = posX, sy = (y() + (Game::K >> 1)) << 8;
    if (animKind == 6 || animKind == 9 || animKind == 12 || animKind == 15 || animKind == 18 || animKind == 21) {
      game_->a_(sx, sy, 0, facingRight, wallCrawlFlipped, ap, animKind);
    } else if (animKind == 7 || animKind == 10 || animKind == 13 || animKind == 16 || animKind == 19 || animKind == 22) {
      game_->a_(sx, sy, 6, facingRight, wallCrawlFlipped, ap, animKind);
    } else if (animKind == 17 || animKind == 20 || animKind == 23) {
      game_->a_(sx, sy, 3, facingRight, wallCrawlFlipped, ap, animKind);
    }
    // (the original also has an unreachable type-18 branch for animKind 7/10/13)
    attackCooldown = ATTACK_COOLDOWN_BY_TYPE[kind];
    stateTimer++;
  }
}

void Enemy::tick() {
  Player* pl = game_->player;
  if (animState == 5) {
    if (animRestart == 2) kind = -1;
    return;
  }
  if (animState == 7 || animState == 6) return;

  if (!(abs(pl->x() - x()) <= screen::width && abs(pl->y() - y()) <= 220)) {
    velX = velY = 0;
    return;
  }
  if (kind == 3 && !(abs(pl->x() - x()) <= 3 * tileWidth + (tileWidth >> 1) && abs(pl->y() - y()) <= 5 * tileHeight))
    return;

  updateWalk();
  if (bounceTimer != 0 && kind != 3) {
    velX = (jshort)(bounceTimer << 8);
    velY = 0;
    setAnimState(4);
    if (bounceTimer > 0) { bounceTimer--; facingRight = true; }
    else { bounceTimer++; facingRight = false; }
    if (invalidTileAhead) velX = 0;
    return;
  }

  flattenedRenderFlag = false;
  int dx = pl->x() - x();
  int dy = pl->y() + tileHeight - Game::K - y();
  int tw = tileWidth;
  int far3 = tileWidth * 3;

  if (kind == 0) {
    if (wallAhead) {
      facingRight = !facingRight;
      stateTimer = 10;
    } else if (invalidTileAhead) {
      if (tileAhead == -96) {
        velY = -510;
        setAnimState(2);
        return;
      }
      facingRight = !facingRight;
      stateTimer = 10;
    } else if (edgeAhead) {
      if (tileAhead == -97) {
        velY = -768;
        velX = (jshort)(SPEED_BY_TYPE[kind] << 2);
        if (!facingRight) velX = (jshort)(velX * -1);
        setAnimState(2);
        return;
      }
      if (tileAhead != -96) {
        facingRight = !facingRight;
        stateTimer = 10;
      }
    }
    if (animState == 2) return;

    if (dx < far3 && dx > tw && abs(dy) < tileHeight && stateTimer == 0) {
      if (animState != 1) setAnimState(1);
      velX = SPEED_BY_TYPE[kind];
      facingRight = true;
    } else if (dx < -tw && dx > -far3 && abs(dy) < tileHeight && stateTimer == 0) {
      if (animState != 1) setAnimState(1);
      velX = (jshort)(-SPEED_BY_TYPE[kind]);
      facingRight = false;
    } else if (abs(dx) <= tw && dy == 0) {
      velX = 0;
      game_->a_(posX, (y() + (tileHeight >> 1)) << 8, 21, facingRight, false, 0, animKind);
      kind = -1;
      jshort room = game_->X;
      if (game_->cV) room = 0;
      int word = (spawnBitIndex + room * 10) >> 5;
      game_->bv[word] &= ~(1 << (spawnBitIndex + room * 10 - (word << 5)));
    } else {
      startWalk();
    }
    if (stateTimer != 0) {
      stateTimer--;
      return;
    }
    return;
  }

  if (kind == 1 && animState != 2) {
    if (tileAhead == -95) {
      if (dy < 0 && abs(dx) > tileWidth / 2) {
        velY = -3584;
        velX = 0;
        setAnimState(2);
      }
    } else if (wallAhead) {
      facingRight = !facingRight;
    } else if (invalidTileAhead) {
      if (tileAhead == -96 && abs(dx) > tileWidth / 2) {
        velY = -1700;
        setAnimState(2);
        return;
      }
      facingRight = !facingRight;
    } else if (edgeAhead) {
      if (tileAhead == -97 && abs(dx) > tileWidth / 2) {
        velY = -3584;
        velX = (jshort)(SPEED_BY_TYPE[kind] << 2);
        if (!facingRight) velX = (jshort)(velX * -1);
        setAnimState(2);
        return;
      }
      if (tileAhead != -96) facingRight = !facingRight;
    }
    if (animState == 2) return;
    if (abs(dx) <= tw && abs(dy) <= tw && abs(dy) < tileHeight) {
      velX = 0;
      meleeAttack();
      return;
    }
    startWalk();
    return;
  }

  if (kind == 2) {
    if (wallAhead || invalidTileAhead || edgeAhead) {
      velX = 0;
      animRestart = 1;
      setAnimState(0);
      facingRight = !facingRight;
    }
    int patrolDist = abs(patrolTargetX - posX) >> 8;
    if (attackPhase != 0) {
      if (attackPhase == 1) {
        if (stateTimer < ATTACK_WINDUP_TICKS[animKind]) {
          rangedAttack();
          return;
        }
        if (animRestart == 2 && animState == 0) {
          attackPhase = 0;
          return;
        }
        if (animState != 0) {
          animRestart = 1;
          setAnimState(0);
          return;
        }
      }
    } else {
      if (((dx > 0 && facingRight) || (dx < 0 && !facingRight)) && abs(dy) < tileHeight && abs(dx) < tileWidth * 3) {
        velX = 0;
        attackPhase = 1;
        stateTimer = 0;
        attackCooldown = 0;
        return;
      }
      if (((patrolTargetX > posX && !facingRight) || (patrolTargetX < posX && facingRight)) && patrolDist >= tileWidth * 3) {
        velX = 0;
        animRestart = 1;
        setAnimState(0);
        facingRight = !facingRight;
        return;
      }
      if (animState != 0 || (animRestart == 2 && animState == 0)) {
        startWalk();
        return;
      }
    }
  } else if (kind == 3) {
    if (attackPhase == 0) {
      if (stateTimer < ATTACK_WINDUP_TICKS[animKind]) {
        rangedAttack();
      } else {
        stateTimer = 0;
        attackPhase = 1;
      }
    }
    if (attackPhase == 1 && stateTimer++ > 10) {
      if (facingRight && !wallCrawlFlipped) {
        wallCrawlFlipped = true;
      } else if (facingRight) {
        wallCrawlFlipped = facingRight = false;
      } else {
        facingRight = true;
      }
      attackPhase = 0;
      stateTimer = 0;
      return;
    }
  } else if (kind == 4) {
    if (wallAhead || invalidTileAhead || edgeAhead) facingRight = !facingRight;
    startWalk();
  }
}
