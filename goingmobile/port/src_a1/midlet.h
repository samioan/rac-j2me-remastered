// midlet.h -- port of goingmobile/src_a1/ratchetandclank.java (the MIDlet).
// a1's MIDlet: RMS saves (214-byte game-slot records + 3-byte settings,
// CONFIRMED different from the legacy build's 220-byte layout -- see
// ratchetandclank.java's own header comment and verifySaveStore()), the
// .txt string table, and the two controller objects (Game/IntroManager)
// CanvasShell delegates to. Font is its own class here (font.h), unlike
// the legacy build where it lived directly on ratchetandclank.
//
// Naming policy (see ../src/midlet.h, same convention): obfuscated methods
// keep their letter + trailing '_'; renamed members (docs/CLASS_MAP.md)
// keep their phase-1 names. No MIDlet manifest API in the port (see
// PORT_ROADMAP.md's "Decisions carried through every milestone") --
// isGameWon() skips the GameIsWon property lookup entirely, since a1's
// shipped jar doesn't carry it either (ratchetandclank.java's own comment
// confirms it "falls through to the RMS value in practice").
#pragma once
#include "midp.h"
#include "font.h"

class Game;
class IntroManager;
class CanvasShell;

class ratchetandclank {
 public:
  IntroManager* introManager = nullptr;
  Game* game = nullptr;
  CanvasShell* canvas = nullptr;
  SoundPlayer* soundPlayer = nullptr;

  static Font* currentFont;
  static Font* largeFont;       // f3.v, line height 13
  static Font* smallFontAlias;  // dead alias, always == smallFont
  static Font* smallFont;       // f2.v, line height 10

  bool gameStarted = false;
  bool soundEnabled = false;
  jbyte saveSlotFlags[3] = {0, 0, 0};
  jint saveSlotTimes[3] = {0, 0, 0};
  static String strings[321];
  jbyte saveBuffer[214] = {};
  bool settingsLoaded = false;
  static int language;
  static const String LANGUAGE_FILES[5];

  ratchetandclank();

  void startApp();
  void pauseApp();
  void destroyApp(bool unconditional);

  void startNewGame(int level);
  void continueGame(int slot);
  void returnToIntro();
  void notifyDestroyed();  // MIDlet built-in; the port's "Exit Game" path
  String getAppProperty(const String& key);  // no manifest API in the port
                                              // (PORT_ROADMAP.md) -- always null

  void stopSoundHard();
  void playSoundIfEnabled(int id);
  bool playMenuLoopSound(bool loop);
  void stopSoundHardOnLevelStart();
  void stopSoundSoft();
  void refreshSaveSlotSummaries();

  void writeInt(int v, jbyte* buf, int off);
  int readInt(const jbyte* buf, int off);
  void writeShort(jshort v, jbyte* buf, int off);
  jshort readShort(const jbyte* buf, int off);

  void verifySaveStore();
  void rebuildSaveStore();
  void writeSaveSlot(int slot);
  void readSaveSlot(int slot);
  void clearSaveSlot(int slot);
  void readSaveSlotRaw(int slot);

  bool isGameWon();
  jbyte readSoundSetting();
  jbyte readLanguageSetting();
  void writeSoundAndLanguageSettings(jbyte soundFlag);
  void markGameWon();

  void loadStrings();

 private:
  bool midletStarted = false;
  static const int SAVE_RECORD_IDS[4];
  jbyte settings[3] = {0, 0, 0};
};
