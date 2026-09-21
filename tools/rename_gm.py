#!/usr/bin/env python3
"""Phase 1: rename the canonical Going Mobile decompilation into goingmobile/src/.

Reads goingmobile/decompiled/{a,c,d,f,g,ratchetandclank}.java and writes
goingmobile/src/{Player,LevelMap,Enemy,Game,Projectile,ratchetandclank}.java
with the class-level renames from docs/CLASS_MAP.md applied (cross-file
references included), plus member-level renames where the member's role is
confirmed and the name is unambiguous in context.

Entity (h), MenuItem (e) and SoundPlayer (b) are hand-written (their members
are fully renamed); the references to them from the generated files are
rewritten here to match, and javac validates the result.

Method-name collisions (the obfuscator reused the same single letters for
fields and methods, which Java allows) make blind word-boundary renames of
single-letter members unsafe; every such rename is checked against the
method declarations of the file first and skipped with a warning instead.
The compiler is the final arbiter: src/ must build against the MIDP/CLDC/
Nokia-UI stub jars with zero errors.
"""
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "goingmobile" / "decompiled"
OUT = ROOT / "goingmobile" / "src"

CLASS = {
    "a": "Player", "b": "SoundPlayer", "c": "LevelMap", "d": "Enemy",
    "e": "MenuItem", "f": "Game", "g": "Projectile", "h": "Entity",
}
# ratchetandclank keeps its real (manifest) name.

# Which `X.member` class-qualified references exist per source file, and the
# members that legitimately follow each qualifier (statics/static methods of
# that class). Everything else after a bare single letter is a field access.
QUALIFIERS = {
    # file: {qualifier: (new class, allowed members)}
    "a.java": {"f": ("Game", None), "c": ("LevelMap", None),
               "d": ("Enemy", None)},
    "c.java": {"f": ("Game", None)},
    "d.java": {"c": ("LevelMap", None), "f": ("Game", None)},
    "g.java": {"c": ("LevelMap", None), "d": ("Enemy", None), "f": ("Game", None)},
    "ratchetandclank.java": {"f": ("Game", None)},  # b. is a field (Game instance), never class b
    "f.java": {"c": ("LevelMap", None), "d": ("Enemy", None), "g": ("Projectile", None)},
    # a. in f.java is Game's midlet field: handled by the special rules below.
}

GAME_STATICS = set("abcdefghijklmnoprstuvwxyz") | set("ABCDEFGHIJKLM") | {
    "aa", "ab", "ag", "ah", "ai", "aj", "ak", "al", "am", "au", "av",
    "aM", "aN", "aQ", "aR", "bk", "bl", "bA", "bB", "bC", "bD", "bE", "bF",
    "bG", "bH", "bI", "bJ", "bK", "bL", "bM", "bN", "bO", "bP", "bQ", "bR",
    "bS", "bT", "bU", "bV", "bW", "bX", "bY", "bZ", "cb"}

LEVELMAP_STATICS = {"a", "b", "d", "e", "f", "g", "h", "i", "j"}
ENEMY_STATICS = set("abcdefghijklmnopqr")
PROJECTILE_STATICS = set("abcdef")
QUALIFIER_MEMBERS = {
    "Game": GAME_STATICS,
    "LevelMap": LEVELMAP_STATICS,
    "Enemy": ENEMY_STATICS,
    "Projectile": PROJECTILE_STATICS,
}


def methods_of(text: str) -> set:
    """Names of methods declared in this file (to avoid field/method name clashes)."""
    out = set()
    for m in re.finditer(
        r"(?:public|private|protected)(?:\s+static)?(?:\s+final)?\s+"
        r"[\w\[\]<>.]+\s+(\w+)\s*\(", text
    ):
        out.add(m.group(1))
    return out


