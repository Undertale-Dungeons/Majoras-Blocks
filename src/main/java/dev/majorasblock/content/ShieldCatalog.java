package dev.majorasblock.content;

import java.util.List;
import java.util.Optional;

/** Shield definitions and the attachment used while the shield is not raised. */
public final class ShieldCatalog {
    public record Shield(
            String id,
            String displayName,
            float blockStrength,
            int durability,
            boolean reflectsMagic,
            String backTexture,
            String armTexture
    ) {}

    private static final List<Shield> SHIELDS = List.of(
            new Shield("hero_shield", "Hero's Shield", 0.75f, 420,
                    false, "hero_shield_back", "hero_shield_arm"),
            new Shield("mirror_shield", "Mirror Shield", 0.85f, 560,
                    true, "mirror_shield_back", "mirror_shield_arm"),
            new Shield("deku_shield", "Deku Shield", 0.55f, 180,
                    false, "deku_shield_back", "deku_shield_arm"),
            new Shield("goron_guard", "Goron Guard", 0.9f, 700,
                    false, "goron_guard_back", "goron_guard_arm"),
            new Shield("zora_guard", "Zora Guard", 0.7f, 500,
                    true, "zora_guard_back", "zora_guard_arm")
    );

    private ShieldCatalog() {}

    public static List<Shield> all() {
        return SHIELDS;
    }

    public static Optional<Shield> byId(String id) {
        return SHIELDS.stream().filter(shield -> shield.id().equals(id)).findFirst();
    }
}
