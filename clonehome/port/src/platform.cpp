#include "platform.h"

#include <windows.h>
#include <mmsystem.h>

#include <cstdio>
#include <string>

namespace ch {

namespace {
int g_nextMci = 1;
Player* g_loopingMidi = nullptr;

void audioLog(const char* what, int code) {
  FILE* f = fopen("audio.log", "a");
  if (f) { fprintf(f, "%s: %d\n", what, code); fclose(f); }
}

// Length of a PCM wav in milliseconds (0 if the header is not understood).
int wavMillis(const Arr<int8_t>& d) {
  if (d.length() < 44) return 0;
  const uint8_t* p = (const uint8_t*)d.data();
  auto u32 = [&](int o) { return (uint32_t)p[o] | (uint32_t)p[o + 1] << 8 | (uint32_t)p[o + 2] << 16 | (uint32_t)p[o + 3] << 24; };
  uint32_t byteRate = 0, dataLen = 0;
  for (int o = 12; o + 8 <= d.length();) {
    uint32_t id = u32(o), len = u32(o + 4);
    if (id == 0x20746D66) byteRate = u32(o + 16);  // "fmt "
    if (id == 0x61746164) { dataLen = len; break; }  // "data"
    o += 8 + (int)len + (int)(len & 1);
  }
  return byteRate ? (int)((int64_t)dataLen * 1000 / byteRate) : 0;
}

std::wstring tempMidiPath(int id) {
  wchar_t dir[MAX_PATH];
  GetTempPathW(MAX_PATH, dir);
  return std::wstring(dir) + L"ch_music_" + std::to_wstring(GetCurrentProcessId()) + L"_" + std::to_wstring(id) + L".mid";
}
}  // namespace

void Player::start() {
  if (data == nullptr) return;
  if (isMidi()) {
    if (!mciOpen_) {
      mciId_ = g_nextMci++;
      std::wstring path = tempMidiPath(mciId_);
      FILE* f = _wfopen(path.c_str(), L"wb");
      if (!f) return;
      fwrite(data.data(), 1, (size_t)data.length(), f);
      fclose(f);
      std::wstring cmd = L"open \"" + path + L"\" type sequencer alias chm" + std::to_wstring(mciId_);
      MCIERROR e = mciSendStringW(cmd.c_str(), nullptr, 0, nullptr);
      mciOpen_ = e == 0;
      if (e) audioLog("mci open failed", (int)e);
    }
    if (mciOpen_) {
      std::wstring a = L"chm" + std::to_wstring(mciId_);
      mciSendStringW((L"seek " + a + L" to start").c_str(), nullptr, 0, nullptr);
      MCIERROR e = mciSendStringW((L"play " + a).c_str(), nullptr, 0, nullptr);
      g_loopingMidi = loops_ < 0 ? this : nullptr;
      if (e) audioLog("mci play failed", (int)e);
    }
    endTime_ = 0;
  } else {
    DWORD flags = SND_MEMORY | SND_ASYNC | SND_NODEFAULT | (loops_ < 0 ? SND_LOOP : 0);
    if (!PlaySoundW((LPCWSTR)data.data(), nullptr, flags)) audioLog("PlaySound failed", (int)GetLastError());
    int ms = wavMillis(data);
    endTime_ = loops_ < 0 || ms == 0 ? 0 : GetTickCount64() + ms;
  }
  state_ = 400;
}

void Player::stop() {
  if (g_loopingMidi == this) g_loopingMidi = nullptr;
  if (isMidi()) {
    if (mciOpen_) mciSendStringW((L"stop chm" + std::to_wstring(mciId_)).c_str(), nullptr, 0, nullptr);
  } else {
    PlaySoundW(nullptr, nullptr, 0);
  }
  state_ = 300;
}

void Player::close() {
  stop();
  if (mciOpen_) {
    mciSendStringW((L"close chm" + std::to_wstring(mciId_)).c_str(), nullptr, 0, nullptr);
    DeleteFileW(tempMidiPath(mciId_).c_str());
    mciOpen_ = false;
  }
  state_ = 0;
}

void Player::pumpLoops() {
  Player* p = g_loopingMidi;
  if (!p || !p->mciOpen_) return;
  std::wstring a = L"chm" + std::to_wstring(p->mciId_);
  wchar_t mode[32] = {};
  if (mciSendStringW((L"status " + a + L" mode").c_str(), mode, 32, nullptr) == 0 && std::wstring(mode) == L"stopped") {
    mciSendStringW((L"seek " + a + L" to start").c_str(), nullptr, 0, nullptr);
    mciSendStringW((L"play " + a).c_str(), nullptr, 0, nullptr);
  }
}

int Player::getState() {
  if (state_ == 400 && endTime_ && (int64_t)GetTickCount64() >= endTime_) state_ = 300;
  return state_;
}

}  // namespace ch