def rename_classes(text: str, fname: str) -> str:
    # 1. class declarations
    for old, new in CLASS.items():
        text = re.sub(r"\bclass %s\b" % old, "class %s" % new, text)
        # 2. extends
        text = re.sub(r"\bextends %s\b" % old, "extends %s" % new, text)
        # 3. constructors (a method would carry a return type)
        text = re.sub(r"\bpublic %s\(" % old, "public %s(" % new, text)
        # 4. `new X(...)` / `new X[...]`
        text = re.sub(r"\bnew %s(?=[\(\[])" % old, "new %s" % new, text)
        # 5. typed locals (Vineflower uses varN names exclusively)
        text = re.sub(r"\b%s(?=\[\] var\d+\b)" % old, new, text)
        text = re.sub(r"\b%s(?= var\d+\b)" % old, new, text)
        # 6. casts: `(X[])expr` / `(X)(...)`
        text = re.sub(r"\(%s\[\]\)" % old, "(%s[])" % new, text)
        text = re.sub(r"\(%s\)\(" % old, "(%s)(" % new, text)
        # 7. field declarations typed with the class (`public f c;` etc)
        text = re.sub(
            r"^(\s*(?:public|private|protected)\s+(?:static\s+)?(?:final\s+)?)"
            r"%s(\[\])*\s+(\w+)\s*;" % old,
            lambda m: "%s%s%s %s;" % (m.group(1), new, m.group(2) or "", m.group(3)),
            text, flags=re.M,
        )
    # 8. class-qualified static references (per-file allowlist)
    for old, (new, _) in QUALIFIERS.get(fname, {}).items():
        members = QUALIFIER_MEMBERS[new]
        text = re.sub(
            r"(?<![\w.])%s\.(%s)\b" % (old, "|".join(sorted(members))),
            r"%s.\1" % new, text,
        )
    return text


# Entity (hand-written) member names, for rewriting references from the
# generated files. The compiler enforces completeness.
ENTITY_FIELDS = {  # old -> new
    "R": "kind", "S": "row", "T": "posX", "U": "velY", "V": "velX",
    "W": "fieldW", "X": "spawnRow", "Y": "posInRow", "Z": "activeFlag",
    "aa": "health", "ab": "animFrame", "ac": "animCounter",
    "ad": "animState", "ae": "animRestart", "af": "animHold",
    "ag": "facingRight", "ah": "subState",
}
ENTITY_REFS = r"(?:R|S|T|U|V|W|X|Y|Z|aa|ab|ac|ad|ae|af|ag|ah)"


def qualified_refs(text: str) -> str:
    """Rewrite member accesses reached through Game's pools -- player field
    (X), enemy pool (W[i]), projectile pools (Y[i]/Z[i]) -- in every
    generated file, plus the few varN-alias sites in f.java (verified by
    context: var10000 = this.X / this.W[...] / this.Y[...] / this.Z[...]).
    Game itself is not an Entity, so this./super. refs are never touched
    here. `.N.f` is LevelMap's tiles array through Game's levelMap field."""
    def both(m):
        return "%s.%s" % (m.group(1), ENTITY_FIELDS[m.group(2)])
    # Entity members
    text = re.sub(r"\b(X)\.(%s)\b" % ENTITY_REFS, both, text)
    text = re.sub(r"\b(W\[[^\]]+\])\.(%s)\b" % ENTITY_REFS, both, text)
    # Player fields through the player field
    for old, new in PLAYER_FIELDS.items():
        text = re.sub(r"\bX\.%s\b(?!\()" % old, "X.%s" % new, text)
    # Enemy fields through the enemy pool
    for old, new in ENEMY_FIELDS.items():
        if old == "D":
            continue
        text = re.sub(r"\b(W\[[^\]]+\])\.%s\b(?!\()" % old, r"\1.%s" % new,
                      text)
    # Projectile fields through the two projectile pools
    for old, new in PROJ_FIELDS.items():
        text = re.sub(r"\b((?:Y|Z)\[[^\]]+\])\.%s\b(?!\()" % old,
                      r"\1.%s" % new, text)
    # inherited Entity method through the pools
    text = text.replace("X.a((byte", "X.setAnimState((byte")
    text = re.sub(r"(W\[[^\]]+\])\.a\(\(byte", r"\1.setAnimState((byte", text)
    # LevelMap tiles through Game's levelMap field
    text = re.sub(r"\.N\.f\b", ".N.tiles", text)
    # f.java varN-alias sites (unique strings, verified by context)
    text = text.replace("var10000.ag = var10001;",
                        "var10000.facingRight = var10001;")
    text = text.replace("var10000.R = var10001;", "var10000.kind = var10001;")
    text = text.replace("var10000.aa = (byte)(var10001 - var10002);",
                        "var10000.health = (byte)(var10001 - var10002);")
    text = text.replace("var10000.E = var10001;",
                        "var10000.platformUnder = var10001;")
    text = text.replace("var10000.n = var10001;", "var10000.vx = var10001;")
    text = text.replace("var10000.o = var9;", "var10000.vy = var9;")
    return text


