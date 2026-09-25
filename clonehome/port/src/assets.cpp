#define _CRT_SECURE_NO_WARNINGS
#include "assets.h"

#include <cstdio>
#include <cstring>

#define STBI_NO_STDIO
#include "stb_image.h"

namespace ch {

namespace {

bool readFile(const std::string& path, std::vector<uint8_t>& out) {
  FILE* f = std::fopen(path.c_str(), "rb");
  if (!f) return false;
  std::fseek(f, 0, SEEK_END);
  long n = std::ftell(f);
  std::fseek(f, 0, SEEK_SET);
  out.resize((size_t)n);
  size_t got = n ? std::fread(out.data(), 1, (size_t)n, f) : 0;
  std::fclose(f);
  return got == (size_t)n;
}

// Big-endian cursor over a byte range; reads past the end return 0 and set bad.
struct Reader {
  const uint8_t* p;
  int n, pos = 0;
  bool bad = false;
  Reader(Bytes b) : p(b.p), n(b.n) {}
  int u8() {
    if (pos >= n) { bad = true; return 0; }
    return p[pos++];
  }
  int s8() { int v = u8(); return v > 127 ? v - 256 : v; }
  int u16() { int a = u8(); return a << 8 | u8(); }
  int s16() { int v = u16(); return v > 32767 ? v - 65536 : v; }
  uint32_t u32() { uint32_t a = (uint32_t)u16(); return a << 16 | (uint32_t)u16(); }
};

}  // namespace

bool Assets::load(const std::string& dir) {
  for (int b = 1; b <= 3; b++) {
    std::vector<uint8_t> d;
    if (!readFile(dir + "/RP" + std::to_string(b), d)) return false;
    Reader r(Bytes{d.data(), (int)d.size()});
    int width = (1 + (r.u8() & 1)) << 1;
    int cnt = r.u16();
    Bank& k = banks_[b];
    k.lens.assign(cnt, 0);
    for (int i = 0; i < cnt; i++)
      for (int j = 0; j < width; j++) k.lens[i] = k.lens[i] << 8 | r.u8();
    k.types.resize(cnt);
    for (int i = 0; i < cnt; i++) k.types[i] = (uint8_t)r.u8();
    uint32_t total = r.u32();
    if (r.bad || r.pos + (int)total != (int)d.size()) return false;
    k.data.assign(d.begin() + r.pos, d.end());
    k.offs.assign(1, 0);
    for (int l : k.lens) k.offs.push_back(k.offs.back() + l);
    if (k.offs.back() != (int)total) return false;
  }
  return true;
}

Assets::Bank* Assets::select(int id) {
  int b = id >> 10;
  if (b != 0) current_ = b;
  if (current_ < 1 || current_ > 3) return nullptr;
  return &banks_[current_];
}

Bytes Assets::resource(int id) {
  Bank* k = select(id);
  int i = id & 1023;
  if (!k || i >= (int)k->lens.size()) return {};
  return Bytes{k->data.data() + k->offs[i], k->lens[i]};
}

int Assets::type(int id) {
  Bank* k = select(id);
  int i = id & 1023;
  return (k && i < (int)k->types.size()) ? k->types[i] : -1;
}

int Assets::count(int bank) const { return (int)banks_[bank].lens.size(); }

SpriteSet Assets::loadSprites(int id) {
  SpriteSet out;
  Bytes res = resource(id);
  Reader r(res);
  int imageId = r.u16();
  r.u8();
  int dlen = r.s16() - 4;
  int mask = r.u16();
  int count = r.u16();
  if (r.bad || dlen < 0 || r.pos + dlen > res.n) return out;
  const uint8_t* data = res.p + r.pos;

  bool present[16];
  for (int i = 0; i < 16; i++) present[i] = (mask >> i & 1) == 0;
  int sizes[10], bit = 5, k = 0;
  for (int g = 0; g < 3; g++)
    for (int j = 0; j < (g == 0 ? 2 : 4); j++)
      sizes[k++] = present[g] ? (present[bit++] ? 2 : 1) : 0;

  out.imageId = imageId;
  out.sprites.resize(count);
  int pos = 0;
  for (int f = 0; f < 10; f++) {
    if (!sizes[f]) continue;
    for (int i = 0; i < count; i++) {
      int v;
      if (sizes[f] == 1) {
        v = data[pos + i];
        if (v > 127 && (f <= 1 || f == 6 || f == 7)) v -= 256;
      } else {
        v = (int16_t)(data[pos + i * 2] << 8 | data[pos + i * 2 + 1]);
      }
      int16_t* s = &out.sprites[i].offX;
      s[f] = (int16_t)v;
    }
    pos += count * sizes[f];
  }
  if (pos != dlen) return SpriteSet{};
  return out;
}

bool Assets::loadImage(int id, const Bytes* swap, Image& out) {
  Bytes res = resource(id);
  if (res.n < 8) return false;
  std::vector<uint8_t> png(res.p, res.p + res.n);
  if (swap) {
    // Walk the chunks to the PLTE payload and overwrite it (findPngDataChunk).
    int p = 8;
    while (p + 8 <= (int)png.size()) {
      uint32_t len = (uint32_t)png[p] << 24 | png[p + 1] << 16 | png[p + 2] << 8 | png[p + 3];
      uint32_t typ = (uint32_t)png[p + 4] << 24 | png[p + 5] << 16 | png[p + 6] << 8 | png[p + 7];
      p += 8;
      if (typ == 0x504C5445) {  // "PLTE"
        for (int i = 0; i < swap->n && p + i < (int)png.size(); i++) png[p + i] = swap->p[i];
        break;
      }
      if (typ == 0x49454E44) break;  // "IEND"
      p += (int)len + 4;
    }
  }
  int w, h, comp;
  unsigned char* px = stbi_load_from_memory(png.data(), (int)png.size(), &w, &h, &comp, 4);
  if (!px) return false;
  out.w = w;
  out.h = h;
  out.px.resize((size_t)w * h);
  for (int i = 0; i < w * h; i++)
    out.px[i] = (uint32_t)px[i * 4 + 3] << 24 | px[i * 4] << 16 | px[i * 4 + 1] << 8 | px[i * 4 + 2];
  stbi_image_free(px);
  return true;
}

bool Assets::parseAnimSet(int id, AnimSetRaw& out) {
  Bytes res = resource(id);
  Reader r(res);
  int nSeq = r.u16() & 0x7FFF;
  int nLists = r.u16();
  r.u16(); r.u16(); r.u16();
  out = AnimSetRaw{};
  out.sequences = nSeq;
  out.frameLists = nLists;
  int steps = 0, parts = 0;
  for (int c = 0; c < 11; c++) {
    bool delta = c != 8;
    int n = nSeq;
    if (c > 1) n = steps;
    if (c > 6) n = nLists;
    if (c > 7) n = parts;
    int acc = 0;
    for (int i = 0; i < n; i++) {
      int v = r.s8();
      if (v == -128 && delta) v = r.s16();
      int val = acc + v;
      if (delta) acc = val;
      if (c == 1) steps += val;
      if (c == 7) parts += val;
      out.cols[c].push_back(val);
    }
  }
  out.steps = steps;
  out.parts = parts;
  return !r.bad && r.pos == res.n;
}

bool Assets::loadStringTable(int id) {
  Bytes res = resource(id);
  Reader r(res);
  int n = r.s16();
  strOffsets_.assign(1, 0);
  strBytes_.clear();
  for (int i = 0; i < n; i++) {
    int len = r.s16();
    for (int j = 0; j < len; j++) strBytes_.push_back((uint8_t)r.u8());
    strOffsets_.push_back((int)strBytes_.size());
  }
  return !r.bad && r.pos == res.n;
}

std::string Assets::str(int i) const {
  if (i < 0 || i + 1 >= (int)strOffsets_.size()) return {};
  return std::string((const char*)strBytes_.data() + strOffsets_[i], strOffsets_[i + 1] - strOffsets_[i]);
}

}  // namespace ch
