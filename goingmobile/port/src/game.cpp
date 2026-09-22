// game.cpp -- port of goingmobile/src/Game.java (class f).
// Milestone 3.1: boot chain, splash, menus, text screens, name entry,
// save serialization, store screens -- everything except the b==0 gameplay
// internals (declared in game.h, stubbed at the bottom until milestone 3.2).
#include "game.h"

// ---- statics ---------------------------------------------------------------

ratchetandclank* Game::a = nullptr;
jint Game::f = 35;
jint Game::g = 100;
jbyte Game::h = 22;
bool Game::i = false;
bool Game::j = false;
int Game::k = 0, Game::l = 0, Game::m = 0;
std::vector<String>* Game::n = nullptr;
std::vector<String>* Game::o = nullptr;
std::vector<String>* Game::p = nullptr;
std::vector<String>* Game::q = nullptr;
int Game::r = 0, Game::s = 0;
bool Game::t = false, Game::u = false, Game::v = false;
jbyte Game::w = 0;
bool Game::x = false;
int Game::y = 0;
const char* Game::A[5] = {"English", "Fran\xE7" "ais", "Deutsch",
                          "Italiano", "Espa\xF1" "ol"};
const char* Game::B[5] = {"", "_fr", "_ge", "_it", "_sp"};
jbyte Game::C = 0;
std::vector<std::vector<MenuItem>> Game::H;
jbyte* Game::menuSoftLeft = nullptr;
jbyte* Game::menuSoftRight = nullptr;
jbyte* Game::menuTitle = nullptr;
jbyte* Game::menuBackTarget = nullptr;
int Game::I = 0, Game::J = 0, Game::K = 0, Game::L = 0, Game::M = 0;
Image* Game::tileSetImage = nullptr;
Image* Game::enemySegmentImage = nullptr;
Image* Game::hudIconImage = nullptr;
Image* Game::portraitImage = nullptr;
Image* Game::smallSpriteImage = nullptr;
Image* Game::playerImage = nullptr;
Image* Game::weaponImage = nullptr;
Image* Game::titaniumBoltImage = nullptr;
Image* Game::actorImage = nullptr;
const jbyte Game::INFOLINK_MESSAGE_TICKS[40] = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1};
const jshort Game::INFOLINK_MESSAGE_IDS[40] = {75, 76, 77, 78, 79, 80, 81, 82,
    83, 84, 85, 86, 87, 88, 89, 90, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
jbyte Game::MAX_ENEMIES = 5;
jbyte Game::PLAYER_SLOT = 5;
jbyte Game::TITANIUM_BOLTS_FOR_RYNO = 9;
jbyte Game::TITANIUM_BOLT_SLOTS = 60;
int Game::repaintDelay = 0;
int Game::bA = 0, Game::bB = 0, Game::bG = 0, Game::bH = 0;
int Game::enemyKills = 0, Game::baseScore = 0, Game::boltsCollected = 0;
int Game::levelsCleared = 0;
int Game::specialKills = 0, Game::timeBonus = 0;
jlong Game::scoringStart = 0, Game::timerStart = 0, Game::levelTimeMs = 0;
jlong Game::PAR_TIME_MS = 0;
int Game::totalScore = 0, Game::totalTitaniumBolts = 0, Game::totalKills = 0;
int Game::shotsFired = 0, Game::enemyShotHits = 0, Game::boxesHit = 0;
bool Game::noDamageBonus = false, Game::fastKillBonus = false;
bool Game::allKilledBonus = false, Game::playerDamaged = false;
int Game::enemiesRemainingPerLevel[12] = {};
bool Game::enemiesCountedPerLevel[12] = {};
const jbyte Game::BOSS_SHELL_COL[4] = {14, 14, 13, 13};
const jbyte Game::BOSS_SHELL_ROW[4] = {8, 9, 8, 9};
const jbyte Game::KEYPAD_CHARS[10][9] = {
    {'0', 0, 0, 0, 0, 0, 0, 0, 0},
    {'_', '1', 0, 0, 0, 0, 0, 0, 0},
    {'a', 'b', 'c', '2', 'A', 'B', 'C', 0, 0},
    {'d', 'e', 'f', '3', 'D', 'E', 'F', 0, 0},
    {'g', 'h', 'i', '4', 'G', 'H', 'I', 0, 0},
    {'j', 'k', 'l', '5', 'J', 'K', 'L', 0, 0},
    {'m', 'n', 'o', '6', 'M', 'N', 'O', 0, 0},
    {'p', 'q', 'r', 's', '7', 'P', 'Q', 'R', 'S'},
    {'t', 'u', 'v', '8', 'T', 'U', 'V', 0, 0},
    {'w', 'x', 'y', 'z', '9', 'W', 'X', 'Y', 'Z'}};
const int Game::KEYPAD_CHARS_LEN[10] = {1, 2, 7, 7, 7, 7, 7, 9, 7, 9};

Game::Game(ratchetandclank* midlet) {
  const jshort portraitRows[][12] = {
      {0, 112, 1, 113, 0, 114}, {1, 115, 0, 116, 1, 117}, {0, 118, 1, 119},
      {1, 125}, {0, 126}, {1, 127}, {-1}, {0, 128}, {-1},
      {1, 129, -1, 84}, {1, 130, 0, 131, 1, 132},
      {2, 120, 1, 121, 2, 122, 0, 123, 2, 124, -1, 77}};
  const int rowLens[12] = {6, 6, 4, 2, 2, 2, 0, 2, 0, 4, 6, 12};
  PORTRAIT_TABLE.resize(12);
  for (int row = 0; row < 12; row++) {
    for (int col = 0; col < rowLens[row]; col++)
      PORTRAIT_TABLE[(size_t)row].push_back(portraitRows[row][col]);
  }
  a = midlet;
  splashHoldoff = 2;
  repaintEachFrame = true;
  ad = 0;
  af = true;
  b = 20;
  splashImage = b_("sony");
}

// ---- scoring --------------------------------------------------------------

// k(): reset the scoring state for a new game
void Game::k_() {
  scoringActive = true;
  baseScore = 10000;
  bB = 0;
  bA = 0;
  enemyKills = 0;
  specialKills = 0;
  boltsCollected = 0;
  levelsCleared = 0;
  timeBonus = 0;
  timerStart = 0;
  levelTimeMs = 0;
  PAR_TIME_MS = 180000;
  totalScore = 0;
  shotsFired = 0;
  enemyShotHits = 0;
  boxesHit = 0;
  noDamageBonus = false;
  fastKillBonus = false;
  playerDamaged = false;
  allKilledBonus = false;
  m_();
  scoringStart = currentTimeMillis();
}

// l(): how many live enemies remain (non-boss-part kinds)
int Game::l_() {
  int count = 0;
  for (int idx = 4; idx >= 0; idx--) {
    if (enemies[idx]->kind != 4 && enemies[idx]->kind != -1) count++;
  }
  return count;
}

// f(int): record the remaining-enemy count for a level
void Game::f_(int level) { enemiesRemainingPerLevel[level] = l_(); }

// m(): clear the per-level enemy counters
void Game::m_() {
  for (int idx = 11; idx >= 0; idx--) {
    enemiesCountedPerLevel[idx] = false;
    enemiesRemainingPerLevel[idx] = 0;
  }
}

// a(byte): the scoring state machine (0 = timer start, 1 = level tally,
// 2 = final bonuses)
void Game::a_(jbyte phase) {
  switch (phase) {
    case 0:
      timerStart = currentTimeMillis();
      return;
    case 1: {
      if (!playerDamaged) {
        if (shotsFired == 0) fastKillBonus = true;
        if (shotsFired == enemyShotHits + boxesHit) noDamageBonus = true;
      }
      if (specialKills == enemyKills) allKilledBonus = true;
      for (int idx = 11; idx >= 0; idx--) {
        if (enemiesRemainingPerLevel[idx] > 0) bB += enemiesRemainingPerLevel[idx];
      }
      baseScore = enemyKills * 100;
      totalScore = totalScore + baseScore;
      bG = boltsCollected * 1;
      bH = levelsCleared * 100000;
      totalScore = totalScore + bG;
      totalScore = totalScore + bH;
      levelTimeMs = timerStart - scoringStart;
      if (levelTimeMs < PAR_TIME_MS) {
        timeBonus = (int)((PAR_TIME_MS - levelTimeMs) / 1000 * 10);
      }
      totalScore = totalScore + timeBonus;
      return;
    }
    case 2:
      if (fastKillBonus) totalScore += 100000;
      if (noDamageBonus) totalScore += 100000;
      if (allKilledBonus) totalScore += 10000;
      return;
    default:
      return;
  }
}

// n(): how many levels are marked cleared
int Game::n_() {
  int count = 0;
  for (int idx = 0; idx < 16; idx++) {
    if ((levelStateMask & (1 << idx)) != 0) count++;
  }
  return count;
}

// ---- menu-state setup ------------------------------------------------------

// o(): splash setup + menu table load
void Game::o_() {
  ad = 0;
  splashImage = b_("sony");
  digitStripImage = b_("uc");
  af = true;
  b = 20;
  a_("/o");
  J = 0;
  bg = 0;
  I = 0;
}

// p(): game-over/results screen setup
void Game::p_() {
  y_();
  b = 19;
  bg = 0;
  I = 0;
  J = 21;
  a_((jbyte)0);
  a_((jbyte)1);
  a_((jbyte)2);
  totalTitaniumBolts = totalTitaniumBolts + totalScore;
  q = ratchetandclank::c_(ratchetandclank::strings[101 + (Q - 1)], 118);
  q->push_back("");
  a_(ratchetandclank::c_(i_(63, enemyKills), 118));
  a_(ratchetandclank::c_(i_(64, boltsCollected), 118));
  a_(ratchetandclank::c_(i_(65, shotsFired), 118));
  a_(ratchetandclank::c_(i_(66, enemyShotHits), 118));
  a_(ratchetandclank::c_(i_(67, boxesHit), 118));
  a_(ratchetandclank::c_(ratchetandclank::strings[68] + " " + o_((int)levelTimeMs), 118));
  a_(ratchetandclank::c_(i_(111, totalScore), 118));
  q->push_back("");
  a_(ratchetandclank::c_(i_(69, B_()), 118));
  int owned = 0;
  for (int idx = 1; idx < 8; idx++) {
    if ((player->ownedWeapons & (1 << idx)) > 0) owned++;
  }
  a_(ratchetandclank::c_(i_(70, owned), 118));
  l = (int)q->size();
  k = 0;
}

// q(): from gameplay -> pause menu
void Game::q_() {
  if (0 == b) {
    y_();
    b = 19;
    bg = 0;
    I = 0;
    J = 9;
  }
}

// r(): -> pause menu
void Game::r_() {
  y_();
  b = 19;
  bg = 0;
  I = 0;
  J = 8;
}

// s(): level results setup
void Game::s_() {
  y_();
  a_((jbyte)0);
  a_((jbyte)1);
  a_((jbyte)2);
  q = new std::vector<String>();
  jbyte id;
  if (a->a_(ratchetandclank::strings[97]) < 118) {
    id = 97;
  } else {
    q->push_back(ratchetandclank::strings[98]);
    id = 99;
  }
  q->push_back(ratchetandclank::strings[id]);
  q->push_back("");
  a_(ratchetandclank::c_(i_(63, totalKills), 118));
  a_(ratchetandclank::c_(i_(64, boltCount), 118));
  a_(ratchetandclank::c_(ratchetandclank::strings[68] + " " + o_(levelElapsedMs), 118));
  a_(ratchetandclank::c_(i_(111, totalTitaniumBolts), 118));
  a_(ratchetandclank::c_(i_(69, B_()), 118));
  l = (int)q->size();
  k = 0;
  b = 19;
  bg = 0;
  I = 0;
  J = 20;
  ca = 10;
}

// a(Vector): move every element of src into q
void Game::a_(std::vector<String>* src) {
  while (src->size() > 0) {
    q->push_back(src->front());
    src->erase(src->begin());
  }
}

// a(String): the /o menu-definition loader -- 22 pages, each: item count,
// title, softkey labels, back-target, then (type, stringId, action) triples.
// Page 2 is special: the count byte is bumped +1 and a Language row is
// injected as the last item (0, -112, -104).
void Game::a_(const String& path) {
  ByteArray data = loadResource(path);
  if (data.empty()) return;
  int pos = 0;
  jbyte pageCount = (jbyte)(uint8_t)data[(size_t)pos++];
  int count = pageCount;
  H.clear();
  delete[] menuSoftLeft;  delete[] menuSoftRight;
  delete[] menuTitle;     delete[] menuBackTarget;
  menuSoftLeft = new jbyte[(size_t)count];
  menuSoftRight = new jbyte[(size_t)count];
  menuTitle = new jbyte[(size_t)count];
  menuBackTarget = new jbyte[(size_t)count];
  for (int page = 0; page < count; page++) {
    jbyte items = (jbyte)(uint8_t)data[(size_t)pos++];
    if (page == 2) items = (jbyte)(items + 1);
    menuTitle[page] = (jbyte)(uint8_t)data[(size_t)pos++];
    menuSoftLeft[page] = (jbyte)(uint8_t)data[(size_t)pos++];
    menuSoftRight[page] = (jbyte)(uint8_t)data[(size_t)pos++];
    menuBackTarget[page] = (jbyte)(uint8_t)data[(size_t)pos++];
    std::vector<MenuItem> rows;
    for (int item = 0; item < items; item++) {
      jbyte type, stringId, action;
      if (page == 2 && item == items - 1) {
        type = 0;
        stringId = (jbyte)-112;
        action = (jbyte)-104;
      } else {
        type = (jbyte)(uint8_t)data[(size_t)pos++];
        stringId = (jbyte)(uint8_t)data[(size_t)pos++];
        action = (jbyte)(uint8_t)data[(size_t)pos++];
      }
      rows.push_back(MenuItem(type, action, stringId));
    }
    H.push_back(std::move(rows));
  }
}

// ---- draw helpers ------------------------------------------------------------

// a(Graphics): draw the pending splash image
void Game::a_(Graphics* g) {
  if (splashImage != nullptr) {
    if (ad == 1) {
      g->setColor(16777215);  // white for the HHG splash
    } else {
      g->setColor(0);
    }
    g->setClip(0, 0, 128, 128);
    g->fillRect(0, 0, 128, 128);
    g->drawImage(splashImage, 64, 64, 3);  // HCENTER|VCENTER
    delete splashImage;
    splashImage = nullptr;
  }
}

// c(Graphics,int,int): the softkey bar (left/right labels on gray chips)
void Game::c_(Graphics* g, int left, int right) {
  g->setClip(0, 0, 128, 128);
  if (left >= 0) {
    const String& s = ratchetandclank::strings[left];
    int lh = a->fontLineHeight;
    int y = 128 - lh;
    int w = a->a_(s);
    g->setColor(160);
    g->fillRect(0, y - 1, w + 2, lh + 1);
    g->setColor(16777215);
    a->a_(g, s, 1, y, 20);  // LEFT|BOTTOM
  }
  if (right >= 0) {
    const String& s = ratchetandclank::strings[right];
    int lh = a->fontLineHeight;
    int y = 128 - lh;
    int w = a->a_(s);
    g->setColor(160);
    g->fillRect(128 - w - 2, y - 1, w + 2, lh + 1);
    g->setColor(16777215);
    a->a_(g, s, 128 - w - 1, y, 20);
  }
}

// b_(Graphics,int,int): the high-score digit strip (name hash -> 8 digits)
int Game::b_(Graphics* g, int slot, int y) {
  int h = digitStripImage->getHeight();
  int x = 128 - (8 * h + 14) - 8 >> 1;
  for (int digit = 0; digit < 8; x += h + 2) {
    g->setClip(x, y, h, h);
    g->drawImage(digitStripImage, x - G[slot][digit] * h, y, 20);
    digit++;
  }
  g->setClip(0, 0, 128, 128);
  return y + 14;
}

// a(Graphics,String,int,boolean): menu row (white when selected)
int Game::a_(Graphics* g, const String& s, int y, bool selected) {
  g->setColor(selected ? 16777215 : 2914559);
  return a_(g, s, 0, y, 17);
}

// b(Graphics,String,int,boolean): dim menu row (gray when not selected)
int Game::b_(Graphics* g, const String& s, int y, bool selected) {
  g->setColor(selected ? 16777215 : 8421504);
  return a_(g, s, 0, y, 17);
}

// a(Graphics,byte,int,boolean): save-slot row ("Slot N" / "Slot N, time")
int Game::a_(Graphics* g, jbyte slot, int y, bool selected) {
  if (a->saveSlotFlags[slot] == 0) {
    return b_(g, valueOf(slot + 1) + ratchetandclank::strings[31], y, selected);
  }
  std::vector<String> args{valueOf(slot + 1), o_(a->saveSlotTimes[slot])};
  String s = a_(ratchetandclank::strings[32], args);
  return a_(g, s, y, selected);
}

// a(Graphics,int,int,int,boolean): formatted value row
int Game::a_(Graphics* g, int id, int value, int y, bool selected) {
  std::vector<String> args{valueOf(value)};
  String s = a_(ratchetandclank::strings[id], args);
  return a_(g, s, y, selected);
}

// a(Graphics,String,int): title draw (centered, yellow)
int Game::a_(Graphics* g, const String& s, int y) {
  g->setColor(14481424);
  return a_(g, s, 64, y, 17);
}

// o(int): mm:ss:cc
String Game::o_(int ms) {
  int hundredths = ms / 1000;
  int minutes = ms / 60000;
  int hours = minutes / 60;
  minutes = minutes % 60;
  hundredths = hundredths % 60;
  return valueOf(hours) + (minutes < 10 ? ":0" : ":") + valueOf(minutes) +
         (hundredths < 10 ? ":0" : ":") + valueOf(hundredths);
}

// i(int,int): strings[id] with %d replaced by the value
String Game::i_(int id, int value) {
  std::vector<String> args{valueOf(value)};
  return a_(ratchetandclank::strings[id], args);
}

// a(String,Object[]): the %-format engine (%digit picks the argument)
String Game::a_(const String& fmt, const std::vector<String>& args) {
  String out;
  out.reserve(fmt.size());
  int arg = 0;
  bool useArg = false;
  for (int idx = 0; idx < length(fmt); idx++) {
    char c = charAt(fmt, idx);
    if (c == '%' && idx + 1 < length(fmt) && charAt(fmt, idx + 1) >= '0' &&
        charAt(fmt, idx + 1) <= '9') {
      arg = charAt(fmt, idx + 1) - '0';
      useArg = true;
    }
    if (useArg) {
      useArg = false;
      out += args[(size_t)arg];
      idx++;
    } else {
      out += c;
    }
  }
  return out;
}

// a(Graphics,String,int,int,int): word-wrap draw -- returns the next y.
// Java: x becomes 0 when HCENTER; words accumulate until 128px is exceeded,
// then the consumed range is drawn and the scan rewinds to the word start.
int Game::a_(Graphics* g, const String& s, int x, int y, int anchor) {
  int lineWidth = 0, wordWidth = 0, wordStart = 0;
  int lineHeight = a->fontLineHeight;
  if ((anchor & 1) > 0) x = 0;  // HCENTER
  int lineStart = 0, pos = 0;
  do {
    wordWidth = 0;
    wordStart = pos;
    while (pos < length(s)) {
      char c = charAt(s, pos);
      wordWidth += a->a_(c);
      pos++;
      if (c == ' ') break;
    }
    if (lineWidth + wordWidth <= 128 && pos != length(s)) {
      lineWidth += wordWidth;
    } else {
      if (lineWidth + wordWidth > 128) pos = wordStart;
      if (pos - lineStart > 0) {
        if ((anchor & 1) > 0) {
          a->a_(g, s, lineStart, pos - lineStart, 64, y, anchor);
        } else {
          a->a_(g, s, lineStart, pos - lineStart, x, y, anchor);
        }
      }
      y += lineHeight;
      lineWidth = x;
      lineStart = pos;
    }
  } while (pos < length(s));
  return y;
}

// ---- menu renderer -----------------------------------------------------------

// b(Graphics): the whole menu mode (b == 19): special screens (name entry,
// high-score viewer, language) or the current /o page with scroll arrows.
void Game::b_(Graphics* g) {
  if (D != 114) {
    int leftId, rightId;
    if (D == 116) {
      // the pre-name-entry info screen
      g->setClip(0, 0, 128, 128);
      g->setColor(1052688);
      g->fillRect(0, 0, 128, 128);
      a_(g, ratchetandclank::strings[133], 5);
      a_(g, cs, false);
      leftId = -1;
      rightId = 8;
    } else if (D == 117) {
      // high-score viewer
      g->setClip(0, 0, 128, 128);
      g->setColor(0);
      g->fillRect(0, 0, 128, 128);
      int titleY = a_(g, ratchetandclank::strings[133], 5);
      a_(g, ct, true);
      if (ct != nullptr) {
        if (cu - k >= 0 && cu - k <= m - 1) {
          b_(g, 1, (cu - k + 1) * 10 + titleY);
        }
        if (cv - k >= 0 && cv - k <= m - 1) {
          b_(g, 0, (cv - k + 1) * 10 + titleY);
        }
      }
      leftId = 140;
      rightId = 8;
    } else if (D == 115) {
      // name entry
      g->setClip(0, 0, 128, 128);
      g->setColor(0);
      g->fillRect(0, 0, 128, 128);
      const String& label =
          nameEntryWarning.empty() ? ratchetandclank::strings[139] : nameEntryWarning;
      int cursorX = a_(g, label, 0) + 5;
      String shown = E;
      if (currentTimeMillis() - lastMultitapTime > 1500 && length(E) < E_CAPACITY) {
        shown += "|";
      }
      a_(g, shown, cursorX);
      leftId = 140;
      rightId = 8;
    } else {
      if (D != 118 && D != 120) return;
      // language selection
      g->setClip(0, 0, 128, 128);
      g->setColor(0);
      g->fillRect(0, 0, 128, 128);
      int rows = 128 / a->fontLineHeight - 1;
      int yy = 3;
      if (rows < l + 1) {
        int top = 0;
        if (k > rows / 2) top = k - rows / 2;
        if (k > l - (rows - rows / 2)) top = l - rows + 1;
        for (int row = 0; row < Math::min(rows, l); row++) {
          yy = a_(g, A[(size_t)(top + row)], yy, top + row == k);
        }
      } else {
        yy = 3 + (rows - l - 1) * a->fontLineHeight / 2;
        for (int row = 0; row < l + 1; row++) {
          yy = a_(g, A[(size_t)row], yy, row == k);
        }
      }
      if (D == 120) {
        leftId = 7;
        rightId = 8;
      } else {
        leftId = 7;
        rightId = -1;
      }
    }
    c_(g, leftId, rightId);
    return;
  }

  // --- normal /o menu page -----------------------------------------------
  const std::vector<MenuItem>& page = H[(size_t)J];
  int pageCount = (int)page.size();
  if (J == 8) pageCount = n_();
  g->setClip(0, 0, 128, 128);
  g->setColor(0);
  g->fillRect(0, 0, 128, 128);
  int y;
  bool ownedOverlay = false;
  if (J == 18 && (player->ownedWeapons & (1 << (cr + 1))) != 0) {
    y = a_(g, ratchetandclank::strings[48], 5);
    ownedOverlay = true;
  } else if (J == 0) {
    y = a_(g, ratchetandclank::strings[(size_t)menuTitle[J]], 5);
    y = a_(g, ratchetandclank::strings[95], y);
    y += 5;
  } else {
    y = a_(g, ratchetandclank::strings[(size_t)menuTitle[J]], 5);
  }
  if (J == 17 && (player->ownedWeapons & 128) == 0) pageCount--;
  if (J == 18) {
    y += 2;
    y = a_(g, ratchetandclank::strings[(size_t)(49 + cr)], y, false);
    y = a_(g, 56, boltCount, y, false);
    int price = ownedOverlay ? storeAmmoNeeded[(size_t)cr + 1]
                             : Player::h[(size_t)cr + 1];
    y = a_(g, 57, price, y, false);
  }
  L = (116 - y - 14) / 12;
  if (L < 2) L = 2;
  int top = y;
  y = (116 - top - 12 * (pageCount > L ? L : pageCount) >> 1) + top;
  c_(g, menuSoftLeft[J], menuSoftRight[J]);
  if (pageCount != 0) {
    int idx = I;
    M = 0;
    int row = 0;
    while (row < L && idx < (int)page.size()) {
      jbyte type = page[(size_t)idx].type;
      jbyte sid = page[(size_t)idx].stringId;
      int rowY = y;
      bool advance = true;
      if (type != 0 && (type != 10 || (player->ownedWeapons & 128) == 0)) {
        if (type >= 1 && type <= 3) {
          y = a_(g, (jbyte)(type - 1), y, bg == row + I);
        } else if (type == 4) {
          // text-screen rows draw their whole page content inline
          switch (page[0].stringId) {
            case 96:
              a_(g, o, false);
              break;
            case 97:
              a_(g, n, true);
              break;
            case 98:
              a_(g, p, true);
              break;
            default:
              advance = false;
              break;
          }
        } else if (type == 5) {
          y = a_(g, ratchetandclank::strings[(size_t)(sid + (a->soundEnabled ? 0 : 1))],
                 y, bg == row + I);
        } else if (type == 6 && (levelStateMask & (1 << idx)) != 0) {
          y = a_(g, ratchetandclank::strings[(size_t)sid], y, bg == row + I);
        } else if (type == 8) {
          a_(g, q, true);
        } else if (type != 9) {
          advance = false;
        }
      } else {
        y = a_(g, ratchetandclank::strings[(size_t)(sid & 0xFF)], y, bg == row + I);
      }
      int rowsUsed = (y - rowY) / 10;
      if (rowsUsed > 1) M = rowsUsed - 1;
      if (advance) {
        y += 2;
        row++;
      }
      idx++;
    }
    if (++K > 6) {
      if (I > 0) {
        g->fillTriangle(117, top + 4 + 7, 122, top + 4, 127, top + 4 + 7, -265783);
        g->fillTriangle(1, top + 4 + 7, 6, top + 4, 11, top + 4 + 7, -265783);
      }
      if (I + (L - M) < pageCount) {
        g->fillTriangle(117, 108, 122, 115, 127, 108, -265783);
        g->fillTriangle(1, 108, 6, 115, 11, 108, -265783);
      }
      if (K > 24) K = 0;
    }
  }
}

// a(Graphics,Vector,boolean): scrollable text-screen renderer (help/about/
// credits/high-scores), with the scrollbar thumb on the right edge.
void Game::a_(Graphics* g, std::vector<String>* text, bool centered) {
  if (!text || text->empty()) return;
  int y = 5 + a->fontLineHeight + 4;
  g->setColor(16777215);
  if (m < l) {
    int barH = m * 10;
    int filled = m * barH / l;
    int barY = y + k * (barH - filled) / (l - m);
    g->drawRect(122, y, 3, barH);
    g->fillRect(122, barY, 3, filled);
  }
  for (int row = k; row < l && row < k + m; row++, y += 10) {
    const String& line = (*text)[(size_t)row];
    int x = !centered ? 2 : (118 - a->a_(line)) >> 1;
    a->a_(g, line, x, y, 20);
  }
}

// a(Vector,Vector): move every element of src into dst.
void Game::a_(std::vector<String>* dst, std::vector<String>* src) {
  while (!src->empty()) {
    dst->push_back(src->front());
    src->erase(src->begin());
  }
}

// u(): advance the 4 moving-platform slots (2 axes x 2 directions per
// slot, encoded as platformState 0..3: +x, +y, -x, -y) by 1px/tick, up to
// their 44/28px throw, then reverse.
void Game::u_() {
  for (int idx = 3; idx >= 0; idx--) {
    if (platformState[idx] == -1) continue;
    switch (platformState[idx]) {
      case 0:
        platformDx[idx]++;
        if (platformDx[idx] <= 44) continue;
        platformDx[idx]--;
        platformState[idx] += 2;
        break;
      case 1:
        platformDy[idx]++;
        if (platformDy[idx] <= 28) continue;
        platformDy[idx]--;
        platformState[idx] += 2;
        break;
      case 2:
        platformDx[idx]--;
        if (platformDx[idx] >= 0) continue;
        platformDx[idx]++;
        platformState[idx] -= 2;
        break;
      case 3:
        platformDy[idx]--;
        if (platformDy[idx] < 0) {
          platformDy[idx]++;
          platformState[idx] -= 2;
        }
        break;
      default:
        continue;
    }
  }
}

// ---- lifecycle --------------------------------------------------------------

// t(): random init (via z()) + the MIDlet-Spec-Code manifest check.
void Game::t_() {
  z_();
  // The port has no MIDlet manifest property lookup, and the canonical
  // build's manifest doesn't set MIDlet-Spec-Code either (only the three
  // BOSS_* tuning attributes -- see docs/ROADMAP.md), so `j` keeps its
  // default (false), matching the shipped game's actual behavior.
}

void Game::keyPressed(int rawKey) {
  if (rawKey == -10) return;  // SEND: unused
  if (splashHoldoff > 0) return;

  bool nameEntryDigit = (D == 115) && ((rawKey >= 48 && rawKey <= 57) || rawKey == 42 || rawKey == 0);
  if (!nameEntryDigit) {
    int navKey;
    if (rawKey == 50) navKey = -1;
    else if (rawKey == 56) navKey = -2;
    else if (rawKey == 52) navKey = -3;
    else if (rawKey == 54) navKey = -4;
    else if (rawKey == 53) navKey = -5;
    else navKey = (rawKey == -6 || rawKey == -7) ? 0 : rawKey;

    switch (b) {
      case 0: h_(rawKey, navKey); return;
      case 19: f_(rawKey, navKey); return;
      case 20: ac = 0; return;
    }
    return;
  }

  jlong now = currentTimeMillis();
  int digit = rawKey - 48;
  if (rawKey == 42 || rawKey == 0) {
    lastMultitapTime = now;
    multitapCycle = 0;
    lastMultitapKey = -1;
    if (length(E) > 0) E = substring(E, 0, length(E) - 1);
    return;
  }
  if (digit >= 0) {
    if ((lastMultitapKey != digit || now - lastMultitapTime > 1500) && length(E) < E_CAPACITY) {
      lastMultitapTime = now;
      multitapCycle = 0;
      lastMultitapKey = digit;
      E += (char)KEYPAD_CHARS[digit][0];
      return;
    }
    if (lastMultitapKey == digit && now - lastMultitapTime <= 1500) {
      lastMultitapTime = now;
      multitapCycle = (multitapCycle + 1) % KEYPAD_CHARS_LEN[digit];
      lastMultitapKey = digit;
      E[(size_t)(length(E) - 1)] = (char)KEYPAD_CHARS[digit][multitapCycle];
      return;
    }
    lastMultitapTime = 0;
    multitapCycle = 0;
    lastMultitapKey = -1;
  }
}

void Game::keyReleased(int rawKey) {
  if (rawKey == -10) return;
  if (splashHoldoff <= 0) i_();
}

void Game::paint(Graphics* g) {
  if (splashHoldoff > 0) splashHoldoff--;

  if (b == 19) {
    b_(g);
  } else if (b == 20) {
    if (af) {
      af = false;
      a_(g);
    }
  } else {
    if (by) player->invulnTimer = 1;

    N->a_(g);
    d_(g);
    f_(g);
    g_(g);
    e_(g);
    h_(g);
    if (Q == 12) i_(g);

    if (e) {
      c_(g);
      e = false;
    }

    if (player->actionState == -1) {
      g->setClip(59, 20, 11, 12);
      g->drawImage(hudIconImage, 59, -64, 0);
    }

    if (weaponOverlayOpen) {
      j_(g);
      messageChar = a_(g, messageScroll);
    }
  }
}

// a(int): Thread.sleep(ms) -- the port's loop is single-threaded, so this
// really does block the message pump; only used for a brief pause before a
// level-loading transition.
void Game::a_(int ms) { sleepMs(ms); }

void Game::b_() {  // showNotify
  if (isHidden) {
    isHidden = false;
    if (b == 20) af = true;
    else if (b == 19 && D != 118) repaintDelay = 4;
    hiddenSince = currentTimeMillis();
  }
}

void Game::a_() {  // hideNotify
  if (!isHidden && currentTimeMillis() - hiddenSince > 200) {
    isHidden = true;
    q_();
    if (a->soundPlayer) a->soundPlayer->stop();
    if (player) {
      player->velY = 0;
      player->jumpPhase = -1;
    }
    bh = 0;
    bi = 0;
  }
}

// c(): MIDP display.setCurrent(this) + new Thread(this).start() -- the port
// has no Display/thread; main() drives run_()/paint() itself each frame, so
// only the splash timer reset carries over.
void Game::c_() { ac = currentTimeMillis(); }

void Game::run_() {
  nowMs = currentTimeMillis();
  if (!isHidden) {
    if (nowMs - lastTickMs > 100) {
      v_();
      if (repaintEachFrame) repaint();
      lastTickMs = nowMs;
    }
    // else: throttled to ~10 ticks/sec -- lastTickMs is deliberately left
    // stale here, matching the original's callSerially re-check loop.
  } else {
    lastTickMs = nowMs;
  }
  // MIDP: display.callSerially(this) + Thread.yield() reschedule the next
  // tick on the platform's serial-repaint thread; the port's main loop just
  // calls run_() again next frame, so there's nothing to reschedule.
}

void Game::v_() {
  repaintEachFrame = true;
  if (repaintDelay > 0 && --repaintDelay == 0) {
    if (c && a->soundPlayer) {
      if (a->soundEnabled) a->soundPlayer->queue(6, -1);
      else a->soundPlayer->stop();
    }
    a->a_((jbyte)(a->soundEnabled ? 1 : 0), 0);
  }

  M_();
  if (b == 0 && !weaponOverlayOpen) {
    levelElapsedMs = (jint)(levelElapsedMs + (nowMs - lastTickMs));
    F_();
    u_();
    player->j_();
    player->k_();
    if (player->invulnTimer <= 0) K_();
    else player->invulnTimer--;
    w_();
    G_();

    for (int idx = 9; idx >= 0; idx--) {
      playerShots[idx]->b_(false);
      if (playerShots[idx]->type != -1) e_(idx);
    }
    for (int idx = 9; idx >= 0; idx--) {
      enemyShots[idx]->b_(true);
      if (enemyShots[idx]->type != -1) k_(idx);
    }
    for (int idx = MAX_ENEMIES - 1; idx >= 0; idx--) {
      if (enemies[idx]->kind != -1) {
        if (Q == 12 && (enemies[idx]->kind == 1 || enemies[idx]->kind == 4)) {
          if (enemies[idx]->B > 80) { enemies[idx]->kind = -1; continue; }
          enemies[idx]->B++;
        }
        if (enemies[idx]->animState != 5 && player->animState != 10) n_(idx);
        enemies[idx]->d_();
        enemies[idx]->e_();
      }
    }
    if (Q == 12) I_();
  }

  if (D == 114) return;
  if (D == 115) return;
  if (D == 117) {
    a_(E, 0);
    a_(E, 1);
    if (!ct) {
      String blank;
      String masked = E;
      if (length(E) > 7) masked = substring(E, 0, 7) + "^" + substring(E, 7, length(E));
      ct = new std::vector<String>();
      a_(ct, ratchetandclank::c_(ratchetandclank::strings[141], 94));
      a_(ct, ratchetandclank::c_(masked, 94));
      ct->push_back(blank);
      a_(ct, ratchetandclank::c_(ratchetandclank::strings[142], 94));
      cu = (int)ct->size();
      ct->push_back(blank);
      ct->push_back(blank);
      a_(ct, ratchetandclank::c_(ratchetandclank::strings[143], 94));
      cv = (int)ct->size();
      ct->push_back(blank);
      ct->push_back(blank);
      a_(ct, ratchetandclank::c_(ratchetandclank::strings[135], 94));
      l = (int)ct->size();
      k = 0;
      return;
    }
  } else if (D == 116) {
    if (!cs) {
      cs = new std::vector<String>();
      a_(cs, ratchetandclank::c_(ratchetandclank::strings[134], 120));
      cs->push_back("");
      a_(cs, ratchetandclank::c_(ratchetandclank::strings[135], 120));
      l = (int)cs->size();
      k = 0;
      return;
    }
  } else if (D == 119) {
    delete[] ratchetandclank::strings;
    ratchetandclank::strings = nullptr;
    q = p = o = n = nullptr;
    ratchetandclank::strings = ratchetandclank::a_(String("/m") + B[(size_t)C], 145);
    a->a_(C, 2);
    D = 114;
    m = (116 - (5 + a->fontLineHeight + 4)) / 10;
    if (a->soundEnabled && a->soundPlayer) a->soundPlayer->queue(6, -1);
  }
}

// a(String,int): hash the entered name into 8 digit-strip indices (2 bits ->
// one of 4 digit rows per position) for the high-score digit-strip display.
void Game::a_(const String& name, int slot) {
  String upper = toUpperCase(name);
  int hash = 0;
  int len = length(upper);
  for (int idx = 0; idx < len; idx++) {
    hash += (uint8_t)charAt(upper, idx) * (idx + 1);
  }
  int packed = hash * F[slot];
  for (int idx = 0; idx < 8; idx++) {
    int shift = idx * 4;
    int nibble = (packed & (15 << shift)) >> shift;
    G[slot][idx] = g_(nibble);
  }
}

int Game::g_(int v) {
  if (v >= 0 && v <= 3) return 0;
  if (v >= 4 && v <= 7) return 1;
  return (v >= 8 && v <= 11) ? 2 : 3;
}

// w(): camera -- follows the player horizontally (or a fixed look-at point
// while t is set) and the current grid's parallax band vertically, both
// eased by fixed 10px/tick steps and clamped to the level bounds.
void Game::w_() {
  int targetX;
  jshort px = player->b_();
  jshort py = player->c_();
  if (!t) {
    targetX = player->facingRight ? -(px - 22) : -(px + 22 - 128);
  } else {
    targetX = -(y - 64);
    if (px > y + 11 || px < y - 11 || player->c_() == player->a_(false)) t = false;
  }

  int dy = -(u ? (py + 22 - 128 + 15) : (py + 22 + 28 - 128 - 15)) - s;
  s += dy >> 1;
  if (r > targetX) {
    r -= 10;
    if (r < targetX) r = targetX;
  } else if (r < targetX) {
    r += 10;
    if (r > targetX) r = targetX;
  }

  if (r > 0) r = 0;
  if (r < -488) r = -488;
  if (s > 0) s = 0;
  if (s < -124) s = -124;
}

Image* Game::b_(const String& name) { return Image::createImage("/" + name + ".png"); }

void Game::x_() {
  H.clear();
  if (!tileSetImage) tileSetImage = b_("a");
  if (!actorImage) actorImage = b_("d");
  if (!enemySegmentImage) enemySegmentImage = b_("b");
  if (!playerImage) playerImage = b_("c");
  if (!titaniumBoltImage) titaniumBoltImage = b_("e");
  if (!weaponImage) weaponImage = b_("f");
  if (!hudIconImage) hudIconImage = b_("g");
  if (!portraitImage) portraitImage = b_("p");
  if (!smallSpriteImage) smallSpriteImage = b_("h");
}

void Game::y_() {
  tileSetImage = enemySegmentImage = playerImage = actorImage = nullptr;
  titaniumBoltImage = weaponImage = hudIconImage = portraitImage = smallSpriteImage = nullptr;
  gc();
  a_(String("/o"));
}

// z(): allocate the level map + player/enemy/projectile pools. The port's
// arrays (bossShellState, zip*, titaniumBolt*, pickup*, platform*, ...) are
// fixed-size members of Game already (see game.h), so unlike the Java
// `new byte[N]` calls there's nothing to allocate for those.
void Game::z_() {
  // Java re-seeds `this.O` with a fresh `new Random()` (system-time-based)
  // every time z() runs; the port's JRandom is a value member already
  // constructed with a fixed seed, so reseed it from the clock here instead.
  O.setSeed((uint64_t)currentTimeMillis());
  N = new LevelMap(this);
  player = new Player(this);
  player->a_();
  for (int idx = MAX_ENEMIES - 1; idx >= 0; idx--) enemies[idx] = new Enemy(this);
  enemies[0]->a_();
  for (int idx = 0; idx < 10; idx++) playerShots[idx] = new Projectile(this);
  for (int idx = 0; idx < 10; idx++) enemyShots[idx] = new Projectile(this);
  m = (116 - (5 + a->fontLineHeight + 4)) / 10;
}

// h(level): enter a level coming from the results screen (checkpoint/spawn
// state is whatever LevelMap::a_(level) leaves in R/S/T/U -- 3.2 territory).
void Game::h_(int level) {
  x_();
  player->actionState = -2;
  by = false;
  A_();
  Q = (jbyte)level;
  N->a_(level);
  P = LevelMap::a[(size_t)Q];
  N->a_(P, true);
  player->posX = (R * 22 + 11) << 8;
  player->row = player->spawnRow = S;
  player->animFrame = 1;
  player->animCounter = 0;
  player->animState = 1;
  player->animRestart = 0;
  player->posInRow = 0;
  player->kind = 0;
  player->activeFlag = 1;
  player->health = 20;
  levelElapsedMs = 0;
  lastTickMs = currentTimeMillis();
  f_(P);
  player->invulnTimer = 0;
  player->facingRight = true;
  boltCount = 0;
  player->setAnimState(0);
  player->animRestart = 0;
  r = T;
  s = U;
  e = true;
  b = 19;
}

// A(): reset all new-game state (weapons, collectibles, scoring).
void Game::A_() {
  k_();
  area1BoltsTaken = -1;
  area2BoltsTaken = -1;
  collectiblesTaken1 = -1;
  introSeenMask = -1;
  collectiblesTaken2 = -1;
  levelStateMask = 2049;
  player->ownedWeapons = 3;
  player->currentWeapon = 1;
  for (int idx = 7; idx >= 0; idx--) {
    player->weaponXp[idx] = 0;
    player->weaponLevel[idx] = 0;
    player->ammo[idx] = Player::e[idx];
  }
  c = false;
  if (a->soundPlayer) a->soundPlayer->stop();
}

// i(level): enter a level starting fresh (continue-game / new-game path).
void Game::i_(int level) {
  x_();
  player->actionState = -2;
  by = false;
  Q = (jbyte)level;
  N->a_(level);
  player->facingRight = true;
  P = LevelMap::a[(size_t)Q];
  player->invulnTimer = 10;
  f_(P);
  N->a_(P, true);
  if (Q == 12) J_();
  player->posX = (R * 22 + 11) << 8;
  player->row = player->spawnRow = S;
  player->animFrame = 1;
  player->animCounter = 0;
  player->animState = 1;
  player->animRestart = 0;
  player->posInRow = 0;
  player->kind = 0;
  player->activeFlag = 1;
  player->health = 20;
  lastTickMs = currentTimeMillis();
  e = true;
  r = T;
  s = U;
  k_();
  b = 0;
  w_();
}

// B(): count titanium bolts not yet taken across both collectible areas.
int Game::B_() {
  int count = 0;
  for (int idx = (TITANIUM_BOLT_SLOTS >> 1) - 1; idx >= 0; idx--) {
    if ((area1BoltsTaken & (1 << idx)) == 0) count++;
    if ((area2BoltsTaken & (1 << idx)) == 0) count++;
  }
  return count;
}

// g(area,idx): mark a titanium bolt taken; unlocks the RYNO once enough are
// collected.
void Game::g_(int area, int idx) {
  if (area > 0 && area <= 5) {
    area1BoltsTaken &= ~(1 << ((area - 1) * 6 + idx));
  } else if (area >= 6 && area <= 10) {
    area2BoltsTaken &= ~(1 << ((area - 6) * 6 + idx));
  }
  if (B_() >= TITANIUM_BOLTS_FOR_RYNO && (player->ownedWeapons & 128) == 0) {
    e_(72, -1);
    player->ownedWeapons = (jbyte)(player->ownedWeapons | 128);
  } else {
    e_(74, -1);
  }
}

// d(): respawn the player at the level's checkpoint grid (V/R/S/T/U), reset
// the boss shells if mid-boss-fight.
void Game::d_() {
  if (a->soundEnabled && a->soundPlayer) a->soundPlayer->queue(0, 1);
  player->actionState = -2;
  P = V;
  f_(P);
  N->a_(V, false);
  player->invulnTimer = 10;
  player->posX = (R * 22 + 11) << 8;
  player->row = S;
  r = T;
  s = U;
  player->posInRow = 0;
  player->health = 20;
  player->velX = player->velY = 0;
  player->setAnimState(0);
  player->animRestart = 0;
  e = true;
  if (Q == 12) {
    bossShellTimer[0] = bossShellTimer[1] = bossShellTimer[2] = bossShellTimer[3] = 0;
    bossShellState[0] = bossShellState[1] = bossShellState[2] = bossShellState[3] = 0;
    bossHp[0] = bossHp[1] = bossHp[2] = bossHp[3] = f;
    bossHp[4] = g;
  }
}

// a(byte[]): serialize the running game into a 220-byte save slot.
void Game::a_(jbyte* out) {
  out[0] = 1;
  ratchetandclank::a_(levelStateMask, out, 1);
  ratchetandclank::a_(area1BoltsTaken, out, 5);
  ratchetandclank::a_(area2BoltsTaken, out, 9);
  ratchetandclank::a_(collectiblesTaken1, out, 13);
  out[17] = player->ownedWeapons;
  for (int idx = 0; idx < 8; idx++) {
    out[18 + idx] = player->weaponLevel[idx];
    ratchetandclank::a_(player->weaponXp[idx], out, 26 + idx * 2);
    ratchetandclank::a_(player->ammo[idx], out, 42 + idx * 2);
  }
  ratchetandclank::a_(boltCount, out, 58);
  ratchetandclank::a_(levelElapsedMs, out, 195);
  ratchetandclank::a_(collectiblesTaken2, out, 204);
  ratchetandclank::a_(totalTitaniumBolts, out, 208);
  ratchetandclank::a_(introSeenMask, out, 212);
  ratchetandclank::a_(totalKills, out, 216);
}

// b(byte[]): deserialize a save slot into the running game.
void Game::b_(jbyte* in) {
  levelStateMask = ratchetandclank::a_(in, 1);
  area1BoltsTaken = ratchetandclank::a_(in, 5);
  area2BoltsTaken = ratchetandclank::a_(in, 9);
  collectiblesTaken1 = ratchetandclank::a_(in, 13);
  player->ownedWeapons = in[17];
  for (int idx = 0; idx < 8; idx++) {
    player->weaponLevel[idx] = in[18 + idx];
    player->weaponXp[idx] = ratchetandclank::b_(in, 26 + idx * 2);
    player->ammo[idx] = ratchetandclank::b_(in, 42 + idx * 2);
  }
  boltCount = ratchetandclank::a_(in, 58);
  levelElapsedMs = ratchetandclank::a_(in, 195);
  collectiblesTaken2 = ratchetandclank::a_(in, 204);
  totalTitaniumBolts = ratchetandclank::a_(in, 208);
  introSeenMask = ratchetandclank::a_(in, 212);
  totalKills = ratchetandclank::a_(in, 216);
  cr = bg = 0;
}

// j(): on entering a level, show its one-time intro infolink message (if
// any) unless it's one of the levels that skip it.
void Game::j_() {
  scoringActive = false;
  if (Q <= 10) {
    const String& s = ratchetandclank::strings[112 + (Q - 1)];
    if (!s.empty()) {
      if ((introSeenMask & (1 << (Q - 1))) > 0) {
        introSeenMask &= ~(1 << (Q - 1));
        if (Q != 7 && Q != 9 && Q != 10 && Q != 12) {
          e_(112 + (Q - 1), Q - 1);
        }
      }
    }
  }
}

// e(stringId,portrait): open the message overlay (portrait dialogue or a
// plain infolink/pickup toast).
void Game::e_(int stringId, int portrait) {
  player->invulnTimer = 0;
  if (a->soundEnabled && a->soundPlayer) a->soundPlayer->queue(2, 1);
  messageScroll = 0;
  messagePortrait = portrait;
  messageStringId = stringId;
  String text;
  if (stringId == 74) {
    text = ratchetandclank::strings[74] + " " + valueOf(B_());
  } else if (messagePortrait >= 0) {
    messageTimer = (jbyte)((int)PORTRAIT_TABLE[(size_t)messagePortrait].size() >> 1);
    messagePortraitFrame = 1;
    text = ratchetandclank::strings[(size_t)PORTRAIT_TABLE[(size_t)messagePortrait][(size_t)messagePortraitFrame]];
  } else {
    text = ratchetandclank::strings[(size_t)stringId];
  }
  messageText = text;
  if (messageText.empty()) messageScroll = -1;
  weaponOverlayOpen = true;
  bj = 0;
}

// L(): advance the message overlay (portrait dialogue steps through
// PORTRAIT_TABLE; infolink toasts step through consecutive string ids), or
// close it and mark the level/boss-intro seen once it's done.
void Game::L_() {
  weaponOverlayOpen = false;
  if (messagePortrait >= 0) {
    if (--messageTimer > 0) {
      player->invulnTimer = 0;
      messageScroll = 0;
      weaponOverlayOpen = true;
      messagePortraitFrame += 2;
      messageText = ratchetandclank::strings[(size_t)PORTRAIT_TABLE[(size_t)messagePortrait][(size_t)messagePortraitFrame]];
      return;
    }
    if ((Q != 3 && Q != 10) || messageStringId < INFOLINK_MESSAGE_IDS[0] ||
        messageStringId > INFOLINK_MESSAGE_IDS[15] + INFOLINK_MESSAGE_TICKS[15]) {
      if (Q == 12) { s_(); return; }
      return;
    }
  } else {
    if (--messageTimer > 0) {
      e_(messageStringId + 1, messagePortrait);
      return;
    }
    if (messageStringId < INFOLINK_MESSAGE_IDS[0] ||
        messageStringId > INFOLINK_MESSAGE_IDS[15] + INFOLINK_MESSAGE_TICKS[15]) {
      return;
    }
  }
  levelStateMask |= (1 << Q);
  p_();
}

// M(): per-tick housekeeping outside the main gameplay step -- charges the
// current weapon's held-fire meter in gameplay mode, and advances the
// boot-time splash chain (sony -> hhg -> logo -> language/menu) otherwise.
void Game::M_() {
  if (b == 0) {
    int charge = bj & 63;
    jbyte level = player->weaponLevel[(size_t)player->currentWeapon];
    if (charge > 0) {
      bj++;
      if (charge >= Player::g[(size_t)level][(size_t)player->currentWeapon]) {
        if ((bj & 128) == 0) {
          bj = 0;
          return;
        }
        bj = 129;
        if (player->m_()) {
          if (a->soundEnabled && a->soundPlayer) a->soundPlayer->queue(5, 1);
          shotsFired++;
          return;
        }
      }
    }
  } else if (b == 20 && ad < 3) {
    if (!d) N_();
    if (ac + 2000 < currentTimeMillis()) {
      ac = currentTimeMillis();
      if (ad == 0) splashImage = b_("hhg");
      else if (ad == 1) splashImage = b_("logo");
      ad++;
      af = true;
      if (ad == 3) {
        af = false;
        c = true;
        b = 19;
        if (a->e_(2) == -1) {
          D = 118;
          k = 0;
          l = (int)(sizeof(A) / sizeof(A[0])) - 1;
          m = 0;
          return;
        }
        C = (jbyte)Math::min(a->e_(2), (int)(sizeof(A) / sizeof(A[0])) - 1);
        ratchetandclank::strings = ratchetandclank::a_(String("/m") + B[(size_t)C], 145);
        if (a->soundEnabled && a->soundPlayer) a->soundPlayer->queue(6, -1);
      }
    }
  }
}

// N(): boot init -- font, save-record verification, settings, sounds, string
// table. Runs once, from the first M_() call while the splash is up.
void Game::N_() {
  a->a_("/f2.v", 10, 1);
  a->c_();
  a->e_();
  a->soundPlayer = new SoundPlayer();
  a->soundPlayer->loadAll();
  a->soundEnabled = a->e_(0) > 0;
  t_();
  o_();
  d = true;
  ratchetandclank::strings = ratchetandclank::a_("/m", 145);
}

// O(): total titanium-bolt price to fully restock every owned weapon's ammo
// at the store; also fills storeAmmoNeeded per-weapon for the store screen.
int Game::O_() {
  int total = 0;
  for (int w = 0; w < 8; w++) {
    int needed = Player::d[(size_t)(w * 3 + player->weaponLevel[w])] - player->ammo[w];
    switch (w) {
      case 0:
      case 6:
        storeAmmoNeeded[w] = 0;
        break;
      case 1:
        total += needed >> 1;
        storeAmmoNeeded[w] = needed >> 1;
        break;
      case 2:
        if ((player->ownedWeapons & (1 << w)) <= 0) continue;
        total += needed >> 1;
        storeAmmoNeeded[w] = needed >> 1;
        break;
      case 3:
      case 4:
      case 5:
      case 7:
        if ((player->ownedWeapons & (1 << w)) > 0) {
          total += needed;
          storeAmmoNeeded[w] = needed;
        }
        break;
      default:
        continue;
    }
  }
  return total;
}

// f(rawKey,navKey): menu input -- confirm/back navigation, row scrolling,
// page transitions and the store/save/continue/new-game/language actions.
// Ported 1:1 from the decompile's label399/label419 control flow: the three
// "commit a cursor move and stop" exits (bg++ / bg-- with I>0 / bg-- landing
// on I=0) become direct `I = ...; return;` instead of the original's labeled
// breaks, since every other path already ends in an explicit return.
void Game::f_(int rawKey, int navKey) {
  jbyte type0 = 0;
  jbyte action = -1;
  bool enabled = true;
  if (ca > 0) ca--;

  const std::vector<MenuItem>* page = &H[(size_t)J];
  int count = (int)page->size();
  if (J == 17 && (player->ownedWeapons & 128) == 0) count--;

  if (count != 0) {
    type0 = (*page)[0].type;
    enabled = (*page)[(size_t)bg].enabled;
    action = (*page)[(size_t)bg].action;
  }

  if (J == 8) count = n_();

  if ((navKey == -5 || rawKey == -6) && enabled) {
    if (action != -1) {
      if (D != 114) {
        if (D == 117) {
          D = 114;
          cs = ct = nullptr;
          cr = bg = 0;
          I = 0;
          return;
        }
        if (D == 115) {
          if (length(E) < 4) {
            nameEntryWarning = ratchetandclank::strings[137];
            return;
          }
          nameEntryWarning.clear();
          D = 117;
          return;
        }
        if (D != 118 && D != 120) return;
        C = (jbyte)k;
        D = 119;
        return;
      }

      if (action == 0) {
        weaponOverlayOpen = false;
      } else if (action == -105) {
        D = (a->e_(1) != 0) ? 115 : 116;
        return;
      } else if (action == -104) {
        l = (int)(sizeof(A) / sizeof(A[0])) - 1;
        m = 0;
        k = 0;
        D = 120;
        return;
      } else if (action == 127) {
        q = p = o = n = nullptr;
        gc();
        a_(20);
        h_(1);
        e = true;
        b = 0;
        w_();
        return;
      } else if (action == 126) {
        a->a_();
        return;
      } else if (action == 125) {
        if (a->saveSlotFlags[bg] == 0) return;
        q = p = o = n = nullptr;
        gc();
        a_(20);
        A_();
        a->b_(bg);
        b = 19;
        action = 8;
      } else if (action == 124) {
        if ((player->ownedWeapons & (1 << (cr + 1))) == 0) {
          action = 19;
          if (boltCount >= Player::h[cr + 1]) {
            boltCount -= Player::h[cr + 1];
            player->ownedWeapons = (jbyte)(player->ownedWeapons | (1 << (cr + 1)));
            player->ammo[cr + 1] = Player::e[cr + 1];
            action = 17;
          }
        } else {
          action = 19;
          if (boltCount >= storeAmmoNeeded[cr + 1]) {
            boltCount -= storeAmmoNeeded[cr + 1];
            player->ammo[cr + 1] = Player::d[(cr + 1) * 3 + player->weaponLevel[cr + 1]];
            action = 17;
          }
        }
      } else if (action == 123) {
        a->c_(cr);
        action = menuBackTarget[J];
      } else if (action == 122) {
        x_();
        e = true;
        b = 0;
        w_();
        return;
      } else if (action == 121) {
        if (a->saveSlotFlags[bg] == 0) {
          a->a_(bg);
          a->b_();
          action = 12;
        } else {
          action = 11;
        }
      } else if (action == 120) {
        a->a_(cr);
        a->b_();
        action = 12;
      } else if (action == 119) {
        int idx = 0;
        for (int rem = bg; rem >= 0; idx++) {
          if ((levelStateMask & (1 << idx)) != 0) rem--;
        }
        if (idx != 12) {
          if (idx == 11) idx++;
          splashHoldoff = 5;
          i_(idx);
          e = true;
          b = 0;
          w_();
          return;
        }
        action = (*page)[(size_t)(idx - 1)].action;
      }

      if (action != -1 && (*page)[(size_t)bg].enabled) {
        if (action == 4 && J == 3 && type0 == 1 && a->saveSlotFlags[bg] == 0) return;

        J = action;
        cr = bg;
        bg = 0;
        I = 0;
        page = &H[(size_t)J];
        if (!page->empty()) {
          type0 = (*page)[0].type;
          if ((*page)[0].type == 0 && J == 0) {
            if (!c) {
              c = true;
              if (a->soundEnabled && a->soundPlayer) a->soundPlayer->queue(6, -1);
            }
          } else if (type0 >= 1 && type0 <= 3) {
            a->b_();
          } else if (type0 == 4) {
            std::vector<String>* text = nullptr;
            switch ((*page)[0].stringId) {
              case 96:
                if (!o) o = ratchetandclank::b_("/help", 118);
                text = o;
                break;
              case 97:
                if (!n) {
                  n = ratchetandclank::b_("/about", 118);
                  // No MIDlet-Version manifest property in the port; the
                  // canonical build ships as v1.0.9 (docs/ROADMAP.md).
                  if (n) {
                    for (int idx2 = 0; idx2 < (int)n->size(); idx2++) {
                      String& line = (*n)[(size_t)idx2];
                      size_t pos = line.find("??");
                      if (pos != String::npos) {
                        line = line.substr(0, pos) + "1.0.9";
                        break;
                      }
                    }
                  }
                }
                text = n;
                break;
              case 98:
                if (!p) p = ratchetandclank::b_("/credits", 118);
                text = p;
                break;
              default:
                break;
            }
            if (text) { l = (int)text->size(); k = 0; }
          }
          if (J == 17) { O_(); return; }
          return;
        }
      }
      return;
    }

    if (type0 == 5 && (J == 2 || J == 13)) {
      a->soundEnabled = !a->soundEnabled;
      repaintDelay = 5;
      return;
    }

    if (navKey == -5 && type0 == 8 && J == 21) {
      J = 8;
      cr = bg = 0;
    } else if (navKey == -5 && J == 12) {
      J = 10;
      cr = bg = 0;
    } else {
      if (navKey != -5 || J != 20 || ca != 0) return;
      q = nullptr;
      J = 0;
      if (!c) {
        c = true;
        if (a->soundEnabled && a->soundPlayer) a->soundPlayer->queue(6, -1);
      }
      cr = bg = 0;
    }
    I = 0;
    return;
  }

  // -- navigation (up/down/back) --------------------------------------------
  if (rawKey != -7 && rawKey != 0) {
    if (navKey == -2) {
      if (type0 != 4 && type0 != 8 && type0 != 9 && D == 114) {
        if (bg >= count - 1) return;
        bg++;
        I = (bg >= I + (L - M)) ? I + 1 : 0;
        return;
      }
      if (k + m < l) k++;
      return;
    }
    if (navKey != -1) return;
    if (type0 != 4 && type0 != 8 && type0 != 9 && D == 114) {
      if (bg <= 0) return;
      bg--;
      if (I > 0) { I = I - 1; return; }
      I = 0;
      return;
    }
    if (k > 0) k--;
    return;
  }

  // rawKey is -7 (right soft key) or 0 (back): the "cancel/back" path.
  if (D != 114) {
    if (D == 116) { D = 114; cs = ct = nullptr; cr = bg = 0; I = 0; return; }
    if (D == 115) { cr = bg = 0; I = 0; nameEntryWarning.clear(); D = 114; return; }
    if (D == 117) { ct = nullptr; D = 115; return; }
    if (D == 120) {
      D = 114;
      cr = bg = 0;
      I = 0;
      m = (116 - (5 + a->fontLineHeight + 4)) / 10;
    }
    return;
  }
  if (menuBackTarget[J] == 122) {
    x_();
    e = true;
    b = 0;
    w_();
    return;
  }
  if (menuBackTarget[J] != -1) {
    if (J == 19) {
      J = 17;
    } else {
      J = menuBackTarget[J];
      if (J == 0 && !c) {
        c = true;
        if (a->soundEnabled && a->soundPlayer) a->soundPlayer->queue(6, -1);
      }
    }
    cr = bg = 0;
    I = 0;
    page = &H[(size_t)J];
    if (!page->empty()) {
      type0 = (*page)[0].type;
      if ((*page)[0].type >= 1 && type0 <= 3) { a->b_(); return; }
      return;
    }
  }
}

// ---- gameplay internals (milestone 3.2 stubs) ------------------------------
//
// Everything below touches the b==0 (in-level) simulation: physics/AI ticks,
// collision, the boss fight and the gameplay HUD/render passes. Declared in
// game.h so the boot/menu/save surface above links and runs; filled in
// milestone 3.2 alongside LevelMap/Player/Enemy/Projectile's own stubs.

void Game::h_(int, int) {}     // gameplay input
void Game::e_(int) {}          // player-shot tick
void Game::k_(int) {}          // enemy-shot tick
void Game::F_() {}
void Game::G_() {}
void Game::I_() {}             // boss tick
void Game::J_() {}             // boss setup
void Game::K_() {}             // enemy-vs-player tick
void Game::n_(int) {}          // enemy AI tick

// i(): fire-button release -- clears the charge-shot state and releases the
// player's held attack. Small enough to port now rather than stub, and
// keyReleased (already ported above) needs it.
void Game::i_() {
  bj &= ~128;
  if (player->animState != 8) player->o_();
  bh = 0;
  bi = 0;
}

void Game::c_(Graphics*) {}    // overlay/level-transition draw
void Game::d_(Graphics*) {}
void Game::f_(Graphics*) {}
void Game::g_(Graphics*) {}
void Game::e_(Graphics*) {}
void Game::h_(Graphics*) {}
void Game::i_(Graphics*) {}    // boss render
void Game::j_(Graphics*) {}    // message overlay render
int  Game::a_(Graphics*, int) { return -1; }  // message scroll render
void Game::a_(Graphics*, int, int) {}
void Game::a_(Graphics*, int, int, int) {}
bool Game::c_(int) { return false; }
bool Game::a_(int, int) { return false; }
short Game::e_() { return 0; }
short Game::f_() { return 0; }
bool Game::g_() { return false; }
bool Game::d_(int) { return false; }
bool Game::D_() { return false; }
bool Game::E_() { return false; }
int  Game::h_() { return 0; }
int  Game::b_(int, int) { return 0; }
void Game::c_(int, int) {}     // boss-shell hit at a pixel position
