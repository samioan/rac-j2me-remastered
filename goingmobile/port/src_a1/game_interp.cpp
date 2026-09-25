// game_interp.cpp -- render-time interpolation so the game can be drawn faster than its fixed
// ~30 Hz logic rate. Logic is untouched: before each tick the positions of the camera, player,
// enemies and projectiles are recorded; a frame drawn at fraction `a` of the way to the next
// tick temporarily swaps blended positions into those fields, renders, and restores them.
#include "game.h"
#include "player.h"
#include "enemy.h"
#include "projectile.h"

#include <cmath>

namespace {

constexpr int kMaxEnemies = 16;
constexpr int kJump = 48;  // px; larger moves are teleports (respawn, room change): no blending

struct EntSnap { bool ok = false; jbyte kind = 0; jint px = 0, y = 0; };
struct ProjSnap { bool ok = false; jbyte type = -1; jint px = 0, py = 0; };

EntSnap g_player, g_enemy[kMaxEnemies];
ProjSnap g_pp[10], g_ep[10];
int g_camX = 0, g_camY = 0;
bool g_valid = false;

jint blend(jint prev, jint cur, float a) { return prev + (jint)std::lround((double)(cur - prev) * a); }

jint entY(const Entity& e) { return e.row * (Game::tileHeight << 8) + e.posInRow; }

void setY(Entity& e, jint y256) {
  jint unit = Game::tileHeight << 8;
  e.row = (jbyte)(y256 / unit);
  e.posInRow = y256 % unit;
}

struct EntSaved { jint posX; jbyte row; jint posInRow; };
struct ProjSaved { jint posX, posY, prevX, prevY, prev2X, prev2Y; };

}  // namespace

void Game::snapshotForInterp() {
  g_valid = player != nullptr && enemies != nullptr;
  if (!g_valid) return;
  g_camX = x;
  g_camY = y;
  g_player = {true, player->kind, player->posX, entY(*player)};
  for (int i = 0; i < enemyPoolSize && i < kMaxEnemies; i++) {
    const Enemy& e = *enemies[i];
    g_enemy[i] = {e.kind != -1, e.kind, e.posX, entY(e)};
  }
  for (int i = 0; i < 10; i++) {
    g_pp[i] = {playerProjectiles[i]->type >= 0, playerProjectiles[i]->type, playerProjectiles[i]->posX, playerProjectiles[i]->posY};
    g_ep[i] = {enemyProjectiles[i]->type >= 0, enemyProjectiles[i]->type, enemyProjectiles[i]->posX, enemyProjectiles[i]->posY};
  }
}

void Game::render(Graphics* g) {
  const float a = interpAlpha;
  if (a >= 1.0f || !g_valid || b != 0 || player == nullptr || enemies == nullptr) {
    renderImpl(g);
    return;
  }

  const int saveX = x, saveY = y;
  if (std::abs(saveX - g_camX) <= kJump && std::abs(saveY - g_camY) <= kJump) {
    x = blend(g_camX, saveX, a);
    y = blend(g_camY, saveY, a);
  }

  EntSaved sp = {player->posX, player->row, player->posInRow};
  bool pl = g_player.ok && g_player.kind == player->kind &&
            std::abs(player->posX - g_player.px) <= (kJump << 8) && std::abs(entY(*player) - g_player.y) <= (kJump << 8);
  if (pl) {
    jint cy = entY(*player);
    player->posX = blend(g_player.px, player->posX, a);
    setY(*player, blend(g_player.y, cy, a));
  }

  EntSaved se[kMaxEnemies];
  bool en[kMaxEnemies] = {};
  for (int i = 0; i < enemyPoolSize && i < kMaxEnemies; i++) {
    Enemy& e = *enemies[i];
    se[i] = {e.posX, e.row, e.posInRow};
    if (e.kind != -1 && g_enemy[i].ok && g_enemy[i].kind == e.kind &&
        std::abs(e.posX - g_enemy[i].px) <= (kJump << 8) && std::abs(entY(e) - g_enemy[i].y) <= (kJump << 8)) {
      en[i] = true;
      jint cy = entY(e);
      e.posX = blend(g_enemy[i].px, e.posX, a);
      setY(e, blend(g_enemy[i].y, cy, a));
    }
  }

  ProjSaved sj[2][10];
  bool pj[2][10] = {};
  for (int set = 0; set < 2; set++) {
    Projectile** arr = set == 0 ? playerProjectiles : enemyProjectiles;
    const ProjSnap* snap = set == 0 ? g_pp : g_ep;
    for (int i = 0; i < 10; i++) {
      Projectile& p = *arr[i];
      sj[set][i] = {p.posX, p.posY, p.prevX, p.prevY, p.prev2X, p.prev2Y};
      if (p.type >= 0 && snap[i].ok && snap[i].type == p.type &&
          std::abs(p.posX - snap[i].px) <= (kJump << 8) && std::abs(p.posY - snap[i].py) <= (kJump << 8)) {
        pj[set][i] = true;
        jint nx = blend(snap[i].px, p.posX, a), ny = blend(snap[i].py, p.posY, a);
        jint dx = nx - p.posX, dy = ny - p.posY;
        p.posX = nx; p.posY = ny;
        p.prevX += dx; p.prevY += dy; p.prev2X += dx; p.prev2Y += dy;
      }
    }
  }

  renderImpl(g);

  for (int set = 0; set < 2; set++) {
    Projectile** arr = set == 0 ? playerProjectiles : enemyProjectiles;
    for (int i = 0; i < 10; i++) {
      if (!pj[set][i]) continue;
      Projectile& p = *arr[i];
      p.posX = sj[set][i].posX; p.posY = sj[set][i].posY;
      p.prevX = sj[set][i].prevX; p.prevY = sj[set][i].prevY;
      p.prev2X = sj[set][i].prev2X; p.prev2Y = sj[set][i].prev2Y;
    }
  }
  for (int i = 0; i < enemyPoolSize && i < kMaxEnemies; i++) {
    if (!en[i]) continue;
    enemies[i]->posX = se[i].posX; enemies[i]->row = se[i].row; enemies[i]->posInRow = se[i].posInRow;
  }
  if (pl) { player->posX = sp.posX; player->row = sp.row; player->posInRow = sp.posInRow; }
  x = saveX;
  y = saveY;
}
