# Feature implementation notes

## Collections and equipment

The current registries expose 54 masks, 20 relics, eight Deity Spirit charms, the Ocarina, and the Clockwork Hourglass in the Majora's Block Creative tab. Mask and relic catalogues have crafting recipes. Mask/relic use grants temporary exploration effects. Deku, Goron, and Zora form masks also set a session form state whose effects are refreshed while active; no custom player model swap is present yet.

Six added defensive/traversal relics are Moonlit Buckler, Reefguard Ward, Rootbound Vest, Stargazer Cloak, Quietstep Boots, and Springheel Gloves. They provide temporary protection or exploration effects when used and are not equipment-slot armour.

Directional front, back, and side flip movement bursts are available through `/majorasblock flip`. They have a cooldown. A client-side Z-target focus camera is bound to R and can select visible living entities within 64 blocks; V cycles to the next visible target. Focus can be activated at any time and only changes view. Ledge hanging is a server-side traversal mechanic: hold crouch while airborne and colliding with a wall to hang, then jump while crouching to vault onto the ledge.

## Focus camera

The client initializer registers a configurable focus key (default R) and target-cycle key (V). It selects visible living entities within 64 blocks, smoothly turns the player's view toward the selected entity, and displays its name and distance on the HUD. Focus is client-only and releases when the entity becomes invalid, leaves range, or is no longer visible. It does not send attack actions, apply damage, or implement weapon handling.

## Rupees and blacksmith

Rupees are registered as a stackable inventory currency. Players can check their wallet with `/majorasblock rupees balance`; operators can grant test currency with `/majorasblock rupees grant <amount>`. The Blacksmith Forge is a buildable landmark. A player at the forge can order one heirloom for 100 rupees. The Hero's Sword is available without a form requirement; the Deku, Goron, and Zora heirlooms require the matching active form. Orders take 24,000 adventure-clock ticks (one in-game day) and can be collected at the forge with `/majorasblock smith collect`. Orders are session-only. The heirlooms are collectible/display items with no attack or damage behavior.

## Clock Town shops, pots and smithing

The Blacksmith Forge and five Clock Town stalls are buildable landmarks with named villager shopkeepers. Shop commands sell a shield, food, torches, lanterns, kindling sticks, Deku Nuts, photographs, Golden Dust, pottery, spirit goods and lighting/craft supplies. The spark stall is a non-explosive lighting shop.

The Razor Sword order costs 100 rupees plus a Hero's Sword and Deku Photograph. It has a 100-use durability meter; right-clicking consumes a use and at zero replaces it with a Hero's Sword. The Gilded Sword order costs 100 rupees plus a Razor Sword, Golden Dust and Odolwa Portrait. These heirlooms are progression/display collectibles; they do not implement attack or damage behavior.

Breaking a vanilla decorated pot adds randomized supplies: rupees, sticks, Deku Nuts, seeds, bread and rare upgrade/treasure materials. Sticks are sold and dropped as kindling and use their vanilla furnace fuel behavior.

## Hidden spirits and life crystals

Spirit Grove and Spirit Spring locations award a Spirit Shard. Heart Piece Grove and Heart Crystal Cave locations award a Life Crystal. Each player can claim each site only once per world; claims are saved under the world data directory. Boss catalogue rewards are triggered by an operator after a victory with `/majorasblock boss reward <boss_id>`. A boss reward can be issued only once per world, and drops one Life Crystal solo or two when multiple players are online. Because custom boss encounters are not implemented yet, this is a manual post-victory reward trigger.

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
