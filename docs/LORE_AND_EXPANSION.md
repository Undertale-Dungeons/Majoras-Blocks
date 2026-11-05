# Story direction and expansion plan

This file is a development reference. The public README intentionally keeps story reveals out of view.

## The new story foundation

The world was once connected by the Meridian Loom, an ancient network that kept distant places and important memories in harmony. Its keepers believed time was not something to own or command. It was a path shared by everyone who lived in the world.

When the Loom failed, its safeguards split into a repeating three-day rhythm. The moon became the visible sign of that failure: not simply a falling object, but a vast signal returning to the same point until the world can answer it.

The four familiar regions still hold their own history and guardians. Beyond them lie five sealed sanctums built by a later culture that tried to repair the Loom. Their builders left behind different ideas about memory, change, responsibility, and the cost of forcing the world to stay the same.

The player is not asked to collect power for its own sake. Each region should tell a small story about people adapting to a world that keeps repeating, and each dungeon should change how the player understands the clock.

## The original boss dungeons and the Black Knight trial

| Dungeon | Region | Boss | Main identity |
|---|---|---|---|
| Glassroot Catacombs | The Verdant Scar | The Bloom Warden | Crystal gardens, light paths, living-root machinery |
| Emberwake Foundry | The Cinder March | The Cinder Colossus | Cooling channels, pressure systems, ancient industry |
| Tideglass Observatory | The Shifting Coast | The Undertow Oracle | Reflections, tides, rotating lenses |
| Starfall Bastion | The Starfall Reach | The Astral Bailiff | Constellations, gravity rooms, broken astronomy |
| Clockwork Reliquary | The Quiet Meridian | The Unwound Regent | Repeating rooms, memory records, clock mechanisms |

The Black Knight Trial Court is a separate final threshold before Majora. It tests constellation alignment and composure rather than combat. The original four dungeons remain major pillars. Across side quests, exploration, characters, puzzles, items, and boss content, the design target is an approximately even split between returning inspiration and new material.

## New enemy families

- Ashen Wisps haunt warm stone and old furnaces.
- Glasswings gather around bright crystal formations.
- Mirebound Husks wander wet ruins and flooded paths.
- Clockwork Mites travel in groups and interact with machinery.
- Hollow Stags protect quiet clearings.
- Echo Shades repeat short movement patterns.
- Tide Lurkers move between submerged hiding places.
- Lantern Mimics imitate lights in abandoned corridors.
- Starfall Sentinels patrol old celestial routes.
- Graveblooms remain still until nearby mechanisms wake them.

## Songs and the shared clock

- **Song of Friendship** requests an arrival beside an online friend. Both players must have each other added, and the destination player must allow arrivals.
- **Song of Inverted Time** slows only the adventure day clock. Minecraft simulation, movement, redstone, and entity behavior must continue at normal speed.
- **Song of the Chosen Hour** advances the clock to the next occurrence of a selected 24-hour time.
- **Song of Double Time** advances to the next morning.
- Existing travel, weather, healing, and form songs remain part of the wider songbook.

Server authority decides shared-world time. Clients may show previews, but they do not independently change the clock.

## Deity fountains

Small spirits are hidden near quiet fountains, ruins, and remote landmarks. Each spirit grants one permanent traversal or defensive improvement. Spirit collection belongs to the individual player, while the fountain location itself is shared by the server.

The first upgrade pass focuses on movement, aerial control, underwater exploration, environmental protection, and recovery. Later upgrades can expand this system once the core movement and progression loops are tested.

## Focus movement and camera

The intended default view is third-person behind the player, with the familiar perspective toggle still available. Target focus is a movement and camera mode: jumping while holding back triggers a backflip, and jumping while holding a direction triggers a side dash. These are represented as movement states so animation and input work can be connected separately.

## Optional DROWNED atmosphere mode

DROWNED is designed as an optional, server-configurable atmosphere layer. It can use unusual ambient sounds, distant silhouettes, delayed reflections, and brief non-interactive visual distortions. It must never write to, corrupt, rename, delete, or create world or player save files. It does not create a replacement save and it never blocks normal saving or loading.

The horror comes from uncertainty and atmosphere, not from damaging the player's world or device. Every effect must have a clear opt-out, intensity setting, and multiplayer-safe synchronization rule.

## Development order

1. Keep the catalogue and rule models independent from client rendering.
2. Add server-owned player progression, friend-arrival consent, and world-clock storage.
3. Register entities, song input, and fountain interactions.
4. Build dungeon structures, boss arenas, and original pixel-art assets.
5. Add third-person focus camera and movement animations.
6. Integrate DROWNED as a transient effect layer with save-safety tests.
7. Run multiplayer tests before marking any feature complete.
