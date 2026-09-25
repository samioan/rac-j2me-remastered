# itch.io page copy

The text for the store page, kept in the repo so it can be revised like
anything else rather than living only in a web form. Paste into itch's
description field; it takes Markdown. Adapted from the sibling
`stormhold/docs/ITCH_PAGE.md` in tes-travels-decomp.

Page settings that matter:

- **Kind of project**: Downloadable. Not "HTML" -- there is nothing to run
  in a browser.
- **Pricing**: free. This is someone else's game; charging for a port of
  it would be indefensible whatever the licence situation.
- **Platforms**: Windows only for now.
- **Upload**: the `GoingMobileRemastered-goingmobile-v<version>-win64.zip`
  from the matching GitHub release, marked "Windows" and **not** "This file
  will be played in the browser".
- **Cover image**: `goingmobile/port/src/launcher/assets/banner_source.jpg`,
  cropped to itch's 630x500.
- Leave **"Generate itch.io app manifest"** off -- the launcher already
  updates itself from GitHub, and two update mechanisms fighting over the
  same folder is a bug waiting to happen.

---

## Ratchet & Clank: Going Mobile! -- Remastered

**The 2005 phone game on PC, in widescreen and at 60+ FPS, rebuilt from the
original game's own code.**

*Ratchet & Clank: Going Mobile* was the series' side-scrolling adventure for
J2ME (mobile Java) phones: run, jump and zip-line through the levels, blast
robots with the Bomb Glove, Pyrocitor and friends, buy weapons in the store,
take on challenge arenas and fight the boss. It has been effectively
unplayable for years unless you still have a compatible phone or an emulator.

This is that game, running natively on Windows: not an emulator, but a
re-implementation written by reading the original game's own decompiled
bytecode and rebuilding what it does, piece by piece.

### What you need

**This download does not include the game.** It cannot -- the game belongs
to its rights holders. You supply your own copy of `RAC-GoingMobile-a1.jar`
(the 176x220 build).

1. Download and unzip anywhere.
2. Run **GoingMobile.exe**.
3. Choose your `.jar` file -- the launcher unpacks it for you.
4. Pick **Fullscreen** or **Windowed**, and press **Play**.

### Made for a modern screen

- **Widescreen** (Settings > Resolution): Original, Auto, 4:3, 16:10, 16:9,
  21:9. The world simply shows more of the level; Ratchet, enemies and the
  HUD keep their original proportions -- nothing is stretched.
- **Frame rate** (Settings > FPS): Original (30), 60, 90, 120, 144, 165, 240
  or Unlimited. The game logic keeps its original speed, and the extra
  frames are smoothly interpolated.
- **Controls**: keyboard, XInput gamepad and mouse (left click fires, right
  click opens the weapon wheel).
- **Portable.** No installer, no registry keys, no Visual C++
  redistributable. Your game file is unpacked into `data\`; saves and
  display settings live in `%LOCALAPPDATA%\rac-gm-port-a1\`. Uninstalling is
  deleting the folder.
- **It updates itself** from the GitHub releases, and asks first.

### This is an early work in progress

The whole game loop is in: levels, enemies, weapons, the store, challenge
arenas, the world map, saving, the boss and the ending. It has **not** yet
been played end to end through every level, so expect rough edges. Moving
platforms, bosses and animation frames still step at the original 30 Hz even
at high frame rates.

Source, the full record of how it was reverse engineered, and the issue
tracker: <https://github.com/samioan/rac-j2me-remastered>

---

*Ratchet & Clank* is a trademark of Sony Interactive Entertainment. This
project is not affiliated with, endorsed by, or supported by Sony, Insomniac
Games or the game's original developers and publishers.
