# Going Mobile -- asset formats

All paths below are relative to `extracted/` (regenerate with
`python ../../tools/extract_jar.py goingmobile`). Where noted, the
`extracted_a1/` build (unobfuscated asset names) confirms the format.

## Confirmed from the loader code

| File(s) | Format | Evidence |
|---|---|---|
| `m.txt`, `m_fr.txt`, `m_ge.txt`, `m_it.txt`, `m_sp.txt` | Plain text, one string per `\r\n`-terminated line | `ratchetandclank.a("/m" + f.B[f.C], 145)` reads 145 strings for the main string table; `f.B` = `{"", "_fr", "_ge", "_it", "_sp"}` selected by language setting `f.C`. First entries: `Main Menu`, `New Game`, `Load Game`, ... a1's `txt_en.txt` is the same table in a newer build (`\n`-separated, more strings). |
| `about*.txt`, `help*.txt`, `credits*.txt` | Plain text, word-wrapped at load | `ratchetandclank.b(path, width)` -- the same per-line text reader feeding a word-wrap state machine (`ratchetandclank.a(char, int)`). `_fr/_ge/_it/_sp` variants per language. |
| `n1` .. `n12` | **Level tilemaps: 12 levels, one 28x18-byte grid per level (504 bytes), tile id = raw byte - 0x20** | `c.a(int)`: `getResourceAsStream("/n" + level)`, triple loop into `byte[12][28][18]` (`d[level][x][y]`), `read() - 32` per cell. 12 x 504-byte files = one grid each; the boss level (`f.Q == 11`) sets `g = 12` and reads all 12 grids from one stream. Cells render as 22px-wide columns / 14px-tall rows. Raw bytes are ASCII-printable (`0x20`..`0x2c`, `0xa1`..`0xa3` = special tiles) so the maps are readable in any hexdump. |
| (RMS, not a file) | **Save games: record store `RANDCSm`, 3 slots x 220 bytes + record 4 = 3-byte settings** | `ratchetandclank.c()/d()/e()` -- big-endian `int`/`short` put/get helpers at fixed offsets (`c[0]` = slot-in-use flag, int at 195 = progress). |
| `f2.v` | **Custom animation/"video" format** (the intro cutscene player) | `new a("/f2.v", 10, 1)` in `f.N()` -- class `a`'s constructor takes (path, frame count?, variant?); 1740 bytes / 10 frames = 174 bytes per frame, so frames are tiny -- likely palette/delta-compressed 2D frames drawn onto the canvas, not a video codec. a1 adds `f3.v` (13 frames) played back-to-back with `f2.v`. Parser in `decompiled/a.java` / a1's `decompiled_a1/a.java` -- **not yet read through**. |
| `q` | Enemy sprite box table, 40 bytes | `d.a("/q")`; **byte-identical to a1's `enemy_spr_box.bin`** (same md5), which names it. First 20 bytes = three groups of 5 widths (0x0e/0x08/0x1c/0x24), then box/offset bytes. |
| `o` | Level/entity tuning table, 297 bytes | loaded by `f.a("/o")` at every level start (`f.java` lines ~342 and ~1834) and reloaded on restart. Small non-negative ints with `ff` sentinels -- per-level spawn/parameter data. Parser: `f.java` -- **not yet read through**. |
| `p` | Sprite/animation index table, 99 bytes | `d.b("/p")` right before `/q`. Pairs of small indices into sprite sheets (`a`..`h.png`) -- animation frame sequences. **Not yet read through.** |
| `r` | Entity animation table, 50 bytes | `a.a("/r")` in class `a`'s init; small-index pairs like `p`. **Not yet read through.** |
| `*.mid` (`box`, `bubble`, `death`, `killen`, `menu`, `port`, `shoot`) | Standard MIDI | No RE needed; needs a MIDI synth (or wave-table replacement) in the port. `menu.mid` byte-identical in the a1 build. |
| `*.png` (`a`..`h`, `p`, `hhg`, `icon`, `logo`, `sony`, `uc`) | Standard PNG sprite sheets | No RE needed; sprite-cell geometry comes from the code (`d`/`a` tables above). |
| `t` | **Unknown -- 35 bytes, referenced nowhere in the decompiled code.** | No `"/t"` string in any build. Possibly dead data or loaded via a computed name; flagged as an open question. |

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
