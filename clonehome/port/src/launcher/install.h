#pragma once

// Turning "the .jar the user picked" into a working install: unzip it into
// the install's own `data/` and check the result looks like the game.
// The jar is a plain zip whose entries are flat (tools/extract_jar.py's
// own loader is zipfile.extractall()).

#include <string>

namespace clonehome {
namespace launcher {

// mapData.txt (the a1 build's unobfuscated level data), arrows.png and
// a.class: three top-level files a real RAC-CloneHome.jar always has,
// checked together so an unrelated folder is not mistaken for the game.
bool IsGameDataRoot(const std::string& directory);

// Unzips `jarPath` into `destDir` (creating it, overwriting what's already
// there) and checks the result via IsGameDataRoot. On failure returns
// false and puts something a user can act on in `error` (not a real jar,
// a permission problem, a full disk, a source that vanished mid-copy).
bool InstallGameJar(const std::string& jarPath, const std::string& destDir, std::string& error);

}  // namespace launcher
}  // namespace clonehome
