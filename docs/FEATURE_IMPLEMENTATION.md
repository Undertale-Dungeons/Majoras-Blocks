# Feature implementation notes

## Collections and equipment

The current registries expose 54 masks, 20 relics, eight Deity Spirit charms, the Ocarina, and the Clockwork Hourglass in the Majora's Block Creative tab. Mask and relic catalogues have crafting recipes. Mask/relic use grants temporary exploration effects. Deku, Goron, and Zora form masks also set a session form state whose effects are refreshed while active; no custom player model swap is present yet.

Six added defensive/traversal relics are Moonlit Buckler, Reefguard Ward, Rootbound Vest, Stargazer Cloak, Quietstep Boots, and Springheel Gloves. They provide temporary protection or exploration effects when used and are not equipment-slot armour.

Directional front, back, and side flip movement bursts are available through `/majorasblock flip`. They have a cooldown and are independent from offensive combat actions.

## Focus camera

The client initializer registers a configurable focus key (default R). It selects the best-aligned visible living entity within 16 blocks, smoothly turns the player's view toward that entity, and displays its name and distance on the HUD. Focus is client-only and releases when the entity becomes invalid, leaves range, or is no longer visible. It does not send attack actions, apply damage, or implement weapon handling.

## RPG and spirit progression

Using masks and relics awards session XP. Spirit charms award XP and effects. The eight Deity Spirits can be claimed near a Deity Fountain built with the landmark command; each player may claim each spirit once per server session. Level rewards and spirit blessings are effects-based. Progression is not persistently saved yet.

## Moon and three-day clock

The adventure clock reports a repeating three-day cycle and sends final-day/moon-approach announcements. Moon travel builds a lunar surface, crater rings, star pillars, and a gate above the End's central island. The player can return to the saved point from the same server session. It is not a separate registered dimension and does not trigger a destructive moonfall event.

## Songs and multiplayer

The Ocarina plays original Song of Friendship on normal use and Song of Healing while sneaking. The original score catalog contains Friendship, Healing, Inverted Time, Chosen Hour, and Double Time. Friendship arrivals are opt-in and use a safe landing check. These multiplayer features still need dedicated-server playtesting.

## Dungeons, bosses, enemies, and landmarks

There are nine dungeon entries and themed test-shell builders, nine boss briefs and memorial arenas, 24 enemy design entries with vanilla entity spawn prototypes, 24 friendly-creature entries mapped to vanilla animals, and six buildable landmarks. Enemy prototypes are not original registered entities; boss memorials are not boss encounters; and dungeon shells are not complete puzzle campaigns.

## DROWNED atmosphere

DROWNED is an optional per-player atmosphere layer with four intensity settings. It uses transient sound and text cues only and does not read, write, rename, delete, replace, or create world/player save files.

## Still in development

Custom enemy and boss entity classes, models, AI, animation, encounter interactions, full dungeon puzzle logic, custom player models, persistent RPG progression, a separate Moon dimension, client-side ocarina note input, expanded camera/render effects, and live multiplayer testing remain unfinished.
