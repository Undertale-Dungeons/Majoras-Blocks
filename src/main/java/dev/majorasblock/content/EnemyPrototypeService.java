package dev.majorasblock.content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;

import java.util.Map;
import java.util.Optional;

/**
 * Temporary enemy-spawn prototypes. Each prototype uses a known vanilla base
 * entity until its custom registered type, AI, and renderer are implemented.
 */
public final class EnemyPrototypeService {
    private static final Map<String, String> BASE_TYPES = Map.ofEntries(
            Map.entry("ashen_wisp", "blaze"),
            Map.entry("glasswing", "phantom"),
            Map.entry("mirebound_husk", "drowned"),
            Map.entry("clockwork_mite", "silverfish"),
            Map.entry("hollow_stag", "ravager"),
            Map.entry("echo_shade", "vex"),
            Map.entry("tide_lurker", "guardian"),
            Map.entry("lantern_mimic", "shulker"),
            Map.entry("starfall_sentinel", "iron_golem"),
            Map.entry("gravebloom", "bogged"),
            Map.entry("thornback", "spider"),
            Map.entry("frostbell", "stray"),
            Map.entry("slag_golem", "iron_golem"),
            Map.entry("reef_stalker", "guardian"),
            Map.entry("mirror_wraith", "vex"),
            Map.entry("bellgrave_keeper", "skeleton"),
            Map.entry("dustjack", "husk"),
            Map.entry("shardmole", "silverfish"),
            Map.entry("hushling", "endermite"),
            Map.entry("brass_howler", "ravager"),
            Map.entry("mooncalf", "allay"),
            Map.entry("inkfin", "guardian"),
            Map.entry("rootbound_knight", "iron_golem"),
            Map.entry("clockface_crawler", "spider")
    );

    private EnemyPrototypeService() {}

    public record SpawnResult(boolean success, String message, Entity entity) {}

    public static SpawnResult spawn(ServerLevel level, BlockPos position, String enemyId) {
        if (level == null || position == null || enemyId == null) {
            return new SpawnResult(false, "Missing level, position, or enemy id.", null);
        }

        Optional<EnemyCatalog.Enemy> definition = EnemyCatalog.byId(enemyId);
        if (definition.isEmpty()) {
            return new SpawnResult(false, "Unknown enemy id: " + enemyId, null);
        }

        String baseTypeId = BASE_TYPES.get(enemyId);
        if (baseTypeId == null) {
            return new SpawnResult(false, "No prototype mapping for: " + enemyId, null);
        }

        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getOptional(
                Identifier.parse("minecraft:" + baseTypeId)
        ).orElse(null);
        if (type == null) {
            return new SpawnResult(false, "Missing vanilla prototype base type: " + baseTypeId, null);
        }

        Entity entity = type.spawn(level, position, EntitySpawnReason.COMMAND);
        if (entity == null) {
            return new SpawnResult(false, "Minecraft could not spawn the prototype at that position.", null);
        }

        entity.setCustomName(Component.literal(definition.get().name() + " (Prototype)"));
        entity.setCustomNameVisible(true);
        return new SpawnResult(true, definition.get().name(), entity);
    }

    public static int prototypeCount() {
        return BASE_TYPES.size();
    }
}
