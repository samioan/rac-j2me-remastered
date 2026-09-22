#!/usr/bin/env python3
"""Phase 2 parsers for the canonical Going Mobile build's custom asset
formats. Each parser replicates its loader exactly (see goingmobile/src/)
and validates itself against the real files (exact byte consumption,
string cross-checks). Output goes to stdout -- it is derived from
copyrighted game data and must not be committed (see NOTICE.md).

Usage:
    python tools/parse_gm.py             # everything, with validations
    python tools/parse_gm.py menus
    python tools/parse_gm.py levels [n]
    python tools/parse_gm.py font [--text "..."]
    python tools/parse_gm.py anims
"""
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
EXT = ROOT / "goingmobile" / "extracted"
FAILURES = []


def check(cond, msg):
    if not cond:
        FAILURES.append(msg)
        print("FAIL: %s" % msg)
    return cond


def strings(suffix=""):
    """The m*.txt table (ratchetandclank.a(String,int); ids used & 0xFF)."""
    data = (EXT / ("m%s.txt" % suffix)).read_bytes().split(b"\r\n")
    return [s.decode("latin-1") for s in data]


def resolve(tab, sid):
    return tab[sid & 0xFF]


# /o -- the menu table (Game.a(String)): byte page count; per page:
# item count, title id, left/right softkey ids, back-target page; then
# 3 bytes per item: type, string id, action. Page 2's count byte is one
# less than its real rows (the loader injects the Language row).

def cmd_menus():
    data = (EXT / "o").read_bytes()
    tab = strings()
    pos = 0
    count = data[pos]; pos += 1
    print("=== /o: %d bytes, %d menu pages ===" % (len(data), count))
    for page in range(count):
        items = data[pos]; pos += 1
        real = items + (1 if page == 2 else 0)
        title, soft_l, soft_r, back = data[pos:pos + 4]; pos += 4
        softs = lambda i: "-" if i == 255 else resolve(tab, i)
        print("\npage %2d  %-30r  [L:%s | R:%s | back->%s]"
              % (page, resolve(tab, title), softs(soft_l),
                 softs(soft_r), "-" if back == 255 else back))
        for i in range(real):
            if page == 2 and i == real - 1:
                t, sid, act = 0, -112, -104   # injected Language row
            else:
                t, sid, act = data[pos:pos + 3]; pos += 3
            label = ("screen %d (%s)" % (sid, {96: "help", 97: "about",
                                               98: "credits"}.get(sid, "?"))
                     if t == 4 else resolve(tab, sid))
            print("   item %-2d type=%-3d %-36r action=%d"
                  % (i, t, label, act))
    check(pos == len(data), "/o: consumed %d of %d" % (pos, len(data)))
    print("\n[/o] %d of %d bytes consumed" % (pos, len(data)))


# /n<level> -- the tilemaps (LevelMap.a(int)): 28 columns x 18 rows,
# column-major, tile id = raw byte - 0x20.

ENEMY_47_55 = {47: 0, 48: 2, 49: 1, 50: 0, 51: 2, 52: 1,
               53: 0, 54: 2, 55: 1}   # tiles 50-52 also drop a bolt box
TILE_CHAR = [None] * 256


def _legend():
    def rng(lo, hi, ch):
        for t in range(lo, hi + 1):
            TILE_CHAR[t & 0xFF] = ch
    rng(0, 3, ".")        # background variants
    rng(4, 13, "#")       # solid blocks
    rng(14, 34, ":")      # solid specials
    TILE_CHAR[35] = ">"   # level exit (sets Game.exitTileX/Y)
    rng(36, 42, '"')      # foreground decoration
    rng(43, 46, "B")      # titanium bolt boxes
    rng(47, 55, "E")      # enemies (direct spawn)
    rng(56, 61, "!")      # hazards
    rng(62, 69, "z")      # zip line endpoints
    rng(70, 76, ",")      # background
    rng(77, 96, "i")      # infolink tiles ("Level N unlocked")
    TILE_CHAR[99] = ":"   # marker variants (fall to the ">13" branch,
    TILE_CHAR[100] = ":"  # same handling as the 14-34 solid specials)
    TILE_CHAR[97] = "x"; TILE_CHAR[98] = "x"   # hyper-shot targets
    TILE_CHAR[101] = "m"  # marker
    rng(102, 113, "P")    # moving platforms
    rng(114, 128, "e")    # enemies (indirect, via ENEMY_TILE_TYPES)
    TILE_CHAR[129] = "c"  # 0xA1: camera tile (rendered as background tile 0)
    TILE_CHAR[130] = "c"  # 0xA2: camera tile (rendered as background tile 1)
    TILE_CHAR[131] = "@"  # 0xA3: player start + camera (renders as tile 7)
    rng(132, 164, "c")    # camera/marker continuation range (0xA4..0xC4)


