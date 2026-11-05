# Majora's Block

A fan-made Minecraft Java adventure mod inspired by the atmosphere, world, masks, music, and three-day mystery of *The Legend of Zelda: Majora's Mask*.

## Current development build

- Minecraft Java Edition 26.2
- Fabric Loader 0.19.3+
- Fabric API 0.161.0+26.2
- Java 25
- Status: playable development build, not a finished release

## Content in the project

- **54 masks**, including Deku, Goron, and Zora form masks, Sponge Mask, Peabert Mask, and dozens of exploration masks.
- **20 relics and travel items**, plus eight Deity Spirit charms, the Ocarina, and the Clockwork Hourglass.
- **9 dungeons**: the four returning temple concepts and five original dungeons.
- **9 boss concepts**, with themed memorial arenas for each.
- **24 enemy concepts** and **24 friendly wandering-creature concepts**.
- **6 buildable landmarks**, including the Deity Fountain, Moon Gate, Clock Tower, Peabert Grove, Sponge Grotto, and Starfall Observatory.
- Six new defensive/traversal gear pieces, directional front/back/side flip mobility, a client-side focus camera, five original ocarina melodies, a three-day clock, DROWNED atmosphere mode, friendship travel, and session-based adventure progression.

The catalogues and item collections are in the build. Some encounter and visual content is still prototype-level; see the honest implementation status below.

## Playable systems

**Masks and forms.** Use a mask to activate its exploration ability. The three form masks track a session form and refresh distinct effects: Deku supports jumping and drifting, Goron provides resilience, and Zora supports underwater travel. This is an ability-based form system; fully custom player models and animations are not finished.

**Ocarina and songs.** Right-click the Ocarina to play the original Song of Friendship. Sneak and right-click to play the Song of Healing. Other original melodies are available through commands. If a nearby player has opted in to Friendship arrivals, the Ocarina can bring you to a safe landing near them.

**Deity Fountains.** An operator can build a Deity Fountain. Players standing near its center can claim one of the eight spirit blessings per server session with the spirit command.

**Moon route.** The Moon travel command builds a lunar exploration island above the central island in Minecraft's End dimension. A return point is saved for the current server session. This is a constructed Moon area, not yet a separate registered dimension.

**Adventure progression.** Using masks, relics, spirit charms, and claiming deity spirits awards session XP. Levels unlock short exploration effects. Progress is not yet saved permanently to the player's world data.

**Three-day cycle.** The clock cycles through three adventure days and announces the final-day and moon-approach stages. It does not yet perform a world-destroying moonfall event.

**DROWNED mode.** Optional per-player ambience with four intensity settings. It uses transient sound and text cues only and does not access or modify world or player save files.

**Focus camera.** Press **R** (rebindable in Options > Controls) to focus the nearest visible living entity in front of you within 16 blocks. The camera turns smoothly toward it and a focus label appears on the HUD. Press R again to release focus. This is a client-side camera/movement aid only; it does not add attacks, weapon actions, or damage logic.

**Multiplayer.** Friendship arrivals require the destination player to opt in. The current multiplayer work is limited and still needs live server testing.

## Commands

Run `/majorasblock help` in game for the command list.

Player commands:
- `/majorasblock status` and `/majorasblock cycle`
- `/majorasblock mask list` and `/majorasblock mask grant <id>`
- `/majorasblock relic list` and `/majorasblock relic grant <id>`
- `/majorasblock form status|clear`
- `/majorasblock flip front|back|left|right`
- `/majorasblock progress`
- `/majorasblock spirit list` and `/majorasblock spirit claim <id>`
- `/majorasblock creature list|summon <id>`
- `/majorasblock boss list`, `/majorasblock dungeon list`
- `/majorasblock moon travel|return`
- `/majorasblock friendship accept|deny`
- `/majorasblock drowned off|subtle|uneasy|full`
- `/majorasblock song friendship|healing|inverted_time|double_time`
- `/majorasblock song chosen_hour <hour> <minute>`

Operator commands:
- `/majorasblock dungeon build <dungeon_id>`
- `/majorasblock landmark build <landmark_id>`
- `/majorasblock boss memorial <boss_id>`
- `/majorasblock enemy list` and `/majorasblock enemy spawn <enemy_id>`
- `/majorasblock time slow|normal|set <hour> <minute>`

## Download and install

Open the [GitHub Actions page](https://github.com/Undertale-Dungeons/Majoras-Blocks/actions), choose the latest successful **Build** run, and download the **majoras-block** artifact. Extract it and place the playable JAR in the Minecraft 26.2 Fabric `mods` folder. Install Fabric Loader and the matching Fabric API first.

The build workflow uploads only the playable mod JAR, not the developer sources JAR.

## Still in development

- Original enemy entity types, models, AI, animations, and spawn rules. Current enemy spawns use vanilla entities as prototypes.
- Original boss entities and encounter logic. Current bosses are design entries with buildable memorial arenas, not active boss fights.
- Complete puzzle dungeons. The current builders create themed test shells, rooms, corridors, and pedestals, not finished puzzle campaigns.
- Custom player transformation models and animations.
- A separate Moon dimension. The current Moon destination is a built area in the End.
- Persistent RPG and spirit progression; current progress is session-based.
- Full custom item models, ocarina note-input UI, expanded camera/render effects, and full multiplayer playtesting.

The showcase is the long-term scope, not a claim that every pictured system is finished in this build.

## Credits and legal

Majora's Block is an unofficial fan project and is not affiliated with or endorsed by Nintendo, Mojang Studios, or Microsoft. Names and original game intellectual property belong to their respective owners. The project uses original code and project-created assets unless stated otherwise.