def entity_refs_subclass(text: str, methods: set) -> str:
    """Rewrite Entity-member accesses in Player.java / Enemy.java (subclasses
    of Entity): super./this./varN-qualified fields (all varN aliases in these
    files are this-aliases of the class itself), plus the inherited Entity
    methods -- only when the subclass doesn't shadow the name."""
    def both(m):
        return "%s.%s" % (m.group(1), ENTITY_FIELDS[m.group(2)])
    text = re.sub(r"\b(super|this|var\d+)\.(%s)\b" % ENTITY_REFS, both, text)
    text = text.replace("super.a((byte", "super.setAnimState((byte")
    if "a" not in methods or True:
        # neither Player nor Enemy declares a(byte) (Entity's only overload)
        text = text.replace("this.a((byte", "this.setAnimState((byte")
    if "p" not in methods:
        text = text.replace("super.p()", "super.column()")
        text = text.replace("this.p()", "this.column()")
    if "q" not in methods:
        text = text.replace("super.q()", "super.wrapRow()")
        text = text.replace("this.q()", "this.wrapRow()")
    return text


def game_instance_refs(text: str) -> str:
    """In Player/Enemy/LevelMap/Projectile: Game's entity pools were renamed
    in Game.java (X/W/Y/Z -> player/enemies/playerShots/enemyShots) and its
    2-letter members too -- rewrite the refs through the local `game` field.
    Entity's own same-spelled members were already rewritten beforehand, so
    every remaining `.X`/`.W`/`.aa`/... here is a Game reference."""
    text = re.sub(r"\.X\b", ".player", text)
    text = re.sub(r"\.W\b", ".enemies", text)
    text = re.sub(r"\.Y\b", ".playerShots", text)
    text = re.sub(r"\.Z\b", ".enemyShots", text)
    for old, new in GAME_MEMBERS.items():
        text = re.sub(r"\.%s\b" % old, ".%s" % new, text)
    return text



# Player member renames (declared in Player.java; refs updated everywhere).
PLAYER_FIELDS = {
    "J": "currentWeapon", "K": "ammo", "L": "weaponXp", "M": "ownedWeapons",
    "N": "weaponLevel", "B": "invulnTimer", "D": "onLadder",
    "E": "platformUnder", "F": "meleeActive", "C": "swingTargetType",
    "A": "weaponPose", "z": "attackTimer", "p": "jumpPhase",
    "t": "swingTargetX", "u": "swingTargetY", "v": "swingCurX",
    "w": "swingCurY", "x": "swingStepX", "y": "swingStepY",
    "O": "swingVelX", "P": "swingVelY", "Q": "swingEndX",
}
PLAYER_STATICS = {"G": "ANIM_FRAMES", "H": "ANIM_FRAME_COUNTS",
                  "I": "ANIM_FRAME_EXTRA"}

