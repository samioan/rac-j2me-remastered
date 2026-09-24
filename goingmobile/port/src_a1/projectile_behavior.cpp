// projectile_behavior.cpp -- Projectile's update/detonate/homing/render,
// transcribed from src_a1/Projectile.java (data tables + constructor live in
// projectile.cpp).
#include "projectile.h"
#include "game.h"
#include "canvasshell.h"

void Projectile::detonate(bool enemyShot) {
  if (detonated_ || type == 32) return;
  detonated_ = true;
  switch (type) {
    case 3: case 4: case 5: {
      int k = type - 3;
      if (enemyShot) game_->a_(posX, posY, 21 + k, true, false, 0, sourceAnim);
      else game_->e_(posX, posY, 21 + k);
      return;
    }
    case 6: case 7: case 8: {
      int k = type - 6;
      if (enemyShot) game_->a_(posX, posY, 24 + k, true, false, 0, sourceAnim);
      else game_->e_(posX, posY, 24 + k);
      return;
    }
    case 18: case 19: case 20: {
      int k = type - 18;
      if (enemyShot) game_->a_(posX, posY, 27 + k, true, false, 0, sourceAnim);
      else game_->e_(posX, posY, 27 + k);
      return;
    }
    case 31:
      if (!Game::dI) game_->C_(posX, posY);
      Game::dI = false;
      return;
    default:
      return;
  }
}

jbyte Projectile::weaponIndex() const {
  if (type >= 0 && type <= 2) return 1;
  if (type >= 21 && type <= 23) return 2;
  if (type >= 24 && type <= 26) return 3;
  if (type >= 9 && type <= 11) return 4;
  if (type >= 12 && type <= 14) return 5;
  if (type >= 15 && type <= 17) return 6;
  return (jbyte)(type >= 27 && type <= 29 ? 7 : 0);
}

void Projectile::update(bool enemyShot) {
  int camX = Game::x, camY = Game::y;
  if (detonated_) { reset(); return; }
  if (type == -1) return;

  auto offscreen = [&](int px, int py) {
    return px < -camX - tileWidth || px > -camX + 176 + tileWidth ||
           py < -camY - tileHeight || py > -camY + 220 + tileHeight;
  };

  if (type == 32) {
    if (vx != 0) vx--;
    if (vx == 0) posY += vy;
    if (posY < prev2Y && vx == 0) { vy *= -1; vx = 15; }
    else if (posY > prevY && vx == 0) { vy *= -1; vx = 1; }
    age++;
    if (age >= 3) age = 0;
  } else if (type >= 6 && type <= 8) {
    posX += vx;
    posY += vy;
    if (offscreen(posX >> 8, posY >> 8)) detonate(enemyShot);
  } else if (type >= 0 && type <= 2) {
    posX += vx;
    posY += vy;
    if (offscreen(posX >> 8, posY >> 8)) reset();
  } else if (type >= 3 && type <= 5) {
    posX += vx;
    int px = posX >> 8, py = posY >> 8;
    if (posX >= 0 && posY >= 0 && py <= 17 * tileHeight && px <= 27 * tileWidth) {
      if (py + HALF_HEIGHTS[type] >= game_->b_(px, py, tileHeight - HALF_HEIGHTS[type])) detonate(enemyShot);
    } else {
      detonate(enemyShot);
    }
    posY -= vy;
    vy -= 256;
  } else if (type >= 9 && type <= 11) {
    if (age++ == 2) reset();
  } else if (type >= 12 && type <= 14) {
    homingSeek(1);
    if (offscreen(posX >> 8, posY >> 8)) reset();
  } else if (type >= 15 && type <= 17) {
    if (age++ == 3) reset();
  } else if (type >= 18 && type <= 20) {
    if (enemyShot) return;
    homingSeek(4);
    if (offscreen(posX >> 8, posY >> 8)) detonate(enemyShot);
  } else if (type >= 21 && type <= 30) {
    if (++age == 3) reset();
  } else if (type == 31) {
    posX += vx;
    posY += vy;
  }

  // Java: (type != 15 || type != 16 || ... ) is always true, so this reduces
  // to (type < 21 || type == 31).
  if (type < 21 || type == 31) {
    int col = (posX >> 8) / tileWidth;
    int row = (posY >> 8) / tileHeight;
    if (!game_->levelMap->isWalkable(col, row)) {
      detonate(enemyShot);
      if (type >= 12 && type <= 14 && game_->levelMap->getTile(col, row) == 34) {
        game_->levelMap->tiles[col][row] = 0;
        LevelMap::columnSolidMasks[col] &= ~(1 << row);
      }
    }
  }
}

void Projectile::homingSeek(int) {
  if (homingTargetEnemy_ == -1) {
    int px = posX >> 8, py = posY >> 8;
    for (int i2 = Game::enemyPoolSize - 1; i2 >= 0; i2--) {
      Enemy* en = game_->enemies[i2];
      if (en->kind != -1 && en->animState != 5) {
        short ex = en->x(), ey = en->y();
        if ((ex - px) * (ex - px) + (ey - py) * (ey - py) <= 15876 && (vx > 0 ? ex > px : ex < px)) {
          homingTargetEnemy_ = (jshort)i2;
          break;
        }
      }
    }
  } else if (game_->enemies[homingTargetEnemy_]->kind != -1 && game_->enemies[homingTargetEnemy_]->animState != 5) {
    Enemy* en = game_->enemies[homingTargetEnemy_];
    short ex = en->x();
    int ey = en->y() + Enemy::HITBOX_Y_OFFSETS[en->kind] + (Enemy::HITBOX_HEIGHTS[en->kind] >> 1);
    int px = posX >> 8, py = posY >> 8;
    int dx = ex - px;
    int dy = ey - py;
    int spd = SPEEDS[type] << 8;
    if (type >= 18 && type <= 20) {
      if (dy > 0) {
        vy = spd;
        if ((dy << 8) < vy) vy = 0;
      } else {
        vy = -spd;
        if ((dy << 8) > vy) vy = 0;
      }
    } else if (age >= 10) {
      prev2X = prevX;
      prev2Y = prevY;
      prevX = posX;
      prevY = posY;
      if (game_->abs_(dx) > game_->abs_(dy)) {
        vx = dx > 0 ? spd : -spd;
        vy = 0;
      } else {
        vy = dy > 0 ? spd : -spd;
        vx = 0;
      }
      age = 0;
    }
  } else {
    homingTargetEnemy_ = -1;
  }
  age++;
  posX += vx;
  posY += vy;
}

