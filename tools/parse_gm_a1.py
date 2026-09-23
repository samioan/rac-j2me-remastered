#!/usr/bin/env python3
"""Phase 2 parsers for the a1 build's own asset formats -- the ones that
don't exist in the canonical build (see goingmobile/src_a1/ for the
loaders these replicate). Sibling to tools/parse_gm.py (canonical build);
kept separate since a1 is not the port target, just the current
decompilation target (see goingmobile/docs/ROADMAP.md's target-change
note). Output goes to stdout -- derived from copyrighted game data, must
not be committed (see NOTICE.md).

Usage:
    python tools/parse_gm_a1.py             # everything, with validations
    python tools/parse_gm_a1.py levels [n]
    python tools/parse_gm_a1.py mapdata
    python tools/parse_gm_a1.py anims
    python tools/parse_gm_a1.py font
"""
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
EXT = ROOT / "goingmobile" / "extracted_a1"
FAILURES = []


def check(cond, msg):
    if not cond:
        FAILURES.append(msg)
        print("FAIL: %s" % msg)
    return cond


# level<N>.bin -- LevelMap.loadLevelFile(int)/loadRoomTable(int)
# (src_a1/LevelMap.java): subGridCount consecutive 28x18 grids (column-
# major, tile id = raw byte - 0x20, same cell scheme as the canonical
# build's n<level>), subGridCount taken from LEVEL_ROOM_TABLES[level][0]
# -- the same compiled-in room table LevelMap.java carries. Reproduced
# verbatim here (not re-derived) so this parser tests the *files*
# against the *already-transcribed* loader, not a fresh guess.
LEVEL_ROOM_TABLES = [
    [1, -1, -1, -1, -1, 0, 2],
    [6, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, 5, 3, -1, -1, -1, 4, 0, 1, 2, 3, 4, 5, 0],
    [4, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, -1, 2, 0, 1, 2, 3, 1],
    [5, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, -1, 3, 4, 0, 1, 2, 3, 2],
    [6, -1, -1, 1, -1, -1, 2, 3, 0, 1, -1, 4, -1, -1, 4, -1, 1, 3, -1, 5, 2, -1, -1, -1, 4, 0, 1, 3, 2, 4, 5, 2],
    [5, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, -1, 3, 2, 1, 0, 3, 4, 2],
    [5, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, -1, 3, 0, 1, 2, 3, 4, 0],
    [3, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, -1, 1, 0, 2, 1, 0],
    [4, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, -1, 2, 0, 1, 2, 3, 1],
    [6, -1, 1, 2, -1, 0, -1, 3, -1, -1, 3, 4, 0, 2, -1, -1, 1, -1, -1, 5, 2, -1, -1, -1, 4, 0, 4, 3, 5, 1, 2, 1],
    [6, -1, -1, 1, -1, -1, -1, 2, 0, -1, -1, 3, 1, -1, -1, 4, 2, -1, -1, 5, 3, -1, -1, -1, 4, 0, 1, 2, 3, 5, 4, 2],
    [12,
     -1, 1, 8, -1, 0, 2, 9, -1, 1, 3, 10, -1, 2, 4, 11, -1, 3, 5, -1, -1, 4, 6, -1, -1, 5, 7, -1, -1, 6, -1, -1, -1,
     -1, 9, -1, 0, 8, 10, -1, 1, 9, 11, -1, 2, 10, -1, -1, 3,
     0, 1, 2, 3, 0, 4, 1, 2, 5, 3, 4, 5,
     1],
    [1, -1, -1, -1, -1, 0, 2],
]


def parse_room_table(level):
    tab = LEVEL_ROOM_TABLES[level]
    room_count = tab[0]
    pos = 1
    neighbors = [tab[pos + i * 4:pos + i * 4 + 4] for i in range(room_count)]
    pos += room_count * 4
    index_map = tab[pos:pos + room_count]
    pos += room_count
    start = tab[pos]
    return room_count, neighbors, index_map, start


