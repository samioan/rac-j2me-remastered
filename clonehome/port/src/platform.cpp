#include "platform.h"

#include <windows.h>
#include <mmsystem.h>

#include <condition_variable>
#include <cstdio>
#include <deque>
#include <functional>
#include <mutex>
#include <string>
#include <thread>

namespace ch {

namespace {

void audioLog(const char* what, int code) {
  FILE* f = fopen("audio.log", "a");
  if (f) {
    fprintf(f, "%s: %d\n", what, code);
    fclose(f);
  }
}

// MCI and PlaySound calls can block for hundreds of milliseconds (the first MIDI open takes
// seconds), so every one of them runs on this worker thread, in order. The game thread only
// queues commands. The state is leaked on purpose: the thread outlives static destruction.
struct Worker {
  std::mutex m;
  std::condition_variable cv;
  std::deque<std::function<void()>> queue;
  std::function<void()> idle;  // run every ~200 ms when the queue is empty
  std::thread th;

  Worker() {
    th = std::thread([this] {
      for (;;) {
        std::function<void()> job;
        {
          std::unique_lock<std::mutex> lk(m);
          if (queue.empty()) cv.wait_for(lk, std::chrono::milliseconds(200));
          if (!queue.empty()) {
            job = std::move(queue.front());
            queue.pop_front();
          }
        }
        if (job) job();
        else if (idle) idle();
      }
    });
    th.detach();
  }
  void post(std::function<void()> f) {
    {
      std::lock_guard<std::mutex> lk(m);
      queue.push_back(std::move(f));
    }
    cv.notify_one();
  }
};

// Worker-thread state.
int g_nextMci = 1;
int g_loopingMci = 0;  // alias id of the looping MIDI, 0 if none

std::wstring alias(int id) { return L"chm" + std::to_wstring(id); }

std::wstring tempMidiPath(int id) {
  wchar_t dir[MAX_PATH];
  GetTempPathW(MAX_PATH, dir);
  return std::wstring(dir) + L"ch_music_" + std::to_wstring(GetCurrentProcessId()) + L"_" + std::to_wstring(id) + L".mid";
}

void restartIfStopped() {
  if (!g_loopingMci) return;
  wchar_t mode[32] = {};
  std::wstring a = alias(g_loopingMci);
  if (mciSendStringW((L"status " + a + L" mode").c_str(), mode, 32, nullptr) == 0 && std::wstring(mode) == L"stopped")
    mciSendStringW((L"play " + a + L" from 0").c_str(), nullptr, 0, nullptr);
}

Worker& worker() {
  static Worker* w = [] {
    Worker* nw = new Worker();
    nw->idle = restartIfStopped;
    return nw;
  }();
  return *w;
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

}  // namespace

void Player::start() {
  if (data == nullptr) return;
  state_ = 400;
  if (isMidi()) {
    endTime_ = 0;
    Arr<int8_t> bytes = data;
    bool loop = loops_ < 0;
    int* idp = &mciId_;
    bool* openp = &mciOpen_;
    worker().post([bytes, loop, idp, openp] {
      if (!*openp) {
        int id = g_nextMci++;
        std::wstring path = tempMidiPath(id);
        FILE* f = _wfopen(path.c_str(), L"wb");
        if (!f) return;
        fwrite(bytes.data(), 1, (size_t)bytes.length(), f);
        fclose(f);
        MCIERROR e = mciSendStringW((L"open \"" + path + L"\" type sequencer alias " + alias(id)).c_str(), nullptr, 0, nullptr);
        if (e) {
          audioLog("mci open failed", (int)e);
          return;
        }
        *idp = id;
        *openp = true;
      }
      MCIERROR e = mciSendStringW((L"play " + alias(*idp) + L" from 0").c_str(), nullptr, 0, nullptr);
      if (e) audioLog("mci play failed", (int)e);
      g_loopingMci = loop ? *idp : 0;
    });
  } else {
    int ms = wavMillis(data);
    endTime_ = loops_ < 0 || ms == 0 ? 0 : (int64_t)GetTickCount64() + ms;
    Arr<int8_t> bytes = data;
    DWORD flags = SND_MEMORY | SND_ASYNC | SND_NODEFAULT | (loops_ < 0 ? SND_LOOP : 0);
    worker().post([bytes, flags] {
      if (!PlaySoundW((LPCWSTR)bytes.data(), nullptr, flags)) audioLog("PlaySound failed", (int)GetLastError());
    });
  }
}

void Player::stop() {
  bool wasPlaying = state_ == 400;
  state_ = 300;
  if (!wasPlaying) return;
  if (isMidi()) {
    int* idp = &mciId_;
    bool* openp = &mciOpen_;
    worker().post([idp, openp] {
      if (*openp) {
        if (g_loopingMci == *idp) g_loopingMci = 0;
        mciSendStringW((L"stop " + alias(*idp)).c_str(), nullptr, 0, nullptr);
      }
    });
  } else {
    worker().post([] { PlaySoundW(nullptr, nullptr, 0); });
  }
}

void Player::close() {
  bool wasOpen = mciOpen_;
  int id = mciId_;
  state_ = 0;
  if (!wasOpen) return;
  worker().post([id] {
    if (g_loopingMci == id) g_loopingMci = 0;
    mciSendStringW((L"close " + alias(id)).c_str(), nullptr, 0, nullptr);
    DeleteFileW(tempMidiPath(id).c_str());
  });
  mciOpen_ = false;
}

void Player::pumpLoops() {}  // looping is handled by the worker's idle tick

int Player::getState() {
  if (state_ == 400 && endTime_ && (int64_t)GetTickCount64() >= endTime_) state_ = 300;
  return state_;
}

}  // namespace ch