void Projectile::render(Graphics* g) {
  int camX = Game::x, camY = Game::y;
  if (type < 0) return;
  Graphics* dg = CanvasShell::directGraphics;
  if (type >= 21 && type <= 30) {
    int shift = age * 44;
    int px = (posX >> 8) + camX - 22;
    int py = (posY >> 8) + camY - 22;
    if (px < 176 && px + 44 >= 0) {
      game_->b_(g, px, py, 44, 44);
      g->drawImage(Game::aM, px, py - shift, 0);
    }
  } else if (type == 32) {
    int h3 = Game::ay->getHeight() / 3;
    int w = Game::ay->getWidth();
    int shift = age * h3;
    int px = (posX >> 8) + camX - (w >> 1);
    int py = (posY >> 8) + camY - (h3 >> 1);
    if (0 < h3 && px < 176 && px + w >= 0) {
      game_->b_(g, px, py, w, h3);
      g->drawImage(Game::ay, px, py - shift, 0);
    }
  } else if (type == 31) {
    int px = (posX >> 8) + camX - (Game::aO->getWidth() >> 1);
    int py = (posY >> 8) + camY - (Game::aO->getHeight() >> 1);
    int dx = tileWidth * 14 - ((posX >> 8) - (Game::aO->getWidth() >> 1));
    int dy = tileHeight * 9 - ((posY >> 8) - (Game::aO->getHeight() >> 1));
    if (dx * dx + dy * dy > 1764 && px < 176 && px + 12 >= 0 && py < 220 && py + 12 >= 0) {
      g->setClip(0, hudHeight, 176, 220 - hudHeight);
      g->drawImage(Game::aO, px, py, 0);
    }
  } else {
    int sheetY = RENDER_MODES[type] * 19;
    if (sheetY < 0) {
      int px = (posX >> 8) + camX;
      int py = (posY >> 8) + camY;
      if (type >= 9 && type <= 11) {
        px -= vx > 0 ? halfWidth : -halfWidth;
        g->setClip(0, hudHeight, 176, 220 - hudHeight);
        g->setColor(age == 0 ? 0xFFFFFF : 0x0000FF);
        int ex = vx > 0 ? px + 168 : px - 168;
        g->drawLine(px, py, ex, py + 11);
        g->drawLine(px, py, ex, py + 5);
        g->drawLine(px, py, ex, py - 5);
        g->drawLine(px, py, ex, py - 11);
        return;
      }
      if (type >= 12 && type <= 14) {
        int x1 = (prevX >> 8) + camX, y1 = (prevY >> 8) + camY;
        int x2 = (prev2X >> 8) + camX, y2 = (prev2Y >> 8) + camY;
        g->setClip(0, hudHeight, 176, 220 - hudHeight);
        g->setColor(0xFFFF00);
        g->drawLine(px, py, x1, y1);
        g->drawLine(x1, y1, x2, y2);
        g->fillRect(px - 1, py - 1, 3, 3);
        return;
      }
      if (type >= 15 && type <= 17) {
        Player* pl = game_->player;
        if (pl->facingRight) {
          if (posX < pl->posX + playerOffsetX) posX = pl->posX + playerOffsetX;
        } else if (posX > pl->posX - playerOffsetX) {
          posX = pl->posX - playerOffsetX;
        }
        px = (posX >> 8) + camX;
        py = (posY >> 8) + camY;
        px -= vx > 0 ? halfWidth : -halfWidth;
        g->setClip(0, hudHeight, 176, 220 - hudHeight);
        g->setColor(0x0000FF);
        if (vx > 0) {
          g->fillRoundRect(px, py - 11, 168, 22, 12, 12);
          g->setColor(16250871);
          g->fillRect(px + 4, py - 11 + 10, 164, 2);
        } else {
          g->fillRoundRect(px - 168, py - 11, 168, 22, 12, 12);
          g->setColor(16250871);
          g->fillRect(px - 168, py - 11 + 10, 164, 2);
        }
        return;
      }
      g->setClip(0, hudHeight, 176, 220 - hudHeight);
      g->setColor(0xFFFFFF);
      g->fillRect(px - 3, py - 3, 6, 6);
      return;
    }
    int px = (posX >> 8) + camX - 9;
    int py = (posY >> 8) + camY - 9;
    if (px >= 176 || py >= 220 || px + 19 < 0 || py + 19 < 0) return;
    game_->b_(g, px, py, 19, 19);
    if (vx > 0) {
      g->drawImage(Game::aL, px, py - sheetY, 20);
    } else if (vx == 0) {
      if (vy < 0) dg->drawImageManip(Game::aL, px - sheetY, py, 20, 90);
      else dg->drawImageManip(Game::aL, px - 475 + sheetY, py, 20, 270);
    } else {
      dg->drawImageManip(Game::aL, px, py - sheetY, 20, 8192);
    }
  }
}
