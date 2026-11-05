package dev.majorasblock.time;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.clock.WorldClock;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Server-wide day announcements and final-day moon warnings. */
public final class ThreeDayCycleRuntime {
    private static final Map<MinecraftServer, Integer> LAST_DAY = new ConcurrentHashMap<>();
    private static final Map<MinecraftServer, Long> ACTIVE_CYCLE = new ConcurrentHashMap<>();
    private static final Map<MinecraftServer, Set<String>> SENT_MARKERS = new ConcurrentHashMap<>();
    private static boolean registered;

    private ThreeDayCycleRuntime() {}

    public static synchronized void register() {
        if (registered) return;
        registered = true;
        ServerTickEvents.END_SERVER_TICK.register(ThreeDayCycleRuntime::tick);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            LAST_DAY.remove(server);
            ACTIVE_CYCLE.remove(server);
            SENT_MARKERS.remove(server);
        });
    }

    private static void tick(MinecraftServer server) {
        Optional<Holder<WorldClock>> clock = server.overworld().dimensionType().defaultClock();
        if (clock.isEmpty()) return;

        long ticks = server.clockManager().getTotalTicks(clock.get());
        int day = ThreeDayCycle.dayNumber(ticks);
        Integer previousDay = LAST_DAY.put(server, day);
        if (previousDay == null || previousDay != day) {
            String message = switch (day) {
                case 1 -> "Day 1 of 3 • Three days remain.";
                case 2 -> "Day 2 of 3 • The final day is drawing closer.";
                default -> "Day 3 of 3 • The Moon draws near.";
            };
            announce(server, message);
        }

        long cycle = Math.floorDiv(ticks, ThreeDayCycle.TICKS_PER_CYCLE);
        Long previousCycle = ACTIVE_CYCLE.put(server, cycle);
        if (previousCycle == null || previousCycle != cycle) {
            SENT_MARKERS.put(server, ConcurrentHashMap.newKeySet());
        }

        if (!ThreeDayCycle.isFinalDay(ticks)) return;
        long dayTick = ThreeDayCycle.tickOfDay(ticks);
        if (dayTick >= 12_000L) announceOnce(server, cycle, "midday",
                "The Moon hangs lower over the horizon.");
        if (dayTick >= 18_000L) announceOnce(server, cycle, "dusk",
                "The final night begins. The Moon is almost overhead.");
        if (dayTick >= 22_000L) announceOnce(server, cycle, "late",
                "The last hours of the cycle are close.");
    }

    private static void announceOnce(MinecraftServer server, long cycle, String stage, String message) {
        Set<String> markers = SENT_MARKERS.computeIfAbsent(server, ignored -> ConcurrentHashMap.newKeySet());
        if (markers.add(cycle + ":" + stage)) announce(server, message);
    }

    private static void announce(MinecraftServer server, String message) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendOverlayMessage(Component.literal(message));
        }
    }
}
