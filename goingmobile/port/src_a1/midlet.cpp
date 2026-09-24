// midlet.cpp -- implementation of ratchetandclank (see midlet.h),
// transcribed from src_a1/ratchetandclank.java.
#include "midlet.h"
#include "game.h"
#include "intromanager.h"
#include "canvasshell.h"
#include <cstddef>

Font* ratchetandclank::currentFont = nullptr;
Font* ratchetandclank::largeFont = nullptr;
Font* ratchetandclank::smallFontAlias = nullptr;
Font* ratchetandclank::smallFont = nullptr;
String ratchetandclank::strings[321];
int ratchetandclank::language = -1;
const String ratchetandclank::LANGUAGE_FILES[5] = {
    "txt_en", "txt_fr", "txt_it", "txt_gr", "txt_sp"};
const int ratchetandclank::SAVE_RECORD_IDS[4] = {1, 2, 3, 4};

ratchetandclank::ratchetandclank() {
  verifySaveStore();
  language = readLanguageSetting();
  soundPlayer = new SoundPlayer();
  soundPlayer->loadAll();
  soundEnabled = readSoundSetting() > 0;
  introManager = new IntroManager(this);
}

void ratchetandclank::startApp() {
  if (!midletStarted) {
    midletStarted = true;
    if (introManager) introManager->q = true;
    canvas = new CanvasShell(this);
    canvas->showNotify();
  }
}

void ratchetandclank::pauseApp() {
  if (canvas) canvas->hideNotify();
}

void ratchetandclank::destroyApp(bool) {
  introManager = nullptr;
  game = nullptr;
}

void ratchetandclank::startNewGame(int level) {
  stopSoundHard();
  refreshSaveSlotSummaries();
  if (introManager) introManager->a_(true);
  if (game) {
    game->cu = 0;
    game->c_(level, -1);
  }
  gameStarted = true;
}

void ratchetandclank::continueGame(int slot) {
  stopSoundHard();
  if (introManager) introManager->a_(false);
  if (game) game->c_(-1, slot);
  gameStarted = true;
}

void ratchetandclank::returnToIntro() {
  stopSoundSoft();
  if (game) game->m_();
  gameStarted = false;
  if (introManager) introManager->c_();
}

void ratchetandclank::notifyDestroyed() {
  destroyApp(true);
  platform::requestQuit();
}

String ratchetandclank::getAppProperty(const String&) {
  // No MIDlet manifest API in the port (PORT_ROADMAP.md's "Decisions
  // carried through every milestone") -- every property lookup misses,
  // same as the shipped a1.jar's own missing Unlock-Code attribute.
  return "";
}

void ratchetandclank::stopSoundHard() {
  if (soundPlayer) soundPlayer->haltPlayer();
}

void ratchetandclank::playSoundIfEnabled(int id) {
  if (soundEnabled && soundPlayer) soundPlayer->queue(id);
}

bool ratchetandclank::playMenuLoopSound(bool loop) {
  if (!soundEnabled) return true;
  bool introBusy = introManager && introManager->p;
  return (!gameStarted && !introBusy && soundPlayer) ? soundPlayer->playMenuLoop(loop) : true;
}

void ratchetandclank::stopSoundHardOnLevelStart() {
  if (soundPlayer) soundPlayer->haltPlayer();
}

void ratchetandclank::stopSoundSoft() {
  if (soundPlayer) soundPlayer->stop();
}

void ratchetandclank::refreshSaveSlotSummaries() {
  for (int i = 0; i < 3; i++) {
    readSaveSlotRaw(i);
    saveSlotFlags[i] = saveBuffer[0];
    saveSlotTimes[i] = readInt(saveBuffer, 195);
  }
}

void ratchetandclank::writeInt(int v, jbyte* buf, int off) {
  buf[0 + off] = (jbyte)((v >> 24) & 0xFF);
  buf[1 + off] = (jbyte)((v >> 16) & 0xFF);
  buf[2 + off] = (jbyte)((v >> 8) & 0xFF);
  buf[3 + off] = (jbyte)(v & 0xFF);
}

int ratchetandclank::readInt(const jbyte* buf, int off) {
  int b0 = buf[0 + off] < 0 ? buf[0 + off] + 256 : buf[0 + off];
  int b1 = buf[1 + off] < 0 ? buf[1 + off] + 256 : buf[1 + off];
  int b2 = buf[2 + off] < 0 ? buf[2 + off] + 256 : buf[2 + off];
  int b3 = buf[3 + off] < 0 ? buf[3 + off] + 256 : buf[3 + off];
  return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
}

void ratchetandclank::writeShort(jshort v, jbyte* buf, int off) {
  buf[off++] = (jbyte)(v >> 8);
  buf[off] = (jbyte)v;
}

jshort ratchetandclank::readShort(const jbyte* buf, int off) {
  int b0 = buf[0 + off] < 0 ? buf[0 + off] + 256 : buf[0 + off];
  int b1 = buf[1 + off] < 0 ? buf[1 + off] + 256 : buf[1 + off];
  return (jshort)((b0 << 8) | b1);
}

