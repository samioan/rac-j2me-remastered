<!--
The body of every Going Mobile GitHub release, with {{TAG}} substituted by
.github/workflows/release-goingmobile.yml.
-->
## Ratchet & Clank: Going Mobile Remastered {{TAG}}

A PC port of *Ratchet & Clank: Going Mobile* (the 2007 J2ME game), rebuilt
from the original game's own decompiled code, with widescreen and high
frame rates.

### Getting it running

1. Download `GoingMobileRemastered-{{TAG}}-win64.zip` below and unzip it
   anywhere.
2. Run **GoingMobile.exe**.
3. Point it at your own copy of `RAC-GoingMobile-a1.jar` -- the launcher
   unpacks it for you.
4. Press **Play**.

The launcher updates itself: when a newer release exists it offers an
**Update** button.

### This download does not include the game

It cannot: the game belongs to its rights holders. You supply your own copy.

### In the game

- **Settings > Resolution:** Original, Auto, 4:3, 16:10, 16:9, 21:9. Widescreen
  never stretches sprites or the HUD.
- **Settings > FPS:** Original, 60, 90, 120, 144, 165, 240, Unlimited. Game
  logic keeps its original speed; extra frames are interpolated.
- Keyboard, XInput gamepad and mouse controls.

### Notes

- Windows 64-bit. No installer and no Visual C++ redistributable -- unzip
  and run.
- Your game files are unpacked to `data\` next to the launcher; saves and
  display settings live in `%LOCALAPPDATA%\rac-gm-port-a1\`.

*Ratchet & Clank* is a trademark of Sony Interactive Entertainment. This
project is not affiliated with or endorsed by Sony or the game's original
developers.
