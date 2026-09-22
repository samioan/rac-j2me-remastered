// midlet.cpp -- port of goingmobile/src/ratchetandclank.java.
//
// Naming policy for the C++ port: Java allowed a field and a method to share
// one obfuscated letter; C++ does not, so every obfuscated method keeps its
// letter with a trailing underscore (a(), b(), c()... -> a_(), b_(), c_()).
// Renamed members (from docs/CLASS_MAP.md) keep their new names.
#include "midlet.h"
#include "game.h"

Game* ratchetandclank::b = nullptr;
String* ratchetandclank::strings = nullptr;
std::vector<String>* ratchetandclank::i = nullptr;
int ratchetandclank::j = 0;
int ratchetandclank::k = 0;
int ratchetandclank::l = 0;
int ratchetandclank::m = 0;
String ratchetandclank::n;
const jint ratchetandclank::SAVE_RECORD_IDS[4] = {1, 2, 3, 4};

void ratchetandclank::startApp() {
  if (!a) {
    a = true;
    // The manifest tuning attributes (BOSS_HIT_POINT_SHELL=35,
    // BOSS_HIT_POINT_KERNEL=100, BOSS_FIRE_RATE=22) -- the port bakes in the
    // shipped values; Game's statics already hold these defaults.
    b = new Game(this);
    b->c_();
  }
  // pauseApp/resumeApp path (b.b()) is not applicable on the PC port.
}

// quit: stop the sound thread and destroy.
void ratchetandclank::a_() {
  if (soundPlayer != nullptr) {
    soundPlayer->stop();
    soundPlayer->threadAlive = false;
  }
}

// Refresh the save-slot summary (in-use flag + elapsed time) for the menus.
void ratchetandclank::b_() {
  for (int slot = 0; slot < 3; slot++) {
    d_(slot);
    saveSlotFlags[slot] = saveBuffer[0];
    saveSlotTimes[slot] = a_(saveBuffer, 195);
  }
}

// ---- byte-order helpers --------------------------------------------------

void ratchetandclank::a_(int v, jbyte* buf, int off) {
  buf[0 + off] = (jbyte)((v >> 24) & 0xFF);
  buf[1 + off] = (jbyte)((v >> 16) & 0xFF);
  buf[2 + off] = (jbyte)((v >> 8) & 0xFF);
  buf[3 + off] = (jbyte)(v & 0xFF);
}

int ratchetandclank::a_(const jbyte* buf, int off) {
  return ((buf[off] & 0xFF) << 24) | ((buf[1 + off] & 0xFF) << 16) |
         ((buf[2 + off] & 0xFF) << 8) | (buf[3 + off] & 0xFF);
}

void ratchetandclank::a_(jshort v, jbyte* buf, int off) {
  buf[off++] = (jbyte)(v >> 8);
  buf[off] = (jbyte)v;
}

jshort ratchetandclank::b_(const jbyte* buf, int off) {
  return (jshort)(((buf[off] & 0xFF) << 8) | (buf[1 + off] & 0xFF));
}

// ---- save records ---------------------------------------------------------

// verify the record store; rebuild it if anything is off
void ratchetandclank::c_() {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", true);
  if (!rs) { d_(); return; }
  if (rs->getNumRecords() < 4) {
    delete rs;
    d_();
  } else {
    for (int idx = 0; idx < 4; idx++) {
      if ((idx < 3 && rs->getRecordSize(SAVE_RECORD_IDS[idx]) != 220) ||
          (idx == 3 && rs->getRecordSize(SAVE_RECORD_IDS[3]) != 3)) {
        delete rs;
        d_();
        return;
      }
    }
    delete rs;
  }
}

// (re)create the record store: 3 x 220-byte slots + 3-byte settings
void ratchetandclank::d_() {
  RecordStore::deleteRecordStore("RANDCSm");
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", true);
  if (!rs) return;
  for (int slot = 0; slot < 3; slot++) rs->addRecord(saveBuffer, 0, 220);
  settings[0] = 1;
  settings[1] = 0;
  settings[2] = -1;
  rs->addRecord(settings, 0, 3);
  delete rs;
}

// serialize the running game into the slot
void ratchetandclank::a_(int slot) {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
  if (!rs) return;
  b->a_(saveBuffer);  // Game.a(byte[]): serialize
  rs->setRecord(SAVE_RECORD_IDS[slot], saveBuffer, 0, 220);
  delete rs;
}

// read a slot and deserialize
void ratchetandclank::b_(int slot) {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
  if (!rs) return;
  rs->getRecord(SAVE_RECORD_IDS[slot], saveBuffer, 0);
  delete rs;
  b->b_(saveBuffer);  // Game.b(byte[]): deserialize
}

