# Going Mobile -- asset formats

All paths below are relative to `extracted/` (regenerate with
`python ../../tools/extract_jar.py goingmobile`). Where noted, the
`extracted_a1/` build (unobfuscated asset names) confirms the format.

## Confirmed from the loader code

| File(s) | Format | Evidence |
|---|---|---|
| `m.txt`, `m_fr.txt`, `m_ge.txt`, `m_it.txt`, `m_sp.txt` | Plain text, one string per `\r\n`-terminated line | `ratchetandclank.a("/m" + f.B[f.C], 145)` reads 145 strings for the main string table; `f.B` = `{"", "_fr", "_ge", "_it", "_sp"}` selected by language setting `f.C`. First entries: `Main Menu`, `New Game`, `Load Game`, ... a1's `txt_en.txt` is the same table in a newer build (`\n`-separated, more strings). Confirmed indices (phase 1): 31/32 save-slot labels, 48-55 weapon names (Lancer ... R.Y.N.O.), 56/57 store labels, 63-70 results labels, 72-74 messages, 95 title, 101-110 level-select entries, 111 score, 112+ level intro lines, 133-143 name-entry screens. |
| `about*.txt`, `help*.txt`, `credits*.txt` | Plain text, word-wrapped at load | `ratchetandclank.b(path, width)` -- the same per-line text reader feeding a word-wrap state machine (`ratchetandclank.a(char, int)`). `_fr/_ge/_it/_sp` variants per language. |
| `n1` .. `n12` | **Level tilemaps: 12 levels, one 28x18-byte grid per level (504 bytes), tile id = raw byte - 0x20** | `LevelMap.a(int)`: `getResourceAsStream("/n" + level)`, triple loop into `byte[12][28][18]`, `read() - 32` per cell. 12 x 504-byte files = one grid each; the boss level (`Game.Q == 11`) reads all 12 grids from one stream. **Full tile legend confirmed** (phase 1, `CLASS_MAP.md`): 0-3 background, 4-13 solid, 43-46 titanium bolts, 47-55/114-127 enemies, 56-61 hazards, 62-69 zip lines, 97/98 hyper-shot targets, 102-113 platforms, -125 (raw 0x20) player start. |
| (RMS, not a file) | **Save games: record store `RANDCSm`, 3 slots x 220 bytes + record 4 = 3-byte settings. Byte-exact layout confirmed from `Game.a(byte[])`/`b(byte[])`**: offset 0 = slot in use; 1-4 level-state mask; 5-8/9-12 titanium-bolt masks (area 1/2); 13-16 collectibles mask; 17 owned-weapons bitfield; 18-25 weapon levels; 26-41 weapon XP (8 shorts); 42-57 ammo (8 shorts); 58-61 bolt count; 195-198 level elapsed ms; 204-207 collectibles mask 2; 208-211 total titanium bolts; 212-215 intro-seen mask; 216-219 total kills. (62-194 unused.) | `Game.a(byte[])` (serialize) / `Game.b(byte[])` (deserialize), written through `ratchetandclank.a(int)/(int)` via the `RANDCSm` record store. |
| `f2.v` | **Bitmap font: 192 glyph records, drawn as `fillRect` runs** | `ratchetandclank.a("/f2.v", 10, 1)` (the "video player" guess from phase 0 was wrong -- this is the font loader): per glyph one header byte (high nibble = advance width, low nibble = run count) then that many big-endian shorts encoding x/y/w/h rectangles; the args are line height (10) and letter spacing (1). Used by every menu/HUD text draw. a1 adds `f3.v` (13px line height) for its larger UI. |
| `q` | **Enemy geometry, 40 bytes = 8 tables x 5 enemy types, alternating half/double scale** | `Enemy.a("/q")` reads 8 consecutive 5-byte groups (odd ones scaled `x*22/44`); `Enemy.a/b/c/d` = per-type hitbox x-offset/y-offset/width/height (used by every enemy-vs-player/shot test), `e..h` = the second geometry group. Byte-identical to a1's `enemy_spr_box.bin` (same md5), which names it. |
| `o` | **Menu definitions, 297 bytes binary** (the phase-0 "level tuning" guess was wrong) | `Game.a("/o")`: byte menu count; per menu: item count byte, title-string byte, left/right softkey-string bytes, back-target byte; then 3 bytes per item (type, action, string id) -> the `H` vector of `MenuItem[]` pages. Loaded once at splash; the item-count byte for page 2 is incremented by a code hack (an injected extra item). |
| `p` | **Enemy animation table, 99 bytes: 5 types x 6 anims, each {frame count, extra byte, count frame indices}** | `Enemy.b("/p")` reads 5x6 pairs + frame bytes into `ANIM_FRAMES[5][6][5]`/`ANIM_FRAME_COUNTS`/`ANIM_FRAME_EXTRA`. |
| `r` | **Player animation table, 50 bytes: 15 anims, each {frame count, extra byte, up to 4 frame indices}** | `Player.a("/r")` into `ANIM_FRAMES[1][15][5]` etc.; frame indices select rows of the geometry table `Player.c` (100 entries). |
| `*.mid` (`box`, `bubble`, `death`, `killen`, `menu`, `port`, `shoot`) | Standard MIDI | No RE needed; needs a MIDI synth (or wave-table replacement) in the port. `menu.mid` byte-identical in the a1 build. Played one-at-a-time by `SoundPlayer` (MMAPI, `audio/midi`). |
| `*.png` (`a`..`h`, `p`, `hhg`, `icon`, `logo`, `sony`, `uc`) | Standard PNG sprite sheets | No RE needed; confirmed roles (phase 1): `a` = tileset, `b` = rotating enemy segments, `c` = player, `d` = actors/platforms/boss, `e` = titanium-bolt boxes, `f` = weapon in hand, `g` = HUD icons, `h` = 8x8 projectiles/pickups, `p` = dialogue portraits, `uc` = digit strip, `sony`/`hhg`/`logo` = splash chain, `icon` = MIDlet icon. |
| `t` | **Dead data -- 35 bytes, referenced by no code path** | Byte-identical in the two builds that share this code (canonical and `(a)`), absent from the a1 build's file list, and every resource load in all three decompiled trees is accounted for without it (`/n*`, `/m*`, `/o`, `/p`, `/q`, `/r`, `/f2.v`, `/help`/`about`/`credits`, sounds, `.png`s). Leftover from an earlier build; the port should never load it. (Settled by the cross-build comparison, `../docs/BUILD_COMPARISON.md`.) |

