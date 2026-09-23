# Going Mobile -- asset formats

> **Target changed 2026-09-22** to `roms/RAC-GoingMobile-a1.jar` -- see
> `ROADMAP.md`. The "Confirmed from the loader code" section below is the
> **canonical/legacy build's** phase-2 work (`extracted/`, paths below
> relative to it), kept as a reference/pattern library; it's no longer
> the port target's own asset set. a1's own formats are in the
> "a1-build-only formats" section, relative to `extracted_a1/`.

All paths in the first section below are relative to `extracted/`
(regenerate with `python ../../tools/extract_jar.py goingmobile`). Where
noted, the `extracted_a1/` build (unobfuscated asset names) confirms the
format.

## Confirmed from the loader code

| File(s) | Format | Evidence |
|---|---|---|
| `m.txt`, `m_fr.txt`, `m_ge.txt`, `m_it.txt`, `m_sp.txt` | Plain text, one string per `\r\n`-terminated line | `ratchetandclank.a("/m" + f.B[f.C], 145)` reads 145 strings for the main string table; `f.B` = `{"", "_fr", "_ge", "_it", "_sp"}` selected by language setting `f.C`. First entries: `Main Menu`, `New Game`, `Load Game`, ... a1's `txt_en.txt` is the same table in a newer build (`\n`-separated, more strings). Confirmed indices (phase 1): 31/32 save-slot labels, 48-55 weapon names (Lancer ... R.Y.N.O.), 56/57 store labels, 63-70 results labels, 72-74 messages, 95 title, 101-110 level-select entries, 111 score, 112+ level intro lines, 133-143 name-entry screens. |
| `about*.txt`, `help*.txt`, `credits*.txt` | Plain text, word-wrapped at load | `ratchetandclank.b(path, width)` -- the same per-line text reader feeding a word-wrap state machine (`ratchetandclank.a(char, int)`). `_fr/_ge/_it/_sp` variants per language. |
| `n1` .. `n10`, `n12` | **Level tilemaps: 11 files (level 11 is deliberately skipped -- the level select maps "11" to the boss 12), one 28x18-byte grid per level (504 bytes), tile id = raw byte - 0x20, column-major** | `LevelMap.a(int)`: `getResourceAsStream("/n" + level)`, triple loop into `byte[12][28][18]`, `read() - 32` per cell. Cells render as 22px columns / 14px rows; raw bytes are ASCII-printable. The `Q == 11` path that reads 12 grids from one stream is dead code in this build (no n11 exists; the level select bumps 11 -> 12). **Full tile legend confirmed and tool-verified** (`tools/parse_gm.py levels`): 0-3 background, 4-13 solid, 35 exit, 43-46 titanium bolts, 47-55 enemies (types 0/2/1, tiles 50-52 drop a bolt), 56-61 hazards, 62-69 zips, 77-96 infolinks, 97/98 hyper-shot targets, 102-113 platforms, 114-127/0x80 enemies, 0xA3 player start, 0xA1/0xA2 + 0x84..0xC4 camera/marker tiles. |
| (RMS, not a file) | **Save games: record store `RANDCSm`, 3 slots x 220 bytes + record 4 = 3-byte settings. Byte-exact layout confirmed from `Game.a(byte[])`/`b(byte[])`**: offset 0 = slot in use; 1-4 level-state mask; 5-8/9-12 titanium-bolt masks (area 1/2); 13-16 collectibles mask; 17 owned-weapons bitfield; 18-25 weapon levels; 26-41 weapon XP (8 shorts); 42-57 ammo (8 shorts); 58-61 bolt count; 195-198 level elapsed ms; 204-207 collectibles mask 2; 208-211 total titanium bolts; 212-215 intro-seen mask; 216-219 total kills. (62-194 unused.) | `Game.a(byte[])` (serialize) / `Game.b(byte[])` (deserialize), written through `ratchetandclank.a(int)/(int)` via the `RANDCSm` record store. |
| `f2.v` | **Bitmap font: 192 glyph records, drawn as `fillRect` runs** | `ratchetandclank.a("/f2.v", 10, 1)` (the "video player" guess from phase 0 was wrong -- this is the font loader): per glyph one header byte (high nibble = advance width, low nibble = run count) then that many big-endian shorts encoding x/y/w/h rectangles; the args are line height (10) and letter spacing (1). Used by every menu/HUD text draw. a1 adds `f3.v` (13px line height) for its larger UI. |
| `q` | **Enemy geometry, 40 bytes = 8 tables x 5 enemy types, alternating half/double scale** | `Enemy.a("/q")` reads 8 consecutive 5-byte groups (every 2nd group scaled `x*22/44`); `Enemy.a/b/c/d` = per-type hitbox **x-offset / y-offset / width / height** (used by every enemy-vs-player/shot test), `e..h` = the second geometry group. Tool-verified (`tools/parse_gm.py anims`): the hitbox is uniform across all 5 types (x-7, y+8, 14x36) and only type 1 carries extra group values (1, 17, 21 -- the boss parts). Byte-identical to a1's `enemy_spr_box.bin` (same md5), which names it. |
| `o` | **Menu definitions, 297 bytes binary** (the phase-0 "level tuning" guess was wrong) | `Game.a("/o")`: byte menu count; per menu: item count byte, title-string byte, left/right softkey-string bytes, back-target byte; then **3 bytes per item: type, string id, action** (string id is used `& 0xFF` at every access -- negative ids reach the 128-145 block, e.g. the injected row -112 -> 144 "Language"; its action -104 opens the language screen). Page 2's count byte is one less than its real rows (the loader injects the Language row). Tool-verified (`tools/parse_gm.py menus`): all 22 pages parse with exact byte consumption and every string resolves. |
| `p` | **Enemy animation table, 99 bytes: 5 types x 6 anims, each {frame count, extra byte, count frame indices}** | `Enemy.b("/p")` reads 5x6 pairs + frame bytes into `ANIM_FRAMES[5][6][5]`/`ANIM_FRAME_COUNTS`/`ANIM_FRAME_EXTRA`. |
| `r` | **Player animation table, 50 bytes: 15 anims, each {frame count, extra byte, up to 4 frame indices}** | `Player.a("/r")` into `ANIM_FRAMES[1][15][5]` etc.; frame indices select rows of the geometry table `Player.c` (100 entries). |
| `*.mid` (`box`, `bubble`, `death`, `killen`, `menu`, `port`, `shoot`) | Standard MIDI | No RE needed; needs a MIDI synth (or wave-table replacement) in the port. `menu.mid` byte-identical in the a1 build. Played one-at-a-time by `SoundPlayer` (MMAPI, `audio/midi`). |
| `*.png` (`a`..`h`, `p`, `hhg`, `icon`, `logo`, `sony`, `uc`) | Standard PNG sprite sheets | No RE needed; confirmed roles (phase 1): `a` = tileset, `b` = rotating enemy segments, `c` = player, `d` = actors/platforms/boss, `e` = titanium-bolt boxes, `f` = weapon in hand, `g` = HUD icons, `h` = 8x8 projectiles/pickups, `p` = dialogue portraits, `uc` = digit strip, `sony`/`hhg`/`logo` = splash chain, `icon` = MIDlet icon. |
| `t` | **Dead data -- 35 bytes, referenced by no code path** | Byte-identical in the two builds that share this code (canonical and `(a)`), absent from the a1 build's file list, and every resource load in all three decompiled trees is accounted for without it (`/n*`, `/m*`, `/o`, `/p`, `/q`, `/r`, `/f2.v`, `/help`/`about`/`credits`, sounds, `.png`s). Leftover from an earlier build; the port should never load it. (Settled by the cross-build comparison, `../docs/BUILD_COMPARISON.md`.) |

