# Majora's Block

A fan-made Minecraft Java adventure mod inspired by the atmosphere, world, and sense of mystery of The Legend of Zelda: Majora's Mask.

## Current build

- Minecraft Java Edition 26.2
- Fabric Loader 0.19.3 or newer
- Fabric API 0.161.0+26.2
- Java 25
- Development build; not a finished release

## What's playable in this build

- Ocarina: appears in the Majora's Block Creative tab. Right-click plays the original Song of Friendship. Sneak and right-click plays the original Song of Healing.
- Friendship arrival: /majorasblock friendship accept or deny controls whether other players may arrive nearby. The Ocarina only moves you to a nearby player who has opted in and only when a safe landing spot is found.
- Clockwork Hourglass: a craftable item that advances the adventure clock toward sunrise.
- Eight Spirit Charms: craftable reusable items that grant short exploration or defensive buffs.
- Original songs: Friendship, Healing, Inverted Time, Chosen Hour, and Double Time play through Minecraft's chime instrument.
- Time systems: view the adventure clock with /majorasblock status; use the song commands for the supported time effects.
- DROWNED atmosphere: optional per-player sound and text ambience, with four intensity levels.

## Getting the build

Open the repository's Actions page at https://github.com/Undertale-Dungeons/Majoras-Blocks/actions, choose the latest successful Build run, and download its majoras-block artifact. Extract the archive and place the playable JAR in your Minecraft 26.2 Fabric mods folder. Install Fabric Loader and the matching Fabric API first.

The workflow uploads only the playable mod JAR. Developer source archives are not included in the artifact.

## Commands

Run /majorasblock help in game to see the current command list.

Player commands:
- /majorasblock status
- /majorasblock drowned off|subtle|uneasy|full
- /majorasblock friendship accept and /majorasblock friendship deny
- /majorasblock song friendship [player]
- /majorasblock song healing
- /majorasblock song inverted_time
- /majorasblock song double_time
- /majorasblock song chosen_hour <hour> <minute>
- /majorasblock dungeon list
- /majorasblock spirit list

Operator commands:
- /majorasblock dungeon build <dungeon_id>
- /majorasblock enemy list
- /majorasblock enemy spawn <enemy_id>
- /majorasblock time slow|normal|set <hour> <minute>

## What is not finished yet

The dungeon builder currently creates test shells and pedestals, not complete puzzle dungeons. The enemy roster is still mapped to vanilla prototype entities; custom enemy models and AI are unfinished. Boss encounters, puzzle interactions, persistent spirit progression, and live multiplayer testing are also unfinished. Do not treat the catalogue entries as complete encounters.

## Project and legal

This is an unofficial fan project and is not affiliated with or endorsed by Nintendo, Mojang Studios, or Microsoft. Names and original game intellectual property belong to their respective owners. This repository's code and assets remain subject to the terms stated in the repository until an explicit software license is published.