# Enemy member renames.
ENEMY_FIELDS = {"D": "game", "t": "animKind", "u": "bounceTimer"}
ENEMY_STATICS = {"i": "HP_BY_ANIM", "j": "DAMAGE_BY_ANIM",
                 "k": "BOLTS_DROPPED_BY_ANIM", "p": "ANIM_FRAMES",
                 "q": "ANIM_FRAME_COUNTS", "r": "ANIM_FRAME_EXTRA"}

# Projectile member renames.
PROJ_FIELDS = {
    "g": "type", "h": "posX", "i": "posY", "j": "prevX", "k": "prevY",
    "l": "prev2X", "m": "prev2Y", "n": "vx", "o": "vy", "p": "age",
    "q": "halfWidth", "r": "halfHeight", "s": "sourceAnim",
    "t": "facingRight", "u": "spawnX", "v": "detonated", "x": "game",
}
PROJ_STATICS = {"a": "DAMAGE_BY_TYPE", "b": "HALF_WIDTHS", "c": "HALF_HEIGHTS",
                "d": "SPEEDS", "e": "RENDER_MODES"}


# Game member renames (2+ letters only: every single letter in Game.java is
# also a method name, so none of them can be renamed safely -- except the
# entity-pool fields, checked against the method list at runtime).
GAME_MEMBERS = {
    # images
    "aa": "tileSetImage", "ab": "enemySegmentImage", "ae": "splashImage",
    "ag": "hudIconImage", "ah": "portraitImage", "ai": "smallSpriteImage",
    "aj": "playerImage", "ak": "weaponImage", "al": "titaniumBoltImage",
    "am": "actorImage", "cm": "digitStripImage",
    # sprite-orientation cache used by the actor renderer
    "an": "sprKind", "ao": "sprAnimState", "ap": "sprAnimFrame",
    "aq": "sprFrameOffset", "ar": "sprOrientation",
    # entity pools / indices
    "au": "MAX_ENEMIES", "av": "PLAYER_SLOT",
    # titanium bolts (16x16 boxes; 9 of them unlock the RYNO)
    "aD": "titaniumBoltX", "aE": "titaniumBoltY", "aF": "titaniumBoltType",
    "aQ": "TITANIUM_BOLTS_FOR_RYNO", "aR": "TITANIUM_BOLT_SLOTS",
    "aS": "area1BoltsTaken", "aT": "area2BoltsTaken",
    # magnet-attracted pickups (bolts/health/ammo)
    "aU": "pickupX", "aV": "pickupY", "aW": "pickupVx", "aX": "pickupVy",
    "aY": "pickupType",
    # moving platforms
    "aZ": "platformX", "ba": "platformY", "bb": "platformDx",
    "bc": "platformDy", "bd": "platformState",
    # zip lines
    "aw": "zipX1", "ax": "zipY1", "ay": "zipX2", "az": "zipY2",
    "aA": "zipDx", "aB": "zipDy", "aC": "zipBaseY",
    # boss fight (level 12)
    "aG": "bossShellState", "aH": "bossShellTimer", "aI": "bossHp",
    "aJ": "bossAim", "aK": "bossFireTick", "aL": "bossFirePhase",
    "aM": "BOSS_SHELL_COL", "aN": "BOSS_SHELL_ROW",
    # level exit
    "aO": "exitTileX", "aP": "exitTileY",
    # HUD / messages / timers
    "as": "boltCount", "at": "storeAmmoNeeded", "be": "levelElapsedMs",
    "bf": "lastTickMs", "bn": "messageText", "bo": "messagePortrait",
    "bp": "messagePortraitFrame", "bq": "messageScroll", "br": "messageChar",
    "bs": "messageStringId", "bt": "messageTimer", "bm": "weaponOverlayOpen",
    # progress masks
    "bu": "collectiblesTaken1", "bv": "collectiblesTaken2",
    "bw": "levelStateMask", "bx": "introSeenMask",
    # scoring (labels confirmed against m.txt strings 63-70/111)
    "bC": "enemyKills", "bE": "boltsCollected", "bR": "shotsFired",
    "bS": "enemyShotHits", "bT": "boxesHit", "bP": "totalTitaniumBolts",
    "bQ": "totalKills", "bO": "totalScore", "bD": "baseScore",
    "bJ": "timeBonus", "bK": "scoringStart", "bL": "timerStart",
    "bM": "levelTimeMs", "bN": "PAR_TIME_MS", "bF": "levelsCleared",
    "bU": "noDamageBonus", "bV": "fastKillBonus", "bW": "allKilledBonus",
    "bX": "playerDamaged", "bI": "specialKills",
    # per-level enemy bookkeeping
    "bY": "enemiesRemainingPerLevel", "bZ": "enemiesCountedPerLevel",
    # menus / ui
    "ce": "PORTRAIT_TABLE", "cn": "menuSoftLeft", "co": "menuSoftRight",
    "cp": "menuTitle", "cq": "menuBackTarget", "cb": "repaintDelay",
    "ck": "KEYPAD_CHARS", "cl": "nameEntryWarning",
    # lifecycle / display
    "cg": "display", "cd": "repaintEachFrame", "cc": "nowMs",
    "cf": "scoringActive", "cx": "isHidden", "cy": "hiddenSince",
    "cz": "bossVulnerable", "cw": "splashHoldoff",
    "ch": "lastMultitapTime", "ci": "lastMultitapKey",
    "cj": "multitapCycle",
}

