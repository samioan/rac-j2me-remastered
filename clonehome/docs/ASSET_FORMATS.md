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
| Level/game data | **Binary, unconfirmed** | Consumed via `a.e(id)` (a `DataInputStream` over the raw resource) by `b`/`c`. This is the phase-2 work item: map resource ids to roles from their call sites. |
| Save games (RMS, not a file) | **3 slots x 218 bytes + settings record, byte-exact layout** | `RatchetMIDlet.a(byte[])`/`b(byte[])` document every offset: slot-in-use flag at 0, ints at 1/5/9/195, weapon ammo arrays at 71..190, settings tail at 191..217. |

## Other files

| File | Format | Notes |
|---|---|---|
| `i.png` | Standard PNG | MIDlet icon (also the launcher icon for the port, eventually). |
| `Ratchet_and_Clank_2.jad` (in `roms/`) | Text descriptor | Keep with the jar: documents the target handset (`SEMC-Screen-Size: 240,320`), `MIDlet-Jar-Size: 312125` (matches this jar exactly) and the `QP-*` qualification entries. Its `MIDlet-Jar-URL` still says `Ratchet_and_Clank_2.jar` -- the original dump filename. |

## How to make progress on these

The container is solved; the remaining work is *semantic*: which resource
id is which image/string/level. Start from `a.g()`/`a.e()` call sites in
`decompiled/b.java` and `c.java`, dump each id with a small
`tools/parse_rp.py` (to be written once the first ids are mapped), and
label them. The PNG byte-swap decode in `a.java` needs one careful
read-through before images can be extracted losslessly.
