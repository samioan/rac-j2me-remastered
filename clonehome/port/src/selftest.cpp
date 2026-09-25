// Loads every resource of the three banks and checks it the way
// tools/parse_ch.py does. usage: clonehome_selftest <dir holding RP1..RP3>
#include <cstdio>

#include "assets.h"

using namespace ch;

static int fails = 0;
#define CHECK(c, ...) do { if (!(c)) { fails++; std::printf("FAIL: " __VA_ARGS__); std::printf("\n"); } } while (0)

int main(int argc, char** argv) {
  Assets a;
  if (!a.load(argc > 1 ? argv[1] : "../extracted")) {
    std::printf("cannot load banks\n");
    return 2;
  }
  int nType[256] = {};
  int atlases = 0, images = 0, anims = 0, rects = 0;
  for (int b = 1; b <= 3; b++) {
    for (int i = 0; i < a.count(b); i++) {
      int id = b << 10 | i, t = a.type(id);
      nType[t]++;
      if (t == kAtlas) {
        atlases++;
        SpriteSet s = a.loadSprites(id);
        CHECK(!s.sprites.empty(), "atlas %d did not parse", id);
        Image im;
        CHECK(a.loadImage(s.imageId, nullptr, im), "atlas %d: image %d does not decode", id, s.imageId);
        for (auto& r : s.sprites) {
          rects++;
          if (r.w && r.h)
            CHECK(r.srcX >= 0 && r.srcY >= 0 && r.srcX + r.w <= im.w && r.srcY + r.h <= im.h,
                  "atlas %d: rect outside %dx%d", id, im.w, im.h);
        }
      } else if (t == kPng || t == kSplash) {
        images++;
        Image im;
        CHECK(a.loadImage(id, nullptr, im), "image %d does not decode", id);
      } else if (t == kAnimSet) {
        anims++;
        AnimSetRaw as;
        CHECK(a.parseAnimSet(id, as), "anim set %d: bad byte count", id);
      }
    }
  }
  CHECK(atlases == 30 && images == 39 && anims == 10, "counts: %d atlases %d images %d anim sets", atlases, images, anims);
  CHECK(nType[kData] == 36 && nType[kStrings] == 1 && nType[kSwapArray] == 2, "data/string/swap counts");

  CHECK(a.loadStringTable(2098) && a.stringCount() == 449, "string table: %d strings", a.stringCount());

  // font palette swaps: each swap array must produce a decodable image
  for (int sw : {1094, 1095}) {
    Bytes s = a.resource(sw);
    Image im;
    CHECK(a.loadImage(1066, &s, im), "swap %d on image 1066", sw);
  }
  AnimSetRaw player;
  a.parseAnimSet(1028, player);
  CHECK(player.sequences == 15, "player anim set has %d sequences", player.sequences);

  std::printf("%d atlases (%d rects), %d images, %d anim sets, %d strings: %s\n", atlases, rects, images, anims,
              a.stringCount(), fails ? "FAILED" : "all ok");
  return fails ? 1 : 0;
}
