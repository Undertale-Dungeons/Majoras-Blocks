# Feature implementation notes

## Equipment presentation

The active sword or tool is stored in an equipment carry state. While holstered, one-handed swords use a back sheath attachment and larger weapons use a dedicated back mount. Drawing moves the weapon attachment to the right hand. Shields use a back mount until raised, then move to the left arm. The render layer and animations will consume this state.

The swordsmith progression is Hero's Sword → Razor Sword → Gilded Sword. The Razor Sword is a temporary durability-focused upgrade; the Gilded Sword is the final one-handed upgrade, with the highest damage and an extra reach bonus.

## Original sprite atlas and music

The sprite atlas contains 32 original pixel-art icons covering sword variants, shields, a sheath, tools, new enemy families, the five original dungeon bosses, spirits, and adventure items. The friendship theme is a newly synthesized melody made for this project. Nintendo game sprites, soundtrack recordings, and extracted sound effects are not included.

## New threats and bosses

Enemy and boss catalogues provide encounter identifiers, home regions, health targets, movement targets, and mechanic briefs. They are data foundations, not finished entity AI. Every enemy still needs registration, models, animations, drops, spawn rules, and multiplayer tests.

## DROWNED atmosphere mode

DROWNED effects are rare and transient. It cannot modify save files, make replacement saves, delete worlds, or impersonate an online player. Final client-side cues must use original audio/visual assets and provide intensity and opt-out controls.

## Not yet complete

The current project still needs actual registered items and entities, boss AI, dungeon generation/structures, song note-recognition UI, camera/render layers, model animations, recipes, player/world persistent-data integration, and in-game multiplayer playtests.
