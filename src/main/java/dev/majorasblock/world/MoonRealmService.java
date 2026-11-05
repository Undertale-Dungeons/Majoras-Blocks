package dev.majorasblock.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Builds a safe lunar exploration zone above the End's central island.
 * Return positions are kept for the current multiplayer server session.
 */
public final class MoonRealmService {
    private static final int SURFACE_Y = 128;
    private static final BlockPos CENTER = new BlockPos(0, SURFACE_Y, 0);
    private static final Map<UUID, ReturnPoint> RETURN_POINTS = new ConcurrentHashMap<>();

    private MoonRealmService() {}

    public static boolean travelToMoon(MinecraftServer server, ServerPlayer player) {
        if (server == null || player == null) return false;
        ServerLevel moon = server.getLevel(Level.END);
        if (moon == null) return false;

        buildMoonSurface(moon);
        RETURN_POINTS.put(player.getUUID(), new ReturnPoint(
                player.level().dimension(),
                player.getX(), player.getY(), player.getZ(),
                player.getYRot(), player.getXRot()
        ));

        return player.teleportTo(moon, 0.5, SURFACE_Y + 1.0, 0.5,
                Set.of(), 0.0f, 0.0f, true);
    }

    public static boolean returnFromMoon(MinecraftServer server, ServerPlayer player) {
        if (server == null || player == null) return false;
        ReturnPoint saved = RETURN_POINTS.get(player.getUUID());
        ServerLevel destination = saved == null ? server.overworld() : server.getLevel(saved.dimension());
        if (destination == null) destination = server.overworld();

        double x;
        double y;
        double z;
        float yaw;
        float pitch;
        if (saved == null || destination == server.overworld() && saved.dimension() != Level.OVERWORLD) {
            BlockPos spawn = server.overworld().getSharedSpawnPos();
            BlockPos safe = findSafeLanding(server.overworld(), spawn);
            if (safe == null) return false;
            x = safe.getX() + 0.5;
            y = safe.getY();
            z = safe.getZ() + 0.5;
            yaw = player.getYRot();
            pitch = 0.0f;
            destination = server.overworld();
        } else {
            BlockPos savedPos = BlockPos.containing(saved.x(), saved.y(), saved.z());
            BlockPos safe = findSafeLanding(destination, savedPos);
            if (safe == null) {
                BlockPos spawn = server.overworld().getSharedSpawnPos();
                safe = findSafeLanding(server.overworld(), spawn);
                if (safe == null) return false;
                destination = server.overworld();
                x = safe.getX() + 0.5;
                y = safe.getY();
                z = safe.getZ() + 0.5;
                yaw = player.getYRot();
                pitch = 0.0f;
            } else {
                x = saved.x();
                y = safe.getY();
                z = saved.z();
                yaw = saved.yaw();
                pitch = saved.pitch();
            }
        }

        boolean moved = player.teleportTo(destination, x, y, z, Set.of(), yaw, pitch, true);
        if (moved) RETURN_POINTS.remove(player.getUUID());
        return moved;
    }

    private static void buildMoonSurface(ServerLevel level) {
        for (int dx = -24; dx <= 24; dx++) {
            for (int dz = -24; dz <= 24; dz++) {
                int distanceSquared = dx * dx + dz * dz;
                if (distanceSquared > 24 * 24) continue;
                Block top = distanceSquared > 19 * 19 ? Blocks.CALCITE
                        : distanceSquared > 13 * 13 ? Blocks.END_STONE_BRICKS : Blocks.END_STONE;
                placeIfAir(level, CENTER.offset(dx, 0, dz), top);
                placeIfAir(level, CENTER.offset(dx, -1, dz), Blocks.END_STONE);
                if (distanceSquared < 18 * 18) {
                    placeIfAir(level, CENTER.offset(dx, -2, dz), Blocks.END_STONE);
                }
            }
        }

        // Meteor crater rings.
        ring(level, CENTER.offset(10, 1, 1), 6, Blocks.TUFF);
        ring(level, CENTER.offset(-10, 1, 4), 4, Blocks.CALCITE);
        ring(level, CENTER.offset(4, 1, -11), 5, Blocks.AMETHYST_BLOCK);

        // Four star pillars.
        pillar(level, CENTER.offset(-18, 1, -14), 9, Blocks.PURPUR_PILLAR, Blocks.END_ROD);
        pillar(level, CENTER.offset(18, 1, -14), 12, Blocks.PURPUR_PILLAR, Blocks.END_ROD);
        pillar(level, CENTER.offset(-18, 1, 14), 7, Blocks.PURPUR_PILLAR, Blocks.END_ROD);
        pillar(level, CENTER.offset(18, 1, 14), 10, Blocks.PURPUR_PILLAR, Blocks.END_ROD);

        // A moon gate to frame the approach.
        for (int y = 1; y <= 10; y++) {
            placeIfAir(level, CENTER.offset(-5, y, -19), Blocks.PURPUR_BLOCK);
            placeIfAir(level, CENTER.offset(5, y, -19), Blocks.PURPUR_BLOCK);
        }
        for (int x = -5; x <= 5; x++) {
            placeIfAir(level, CENTER.offset(x, 10, -19), Blocks.AMETHYST_BLOCK);
        }

        // Small central observatory plinth with a clear landing tile.
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                if (Math.abs(dx) == 3 || Math.abs(dz) == 3) {
                    placeIfAir(level, CENTER.offset(dx, 1, dz), Blocks.POLISHED_BLACKSTONE_BRICKS);
                }
            }
        }
        placeIfAir(level, CENTER.above(), Blocks.SEA_LANTERN);
    }

    private static void ring(ServerLevel level, BlockPos center, int radius, Block block) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int d = dx * dx + dz * dz;
                if (d >= (radius - 1) * (radius - 1) && d <= radius * radius) {
                    placeIfAir(level, center.offset(dx, 0, dz), block);
                }
            }
        }
    }

    private static void pillar(ServerLevel level, BlockPos base, int height, Block wall, Block light) {
        for (int y = 0; y < height; y++) {
            placeIfAir(level, base.above(y), wall);
        }
        placeIfAir(level, base.above(height), light);
    }

    private static void placeIfAir(ServerLevel level, BlockPos pos, Block block) {
        if (level.isEmptyBlock(pos)) {
            level.setBlockAndUpdate(pos, block.defaultBlockState());
        }
    }

    private static BlockPos findSafeLanding(ServerLevel level, BlockPos origin) {
        int[][] offsets = {{0,0},{1,0},{-1,0},{0,1},{0,-1},{2,0},{-2,0},{0,2},{0,-2}};
        for (int dy = 0; dy <= 5; dy++) {
            for (int[] offset : offsets) {
                BlockPos feet = origin.offset(offset[0], dy, offset[1]);
                if (level.isEmptyBlock(feet) && level.isEmptyBlock(feet.above())
                        && !level.isEmptyBlock(feet.below())
                        && level.getFluidState(feet).isEmpty()
                        && level.getFluidState(feet.above()).isEmpty()) {
                    return feet;
                }
            }
        }
        return null;
    }

    private record ReturnPoint(net.minecraft.resources.ResourceKey<Level> dimension,
                               double x, double y, double z, float yaw, float pitch) {}
}