// clear a slot
void ratchetandclank::c_(int slot) {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
  if (!rs) return;
  for (int i = 0; i < 220; i++) saveBuffer[i] = 0;
  rs->setRecord(SAVE_RECORD_IDS[slot], saveBuffer, 0, 220);
  delete rs;
}

// raw read of a slot into saveBuffer
void ratchetandclank::d_(int slot) {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
  if (!rs) return;
  rs->getRecord(SAVE_RECORD_IDS[slot], saveBuffer, 0);
  delete rs;
}

// read the settings record
void ratchetandclank::e_() {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
  if (!rs) return;
  rs->getRecord(SAVE_RECORD_IDS[3], settings, 0);
  delete rs;
}

jbyte ratchetandclank::e_(int idx) { return settings[idx]; }

void ratchetandclank::a_(jbyte value, int idx) {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
  if (!rs) return;
  settings[idx] = value;
  rs->setRecord(SAVE_RECORD_IDS[3], settings, 0, 3);
  delete rs;
}

// ---- text loading --------------------------------------------------------

// a(String, int): read `count` lines from the resource; skip \r, end on \n,
// trim each line. (One byte == one char: the original casts raw bytes.)
String* ratchetandclank::a_(const String& path, int count) {
  ByteArray data = loadResource(path + ".txt");
  if (data.empty()) return nullptr;
  String* out = new String[count]();
  int pos = 0;
  for (int idx = 0; idx < count; idx++) {
    String line;
    while (pos < (int)data.size()) {
      char c = (char)(uint8_t)data[(size_t)pos++];
      if (c == '\r') continue;
      if (c == '\n') break;
      line += c;
    }
    out[idx] = trim(line);
  }
  return out;
}

// the word-wrap state machine (one input char per call)
void ratchetandclank::a_(char c, int width) {
  bool flush = false;
  if (c == ' ') {
    l = k;
    m = m + Game::a->a_(c);
    n += c;
  } else {
    if (c == '^') {
      l = k;
      return;
    }
    if (c == '\n') {
      flush = true;
    } else {
      m = m + Game::a->a_(c);
      n += c;
    }
  }
  if (!flush) k++;

  if (m >= width || flush) {
    if (m >= width) k = l;
    if (m == 0) {
      i->push_back("");
    } else {
      i->push_back(substring(n, j, k));
    }
    if (m >= width) {
      // Java: n.delete(j, k+1) / n.delete(j, k) -- consume [j, k)
      if (charAt(n, k) == ' ') {
        n = substring(n, 0, j) + substring(n, k + 1, length(n));
      } else {
        n = substring(n, 0, j) + substring(n, k, length(n));
      }
      j = 0; l = 0;
      k = length(n);
      m = Game::a->a_(n);
    } else {
      n.clear();
      j = 0; l = 0; k = 0;
      m = 0;
    }
  }
}

// b(String, int): word-wrap a text resource
std::vector<String>* ratchetandclank::b_(const String& path, int width) {
  m = 0; l = 0; k = 0; j = 0;
  i = new std::vector<String>();
  n.clear();
  String p = path + Game::B[Game::C] + ".txt";
  if (!resourceExists(p)) return nullptr;
  ByteArray data = loadResource(p);
  for (int idx = 0; idx < (int)data.size(); idx++) {
    char c = (char)(uint8_t)data[(size_t)idx];
    if (c != '\r') a_(c, width);
  }
  std::vector<String>* out = i;
  i = nullptr;
  n.clear();
  return out;
}

// c(String, int): word-wrap an in-memory string ('\u00A6' = line break)
std::vector<String>* ratchetandclank::c_(const String& text, int width) {
  m = 0; l = 0; k = 0; j = 0;
  i = new std::vector<String>();
  n.clear();
  for (int idx = 0; idx <= length(text); idx++) {
    char c;
    if (idx >= length(text)) {
      c = '\n';
    } else {
      c = charAt(text, idx);
      if ((uint8_t)c == 166) c = '\n';
    }
    a_(c, width);
  }
  std::vector<String>* out = i;
  i = nullptr;
  n.clear();
  return out;
}

// ---- font ----------------------------------------------------------------

