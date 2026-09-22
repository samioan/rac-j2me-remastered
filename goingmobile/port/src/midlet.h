// midlet.h -- port of goingmobile/src/ratchetandclank.java (the MIDlet).
//
// Owns: the RMS save handling (record store "RANDCSm": 3 x 220-byte slots +
// one 3-byte settings record), the .txt string-table loader, the word-wrap
// reader, and the f2.v bitmap font (a fillRect-based glyph renderer).
// Class name kept as in the original (it is the MIDlet's real name).
//
// Naming policy for the C++ port: Java allowed a field and a method to share
// one obfuscated letter; C++ does not, so every obfuscated method keeps its
// letter with a trailing underscore (a(), b(), c()... -> a_(), b_(), c_()).
// Renamed members (docs/CLASS_MAP.md) keep their new names.
#pragma once
#include "midp.h"

class Game;

class ratchetandclank {
 public:
  // --- MIDlet state ------------------------------------------------------
  bool a = false;               // startApp already ran
  static Game* b;                // the Game instance (save entry point)
  jbyte saveSlotFlags[3] = {0, 0, 0};
  jint saveSlotTimes[3] = {0, 0, 0};
  static String* strings;        // the m*.txt table (145 entries)
  SoundPlayer* soundPlayer = nullptr;
  bool soundEnabled = false;
  jbyte saveBuffer[220] = {};
  jbyte settings[3] = {0, 0, 0};

  static const jint SAVE_RECORD_IDS[4];  // {1,2,3,4}

  // --- word-wrap statics -------------------------------------------------
  static std::vector<String>* i;  // the wrap output Vector
  static int j, k, l, m;
  static String n;                // the wrap StringBuffer

  // --- font --------------------------------------------------------------
  std::vector<std::vector<jshort>> fontGlyphs;
  int fontLineHeight = 0;
  int fontSpacing = 0;

  // --- MIDlet lifecycle --------------------------------------------------
  void startApp();
  void a_();                       // quit (stop sound thread)
  void b_();                       // refresh save-slot flags/times

  // --- save records ------------------------------------------------------
  void c_();                       // verify record store, rebuild if bad
  void d_();                       // (re)create the record store
  void a_(int slot);               // serialize + write save slot
  void b_(int slot);               // read + deserialize save slot
  void c_(int slot);               // clear save slot
  void d_(int slot);               // raw read into saveBuffer
  void e_();                       // read settings record
  jbyte e_(int idx);               // settings byte
  void a_(jbyte value, int idx);   // write settings byte

  // --- static byte-order helpers (int/short big-endian) ------------------
  static void a_(int v, jbyte* buf, int off);
  static int a_(const jbyte* buf, int off);
  static void a_(jshort v, jbyte* buf, int off);
  static jshort b_(const jbyte* buf, int off);

  // --- text loading ------------------------------------------------------
  static String* a_(const String& path, int count);               // string table
  static std::vector<String>* b_(const String& path, int width);  // file wrap
  static std::vector<String>* c_(const String& text, int width);   // memory wrap
  static void a_(char c, int width);                              // wrap machine

  // --- font --------------------------------------------------------------
  void a_(const String& path, int lineHeight, int spacing);  // font loader
  int  a_(const String& s);                  // string width
  int  a_(char c);                           // char width
  void a_(Graphics* g, const String& s, int off, int len,
          int x, int y, int anchor);
  void a_(Graphics* g, const String& s, int x, int y, int anchor);

 private:
  // char-at-position(idx) -> glyph index; when chars is null and s is empty,
  // idx IS the raw char code itself (mirrors the original's dual-use index
  // parameter: normally a position, but the character value directly when
  // both source args are absent -- see the public a_(char) overload above).
  static int a_(const char* chars, int charsLen, const String& s, int idx);
  void drawRun(Graphics* g, const char* chars, int charsLen,
               const String& s, int off, int len, int x, int y, int anchor);
};