## a1-build-only formats: confirmed and tool-verified

a1's phase 1 (`src_a1/`, see `CLASS_MAP.md`) read through every loader for
these; `tools/parse_gm_a1.py` replicates each one against
`extracted_a1/` and **all validations pass** (see "Status" below).

| File(s) | Format | Evidence |
|---|---|---|
| `level0.bin` .. `level12.bin` | **Tilemaps, same cell scheme as the canonical build's `n*` (28x18, column-major, tile id = raw byte - 0x20) but multi-grid per file: `LEVEL_ROOM_TABLES[level][0]` "logical room" grids read consecutively.** File size is `distinct_physical_grids x 504`, where distinct grids come from that level's `subGridIndexMap` (several logical rooms can share one physical grid -- confirmed exactly against every file's byte count). | `LevelMap.loadLevelFile(int)`/`loadRoomTable(int)` (`src_a1/LevelMap.java`). **Level 11 edge case**: its table declares 12 logical rooms over only 6 distinct grids (`index_map=[0,1,2,3,0,4,1,2,5,3,4,5]`); the loader's read loop uses the *logical* room count (12) as its bound, so it reads 6 grids' worth past the real 3024-byte file, and `InputStream.read()` returning -1 (EOF) produces tile id `-33` for `subGrids[6..11]`. Those slots are never selected by `getTile()` (the index map only ever points at 0-5), so this never surfaces in normal play, but a port must not replicate the OOB-style read verbatim -- clamp the room-table read loop to the data actually present, mirroring the canonical build's own documented "must bounds-check instead of replicating" lesson below. |
| `mapData.txt` | **Plain text, two blocks, 105 lines total: 62 lines of `id,x,y,` (world-map node positions) then 43 lines of `a,b,` (node-to-node edges, the lines drawn between planets on the map screen).** Every edge's endpoints resolve to a real node id. | `Game.t()` (`src_a1/Game.java:2739`, unrenamed -- see `Game`'s CLASS_MAP.md section): `this.dd[62][3]` (shorts) then `this.de[43][2]` (bytes), each block terminated by scanning for `\n`. |
| `enemy_spr_box.bin` | **40 bytes -- 8 tables x 5 enemy types (x-off/y-off/width/height for hitbox, then attack), same shape as the canonical build's `/q`.** Odd/even tables are scaled by `Game.J`/`Game.K` (both currently 44 in this build, so scaling is a no-op on the shipped data). Hitbox is uniform across all 5 types (14, 8, 28, 36); only type 1 (the boss) carries nonzero attack geometry (2, 0, 35, 21). | `Enemy.loadHitboxTables(String)` (`src_a1/Enemy.java:761`). |
| `enemy.bin` | **149 bytes -- 5 enemy types x 8 anim slots, each `{frameCount, extraTicks}` byte pair + `frameCount` frame-index bytes.** Same shape as the canonical build's `/p`, but 8 anim slots per type instead of 6. | `Enemy.loadAnimTables(String)` (`src_a1/Enemy.java:791`). |
| `player.bin` | **58 bytes -- 15 anim slots, same `{frameCount, extraTicks}` + frame-index-bytes shape as one enemy.bin type.** Same shape as the canonical build's `/r`. | `Player.loadAnimFile(String)` (`src_a1/Player.java:1408`). |
| `f3.v` | **Bitmap font, byte-identical format to the already-confirmed `f2.v`** (192 glyph records, `fillRect`-run encoding) -- 1778 bytes, consumed exactly, 192 glyphs. Constructed with line height 13 (vs. `f2.v`'s 10) and spacing 1; the point size isn't stored in the file, it's a constructor arg. | `ratchetandclank.largeFont = new Font("/f3.v", 13, 1)` (`Game`'s boot step, `src_a1/Game.java` header comment). |
| `txt_en/fr/gr/it/sp.txt` | `\n`-separated string tables | Superset of canonical `m*.txt` (includes per-level names: `Circuit Circuit`, `Battleland`, `Communication Station`, ...). Loaded by `ratchetandclank.loadStrings()` (already confirmed, phase 1). Byte-level format not re-parsed here (plain line-per-string, same as the canonical build's `m*.txt`, already documented above) -- nothing new to confirm. |
| `*.wav` (`box`, `bubble`, `death`, `msound`, `shoot`) | Standard WAV (MIDP-2.0 audio) | No RE needed. Replaces some of the canonical build's `.mid`s on MIDP-2.0 handsets; loaded by `SoundPlayer` (already confirmed, phase 1), same one-at-a-time playback model as the canonical build. |
| `bg_*.png`, `en_*.png`, named sprites (`clank.png`, `ratcht.png`, `weapon.png`, ...) | Standard PNG | No RE needed. Named per-role, unlike canonical `a`..`h.png`; roles confirmed by `Game.java`'s boot-step image loads (phase 1). |

## Status: every format in both builds is tool-verified

**Canonical/legacy build**: `tools/parse_gm.py` parses each format in the
first section by replicating its loader byte-for-byte, and **all
validations pass**: exact byte consumption for `/o` (297/297), `f2.v`
(1740/1740, also the a1 copy), `/p` (99/99), `/r` (50/50), `/q` (40/40)
and all 11 `n*` files (504 bytes, zero unexplained tiles); the font
renders readable text from its rect runs; the menu tree resolves every
string. Two things the parsers exposed, now folded into the table above:

- The shipped level set is `n1..n10` + `n12` -- **there is no n11**: the
  level select deliberately maps "11" to the boss (12), so the `Q == 11`
  12-grid read in `LevelMap` is dead code in this build.
- The special-tile continuation logic indexes `tiles[x][y+1]`,
  `tiles[x][y+2]` and `tiles[x+1][y]` -- out of bounds for a boundary
  tile in a `byte[28][18]` array. The shipped levels never place special
  tiles there, so the original never crashes; **the port must
  bounds-check instead of replicating this**.

**a1 (the current target)**: `tools/parse_gm_a1.py` parses every
a1-build-only format the same way, against `extracted_a1/`, and **all
validations pass**: exact byte consumption for `enemy.bin` (149/149),
`player.bin` (58/58), `f3.v` (1778/1778), all 13 `level*.bin` files
(byte count matches each level's distinct-physical-grid count x 504),
and `mapData.txt` (105/105 lines, every edge resolves to a real node
id). One thing the parser exposed, folded into the table above: level
11's room table claims more logical rooms (12) than the file has
physical grids for (6) -- an a1-specific cousin of the canonical build's
"n11 doesn't exist" finding, same "port must bounds-check, not
replicate" conclusion.

Both builds' phase 2 is now done; nothing asset-format-related remains
unparsed for either.
