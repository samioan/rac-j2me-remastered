#!/usr/bin/env python3
"""Clone Home phase 2: parse the RP* banks and every sprite atlas.

Replicates Engine.loadSprites / Engine.parseBank byte for byte and asserts that
each atlas consumes exactly its payload and that every rectangle lies inside
its PNG. Output goes to stdout (derived from copyrighted data: do not commit).

    python tools/parse_ch.py            # verify everything
    python tools/parse_ch.py atlas 1049 # dump one atlas
"""
import struct
import sys
from pathlib import Path

EXT = Path(__file__).resolve().parent.parent / "clonehome" / "extracted"
FIELDS = ["offX", "offY", "srcX", "srcY", "w", "h", "f6", "f7", "advance", "metric"]


def load_bank(n):
    d = (EXT / f"RP{n}").read_bytes()
    sz = (1 + (d[0] & 1)) << 1
    cnt = struct.unpack(">H", d[1:3])[0]
    p = 3
    lens = [int.from_bytes(d[p + i * sz:p + (i + 1) * sz], "big") for i in range(cnt)]
    p += sz * cnt
    types = list(d[p:p + cnt])
    p += cnt
    total = struct.unpack(">I", d[p:p + 4])[0]
    p += 4
    assert sum(lens) == total and p + total == len(d), f"RP{n}: bad container"
    offs = [0]
    for l in lens:
        offs.append(offs[-1] + l)
    return d[p:p + total], lens, types, offs


BANKS = {n: load_bank(n) for n in (1, 2, 3)}


def resource(rid):
    data, lens, types, offs = BANKS[rid >> 10]
    i = rid & 1023
    return data[offs[i]:offs[i + 1]], types[i]


def png_size(blob):
    assert blob[:8] == b"\x89PNG\r\n\x1a\n", "not a PNG"
    return struct.unpack(">II", blob[16:24])


def parse_atlas(rid):
    r, t = resource(rid)
    assert t == 254, f"{rid}: type {t}, not an atlas"
    image_id = struct.unpack(">H", r[0:2])[0]
    if image_id >> 10 == 0:  # bank 0 = the bank already loaded
        image_id |= rid & ~1023
    dlen = struct.unpack(">h", r[3:5])[0] - 4
    mask = struct.unpack(">H", r[5:7])[0]
    count = struct.unpack(">H", r[7:9])[0]
    data = r[9:9 + dlen]
    assert 9 + dlen == len(r), f"{rid}: {len(r) - 9 - dlen} trailing bytes"
    absent = [(mask >> i) & 1 == 0 for i in range(16)]  # engine: bit clear = present
    sizes, bit = [], 5
    for g in range(3):
        for _ in range(2 if g == 0 else 4):
            if absent[g]:
                sizes.append(2 if absent[bit] else 1)
                bit += 1
            else:
                sizes.append(0)
    pos, cols = 0, []
    for s in sizes:
        if s == 0:
            cols.append(None)
            continue
        width = 1 if s == 1 else 2
        cols.append(data[pos:pos + count * width])
        pos += count * width
    assert pos == dlen, f"{rid}: consumed {pos} of {dlen}"
    sprites = []
    for i in range(count):
        row = []
        for f, (s, col) in enumerate(zip(sizes, cols)):
            if s == 0:
                row.append(0)
            elif s == 1:
                b = col[i]
                row.append(b - 256 if b > 127 and (f <= 1 or f in (6, 7)) else b)
            else:
                row.append(struct.unpack(">h", col[i * 2:i * 2 + 2])[0])
        sprites.append(dict(zip(FIELDS, row)))
    return image_id, sprites, sizes


def verify():
    atlases = [(n << 10) | i for n in BANKS for i, t in enumerate(BANKS[n][2]) if t == 254]
    bad = 0
    for rid in atlases:
        image_id, sprites, _ = parse_atlas(rid)
        blob, it = resource(image_id)
        w, h = png_size(blob)
        oob = [s for s in sprites
               if s["w"] and s["h"] and (s["srcX"] + s["w"] > w or s["srcY"] + s["h"] > h)]
        bad += bool(oob)
        print(f"atlas {rid}: image {image_id} (type {it}) {w}x{h}, {len(sprites)} sprites"
              + (f", {len(oob)} rects outside the image" if oob else ""))
    print(f"{len(atlases)} atlases parsed exactly; {bad} with out-of-bounds rects")


if __name__ == "__main__":
    if len(sys.argv) == 3 and sys.argv[1] == "atlas":
        img, sp, sz = parse_atlas(int(sys.argv[2]))
        print("image", img, "field sizes", sz)
        for i, s in enumerate(sp):
            print(i, s)
    else:
        verify()
