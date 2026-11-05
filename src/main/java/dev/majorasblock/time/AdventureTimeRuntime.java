package dev.majorasblock.time;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.clock.WorldClock;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Applies time songs to Minecraft 26.2's server-owned world clocks.
 * The game simulation and entity ticks are never slowed.
 */
public final class AdventureTimeRuntime {
    private static final float NORMAL_RATE = 1.0f;
    private static final float INVERTED_RATE = 0.5f;

    private AdventureTimeRuntime() {}

    /** Ensure a saved slow-time rate cannot leak into a fresh server session. */
    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(AdventureTimeRuntime::restoreNormalTime);
        ServerLifecycleEvents.SERVER_STOPPING.register(AdventureTimeRuntime::restoreNormalTime);
    }

    public static void activateInvertedTime(MinecraftServer server) {
        forEachDefaultClock(server, clock -> server.clockManager().setRate(clock, INVERTED_RATE));
        DayClockMode.inverted = true;
    }

    public static void restoreNormalTime(MinecraftServer server) {
        forEachDefaultClock(server, clock -> server.clockManager().setRate(clock, NORMAL_RATE));
        DayClockMode.inverted = false;
    }

    public static double dayClockScale(MinecraftServer server) {
        Optional<Holder<WorldClock>> defaultClock = server.overworld().dimensionType().defaultClock();
        return defaultClock
                .map(clock -> (double) server.clockManager().getTotalTicks(clock))
                .map(ignored -> currentRate(server))
                .orElse(NORMAL_RATE);
    }

    private static float currentRate(MinecraftServer server) {
        // Rate is kept by the clock manager. Track the selected song mode here
        // so the status command can report it without changing clock state.
        return DayClockMode.isInverted() ? INVERTED_RATE : NORMAL_RATE;
    }

    public static long fastForwardToChosenHour(MinecraftServer server, int hour, int minute) {
        ServerLevel overworld = server.overworld();
        Holder<WorldClock> clock = overworld.dimensionType().defaultClock()
                .orElseThrow(() -> new IllegalStateException("The Overworld has no default clock"));
        long current = server.clockManager().getTotalTicks(clock);
        long target = TimeSongController.fastForwardTo(current, hour, minute);
        server.clockManager().setTotalTicks(clock, target);
        return target;
    }

    private static void forEachDefaultClock(MinecraftServer server,
                                            java.util.function.Consumer<Holder<WorldClock>> action) {
        Set<Holder<WorldClock>> clocks = new HashSet<>();
        for (ServerLevel level : server.getAllLevels()) {
            level.dimensionType().defaultClock().ifPresent(clocks::add);
        }
        clocks.forEach(action);
    }

    private static final class DayClockMode {
        private static volatile boolean inverted;

        private static boolean isInverted() {
            return inverted;
        }
    }
}
