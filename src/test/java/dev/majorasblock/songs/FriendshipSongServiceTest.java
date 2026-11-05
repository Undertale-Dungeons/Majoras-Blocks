package dev.majorasblock.songs;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipSongServiceTest {
    @Test
    void requiresMutualFriendshipAndRecipientConsent() {
        UUID travellerId = UUID.randomUUID();
        UUID friendId = UUID.randomUUID();
        var traveller = new FriendshipSongService.PlayerPresence(
                travellerId, "minecraft:overworld", 0, 64, 0, Set.of(friendId), true);
        var friend = new FriendshipSongService.PlayerPresence(
                friendId, "minecraft:overworld", 20, 70, 30, Set.of(travellerId), true);

        var plan = FriendshipSongService.plan(
                travellerId, friendId, Map.of(travellerId, traveller, friendId, friend));
        assertTrue(plan.isPresent());
        assertEquals(friendId, plan.get().friendId());
        assertEquals(20, plan.get().x());
    }

    @Test
    void rejectsMissingConsentOrOneWayFriendship() {
        UUID travellerId = UUID.randomUUID();
        UUID friendId = UUID.randomUUID();
        var traveller = new FriendshipSongService.PlayerPresence(
                travellerId, "minecraft:overworld", 0, 64, 0, Set.of(friendId), true);
        var noConsent = new FriendshipSongService.PlayerPresence(
                friendId, "minecraft:overworld", 20, 70, 30, Set.of(travellerId), false);
        assertTrue(FriendshipSongService.plan(travellerId, friendId,
                Map.of(travellerId, traveller, friendId, noConsent)).isEmpty());

        var oneWay = new FriendshipSongService.PlayerPresence(
                friendId, "minecraft:overworld", 20, 70, 30, Set.of(), true);
        assertTrue(FriendshipSongService.plan(travellerId, friendId,
                Map.of(travellerId, traveller, friendId, oneWay)).isEmpty());
    }
}
