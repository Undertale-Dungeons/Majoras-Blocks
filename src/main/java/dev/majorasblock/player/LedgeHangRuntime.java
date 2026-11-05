package dev.majorasblock.player;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Simple multiplayer-safe ledge hang.
 *
 * Hold crouch while airborne, descending, and pressing against a wall to catch
 * the edge. Jump while still holding crouch to vault upward and forward.
 * Releasing crouch lets the player drop naturally.
 */
public final class LedgeHangRuntime {
    private static final Set<UUID> HANGING = new HashSet<>();

    private LedgeHangRuntime() {}

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(LedgeHangRuntime::tick);
    }

    private static void tick(MinecraftServer server) {
        Set<UUID> online = new HashSet<>();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            UUID id = player.getUUID();
            online.add(id);

            if (player.isPassenger() || player.isFallFlying() || player.isSwimming()) {
                HANGING.remove(id);
                continue;
            }

            Vec3 velocity = player.getDeltaMovement();
            boolean climbingOut = HANGING.contains(id) && velocity.y > 0.12;

            if (climbingOut) {
                double yaw = Math.toRadians(player.getYRot());
                Vec3 forward = new Vec3(-Math.sin(yaw), 0.0, Math.cos(yaw));
                player.setDeltaMovement(forward.scale(0.28).add(0.0, 0.48, 0.0));
                player.resetFallDistance();
                HANGING.remove(id);
                continue;
            }

            if (HANGING.contains(id)) {
                if (player.isCrouching() && player.horizontalCollision) {
                    player.setDeltaMovement(Vec3.ZERO);
                    player.resetFallDistance();
                } else {
                    HANGING.remove(id);
                }
                continue;
            }

            boolean canCatch = player.isCrouching()
                    && !player.onGround()
                    && player.horizontalCollision
                    && velocity.y <= 0.0
                    && player.level().getBlockState(player.blockPosition().above(2)).isAir();

            if (canCatch) {
                HANGING.add(id);
                player.setDeltaMovement(Vec3.ZERO);
                player.resetFallDistance();
            }
        }

        HANGING.retainAll(online);
    }
}
