# Clone Home -- asset formats

All paths below are relative to `extracted/` (regenerate with
`python ../../tools/extract_jar.py clonehome`). The jar is tiny (12
entries): 7 class files, one icon, and everything else packed into the
three `RP*` banks.

## The `RP*` container format (confirmed from `a.java`'s parser)

`a.a(DataInputStream)` (`decompiled/a.java` line ~374) parses each
`RP<n>` file as:

```
byte  flags          -- bit 0 selects the length-entry size:
                        0 -> 2-byte lengths, 1 -> 4-byte lengths
                        (all three shipped files use 0x40 -> 2-byte)
char  count          -- 16-bit big-endian number of resources
byte[count] x 2/4    -- per-resource lengths (big-endian)
byte[count]          -- per-resource flag/metadata bytes (this.d)
int   payloadSize    -- 32-bit big-endian
byte[payloadSize]    -- concatenated resource payloads
```

`a.d(id)` / `a.e(id)` fetch resource `id & 1023` (lengths are summed
sequentially -- no offset table, resources are stored in order); the bank
is selected by `id >> 10` and `a.m(id)` lazily opens `/RP<bank>`:

| Bank file | Size | Entries | Contents |
|---|---|---|---|
| `RP1` | 88,884 | 72 (`0x0048`) | 31 embedded PNGs (sprite/level art) + binary resources |
| `RP2` | 189,583 | 51 (`0x0033`) | 6 embedded PNGs (the big background/level art) + strings + data |
| `RP3` | 9,694 | 2 (`0x0002`) | 1 PNG (probably the menu/title image) + 1 resource |

(PNG counts by scanning `\x89PNG` signatures and matching `IEND` counts;
the numbers themselves are read by the loader, not stored as text.)

## Per-resource payload formats

| Type | Format | Evidence |
|---|---|---|
| Images | **Standard PNGs, embedded whole** | `a.b(DataInputStream)` skips the 8-byte PNG signature then walks chunks (32-bit length + 4CC type) until `IEND` (0x49454E44); a custom chunk type constant `"PAGG"` (0x50414747) also appears in the walker. Some resources additionally pass through a **byte-swap decode** (`a`'s two-array interleave loop just above `b()`) before being treated as images -- likely a cheap obfuscation of the pixel data. |
| String tables | **short count+1, short offsets, UTF-8 bytes** | `a.g(int)`: reads `short n`, builds `t[n+1]` offset table, packs the UTF-8 payload into `s`, serves `a.f(i)` as `new String(s, t[i], t[i+1]-t[i], "UTF-8")`. |
| Level tiles | **`sections x 28 x 18` bytes, each `tile + 32`** | `Game.loadLevelTiles`. Verified: every level resource's length is an exact multiple of 504 (3,4,5,6 or 2 sections) and matches its info header. |
| Level info | **`sectionCount, tilesetId, startSection, exits[sectionCount*4], tileBase[sectionCount]`** | `Game.loadLevelInfo`; e.g. resource 2067 = `03 00 00 | ff ff 01 ff ...` (18 bytes = 3 + 12 + 3). `0xff` = no exit. |
| Game tables | **28 length-prefixed byte arrays** (resource 1034) | `Game.loadGameTables`; verified to consume exactly 746/746 bytes. See `CLASS_MAP.md`. |
| Other data | Binary | `Enemy.loadStats` (2049), `Game.loadPlayerHitboxes` (2066), `Game.loadWorldMapData` (1035: 49 x (id, x, y) then 28 x 2 edge bytes). |
| Save games (RMS, not a file) | **3 slots x 218 bytes + settings record, byte-exact layout** | `RatchetMIDlet.a(byte[])`/`b(byte[])` document every offset: slot-in-use flag at 0, ints at 1/5/9/195, weapon ammo arrays at 71..190, settings tail at 191..217. |

## Resource type byte (`Engine.resourceTypes`, the per-resource flag table)

Confirmed by dumping all three banks: the byte classifies the resource, and
`SoundPlayer.mimeType` reads it for audio.

| Byte | Count | Ids (`bank<<10 \| index`) | Meaning |
|---|---|---|---|
| 253 | 38 | 1062.. (RP1) | plain PNG image (`loadImage`) |
| 254 | 30 | 1036..1061 etc. | sprite atlas: PNG + rectangle table (`loadSprites`) |
| 247 | 10 | 1024..1033 | animation set (`loadAnimSet`) |
| 255 | 36 | 1034, 1035, 2048..2066 ... | raw data: level tiles (2050..2064), level info (2067..2081), tables |
| 250 | 1 | 2098 | string table: 449 UTF-8 strings ("Main Menu", "New Game", ...) |
| 251 | 2 | 1094, 1095 | byte-swap arrays for `loadImage(id, swap)` (font palettes) |
| 252 | 1 | 1067 | splash image |
| 1 | 5 | 2091, 2092, 2093, 2095, 2097 | audio, `audio/x-wav` |
| 0 | 2 | 2094, 2096 | audio, `audio/midi` (2094 is the looping music, cue 3) |

Sound cues are resources 2091 + n for n = 0..6 (`SoundPlayer.load`); cue 3 loops.
Level resources are indexed by `worldId` through `Game.levelTileResIds` /
`levelInfoResIds`; worlds 0/1 share resource 2050/2067, and 15/16 share
2057/2074 (16 is the arena world).

## Sprite atlas format (type 254, `Engine.loadSprites`) -- verified

```
char  imageId        -- PNG resource (bank 0 = the atlas's own bank)
byte  (unused)
short dataLen + 4
char  mask           -- bit i clear = present: bits 0..2 = field groups
                        {offX,offY} {srcX,srcY,w,h} {f6,f7,advance,metric};
                        bits 5.. (one per present field, in order) clear = 2-byte
                        column, set = 1-byte column
char  count
byte[dataLen]        -- planar columns: all `count` values of field 0, then field 1, ...
```

Absent fields are 0. 1-byte offsets and `f6`/`f7` are signed, other 1-byte
fields unsigned. `python tools/parse_ch.py` parses all 30 atlases, each consuming
exactly its payload with every rectangle inside its PNG (atlas 1049 = the player,
71 sprites on a 187x231 sheet; 1040 = the 87-glyph font on 21x177). Atlas `N`
mostly uses image `N + 26` in RP1, and 2082..2084 use 2085/2088/2089 (the three
tilesets, 59 tiles each).

## Other files

| File | Format | Notes |
|---|---|---|
| `i.png` | Standard PNG | MIDlet icon (also the launcher icon for the port, eventually). |
| `Ratchet_and_Clank_2.jad` (in `roms/`) | Text descriptor | Keep with the jar: documents the target handset (`SEMC-Screen-Size: 240,320`), `MIDlet-Jar-Size: 312125` (matches this jar exactly) and the `QP-*` qualification entries. Its `MIDlet-Jar-URL` still says `Ratchet_and_Clank_2.jar` -- the original dump filename. |

## How to make progress on these

The container is solved, and levels, tables, strings and sounds are now mapped
(above and in `CLASS_MAP.md`). What remains for phase 2 is only the *labelling*: which
of the 68 image/atlas resources is which sprite. `Game.loadAssetsStep`
(`src/Game.java`) is the index -- it loads the images, atlases and anim sets by role
across 17 boot steps -- and the animation-set format (type 247) still needs a standalone parser.
