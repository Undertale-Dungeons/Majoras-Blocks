package dev.majorasblock.deity;

import java.util.List;

/** Hidden fountain spirits and their non-offensive traversal/defence upgrades. */
public final class DeitySpiritCatalog {
    public enum Power {
        SWIFT_STEP,
        MOON_VEIL,
        TIDE_BREATH,
        ROOTED_STANCE,
        ECHO_FOCUS,
        SUNWARD_SHELL,
        SKYBOUND_GRACE,
        QUIET_PULSE
    }

    public record Spirit(String id, String name, String fountainRegion,
                         Power power, String description) {}

    private static final List<Spirit> SPIRITS = List.of(
            new Spirit("spirit_of_motion", "Spirit of Motion", "The Verdant Scar",
                    Power.SWIFT_STEP, "Improves evasive movement distance"),
            new Spirit("spirit_of_moonlight", "Spirit of Moonlight", "Ikana",
                    Power.MOON_VEIL, "Reduces knockback and improves recovery"),
            new Spirit("spirit_of_tides", "Spirit of Tides", "The Shifting Coast",
                    Power.TIDE_BREATH, "Extends underwater exploration time"),
            new Spirit("spirit_of_stone", "Spirit of Stone", "The Cinder March",
                    Power.ROOTED_STANCE, "Improves footing and fall recovery"),
            new Spirit("spirit_of_echoes", "Spirit of Echoes", "The Quiet Meridian",
                    Power.ECHO_FOCUS, "Makes focus switching more responsive"),
            new Spirit("spirit_of_sunrise", "Spirit of Sunrise", "Woodfall",
                    Power.SUNWARD_SHELL, "Improves resistance to harsh environments"),
            new Spirit("spirit_of_wind", "Spirit of Wind", "Snowhead",
                    Power.SKYBOUND_GRACE, "Improves aerial control"),
            new Spirit("spirit_of_stillness", "Spirit of Stillness", "Stone Tower",
                    Power.QUIET_PULSE, "Improves resistance to disorienting effects")
    );

    private DeitySpiritCatalog() {}

    public static List<Spirit> all() {
        return SPIRITS;
    }

    public static Spirit byId(String id) {
        return SPIRITS.stream().filter(spirit -> spirit.id().equals(id)).findFirst().orElse(null);
    }
}
