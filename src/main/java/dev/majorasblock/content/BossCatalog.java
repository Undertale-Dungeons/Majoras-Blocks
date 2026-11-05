package dev.majorasblock.content;

import java.util.List;
import java.util.Optional;

/** Boss encounter briefs for the four returning temples and five original dungeons. */
public final class BossCatalog {
    public record Boss(String id, String name, String dungeonId, String arenaTheme,
                       String primaryMechanic, String phaseTwoChange) {}

    private static final List<Boss> BOSSES = List.of(
            new Boss("odolwa", "Odolwa", "woodfall", "Overgrown ceremonial arena",
                    "Read attack tells and create openings", "Adds faster area patterns"),
            new Boss("goht", "Goht", "snowhead", "Ice-ring foundry",
                    "Use the arena route and environmental switches", "Changes route hazards"),
            new Boss("gyorg", "Gyorg", "great_bay", "Flooded circular basin",
                    "Alternate safe platforms and underwater routes", "Shortens safe windows"),
            new Boss("twinmold", "Twinmold", "stone_tower", "Inverted desert chamber",
                    "Switch between elevated and ground routes", "Both targets coordinate attacks"),
            new Boss("black_knight", "The Black Knight", "black_knight_trial",
                    "Antlered black-and-white star court",
                    "Align the constellations to prove your worth before Majora",
                    "The star pattern changes between trial stages"),

            new Boss("the_bloom_warden", "The Bloom Warden", "glassroot_catacombs",
                    "Crystal greenhouse", "Redirect light through root mirrors", "Crystal growth changes arena lanes"),
            new Boss("the_cinder_colossus", "The Cinder Colossus", "emberwake_foundry",
                    "Cooling furnace", "Open cooling channels to expose its core", "Furnace vents cycle more quickly"),
            new Boss("the_undertow_oracle", "The Undertow Oracle", "tideglass_observatory",
                    "Tidal lens chamber", "Use reflections to identify the real target", "Tides invert and alter platforms"),
            new Boss("the_astral_bailiff", "The Astral Bailiff", "starfall_bastion",
                    "Fallen-star courtyard", "Align constellation plates to break its ward", "Gravity shifts between arena zones"),
            new Boss("the_unwound_regent", "The Unwound Regent", "clockwork_reliquary",
                    "Clock archive", "Sequence clock switches without resetting the room", "The room repeats its last layout")
    );

    private BossCatalog() {}

    public static List<Boss> all() {
        return BOSSES;
    }

    public static Optional<Boss> byId(String id) {
        return BOSSES.stream().filter(boss -> boss.id().equals(id)).findFirst();
    }

    public static List<Boss> original() {
        return BOSSES.stream()
                .filter(boss -> {
                    DungeonCatalog.Dungeon dungeon = DungeonCatalog.byId(boss.dungeonId());
                    return dungeon != null && dungeon.origin() == DungeonCatalog.Origin.ORIGINAL;
                })
                .toList();
    }
}