def cmd_levels(which=None):
    levels = [int(which)] if which is not None else range(13)
    for n in levels:
        room_count, neighbors, index_map, start = parse_room_table(n)
        distinct_grids = len(set(index_map))
        expected = distinct_grids * 504
        path = EXT / ("level%d.bin" % n)
        data = path.read_bytes()
        print("\n=== level%d.bin: %d bytes -- %d logical rooms, "
              "%d distinct physical grids (index_map=%s), start=room %d ==="
              % (n, len(data), room_count, distinct_grids, index_map, start))
        check(len(data) == expected,
              "level%d.bin: %d bytes (expected %d = %d distinct grids x 504)"
              % (n, len(data), expected, distinct_grids))
        # Read exactly like LevelMap.loadLevelFile: subGridCount (room_count)
        # consecutive grids, NOT distinct_grids -- this is the loader's own
        # loop bound, taken verbatim from src_a1/LevelMap.java. When
        # room_count > distinct_grids (level 11: 12 rooms, 6 grids) the loop
        # runs past the end of the file.
        pos = 0
        grids_read_from_real_data = 0
        ran_dry_at = None
        for room in range(room_count):
            for x in range(28):
                for y in range(18):
                    if pos < len(data):
                        pos += 1
                    elif ran_dry_at is None:
                        ran_dry_at = room
            if pos <= len(data):
                grids_read_from_real_data = room + 1
        if ran_dry_at is not None:
            print("  ** loadLevelFile's read loop (subGridCount=%d) reads "
                  "past end of file starting at logical grid slot %d of "
                  "%d bytes available -- InputStream.read() returns -1 "
                  "(EOF) for every remaining cell there, so subGrids[%d..%d] "
                  "get tile id (-1 - 32) as byte = -33 instead of real "
                  "data. index_map only ever points at slots 0..%d, so no "
                  "room *should* select those slots via getTile(), but the "
                  "read loop fills them regardless (it reads by position, "
                  "not by index_map) **"
                  % (room_count, ran_dry_at, len(data),
                     ran_dry_at, room_count - 1, distinct_grids - 1))


# mapData.txt -- Game.t() (src_a1/Game.java:2739): 62 lines of
# "id,x,y," (world-map node positions, this.dd[62][3] shorts) followed
# by 43 lines of "a,b," (node-to-node edges for the map's connecting
# lines, this.de[43][2] bytes). Confirmed by both the loader code and
# the file's own line count (62 + 43 = 105, matches exactly).

def cmd_mapdata():
    lines = (EXT / "mapData.txt").read_text().splitlines()
    check(len(lines) == 105, "mapData.txt: %d lines (expected 62 + 43 = 105)"
          % len(lines))
    nodes = []
    for line in lines[:62]:
        parts = [p.strip() for p in line.split(",") if p.strip() != ""]
        check(len(parts) == 3, "mapData.txt node line %r: %d fields (want 3)"
              % (line, len(parts)))
        nodes.append(tuple(int(p) for p in parts))
    edges = []
    for line in lines[62:105]:
        parts = [p.strip() for p in line.split(",") if p.strip() != ""]
        check(len(parts) == 2, "mapData.txt edge line %r: %d fields (want 2)"
              % (line, len(parts)))
        edges.append(tuple(int(p) for p in parts))
    print("=== mapData.txt: %d node lines, %d edge lines ===" %
          (len(nodes), len(edges)))
    print("  nodes (id,x,y): %s ... %s" % (nodes[:3], nodes[-3:]))
    print("  edges (a,b):    %s ... %s" % (edges[:3], edges[-3:]))
    node_ids = {n[0] for n in nodes}
    bad = [e for e in edges if e[0] not in node_ids or e[1] not in node_ids]
    check(not bad, "mapData.txt: %d edges reference an id not in the node "
          "list: %s" % (len(bad), bad[:5]))


# enemy_spr_box.bin -- Enemy.loadHitboxTables (src_a1/Enemy.java:761):
# 8 x 5-byte tables (HITBOX x/y/w/h, ATTACK x/y/w/h), odd tables (0-
# indexed: x-offsets, widths, ...) scaled *Game.J/44, even by
# *Game.K/44 -- both currently 44 in this build, so scaling is a no-op
# on the shipped data; replicated anyway for fidelity to the loader.
#
# enemy.bin -- Enemy.loadAnimTables (src_a1/Enemy.java:791): 5 types x
# 8 anim slots, each {frameCount, extraTicks} byte pair + frameCount
# frame-index bytes.
#
# player.bin -- Player.loadAnimFile (src_a1/Player.java:1408): 15 anim
# slots, same {frameCount, extraTicks} + frameCount frame-index bytes
# shape as one "type" of enemy.bin.

