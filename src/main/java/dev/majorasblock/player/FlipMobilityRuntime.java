package dev.majorasblock.player;
import net.minecraft.server.level.ServerPlayer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
public final class FlipMobilityRuntime {
    private static final long COOLDOWN_TICKS = 14L;
    private static final Map<UUID, Long> LAST_FLIP_TICK = new ConcurrentHashMap<>();
    private FlipMobilityRuntime() {}
    public static boolean perform(ServerPlayer player, FlipMovementController.Direction direction) {
        if (player == null || direction == null || player.isPassenger()) return false;
        long now = player.level().getGameTime();
        Long previous = LAST_FLIP_TICK.get(player.getUUID());
        if (previous != null && now - previous < COOLDOWN_TICKS) return false;
        FlipMovementController.Vector vector = FlipMovementController.direction(direction, player.getYRot());
        double strength = switch (direction) {
            case FRONT -> 0.92;
            case BACK -> 0.82;
            case LEFT, RIGHT -> 0.86;
        };
        LAST_FLIP_TICK.put(player.getUUID(), now);
        player.push(vector.x() * strength, 0.42, vector.z() * strength);
        return true;
    }
}