void ratchetandclank::verifySaveStore() {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", true);
  if (!rs) {
    rebuildSaveStore();
    return;
  }
  if (rs->getNumRecords() < 4) {
    rs->closeRecordStore();
    delete rs;
    rebuildSaveStore();
    return;
  }
  for (int i = 0; i < 4; i++) {
    int expect = (i < 3) ? 214 : 3;
    if (rs->getRecordSize(SAVE_RECORD_IDS[i]) != expect) {
      rs->closeRecordStore();
      delete rs;
      rebuildSaveStore();
      return;
    }
  }
  rs->closeRecordStore();
  delete rs;
}

void ratchetandclank::rebuildSaveStore() {
  RecordStore::deleteRecordStore("RANDCSm");
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", true);
  if (!rs) return;
  for (int i = 0; i < 3; i++) rs->addRecord(saveBuffer, 0, 214);
  settings[0] = 1;
  settings[2] = 0;
  settings[1] = -1;
  rs->addRecord(settings, 0, 3);
  rs->closeRecordStore();
  delete rs;
}

void ratchetandclank::writeSaveSlot(int slot) {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
  if (!rs) return;
  if (game) game->writeSaveData(saveBuffer);
  rs->setRecord(SAVE_RECORD_IDS[slot], saveBuffer, 0, 214);
  rs->closeRecordStore();
  delete rs;
}

void ratchetandclank::readSaveSlot(int slot) {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
  if (!rs) return;
  rs->getRecord(SAVE_RECORD_IDS[slot], saveBuffer, 0);
  rs->closeRecordStore();
  delete rs;
  if (game) game->readSaveData(saveBuffer);
}

void ratchetandclank::clearSaveSlot(int slot) {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
  if (!rs) return;
  for (int i = 0; i < 214; i++) saveBuffer[i] = 0;
  rs->setRecord(SAVE_RECORD_IDS[slot], saveBuffer, 0, 214);
  rs->closeRecordStore();
  delete rs;
}

void ratchetandclank::readSaveSlotRaw(int slot) {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
  if (!rs) return;
  rs->getRecord(SAVE_RECORD_IDS[slot], saveBuffer, 0);
  rs->closeRecordStore();
  delete rs;
}

bool ratchetandclank::isGameWon() {
  if (!settingsLoaded) {
    RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
    if (rs) {
      rs->getRecord(SAVE_RECORD_IDS[3], settings, 0);
      settingsLoaded = true;
      rs->closeRecordStore();
      delete rs;
    }
  }
  return settings[2] != 0;
}

jbyte ratchetandclank::readSoundSetting() {
  if (!settingsLoaded) {
    RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
    if (rs) {
      rs->getRecord(SAVE_RECORD_IDS[3], settings, 0);
      settingsLoaded = true;
      rs->closeRecordStore();
      delete rs;
    }
  }
  return settings[0];
}

jbyte ratchetandclank::readLanguageSetting() {
  if (!settingsLoaded) {
    RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
    if (rs) {
      rs->getRecord(SAVE_RECORD_IDS[3], settings, 0);
      settingsLoaded = true;
      rs->closeRecordStore();
      delete rs;
    }
  }
  return settings[1];
}

void ratchetandclank::writeSoundAndLanguageSettings(jbyte soundFlag) {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
  if (!rs) return;
  rs->getRecord(SAVE_RECORD_IDS[3], settings, 0);
  settings[0] = soundFlag;
  settings[1] = (jbyte)language;
  rs->setRecord(SAVE_RECORD_IDS[3], settings, 0, 3);
  rs->closeRecordStore();
  delete rs;
}

void ratchetandclank::markGameWon() {
  RecordStore* rs = RecordStore::openRecordStore("RANDCSm", false);
  if (!rs) return;
  rs->getRecord(SAVE_RECORD_IDS[3], settings, 0);
  settings[2] = 1;
  rs->setRecord(SAVE_RECORD_IDS[3], settings, 0, 3);
  rs->closeRecordStore();
  delete rs;
}

void ratchetandclank::loadStrings() {
  int idx = (language == -1) ? 0 : language;
  ByteArray raw = loadResource(String("/") + LANGUAGE_FILES[(size_t)idx] + ".txt");
  String text;
  text.resize(raw.size());
  for (size_t i = 0; i < raw.size(); i++) text[i] = (char)std::to_integer<uint8_t>(raw[i]);

  int pos = 0;
  for (int i = 0; i < 321; i++) {
    size_t nlPos = text.find('\n', (size_t)pos);
    int nl = (nlPos == String::npos) ? (int)text.size() : (int)nlPos;
    String line = trim(text.substr((size_t)pos, (size_t)(nl - pos)));
    if (!line.empty()) strings[i] = line;
    pos = nl + 1;
  }
}