// a(String, int, int): the f2.v bitmap-font loader -- 192 glyph records,
// each: header byte (advance << 4 | run count), then big-endian shorts that
// pack x/y/w/h as 4-bit fields. The args are the line height and spacing.
void ratchetandclank::a_(const String& path, int lineHeight, int spacing) {
  ByteArray data = loadResource(path);
  if (data.empty()) return;
  fontGlyphs.assign(192, {});
  int pos = 0;
  for (int glyph = 0; glyph < 192; glyph++) {
    jbyte hdr = (jbyte)(uint8_t)data[(size_t)pos++];
    fontGlyphs[(size_t)glyph].resize((size_t)((hdr & 0x0F) + 1));
    fontGlyphs[(size_t)glyph][0] = (jshort)((hdr & 0xF0) >> 4);
    for (int run = 0; run < (hdr & 0x0F); run++) {
      fontGlyphs[(size_t)glyph][(size_t)run + 1] =
          (jshort)(((jbyte)(uint8_t)data[(size_t)pos++] & 0xFF) << 8 |
                   (uint8_t)data[(size_t)pos++] & 0xFF);
    }
  }
  fontLineHeight = lineHeight;
  fontSpacing = spacing;
}

// char-at-position(idx) of either a char[] (charsLen > 0) or the string,
// mapped straight to its glyph index (< 32 and 128..159 map to glyph 0). When
// neither source is present, idx is the raw char code itself (see the public
// a_(char) overload below).
int ratchetandclank::a_(const char* chars, int charsLen, const String& s, int idx) {
  int c;
  if (chars != nullptr && charsLen > 0) c = (uint8_t)chars[idx];
  else if (length(s) > 0) c = (uint8_t)s[(size_t)idx];
  else c = idx;

  if (c < 32) return 0;
  if (c < 128) return c - 32;
  if (c < 160) return 0;
  return c < 256 ? c - 64 : 0;
}

// the glyph-run renderer (font fillRect runs)
void ratchetandclank::drawRun(Graphics* g, const char* chars, int charsLen,
                               const String& s, int off, int len,
                               int x, int y, int anchor) {
  if (len < 0) {
    if (charsLen > 0) {
      len = charsLen - off;
    } else {
      if (length(s) == 0) return;
      len = length(s) - off;
    }
  }
  // vertical anchor bits
  switch (anchor & 114) {
    case 2:  // VCENTER
      y -= fontLineHeight / 2;
      break;
    case 32:  // BOTTOM
      y -= fontLineHeight;
      break;
    default:
      break;
  }
  switch (anchor & 13) {
    case 1: {  // HCENTER: measure, adjust x, then draw (Java fallthrough)
      int w = 0;
      for (int p = off; p < off + len; p++)
        w += fontGlyphs[(size_t)a_(chars, charsLen, s, p)][0] + fontSpacing;
      x -= (w - fontSpacing) / 2;
      // fall through to the draw loop
    }
    /* fall through */
    case 0:
    case 4:
      for (int p = off; p < off + len; p++) {
        int gi = a_(chars, charsLen, s, p);
        const std::vector<jshort>& glyph = fontGlyphs[(size_t)gi];
        for (int r = (int)glyph.size() - 1; r >= 1; r--) {
          jshort run = glyph[(size_t)r];
          g->fillRect(x + ((run >> 12) & 15), y + ((run >> 8) & 15),
                      (run >> 4) & 15, run & 15);
        }
        x += glyph[0] + fontSpacing;
      }
      break;
    case 8:  // RIGHT: draw right to left
      x += fontSpacing;
      for (int p = off + len - 1; p >= off; p--) {
        int gi = a_(chars, charsLen, s, p);
        const std::vector<jshort>& glyph = fontGlyphs[(size_t)gi];
        x -= glyph[0] + fontSpacing;
        for (int r = (int)glyph.size() - 1; r >= 1; r--) {
          jshort run = glyph[(size_t)r];
          g->fillRect(x + ((run >> 12) & 15), y + ((run >> 8) & 15),
                      (run >> 4) & 15, run & 15);
        }
      }
      break;
    default:
      break;
  }
}

void ratchetandclank::a_(Graphics* g, const String& s, int off, int len,
                         int x, int y, int anchor) {
  drawRun(g, nullptr, 0, s, off, len, x, y, anchor);
}

void ratchetandclank::a_(Graphics* g, const String& s, int x, int y, int anchor) {
  drawRun(g, nullptr, 0, s, 0, -1, x, y, anchor);
}

// string width (advance of every glyph, minus the final spacing)
int ratchetandclank::a_(const String& s) {
  int w = 0;
  for (int p = 0; p < length(s); p++)
    w += fontGlyphs[(size_t)a_(nullptr, 0, s, p)][0] + fontSpacing;
  return w - fontSpacing;
}

int ratchetandclank::a_(char c) {
  return fontGlyphs[(size_t)a_(nullptr, 0, String(), (uint8_t)c)][0] + fontSpacing;
}