_legend()


def cmd_levels(which=None):
    # The shipped set is n1..n10 + n12 (11 files): level 11 is deliberately
    # skipped by the level select (Game: 11 -> 12, the boss), and the
    # Q==11 multi-grid path is dead code in this build.
    if which:
        levels = [int(which)]
    else:
        levels = sorted(
            (int(p.name[1:]) for p in EXT.glob("n[0-9]*")),
        )
    for n in levels:
        data = (EXT / ("n%d" % n)).read_bytes()
        if not check(len(data) == 504, "n%d: %d bytes (expected 504)"
                     % (n, len(data))):
            continue
        ents = {"bolts": 0, "enemies": {}, "platforms": 0, "zips": 0,
                "infolinks": 0, "targets": 0, "exit": None, "start": None}
        print("\n=== n%d: %d bytes, 28x18 grid (tile = raw - 0x20) ===" %
              (n, len(data)))
        for y in range(18):
            row = []
            for x in range(28):
                t = (data[x * 18 + y] - 32) & 0xFF
                row.append(TILE_CHAR[t] or "?")
                if 43 <= t <= 46:
                    ents["bolts"] += 1
                elif 47 <= t <= 55:
                    k = ENEMY_47_55[t]
                    ents["enemies"][k] = ents["enemies"].get(k, 0) + 1
                elif 114 <= t <= 128:
                    ents["enemies"]["(table)"] = \
                        ents["enemies"].get("(table)", 0) + 1
                elif 102 <= t <= 113:
                    ents["platforms"] += 1
                elif 62 <= t <= 69:
                    ents["zips"] += 1
                elif 77 <= t <= 96:
                    ents["infolinks"] += 1
                elif t in (97, 98):
                    ents["targets"] += 1
                elif t == 35:
                    ents["exit"] = (x, y)
                elif t == 131:
                    ents["start"] = (x, y)
                elif TILE_CHAR[t] is None:
                    ents.setdefault("unknown", {})
                    ents["unknown"][t] = ents["unknown"].get(t, 0) + 1
            print("  " + "".join(row))
        enems = sorted((str(k), v) for k, v in ents["enemies"].items())
        print("  start=%s exit=%s bolts=%d enemies=%s platforms=%d "
              "zips=%d infolinks=%d targets=%d unknown=%s"
              % (ents["start"], ents["exit"], ents["bolts"],
                 enems, ents["platforms"], ents["zips"],
                 ents["infolinks"], ents["targets"],
                 sorted(ents.get("unknown", {}).items())))


# /f2.v -- the bitmap font (ratchetandclank.a(String,int,int)): 192
# glyphs, one header byte each (high nibble = advance width, low nibble
# = run count) then that many big-endian shorts, each packing a
# fillRect run as nibbles x/y/w/h. Glyph mapping (ratchetandclank.a):
# char < 32 or 128..159 -> 0 (space), 32..127 -> c-32, 160..255 -> c-64.

def load_font(path):
    data = path.read_bytes()
    glyphs = []
    pos = 0
    for _ in range(192):
        head = data[pos]; pos += 1
        runs = head & 0x0F
        entry = [head >> 4]
        for _ in range(runs):
            entry.append((data[pos] << 8) | data[pos + 1])
            pos += 2
        glyphs.append(entry)
    return glyphs, pos, len(data)


def render_font(glyphs, text, spacing=1):
    def idx(c):
        if c < 32 or 128 <= c < 160:
            return 0
        return c - 32 if c < 128 else c - 64

    rows = []
    pen = 0
    for ch in text:
        g = glyphs[idx(ord(ch))]
        width = g[0] + spacing
        while len(rows) < 16:
            rows.append([])
        for r in rows:
            r.extend([False] * (pen + width - len(r)))
        for run in g[1:]:
            x, y, w, h = ((run >> 12) & 15, (run >> 8) & 15,
                          (run >> 4) & 15, run & 15)
            for yy in range(y, min(y + h, 16)):
                for xx in range(pen + x, min(pen + x + w, pen + width)):
                    rows[yy][xx] = True
        pen += width
    out = ["".join("#" if c else " " for c in row[:pen]) for row in rows]
    while out and not out[-1].strip():
        out.pop()
    return out


