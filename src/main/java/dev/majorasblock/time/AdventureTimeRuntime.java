package dev.majorasblock.time;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

/** Connects the time-song clock to the shared server world clock. */
public final class AdventureTimeRuntime {
    private static final TimeSongController CLOCK = new TimeSongController();
    private static boolean registered;

    private AdventureTimeRuntime() {}

    public static synchronized void register() {
        if (registered) {
            return;
        }
        registered = true;

        ServerTickEvents.END_SERVER_TICK.register(AdventureTimeRuntime::onServerTick);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> CLOCK.restoreNormalTime());
    }

    private static void onServerTick(MinecraftServer server) {
        // Vanilla advances day time by one tick each server tick. Correct only
        // the day clock so the rest of Minecraft continues at normal speed.
        long intendedAdvance = CLOCK.scaledDayTicks(1);
        long correction = intendedAdvance - 1L;
        if (correction != 0L) {
            for (ServerLevel level : server.getAllLevels()) {
                level.setDayTime(level.getDayTime() + correction);
            }
        }
    }

    public static void activateInvertedTime() {
        CLOCK.activateInvertedTime();
    }

    public static void restoreNormalTime() {
        CLOCK.restoreNormalTime();
    }

    public static double dayClockScale() {
        return CLOCK.dayClockScale();
    }

    public static long fastForwardToChosenHour(MinecraftServer server, int hour, int minute) {
        ServerLevel overworld = server.overworld();
        long current = overworld.getDayTime();
        long target = TimeSongController.fastForwardTo(current, hour, minute);
        long delta = target - current;

        // Keep dimensions aligned by applying the same jump to each dimension.
        for (ServerLevel level : server.getAllLevels()) {
            level.setDayTime(level.getDayTime() + delta);
        }
        return target;
    }
}
