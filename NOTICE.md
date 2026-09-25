# Notices and attribution

This project is a from-scratch, behavioural re-implementation of
*Ratchet & Clank: Going Mobile* and *Ratchet & Clank: Clone Home*
(J2ME/mobile Java), written by reading the original games' own
decompiled bytecode (see `goingmobile/docs/` and `clonehome/docs/`). It
is not affiliated with, endorsed by, or supported by Sony Interactive
Entertainment, Sony Pictures, Insomniac Games, or the games' original
developers and publishers.

## What this project does not contain, and never will

**The games.** No script, image, sound, string table or byte of the
original `RAC-*.jar`/`RAC-*.jad` releases is committed here, and none is
distributed with any release. See `.gitignore`.

## Artwork and third-party code

The launchers' backgrounds and icons are key art supplied by the project
owner (Going Mobile: `goingmobile/port/src/launcher/assets/banner_source.jpg`;
Clone Home: `clonehome/port/src/launcher/assets/banner_source.png`; the
`banner.png` and `.ico` files are generated from them by `tools/make_banner.py`
/ `tools/make_icon.py`). The "Ratchet & Clank" wordmark and characters in them
belong to their owners.

| Component | Licence | Where |
|---|---|---|
| `stb_image` | public domain / MIT | `goingmobile/port/third_party/stb/`, `clonehome/port/third_party/stb/` |
| `puff` (zlib's reference inflate) | zlib | `goingmobile/port/third_party/puff/`, `clonehome/port/third_party/puff/` |
| `javalang` (dev tool only, not shipped) | MIT | used by `tools/java2cpp.py` |

The launcher's file picker uses `IFileOpenDialog` (COM) and its update check
uses WinHTTP, both Windows system components. Release builds link the CRT
statically, so no Visual C++ redistributable is needed.

## Trademarks

"Ratchet & Clank" and "Ratchet & Clank: Going Mobile" are trademarks of
Sony Interactive Entertainment LLC (Going Mobile was published by Sony
Pictures Mobile / Sony Pictures Digital Inc.). All names are used here
for identification and documentation of the decompilation process only.