# ratchetandclank member renames (fields only; its methods stay obfuscated,
# documented in CLASS_MAP.md).
MIDLET_FIELDS = {
    "h": "saveBuffer", "q": "settings", "p": "SAVE_RECORD_IDS",
    "r": "fontGlyphs", "s": "fontSpacing", "o": "fontLineHeight",
    "e": "strings", "f": "soundPlayer", "g": "soundEnabled",
    "c": "saveSlotFlags", "d": "saveSlotTimes",
}

# LevelMap member renames.
LEVELMAP_FIELDS = {"c": "game", "f": "tiles", "g": "gridCount"}
LEVELMAP_STATICS = {"d": "levelGrids", "e": "columnSolidMasks",
                    "h": "PLATFORM_TILE_TYPES", "i": "ENEMY_TILE_TYPES",
                    "j": "BOLT_TILE_TYPES"}


def sub_all(text, mapping, methods=None):
    """Apply old->new renames of this file's own fields.

    Single-letter names are the dangerous ones (the obfuscator reused them
    for fields *and* methods, and other objects have same-letter fields):
    - if the name collides with a method of this file, only rewrite
      `this.x` / `super.x` / `varN.x` (never a call `x(`);
    - otherwise rewrite bare uses (not behind any dot: `this.game.N` must
      keep Game's N) and this/super/varN-qualified uses.
    Char literals ('x') are always left alone.
    """
    for old, new in mapping.items():
        if len(old) == 1:
            if methods and old in methods:
                pat = r"\b(?:this|super|var\d+)\.%s\b(?!\()" % old
            else:
                pat = r"(?<![\w'.])%s\b(?!')" % old
                text = re.sub(pat, new, text)
                pat = r"\b(?:this|super|var\d+)\.%s\b(?!')" % old
            # ... and the field declaration itself
            decl = re.compile(
                r"^(\s*(?:public|private|protected)\s+(?:static\s+)?"
                r"(?:final\s+)?[\w\[\].]+\s+)%s\s*;" % old, re.M)
            text = decl.sub(r"\1%s;" % new, text)
        else:
            pat = r"\b%s\b" % old
        text = re.sub(pat, new, text)
    return text


