# Feature implementation notes

## Items and exploration

The registered Ocarina is visible in a dedicated Majora's Block Creative tab. It plays the original Friendship theme on normal use and the Healing theme while sneaking. Friendship arrival requires the nearby recipient to opt in for the current server session and checks for a safe landing position.

The Clockwork Hourglass advances the adventure clock toward sunrise. Eight reusable Spirit Charms apply short, five-minute exploration or defensive effects. All ten items are listed in the custom Creative tab and have crafting recipes.

## Original music and time

The five original note sequences are Friendship, Inverted Time, Chosen Hour, Double Time, and Healing. They are played using Minecraft's chime instrument. The day-clock system supports viewing the current adventure day and supported time-song effects.

## Dungeons and enemies

The catalogue contains four returning region concepts and five original dungeons, alongside nine boss concepts and 24 enemy concepts. The dungeon command currently builds a primitive safe test shell, not a complete puzzle dungeon. Enemy spawn entries currently use vanilla base entities as placeholders; original AI, models, animation, loot, and spawn rules are unfinished.

## DROWNED atmosphere mode

DROWNED is an optional per-player atmosphere layer with four intensity levels. It uses transient sound and text cues only and does not access world or player save files.

## Still in development

Complete dungeon puzzle interactions, original enemy and boss entities, boss encounter phases, persistent spirit progression, client-side ocarina note input, custom camera/render layers, and real multiplayer playtesting remain unfinished. CI verifies the build and unit tests; it does not replace launching the mod in Minecraft.