def cmd_font(text=None):
    glyphs, pos, size = load_font(EXT / "f2.v")
    check(pos == size, "f2.v: consumed %d of %d bytes" % (pos, size))
    check(len(glyphs) == 192, "f2.v: %d glyphs (expected 192)" % len(glyphs))
    nonblank = sum(1 for g in glyphs if len(g) > 1)
    print("=== f2.v: %d bytes -> 192 glyphs (%d with pixels), "
          "consumed exactly" % (size, nonblank))
    a1 = ROOT / "goingmobile" / "extracted_a1" / "f2.v"
    if a1.exists():   # same format, same file in the newer build
        g2, p2, s2 = load_font(a1)
        check(p2 == s2, "a1 f2.v: consumed %d of %d" % (p2, s2))
        print("    (a1's identical f2.v also parses exactly)")
    if text is None:
        text = "Ratchet & Clank"
    print("\n--- render of %r ---" % text)
    for l in render_font(glyphs, text):
        print("  |%s|" % l)


# /p, /q, /r -- the enemy/player animation and geometry tables.
# Enemy.b("/p"): 5 types x 6 anims, each {count, extra, count bytes}.
# Enemy.a("/q"): 8 consecutive 5-byte groups (odd ones scaled *22/44).
# Player.a("/r"): 15 anims, each {count, extra, count bytes}.

def cmd_anims():
    # /p -- enemy animation table
    data = (EXT / "p").read_bytes()
    pos = 0
    print("=== /p: %d bytes -- enemy animations, 5 types x 6 anims ===" %
          len(data))
    for t in range(5):
        row = []
        for a in range(6):
            cnt, extra = data[pos], data[pos + 1]
            frames = list(data[pos + 2:pos + 2 + cnt])
            pos += 2 + cnt
            row.append("%d:%s" % (extra, "".join(map(str, frames))))
        print("  type %d: %s" % (t, "  ".join(row)))
    check(pos == len(data), "/p: consumed %d of %d" % (pos, len(data)))

    # /q -- enemy geometry (8 tables x 5 types, odd groups scaled *22/44)
    data = (EXT / "q").read_bytes()
    names = ["hitbox x-off", "hitbox y-off", "hitbox width", "hitbox height",
             "group-e", "group-f", "group-g", "group-h"]
    print("\n=== /q: %d bytes -- enemy geometry, 8 tables x 5 types ===" %
          len(data))
    for g in range(8):
        vals = [b * 22 // 44 if g % 2 == 0 else b
                for b in data[g * 5:g * 5 + 5]]
        print("  %-14s %s" % (names[g], vals))
    check(len(data) == 40, "/q: %d bytes (expected 40)" % len(data))

    # /r -- player animation table
    data = (EXT / "r").read_bytes()
    pos = 0
    print("\n=== /r: %d bytes -- player animations, 15 anims ===" % len(data))
    for a in range(15):
        cnt, extra = data[pos], data[pos + 1]
        frames = list(data[pos + 2:pos + 2 + cnt])
        pos += 2 + cnt
        print("  anim %2d: extra=%-3d frames=%s" % (a, extra, frames))
    check(pos == len(data), "/r: consumed %d of %d" % (pos, len(data)))


def main():
    cmd = sys.argv[1] if len(sys.argv) > 1 else "all"
    if cmd == "menus":
        cmd_menus()
    elif cmd == "levels":
        cmd_levels(sys.argv[2] if len(sys.argv) > 2 else None)
    elif cmd == "font":
        text = None
        if "--text" in sys.argv:
            text = sys.argv[sys.argv.index("--text") + 1]
        cmd_font(text)
    elif cmd == "anims":
        cmd_anims()
    elif cmd == "all":
        cmd_menus()
        cmd_levels()
        cmd_font()
        cmd_anims()
    else:
        sys.exit(__doc__)
    if FAILURES:
        print("\n%d VALIDATION FAILURE(S)" % len(FAILURES))
        sys.exit(1)
    print("\nALL VALIDATIONS PASSED")


if __name__ == "__main__":
    main()