def cmd_anims():
    data = (EXT / "enemy_spr_box.bin").read_bytes()
    names = ["hitbox x-off", "hitbox y-off", "hitbox width", "hitbox height",
             "attack x-off", "attack y-off", "attack width", "attack height"]
    print("=== enemy_spr_box.bin: %d bytes -- 8 tables x 5 enemy types ===" %
          len(data))
    check(len(data) == 40, "enemy_spr_box.bin: %d bytes (expected 40)"
          % len(data))
    for g in range(8):
        raw = list(data[g * 5:g * 5 + 5])
        scale = 44 if g % 2 == 0 else 44  # Game.J / Game.K, both 44 here
        vals = [(b * scale) // 44 for b in raw]
        print("  %-14s raw=%-24s scaled=%s" % (names[g], raw, vals))

    def read_anim_table(data, pos, count):
        rows = []
        for _ in range(count):
            cnt, extra = data[pos], data[pos + 1]
            frames = list(data[pos + 2:pos + 2 + cnt])
            pos += 2 + cnt
            rows.append((cnt, extra, frames))
        return rows, pos

    data = (EXT / "enemy.bin").read_bytes()
    pos = 0
    print("\n=== enemy.bin: %d bytes -- 5 enemy types x 8 anim slots ===" %
          len(data))
    for t in range(5):
        rows, pos = read_anim_table(data, pos, 8)
        print("  type %d: %s" % (t, ["%d:%s" % (e, f) for _, e, f in rows]))
    check(pos == len(data), "enemy.bin: consumed %d of %d" % (pos, len(data)))

    data = (EXT / "player.bin").read_bytes()
    rows, pos = read_anim_table(data, 0, 15)
    print("\n=== player.bin: %d bytes -- 15 player anim slots ===" %
          len(data))
    for i, (cnt, extra, frames) in enumerate(rows):
        print("  anim %2d: extra=%-3d frames=%s" % (i, extra, frames))
    check(pos == len(data), "player.bin: consumed %d of %d" % (pos, len(data)))


# f3.v -- ratchetandclank.largeFont = new Font("/f3.v", 13, 1)
# (src_a1/Game.java's boot step, case 4): same Font class/loader as the
# canonical build's f2.v (already confirmed, tools/parse_gm.py's
# load_font), just a different point size (13 vs 10) passed to the
# constructor, not stored in the file. Re-implemented here (rather than
# importing parse_gm.py, which points at extracted/, not extracted_a1/)
# to keep this script self-contained against EXT.

def load_font(path):
    data = path.read_bytes()
    pos = 0
    glyphs = []
    for _ in range(192):
        head = data[pos]; pos += 1
        runs = head & 0x0F
        entry = [head >> 4]
        for _ in range(runs):
            entry.append((data[pos] << 8) | data[pos + 1])
            pos += 2
        glyphs.append(entry)
    return glyphs, pos, len(data)


def cmd_font():
    for name, expect_height in (("f2.v", 10), ("f3.v", 13)):
        glyphs, pos, size = load_font(EXT / name)
        check(pos == size, "%s: consumed %d of %d bytes" % (name, pos, size))
        check(len(glyphs) == 192, "%s: %d glyphs (expected 192)"
              % (name, len(glyphs)))
        nonblank = sum(1 for g in glyphs if len(g) > 1)
        print("=== %s: %d bytes -> 192 glyphs (%d with pixels), "
              "consumed exactly (constructed with line height %d) ==="
              % (name, size, nonblank, expect_height))


def main():
    cmd = sys.argv[1] if len(sys.argv) > 1 else "all"
    if cmd == "levels":
        cmd_levels(sys.argv[2] if len(sys.argv) > 2 else None)
    elif cmd == "mapdata":
        cmd_mapdata()
    elif cmd == "anims":
        cmd_anims()
    elif cmd == "font":
        cmd_font()
    elif cmd == "all":
        cmd_levels()
        cmd_mapdata()
        cmd_anims()
        cmd_font()
    else:
        sys.exit(__doc__)
    if FAILURES:
        print("\n%d VALIDATION FAILURE(S)" % len(FAILURES))
        sys.exit(1)
    print("\nALL VALIDATIONS PASSED")


if __name__ == "__main__":
    main()