## a1-build-only files (formats to document as they get read)

These exist only in `extracted_a1/`, named; they are the same game's newer
data pipeline and the Rosetta stone for the canonical build's `o`/`p`/`r`:

| File(s) | Format | Notes |
|---|---|---|
| `level0.bin` .. `level12.bin` | Tilemaps, same cell scheme as `n*` but multi-grid per file | Sizes are multiples of 504 (504, 1512, 2016, 2520, 3024) = 1/3/4/5/6 grids of 28x18 -- bigger levels than the canonical build's one-grid-per-file. `mapData.txt` indexes them. |
| `mapData.txt` | Plain text: `level,x,y,\r\n` per line | 62 lines -- world-map node positions (level index + coordinates on the galactic map screen), read by `h.t()` (`decompiled_a1/h.java` line ~2648). |
| `enemy.bin` (149B), `player.bin` (58B) | Binary animation/state scripts | Small-index byte streams -- per-entity animation sequences, almost certainly the readable equivalents of canonical `p`/`r`. **Not yet read through.** |
| `txt_en/fr/gr/it/sp.txt` | `\n`-separated string tables | Superset of canonical `m*.txt` (includes per-level names: `Circuit Circuit`, `Battleland`, `Communication Station`, ...). |
| `*.wav` (`box`, `bubble`, `death`, `msound`, `shoot`) | Standard WAV (MIDP-2.0 audio) | Replaces some of the canonical build's `.mid`s on MIDP-2.0 handsets. |
| `bg_*.png`, `en_*.png`, named sprites (`clank.png`, `ratcht.png`, `weapon.png`, ...) | Standard PNG | Named per-role, unlike canonical `a`..`h.png`. |

## How to make progress on these

Everything unknown is crackable the same way: find the
`getResourceAsStream`/`read` call sites in `decompiled/f.java`,
`d.java`, `a.java` (canonical) and `decompiled_a1/h.java`, `g.java`,
`b.java` (a1) and work the field layout out from the loops. For each
canonical asset, first content-match against a1's named files (md5-level,
the way `q` == `enemy_spr_box.bin` was confirmed), then read whichever
build's parser is cleaner. Once a format is confirmed, add a
`tools/parse_*.py` for it.
