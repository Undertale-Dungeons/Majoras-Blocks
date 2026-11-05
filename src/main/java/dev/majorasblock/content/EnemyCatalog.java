package dev.majorasblock.content;

import java.util.List;

/** Original enemy roster intended to complement returning classic-inspired foes. */
public final class EnemyCatalog {
    public enum Threat { MINOR, STANDARD, ELITE }

    public record Enemy(
            String id,
            String name,
            String homeRegion,
            Threat threat,
            double health,
            double movementSpeed,
            String fieldBehavior
    ) {}

    private static final List<Enemy> ENEMIES = List.of(
            new Enemy("ashen_wisp", "Ashen Wisp", "Cinder March", Threat.MINOR, 10, 0.22,
                    "Fades in and out near warm stone"),
            new Enemy("glasswing_bat", "Glasswing", "Verdant Scar", Threat.MINOR, 8, 0.31,
                    "Circles bright crystal and avoids darkness"),
            new Enemy("mirebound_husk", "Mirebound Husk", "Verdant Scar", Threat.STANDARD, 24, 0.18,
                    "Moves slowly through shallow water and mud"),
            new Enemy("clockwork_mite", "Clockwork Mite", "Quiet Meridian", Threat.MINOR, 6, 0.28,
                    "Travels in small groups and repairs nearby mechanisms"),
            new Enemy("hollow_stag", "Hollow Stag", "Starfall Reach", Threat.ELITE, 34, 0.30,
                    "Guards forest clearings and retreats when its ward is broken"),
            new Enemy("echo_shade", "Echo Shade", "Quiet Meridian", Threat.STANDARD, 18, 0.24,
                    "Repeats a harmless movement pattern before changing direction"),
            new Enemy("tide_lurker", "Tide Lurker", "Shifting Coast", Threat.STANDARD, 22, 0.26,
                    "Moves between submerged cover points"),
            new Enemy("lantern_mimic", "Lantern Mimic", "The Quiet Meridian", Threat.ELITE, 30, 0.20,
                    "Imitates a safe light source until approached"),
            new Enemy("starfall_sentinel", "Starfall Sentinel", "Starfall Reach", Threat.ELITE, 40, 0.17,
                    "Patrols marked paths and returns to its post"),
            new Enemy("gravebloom", "Gravebloom", "The Verdant Scar", Threat.STANDARD, 20, 0.16,
                    "Remains still until a nearby mechanism is activated")
    );

    private EnemyCatalog() {}

    public static List<Enemy> all() {
        return ENEMIES;
    }

    public static Enemy byId(String id) {
        return ENEMIES.stream().filter(enemy -> enemy.id().equals(id)).findFirst().orElse(null);
    }
}
