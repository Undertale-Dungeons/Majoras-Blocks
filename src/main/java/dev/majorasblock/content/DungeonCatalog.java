package dev.majorasblock.content;

import java.util.List;

/**
 * Adventure dungeon catalogue. The four legacy dungeons remain part of the
 * experience, while five original dungeons give the world a second identity.
 */
public final class DungeonCatalog {
    public enum Origin { LEGACY, ORIGINAL }

    public record Dungeon(
            String id,
            String name,
            Origin origin,
            String region,
            String theme,
            String bossId,
            String puzzleFocus
    ) {}

    private static final List<Dungeon> DUNGEONS = List.of(
            new Dungeon("woodfall", "Woodfall Temple", Origin.LEGACY,
                    "Woodfall", "Overgrown ruins", "odolwa", "Water routing and rotating rooms"),
            new Dungeon("snowhead", "Snowhead Temple", Origin.LEGACY,
                    "Snowhead", "Frozen mountain", "goht", "Heat flow and shifting ice"),
            new Dungeon("great_bay", "Great Bay Temple", Origin.LEGACY,
                    "Great Bay", "Coastal machinery", "gyorg", "Water pressure and current direction"),
            new Dungeon("stone_tower", "Stone Tower Temple", Origin.LEGACY,
                    "Ikana", "Inverted stone fortress", "twinmold", "Orientation and mirrored routes"),

            new Dungeon("glassroot_catacombs", "Glassroot Catacombs", Origin.ORIGINAL,
                    "The Verdant Scar", "Crystal roots beneath an abandoned garden",
                    "the_bloom_warden", "Light paths through living crystal"),
            new Dungeon("emberwake_foundry", "Emberwake Foundry", Origin.ORIGINAL,
                    "The Cinder March", "A silent foundry built around ancient heat vents",
                    "the_cinder_colossus", "Pressure valves and cooling channels"),
            new Dungeon("tideglass_observatory", "Tideglass Observatory", Origin.ORIGINAL,
                    "The Shifting Coast", "An observatory suspended above a deep tidal basin",
                    "the_undertow_oracle", "Reflections, tides, and rotating lenses"),
            new Dungeon("starfall_bastion", "Starfall Bastion", Origin.ORIGINAL,
                    "The Starfall Reach", "A ruined fortress beneath a fallen constellation",
                    "the_astral_bailiff", "Constellation alignment and low-gravity rooms"),
            new Dungeon("clockwork_reliquary", "Clockwork Reliquary", Origin.ORIGINAL,
                    "The Quiet Meridian", "A buried archive of interlocking clock chambers",
                    "the_unwound_regent", "Repeating rooms and time-shifted switches"),
            new Dungeon("black_knight_trial", "Black Knight Trial Court", Origin.ORIGINAL,
                    "The Last Threshold", "An antlered star court immediately before Majora",
                    "black_knight", "Constellation alignment and composure")
    );

    private DungeonCatalog() {}

    public static List<Dungeon> all() {
        return DUNGEONS;
    }

    public static List<Dungeon> legacy() {
        return DUNGEONS.stream().filter(dungeon -> dungeon.origin() == Origin.LEGACY).toList();
    }

    public static List<Dungeon> original() {
        return DUNGEONS.stream().filter(dungeon -> dungeon.origin() == Origin.ORIGINAL).toList();
    }

    public static Dungeon byId(String id) {
        return DUNGEONS.stream()
                .filter(dungeon -> dungeon.id().equals(id))
                .findFirst()
                .orElse(null);
    }
}
