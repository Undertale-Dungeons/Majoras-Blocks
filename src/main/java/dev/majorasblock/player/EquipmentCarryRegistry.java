package dev.majorasblock.player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** Runtime equipment pose state keyed per player UUID. */
public final class EquipmentCarryRegistry {
    private static final ConcurrentMap<UUID, EquipmentCarryState> STATES = new ConcurrentHashMap<>();

    private EquipmentCarryRegistry() {}

    public static EquipmentCarryState get(UUID playerId) {
        return STATES.computeIfAbsent(playerId, ignored -> new EquipmentCarryState());
    }

    public static void remove(UUID playerId) {
        STATES.remove(playerId);
    }
}
