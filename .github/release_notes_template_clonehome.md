<!--
The body of every Clone Home GitHub release, with {{TAG}} substituted by
.github/workflows/release-clonehome.yml.
-->
## Ratchet & Clank: Clone Home Remastered {{TAG}}

A PC port of the unreleased *Ratchet and Clank 2* J2ME prototype (known as
**Clone Home**), rebuilt from the game's own decompiled code, with widescreen,
high frame rates, gamepad support and borderless fullscreen.

### Getting it running

1. Download `CloneHomeRemastered-{{TAG}}-win64.zip` below and unzip it
   anywhere.
2. Run **CloneHome.exe**.
3. Point it at your own copy of `RAC-CloneHome.jar` -- the launcher unpacks it
   for you.
4. Press **Play**.

The launcher updates itself: when a newer release exists it offers an
**Update** button.

### This download does not include the game

It cannot: the game belongs to its rights holders. You supply your own copy.

### In the game

- **Settings > Resolution:** Original, Auto, 4:3, 16:10, 16:9, 21:9. Widescreen
  shows more of the level and never stretches sprites or the HUD.
- **Settings > FPS:** Original, 60, 90, 120, 144, 165, 240, Unlimited. Game
  logic keeps its speed; extra frames are interpolated.
- **Settings > Speed / Fullscreen**, F11 / Alt+Enter for borderless fullscreen.
- Keyboard, mouse and XInput gamepad (PlayStation layout: Cross jumps and
  advances, Circle fires, Square is the wrench, Triangle is the weapon wheel).

### Notes

- Windows 64-bit. No installer and no Visual C++ redistributable -- unzip
  and run.
- Your game files are unpacked to `data\` next to the launcher; saves and
  display settings live in `%LOCALAPPDATA%\rac-ch-port\`.

*Ratchet & Clank* is a trademark of Sony Interactive Entertainment. This
project is not affiliated with or endorsed by Sony or the game's original
developers.
