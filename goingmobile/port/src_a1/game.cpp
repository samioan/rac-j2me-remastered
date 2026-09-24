// game.cpp -- stub implementation of Game (see game.h). Everything is a
// no-op until the real boot-chain/gameplay transcription lands.
#include "game.h"

Game::Game(ratchetandclank* midlet) : midlet_(midlet) {}

void Game::pause() {}
void Game::resume() {}
void Game::render(Graphics*) {}
void Game::tick() {}
void Game::keyPressed(int) {}
void Game::keyReleased(int) {}
void Game::writeSaveData(jbyte*) {}
void Game::readSaveData(const jbyte*) {}
void Game::c_(int, int) {}
void Game::m_() {}
