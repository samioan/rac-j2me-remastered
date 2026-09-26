# itch.io page copy

The text for the store page, kept in the repo so it can be revised like
anything else rather than living only in a web form. Paste into itch's
description field; it takes Markdown. Adapted from `goingmobile/docs/ITCH_PAGE.md`.

Page settings that matter:

- **Kind of project**: Downloadable. Not "HTML" -- there is nothing to run
  in a browser.
- **Pricing**: free. This is someone else's game; charging for a port of
  it would be indefensible whatever the licence situation.
- **Platforms**: Windows only for now.
- **Upload**: the `CloneHomeRemastered-clonehome-v<version>-win64.zip`
  from the matching GitHub release, marked "Windows" and **not** "This file
  will be played in the browser".
- **Cover image**: `clonehome/port/src/launcher/assets/banner_source.png`,
  cropped to itch's 630x500 (the title and Ratchet are the parts to keep).
- Leave **"Generate itch.io app manifest"** off -- the launcher already
  updates itself from GitHub, and two update mechanisms fighting over the
  same folder is a bug waiting to happen.

---

## Ratchet & Clank: Clone Home -- Remastered

**The unreleased phone prototype on PC, in widescreen and at 60+ FPS, rebuilt
from the original game's own code.**

*Ratchet and Clank 2* -- known to fans as **Clone Home** -- is a
prototype build of a second side-scrolling *Ratchet & Clank* for J2ME
(mobile Java) phones, made for 240x320 screens. It never got a proper
release, and unless you still have a compatible phone or an emulator it has
been effectively unplayable.

Run and jump through the levels, swing from swingshot anchors, ride
zip-lines and glide with the helipack, blast robots with a wheel of upgradeable
weapons, spend bolts in the weapon shop, work your way across the world map, take on the
challenge arenas, and keep your progress in one of three save slots.

This is that game, running natively on Windows: not an emulator, but a
port made by reading the original game's own decompiled bytecode and
translating it to C++ around a new Windows front end.

### What you need

**This download does not include the game.** It cannot -- the game belongs
to its rights holders. You supply your own copy of `RAC-CloneHome.jar`.

1. Download and unzip anywhere.
2. Run **CloneHome.exe**.
3. Choose your `.jar` file -- the launcher unpacks it for you.
4. Pick **Fullscreen** or **Windowed**, and press **Play**.

### Made for a modern screen

- **Widescreen** (Settings > Resolution): Original, Auto, 4:3, 16:10, 16:9,
  21:9. The world simply shows more of the level; Ratchet, enemies and the
  HUD keep their original proportions -- nothing is stretched. Menus stay
  centred in their original layout.
- **Frame rate** (Settings > FPS): Original, 60, 90, 120, 144, 165, 240 or
  Unlimited. The game logic keeps its speed (Settings > Speed, 25 steps a
  second by default), and the extra frames are smoothly interpolated.
- **Borderless fullscreen** (Settings > Fullscreen, F11 or Alt+Enter), plus
  fit or whole-number scaling (F7) for crisp pixels.
- **Controls**: keyboard, mouse and XInput gamepad, laid out like the
  PlayStation Ratchet & Clank games -- Cross jumps and advances menus and
  dialogue, Circle fires, Square is the wrench, Triangle opens the weapon
  wheel (and goes back in menus).
- **Portable.** No installer, no registry keys, no Visual C++
  redistributable. Your game file is unpacked into `data\`; saves and
  display settings live in `%LOCALAPPDATA%\rac-ch-port\`. Uninstalling is
  deleting the folder.
- **It updates itself** from the GitHub releases, and asks first.

### This is an early work in progress

The whole game is in and runs: every level and section loads, and the
menus, weapon wheel, shop, world map, arenas and saving all work. It has
**not** yet been played end to end, so expect rough edges. The game is also
a prototype and has a few bugs of its own -- it can throw errors the
original phone would have crashed on, and the port simply carries on.
Decorative animations still step at the game's own logic rate even at high frame
rates.

Source, the full record of how it was reverse engineered, and the issue
tracker: <https://github.com/samioan/rac-j2me-remastered>

---

*Ratchet & Clank* is a trademark of Sony Interactive Entertainment. This
project is not affiliated with, endorsed by, or supported by Sony, Insomniac
Games or the game's original developers and publishers.
