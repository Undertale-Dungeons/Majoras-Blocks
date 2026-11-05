package dev.majorasblock.songs;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Validates a Song of Friendship arrival request.
 *
 * This pure planning layer does not teleport players. A server integration must
 * verify a collision-free destination and execute the teleport.
 */
public final class FriendshipSongService {
    public record PlayerPresence(
            UUID playerId,
            String dimensionId,
            double x,
            double y,
            double z,
            Set<UUID> friends,
            boolean acceptsFriendshipArrivals
    ) {
        public PlayerPresence {
            friends = friends == null ? Set.of() : Set.copyOf(friends);
        }
    }

    public record ArrivalPlan(UUID travellerId, UUID friendId,
                              String dimensionId, double x, double y, double z) {}

    private FriendshipSongService() {}

    public static Optional<ArrivalPlan> plan(
            UUID travellerId,
            UUID friendId,
            Map<UUID, PlayerPresence> onlinePlayers
    ) {
        if (travellerId == null || friendId == null || onlinePlayers == null
                || travellerId.equals(friendId)) {
            return Optional.empty();
        }

        PlayerPresence traveller = onlinePlayers.get(travellerId);
        PlayerPresence friend = onlinePlayers.get(friendId);
        if (traveller == null || friend == null || !friend.acceptsFriendshipArrivals()) {
            return Optional.empty();
        }

        if (!traveller.friends().contains(friendId) || !friend.friends().contains(travellerId)) {
            return Optional.empty();
        }

        if (friend.dimensionId() == null || friend.dimensionId().isBlank()
                || !Double.isFinite(friend.x()) || !Double.isFinite(friend.y())
                || !Double.isFinite(friend.z())) {
            return Optional.empty();
        }

        return Optional.of(new ArrivalPlan(
                travellerId, friendId, friend.dimensionId(),
                friend.x(), friend.y(), friend.z()
        ));
    }
}
