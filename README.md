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
- **10 dungeon entries**: the four returning temple concepts, five original dungeons, and the Black Knight's trial court.
- **10 boss concepts**, including the Black Knight as the trial immediately before Majora, with themed memorial arenas for each.
- **24 enemy concepts** and **24 friendly wandering-creature concepts**.
- **16 buildable landmarks**, including the Blacksmith Forge, five Clock Town shops, spirit and heart shrines, Deity Fountain, Moon Gate, Clock Tower, Peabert Grove, Sponge Grotto, and Starfall Observatory.
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

**Z-target focus.** Press **R** (rebindable in Options > Controls) to focus a visible living entity within 64 blocks. Focus can be started at any time, including when the target is not directly in front of you. Press **V** to cycle through visible targets and R again to release. The camera turns smoothly toward the selected target and the HUD shows its name and distance. This is a focus and movement feature, not an attack or damage system.

**Cliff hanging.** Hold crouch while airborne and pressing against a wall to catch a ledge. Jump while still holding crouch to vault up and forward; release crouch to let go. This is a server-side traversal mechanic for multiplayer.

**Rupees and blacksmith.** Rupees are an inventory currency. Build the forge with `/majorasblock landmark build blacksmith`. At the forge, order with `/majorasblock smith order hero|deku|goron|zora`. Each order costs 100 rupees and takes one in-game day. Use `/majorasblock smith status` to check the timer and `/majorasblock smith collect` when ready. Deku, Goron, and Zora heirlooms require the matching form. The Razor Sword upgrade costs another 100 rupees plus the Hero's Sword and a Deku Photograph. It has 100 ceremonial uses; right-click spends one and at zero it returns to the Hero's Sword. The Gilded Sword costs 100 rupees plus a Razor Sword, Golden Dust and an Odolwa Portrait. These are progression/display collectibles, not damaging weapons. Operators can grant test currency with `/majorasblock rupees grant <amount>`; players can check `/majorasblock rupees balance`. Orders are session-only.

**Clock Town shops.** Build `clocktown_market`, `curio_shop`, `spark_shop`, `pottery_shop` and `spirit_shop` as separate stalls using `/majorasblock landmark build <id>`. Each spawns a named villager. Use `/majorasblock shop list` and `/majorasblock shop buy <item> [amount]` near the matching stall. The spark shop sells lighting and craft supplies rather than explosives.

**Black Knight trial.** Build `black_knight_trial` for a black-and-white antlered statue court. The Knight is catalogued as the trial before Majora. Use `/majorasblock knight lore` to read Tatl's warnings, then `/majorasblock knight trial` and follow the star directions with `/majorasblock knight align north|east|west`. Completing the non-combat alignment trial awards a Black Knight Shard once per player session. Maces, axes, halberds and the shard are decorative collectibles only; they do not attack or change damage.

**Pots and hidden treasures.** Breaking vanilla decorated pots can yield rupees, sticks, Deku Nuts, seeds, bread and rare Golden Dust or Spirit Shards. Build `spirit_grove` or `spirit_spring` and use `/majorasblock treasure claim <id>` for a Spirit Shard. Build `heart_grove` or `heart_cave` to find a Life Crystal. Each location is claimable once per player per world. After a boss victory, an operator can use `/majorasblock boss reward <boss_id>`; each boss reward is globally one-time per world, yielding one Life Crystal solo or two when multiple players are online. Bosses are still catalogue/prototype entries, so this is a manual post-victory trigger.

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
- Original boss entities and encounter logic. Current bosses, including the Black Knight, are design entries with buildable memorial arenas or trial set pieces, not active combat encounters. Weapon-shaped collectibles remain non-functional display items.
- Complete puzzle dungeons. The current builders create themed test shells, rooms, corridors, and pedestals, not finished puzzle campaigns.
- Custom player transformation models and animations.
- A separate Moon dimension. The current Moon destination is a built area in the End.
- Persistent RPG and spirit progression; current progress is session-based.
- Full custom item models, ocarina note-input UI, expanded camera/render effects, and full multiplayer playtesting.

The showcase is the long-term scope, not a claim that every pictured system is finished in this build.

## Credits and legal

Majora's Block is an unofficial fan project and is not affiliated with or endorsed by Nintendo, Mojang Studios, or Microsoft. Names and original game intellectual property belong to their respective owners. The project uses original code and project-created assets unless stated otherwise.