def cross_members(text, klass, mapping):
    """Rename `Class.old` -> `Class.new` (refs from other generated files)."""
    for old, new in mapping.items():
        text = re.sub(r"\b%s\.%s\b" % (klass, old), "%s.%s" % (klass, new), text)
    return text


def game_pipeline(text):
    m = methods_of(text)
    # --- vineflower artifact fixes, on the original names (see CLASS_MAP.md):
    # u() (platform mover): var5 aliased both the byte[] platformState and the
    # short[] platformDx/platformDy arrays -- split the byte[] alias out
    text = text.replace("short[] var5;", "short[] var5;\n         byte[] varBd;")
    text = text.replace("var5 = this.bd;", "varBd = this.bd;")
    text = text.replace("var5[var10001] = (byte)var10002;",
                        "varBd[var10001] = (byte)var10002;")
    # l(int) (boss shell hit): var10000 aliased both a byte[] and the int[]
    # bossHp array -- split the int[] alias out
    text = text.replace("var10000 = this.aI;", "varHp = this.aI;")
    text = re.sub(r"int var10002;(\s*)byte\[\] var10003;",
                  r"int var10002;\1int[] varHp;\1byte[] var10003;", text)
    text = text.replace(
        "var10000[var10001] = var10002 - var10003[var10004[var10005]];",
        "varHp[var10001] = var10002 - var10003[var10004[var10005]];")
    # i(Graphics) (boss render): var7 held sprite offsets up to 10*14=140
    # (doesn't fit byte) and var10000 held the 16384 rotation flag -- retype
    text = re.sub(r"byte var6;(\s*)byte var7;", r"byte var6;\1int var7;", text)
    text = re.sub(r"var14 = 126 \+ s;(\s*)byte var10000;",
                  r"var14 = 126 + s;\1short var10000;", text)
    # Player-static qualifiers (bare a.X in Game.java: indexed -> Player's
    # weapon tables, non-indexed a.k/a.i -> Player jump vel / boss damage).
    text = re.sub(r"(?<![\w.])a\.G\b", "Player.G", text)
    text = re.sub(r"(?<![\w.])a\.(?=(?:e|g|h|l|m|n|o)\[)", "Player.", text)
    text = re.sub(r"(?<![\w.])a\.d\[\(this\.cr \+ 1\) \* 3",
                  "Player.d[(this.cr + 1) * 3", text)
    text = re.sub(r"(?<![\w.])a\.d\[var3 \* 3", "Player.d[var3 * 3", text)
    text = re.sub(r"(?<![\w.])a\.(i|k)\b(?![\w\[])", r"Player.\1", text)
    # midlet member renames (Game's field `a` is the ratchetandclank midlet)
    text = re.sub(r"\ba\.f\b", "a.soundPlayer", text)
    text = re.sub(r"\ba\.g\b(?!\[)", "a.soundEnabled", text)
    text = re.sub(r"\ba\.o\b(?!\[)", "a.fontLineHeight", text)
    text = re.sub(r"\ba\.c\[", "a.saveSlotFlags[", text)
    text = re.sub(r"\ba\.c = new", "a.saveSlotFlags = new", text)
    text = re.sub(r"\ba\.d\[", "a.saveSlotTimes[", text)
    text = re.sub(r"\ba\.d = new", "a.saveSlotTimes = new", text)
    text = re.sub(r"\bratchetandclank\.e\b(?!\()", "ratchetandclank.strings",
                  text)
    # hand-written SoundPlayer methods (no-arg a() is stop(), so match it
    # before the general a(...) call form)
    text = text.replace("a.soundPlayer.a()", "a.soundPlayer.stop()")
    text = text.replace("a.soundPlayer.a(", "a.soundPlayer.queue(")
    # member accesses through the player/enemy/projectile pools
    text = qualified_refs(text)
    # hand-written MenuItem members: `...]).a` casts / `varN[i].a` indexing
    menu = {"a": "type", "b": "action", "c": "stringId", "d": "enabled"}
    for old, new in menu.items():
        text = re.sub(r"\]\)\.%s\b(?!\()" % old, "]).%s" % new, text)
        text = re.sub(r"\]\.%s\b(?!\()" % old, "].%s" % new, text)
    # class-qualified member renames
    text = cross_members(text, "Player", PLAYER_STATICS)
    text = cross_members(text, "Enemy", ENEMY_STATICS)
    text = cross_members(text, "Projectile", PROJ_STATICS)
    text = cross_members(text, "LevelMap", LEVELMAP_STATICS)
    text = cross_members(text, "Game", GAME_MEMBERS)
    # entity-pool field renames (only if no method of that name exists;
    # (?<!')/(?!') keeps 'X'/'W'/'Y'/'Z' keypad char literals intact)
    for old, new in (("X", "player"), ("W", "enemies"),
                     ("Y", "playerShots"), ("Z", "enemyShots")):
        if old not in m:
            text = re.sub(r"(?<!')\b%s\b(?!')" % old, new, text)
        else:
            print("  warning: Game field %s kept (method %s() exists)" %
                  (old, old))
    # Game's own 2-letter members
    text = sub_all(text, GAME_MEMBERS)
    # vineflower artifact fix: dead `short[] var = new byte[]{...}` in the
    # constructor (byte[] cannot be assigned to short[]; both statements are
    # dead code)
    text = text.replace("short[] var10000 = new byte[]{",
                        "short[] var10000 = new short[]{")
    return text


