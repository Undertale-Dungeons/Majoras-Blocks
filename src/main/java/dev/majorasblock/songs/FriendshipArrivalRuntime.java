package dev.majorasblock.songs;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Relative;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Consent-based Song of Friendship arrivals.
 *
 * Consent is session-scoped and defaults to off. Teleports only happen after
 * the recipient explicitly enables arrivals and a safe nearby landing spot is
 * found. No player or world save data is rewritten.
 */
public final class FriendshipArrivalRuntime {
    private static final Set<UUID> ACCEPTING_ARRIVALS = ConcurrentHashMap.newKeySet();

    private static final int[][] LANDING_OFFSETS = {
            {2, 0}, {-2, 0}, {0, 2}, {0, -2},
            {2, 2}, {2, -2}, {-2, 2}, {-2, -2},
            {3, 0}, {-3, 0}, {0, 3}, {0, -3}
    };

    private FriendshipArrivalRuntime() {}

    public static void setConsent(UUID playerId, boolean accepts) {
        if (playerId == null) return;
        if (accepts) {
            ACCEPTING_ARRIVALS.add(playerId);
        } else {
            ACCEPTING_ARRIVALS.remove(playerId);
        }
    }

    public static boolean accepts(UUID playerId) {
        return playerId != null && ACCEPTING_ARRIVALS.contains(playerId);
    }

    public static boolean canArrive(UUID travellerId, UUID friendId) {
        return travellerId != null && friendId != null
                && !travellerId.equals(friendId)
                && accepts(friendId);
    }


    /** Finds the closest same-dimension player who explicitly opted in. */
    public static ServerPlayer nearestAcceptingFriend(ServerPlayer traveller, double maxDistance) {
        if (traveller == null || !Double.isFinite(maxDistance) || maxDistance < 0.0) {
            return null;
        }
        double limitSquared = maxDistance * maxDistance;
        ServerLevel travellerLevel = (ServerLevel) traveller.level();
        return travellerLevel.getServer().getPlayerList().getPlayers().stream()
                .filter((ServerPlayer friend) -> friend.level() == travellerLevel)
                .filter(friend -> canArrive(traveller.getUUID(), friend.getUUID()))
                .filter(friend -> distanceSquared(friend, traveller) <= limitSquared)
                .min(java.util.Comparator.comparingDouble(friend -> distanceSquared(friend, traveller)))
                .orElse(null);
    }

    private static double distanceSquared(ServerPlayer first, ServerPlayer second) {
        double dx = first.getX() - second.getX();
        double dy = first.getY() - second.getY();
        double dz = first.getZ() - second.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    public static boolean teleport(ServerPlayer traveller, ServerPlayer friend) {
        if (traveller == null || friend == null
                || !canArrive(traveller.getUUID(), friend.getUUID())) {
            return false;
        }

        ServerLevel destination = (ServerLevel) friend.level();
        BlockPos friendFeet = friend.blockPosition();
        BlockPos safeLanding = findSafeLanding(destination, friendFeet);
        if (safeLanding == null) {
            return false;
        }

        return traveller.teleportTo(
                destination,
                safeLanding.getX() + 0.5,
                safeLanding.getY(),
                safeLanding.getZ() + 0.5,
                Set.of(),
                friend.getYRot(),
                friend.getXRot(),
                true
        );
    }

    private static BlockPos findSafeLanding(ServerLevel level, BlockPos friendFeet) {
        for (int[] offset : LANDING_OFFSETS) {
            for (int dy = 0; dy <= 3; dy++) {
                BlockPos feet = friendFeet.offset(offset[0], dy, offset[1]);
                BlockPos head = feet.above();
                BlockPos floor = feet.below();

                if (level.isEmptyBlock(feet)
                        && level.isEmptyBlock(head)
                        && !level.isEmptyBlock(floor)
                        && level.getFluidState(feet).isEmpty()
                        && level.getFluidState(head).isEmpty()
                        && level.getFluidState(floor).isEmpty()) {
                    return feet;
                }
            }
        }
        return null;
    }
}
