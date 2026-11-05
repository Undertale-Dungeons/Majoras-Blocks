package dev.majorasblock.songs;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipArrivalRuntimeTest {
    @Test
    void consentDefaultsOffAndCanBeRevoked() {
        UUID friend = UUID.randomUUID();
        UUID traveller = UUID.randomUUID();

        FriendshipArrivalRuntime.setConsent(friend, false);
        assertFalse(FriendshipArrivalRuntime.accepts(friend));
        assertFalse(FriendshipArrivalRuntime.canArrive(traveller, friend));

        FriendshipArrivalRuntime.setConsent(friend, true);
        assertTrue(FriendshipArrivalRuntime.accepts(friend));
        assertTrue(FriendshipArrivalRuntime.canArrive(traveller, friend));

        FriendshipArrivalRuntime.setConsent(friend, false);
        assertFalse(FriendshipArrivalRuntime.canArrive(traveller, friend));
    }

    @Test
    void rejectsNullAndSelfArrivals() {
        UUID player = UUID.randomUUID();
        FriendshipArrivalRuntime.setConsent(player, true);

        assertFalse(FriendshipArrivalRuntime.canArrive(player, player));
        assertFalse(FriendshipArrivalRuntime.canArrive(null, player));
        assertFalse(FriendshipArrivalRuntime.canArrive(player, null));
    }
}