def player_pipeline(text):
    m = methods_of(text)
    # a.java has one Game-typed local alias (var7, in the zip-line setter)
    # whose .T is Game's camera field, not Entity's -- protect it from the
    # Entity-member rewrite below
    text = text.replace("var7.T = var10001;", "@@GAME_T@@")
    # vineflower artifact: var15 held `super.W * 22` column offsets (up to
    # 27*22=594), so it was an int, not a byte (the `(byte)var15` cast at
    # its use site proves it)
    text = text.replace("byte var15;", "int var15;")
    text = entity_refs_subclass(text, m)
    text = text.replace("@@GAME_T@@", "var7.T = var10001;")
    text = sub_all(text, PLAYER_FIELDS, m)
    text = sub_all(text, PLAYER_STATICS)
    text = cross_members(text, "Enemy", ENEMY_STATICS)
    text = cross_members(text, "Game", GAME_MEMBERS)
    text = cross_members(text, "LevelMap", LEVELMAP_STATICS)
    # Enemy anim/bounce fields accessed through the game's enemy pool
    for old, new in ENEMY_FIELDS.items():
        if old == "D":
            continue
        text = re.sub(r"\b(W\[[^\]]+\])\.%s\b(?!\()" % old, r"\1.%s" % new,
                      text)
    text = re.sub(r"\bai\b", "game", text)
    # midlet refs (sound flags in the death paths)
    text = re.sub(r"\bGame\.a\.g\b(?!\[)", "Game.a.soundEnabled", text)
    text = re.sub(r"\bGame\.a\.f\b(?!\[)", "Game.a.soundPlayer", text)
    text = text.replace("Game.a.soundPlayer.a(", "Game.a.soundPlayer.queue(")
    # member accesses through the pools, then Game's renamed pool fields and
    # members through the game instance
    text = qualified_refs(text)
    text = game_instance_refs(text)
    return text


def enemy_pipeline(text):
    m = methods_of(text)
    text = entity_refs_subclass(text, m)
    text = sub_all(text, ENEMY_FIELDS, m)
    text = sub_all(text, ENEMY_STATICS)
    text = cross_members(text, "Game", GAME_MEMBERS)
    text = cross_members(text, "LevelMap", LEVELMAP_STATICS)
    text = qualified_refs(text)
    text = game_instance_refs(text)
    return text


