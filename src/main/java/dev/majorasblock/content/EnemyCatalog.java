package dev.majorasblock.content;

import java.util.List;
import java.util.Optional;

/** Returning-inspired and original enemy roster. Entries are encounter design data. */
public final class EnemyCatalog {
    public enum Threat { MINOR, STANDARD, ELITE }

    public record Enemy(String id, String name, String homeRegion, Threat threat,
                        double health, double movementSpeed, String fieldBehavior) {}

    private static final List<Enemy> ENEMIES = List.of(
            new Enemy("ashen_wisp", "Ashen Wisp", "Cinder March", Threat.MINOR, 10, 0.22, "Fades in and out near warm stone"),
            new Enemy("glasswing", "Glasswing", "Verdant Scar", Threat.MINOR, 8, 0.31, "Circles bright crystal and avoids darkness"),
            new Enemy("mirebound_husk", "Mirebound Husk", "Verdant Scar", Threat.STANDARD, 24, 0.18, "Wanders wet ruins and flooded paths"),
            new Enemy("clockwork_mite", "Clockwork Mite", "Quiet Meridian", Threat.MINOR, 6, 0.28, "Travels in groups and repairs mechanisms"),
            new Enemy("hollow_stag", "Hollow Stag", "Starfall Reach", Threat.ELITE, 34, 0.30, "Guards forest clearings"),
            new Enemy("echo_shade", "Echo Shade", "Quiet Meridian", Threat.STANDARD, 18, 0.24, "Repeats short movement patterns"),
            new Enemy("tide_lurker", "Tide Lurker", "Shifting Coast", Threat.STANDARD, 22, 0.26, "Moves between submerged cover points"),
            new Enemy("lantern_mimic", "Lantern Mimic", "Quiet Meridian", Threat.ELITE, 30, 0.20, "Imitates a safe light source"),
            new Enemy("starfall_sentinel", "Starfall Sentinel", "Starfall Reach", Threat.ELITE, 40, 0.17, "Patrols marked celestial paths"),
            new Enemy("gravebloom", "Gravebloom", "Verdant Scar", Threat.STANDARD, 20, 0.16, "Wakes when nearby mechanisms activate"),
            new Enemy("thornback", "Thornback", "Verdant Scar", Threat.STANDARD, 26, 0.19, "Armored crawler that guards narrow paths"),
            new Enemy("frostbell", "Frostbell", "Snowhead", Threat.MINOR, 12, 0.23, "Rings to call nearby winter creatures"),
            new Enemy("slag_golem", "Slag Golem", "Cinder March", Threat.ELITE, 48, 0.13, "Slow guardian that cools into a vulnerable state"),
            new Enemy("reef_stalker", "Reef Stalker", "Shifting Coast", Threat.STANDARD, 21, 0.28, "Ambushes from reef shadows"),
            new Enemy("mirror_wraith", "Mirror Wraith", "Ikana", Threat.ELITE, 32, 0.21, "Creates decoy reflections"),
            new Enemy("bellgrave_keeper", "Bellgrave Keeper", "Ikana", Threat.STANDARD, 25, 0.16, "Patrols old bell chambers"),
            new Enemy("dustjack", "Dustjack", "Starfall Reach", Threat.MINOR, 9, 0.34, "Skips across open ground in quick bursts"),
            new Enemy("shardmole", "Shardmole", "Verdant Scar", Threat.STANDARD, 19, 0.25, "Digs through soft blocks"),
            new Enemy("hushling", "Hushling", "Quiet Meridian", Threat.MINOR, 7, 0.20, "Moves only when the player looks away"),
            new Enemy("brass_howler", "Brass Howler", "Cinder March", Threat.ELITE, 36, 0.18, "Uses a loud pulse to disrupt nearby mechanisms"),
            new Enemy("mooncalf", "Mooncalf", "Moon Expanse", Threat.STANDARD, 23, 0.27, "Wanders in pairs beneath pale trees"),
            new Enemy("inkfin", "Inkfin", "Shifting Coast", Threat.MINOR, 11, 0.33, "Darts through dark water"),
            new Enemy("rootbound_knight", "Rootbound Knight", "Woodfall", Threat.ELITE, 38, 0.16, "Protects old temple gates"),
            new Enemy("clockface_crawler", "Clockface Crawler", "Quiet Meridian", Threat.STANDARD, 17, 0.24, "Clings to ceilings and follows bell sounds")
    );

    private EnemyCatalog() {}

    public static List<Enemy> all() {
        return ENEMIES;
    }

    public static Optional<Enemy> byId(String id) {
        return ENEMIES.stream().filter(enemy -> enemy.id().equals(id)).findFirst();
    }

    public static List<Enemy> original() {
        return ENEMIES;
    }
}
