Ratchet & Clank: Clone Home - Remastered
=========================================

A PC port of the unreleased "Ratchet and Clank 2" J2ME prototype (known as
Clone Home), rebuilt from the game's own decompiled code.


WHAT YOU NEED
-------------

This download does NOT include the game. It cannot: the game belongs to
its rights holders. You supply your own copy of RAC-CloneHome.jar.
The launcher unpacks it for you -- you don't need to extract it first.


HOW TO PLAY
-----------

Run CloneHome.exe. Click "Choose file..." and pick your .jar. Choose
Fullscreen or Windowed. Press Play.

Everything you add is copied into this folder (data\), so you can delete or
move your original .jar afterwards and nothing breaks.

The launcher checks GitHub for new versions on start-up and offers an
"Update" button when there is one. Your saves are kept.


IN THE GAME
-----------

Settings (main menu and pause menu) has Sound, Resolution (Original,
Auto, 4:3, 16:10, 16:9, 21:9 -- widescreen shows more of the level and
never stretches sprites or the HUD), Fullscreen, Speed and FPS (Original,
60, 90, 120, 144, 165, 240, Unlimited -- game logic keeps its speed; extra
frames are interpolated).

Keyboard, mouse and gamepad (XInput, PlayStation layout) work:
  Move       arrows / WASD / d-pad / left stick
  Jump       Space / Left Shift / Cross
  Fire       J / X / Left Ctrl / left mouse / Circle or right trigger
  Wrench     K / C / Square or left trigger
  Weapons    Q / E / Tab / right mouse / Triangle or a shoulder button
  Pause      Esc / P / Start
In menus and dialogue, Enter / Space / Cross advance; Backspace / Triangle go back.

F11 or Alt+Enter toggles fullscreen, F7 the scaling mode, F8 resolution,
F9 frame rate.


NO SOUND?
---------

Run the sound test: open a command prompt in this folder and run

  bin\clonehome_port.exe --sound-test

It plays every sound effect and the music, shows a summary, and writes the
details to %LOCALAPPDATA%\rac-ch-portudio.log (the game also writes any
audio error there while you play). Attach that file to a bug report.

The music is MIDI, so it needs a MIDI synthesizer. Windows has one built in.
On Linux under Wine you need one installed (for example FluidSynth or
TiMidity with a soundfont); without it the sound effects still play but
the music is silent. Also check that sound is ON in the game's Settings.


WHERE YOUR FILES GO
-------------------

  data\             the game files you supplied (unpacked from your .jar)
  launcher.cfg      the launcher's own settings
  %LOCALAPPDATA%\rac-ch-port\   saves and display settings

Delete this folder to uninstall; delete the %LOCALAPPDATA% one too to also
remove your saves.


NOTICE.md lists the licensing and what this project is and is not.