def levelmap_pipeline(text):
    m = methods_of(text)
    # this.c (game) and .f[...] (tiles) refs first -- c/f were class letters
    # before the class-rename pass, so no other member is spelled like this;
    # (?!\() keeps Game method calls (this.c.c(...)) intact
    text = re.sub(r"\.c\b(?!\()", ".game", text)
    text = re.sub(r"\.f\b(?!\()", ".tiles", text)
    text = sub_all(text, LEVELMAP_FIELDS, m)
    text = sub_all(text, LEVELMAP_STATICS, m)
    text = cross_members(text, "Game", GAME_MEMBERS)
    text = qualified_refs(text)
    text = game_instance_refs(text)
    return text


def projectile_pipeline(text):
    m = methods_of(text)
    text = sub_all(text, PROJ_FIELDS, m)
    # static tables: rename the declarations explicitly, uses by index
    decls = {"a": "DAMAGE_BY_TYPE", "b": "HALF_WIDTHS", "c": "HALF_HEIGHTS",
             "d": "SPEEDS", "e": "RENDER_MODES"}
    for old, new in decls.items():
        text = re.sub(r"(byte\[\] )%s( = new byte\[\])" % old,
                      r"\1%s\2" % new, text, count=1)
        text = re.sub(r"(?<![\w.])%s(?=\[)" % old, new, text)
    text = cross_members(text, "Game", GAME_MEMBERS)
    text = cross_members(text, "LevelMap", LEVELMAP_STATICS)
    text = qualified_refs(text)
    text = game_instance_refs(text)
    return text


def midlet_pipeline(text):
    m = methods_of(text)
    text = sub_all(text, MIDLET_FIELDS, m)
    # explicit declaration renames for the members whose old names collide
    # with the midlet's own overloaded methods
    text = text.replace("public static String[] e;",
                        "public static String[] strings;")
    text = text.replace("public SoundPlayer f;",
                        "public SoundPlayer soundPlayer;")
    text = text.replace("public boolean g;", "public boolean soundEnabled;")
    text = text.replace("public byte[] c;", "public byte[] saveSlotFlags;")
    text = text.replace("public int[] d;", "public int[] saveSlotTimes;")
    text = re.sub(r"\bthis\.c\b(?!\()", "this.saveSlotFlags", text)
    text = re.sub(r"\bthis\.d\b(?!\()", "this.saveSlotTimes", text)
    # SoundPlayer refs through the renamed soundPlayer field (f.a() was the
    # player thread's stop, f.a the thread field itself; sub_all's rename
    # drops the `this.` prefix, so match both forms)
    for pre in ("this.", ""):
        text = text.replace(pre + "soundPlayer.a()", pre + "soundPlayer.stop()")
        text = text.replace(pre + "soundPlayer.a = null;",
                            pre + "soundPlayer.thread = null;")
    # Game instance field `b` (save serializer entry points) keeps its name
    return text


PIPELINES = {
    "a.java": ("Player.java", player_pipeline),
    "c.java": ("LevelMap.java", levelmap_pipeline),
    "d.java": ("Enemy.java", enemy_pipeline),
    "f.java": ("Game.java", game_pipeline),
    "g.java": ("Projectile.java", projectile_pipeline),
    "ratchetandclank.java": ("ratchetandclank.java", midlet_pipeline),
}


def main():
    OUT.mkdir(parents=True, exist_ok=True)
    for src_name, (out_name, pipe) in PIPELINES.items():
        text = (SRC / src_name).read_text(encoding="utf-8")
        text = rename_classes(text, src_name)
        text = pipe(text)
        (OUT / out_name).write_text(text, encoding="utf-8", newline="\n")
        print("%-22s -> %-22s (%d lines)" %
              (src_name, out_name, len(text.splitlines())))
    print("done -- Entity.java/MenuItem.java/SoundPlayer.java are"
          " hand-written and must be present in src/ before compiling")


if __name__ == "__main__":
    main()




