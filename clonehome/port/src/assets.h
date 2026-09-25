// RP1..RP3 resource banks and the asset formats stored in them.
// Mirrors Engine.parseBank / selectBank / loadSprites / loadImage /
// loadAnimSet / loadStringTable (clonehome/src/Engine.java); formats are
// documented in ../../docs/ASSET_FORMATS.md.
#pragma once
#include <cstdint>
#include <string>
#include <vector>

#include "gfx.h"

namespace ch {

enum ResType : uint8_t {
  kMidi = 0, kWav = 1, kAnimSet = 247, kStrings = 250, kSwapArray = 251,
  kSplash = 252, kPng = 253, kAtlas = 254, kData = 255,
};

struct Bytes {
  const uint8_t* p = nullptr;
  int n = 0;
};

struct SpriteRect {
  int16_t offX, offY, srcX, srcY, w, h, f6, f7, advance, metric;
};

struct SpriteSet {
  int imageId = 0;
  std::vector<SpriteRect> sprites;
};

// Raw decoded columns of an animation set (see ASSET_FORMATS.md).
struct AnimSetRaw {
  int sequences = 0, frameLists = 0, steps = 0, parts = 0;
  std::vector<int> cols[11];
};

class Assets {
 public:
  bool load(const std::string& dir);  // dir holds the RP1, RP2, RP3 files

  // Bank 0 in an id means "the bank most recently selected", as in the Java.
  Bytes resource(int id);
  int type(int id);
  int count(int bank) const;
  int currentBank() const { return current_; }
  const std::vector<uint8_t>& bankTypes(int bank) const { return banks_[bank].types; }

  SpriteSet loadSprites(int id);
  // swap != null replaces the PNG's PLTE payload (font palette swaps).
  bool loadImage(int id, const Bytes* swap, Image& out);
  bool parseAnimSet(int id, AnimSetRaw& out);
  bool loadStringTable(int id);
  std::string str(int i) const;
  int stringCount() const { return strOffsets_.empty() ? 0 : (int)strOffsets_.size() - 1; }

 private:
  struct Bank {
    std::vector<uint8_t> data;
    std::vector<int> lens, offs;
    std::vector<uint8_t> types;
  };
  Bank banks_[4];
  int current_ = 1;
  std::vector<uint8_t> strBytes_;
  std::vector<int> strOffsets_;
  Bank* select(int id);
};

}  // namespace ch
