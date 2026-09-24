// entity.cpp -- implementation of Entity (see entity.h), transcribed from
// src_a1/Entity.java. Needs Game's full definition for tileWidth/
// tileHeight, hence the split from the header (which only forward-declares
// Game to avoid a circular include with game.h).
#include "entity.h"
#include "game.h"

jbyte Entity::column() const {
  return (jbyte)((posX >> 8) / Game::tileWidth);
}

void Entity::wrapRow() {
  if (posInRow < 0) {
    posInRow += Game::tileHeight << 8;
    row--;
    if (row < 0) {
      row = 0;
      posInRow = 0;
      return;
    }
  } else if (posInRow > Game::tileHeight << 8) {
    posInRow -= Game::tileHeight << 8;
    row++;
  }
}
