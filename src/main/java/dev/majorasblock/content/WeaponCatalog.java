package dev.majorasblock.content;

import java.util.List;
import java.util.Optional;

/** Weapon data used by equipment, combat balancing, and visual attachment layers. */
public final class WeaponCatalog {
    public enum Class { ONE_HANDED_SWORD, TWO_HANDED_SWORD, RANGED, TOOL }

    public record Weapon(
            String id,
            String displayName,
            Class weaponClass,
            float damage,
            int durability,
            float reachBonus,
            String sheathTexture,
            String heldTexture,
            boolean canBeSheathed
    ) {}

    private static final List<Weapon> WEAPONS = List.of(
            new Weapon("hero_sword", "Hero's Sword", Class.ONE_HANDED_SWORD,
                    6.0f, 420, 0.0f, "sheath_hero", "hero_sword", true),
            new Weapon("razor_sword", "Razor Sword", Class.ONE_HANDED_SWORD,
                    7.0f, 100, 0.0f, "sheath_razor", "razor_sword", true),
            new Weapon("gilded_sword", "Gilded Sword", Class.ONE_HANDED_SWORD,
                    9.0f, 900, 1.0f, "sheath_gilded", "gilded_sword", true),
            new Weapon("deity_sword", "Deity's Sword", Class.TWO_HANDED_SWORD,
                    12.0f, 1_200, 0.5f, "deity_back_mount", "deity_sword", true),
            new Weapon("hero_bow", "Hero's Bow", Class.RANGED,
                    1.0f, 500, 0.0f, "bow_back_mount", "hero_bow", true),
            new Weapon("hookshot", "Hookshot", Class.TOOL,
                    0.0f, 350, 0.0f, "hookshot_mount", "hookshot", true)
    );

    private WeaponCatalog() {}

    public static List<Weapon> all() {
        return WEAPONS;
    }

    public static Optional<Weapon> byId(String id) {
        return WEAPONS.stream().filter(weapon -> weapon.id().equals(id)).findFirst();
    }

    public static Weapon require(String id) {
        return byId(id).orElseThrow(() -> new IllegalArgumentException("Unknown weapon: " + id));
    }
}
