package dev.majorasblock.time;

/**
 * Pure time-cycle model for the adventure's three-day structure.
 * A Minecraft day is 24,000 game ticks; day numbers are zero-based internally.
 */
public final class ThreeDayCycle {
    public static final long TICKS_PER_DAY = 24_000L;
    public static final long TICKS_PER_CYCLE = TICKS_PER_DAY * 3L;

    private ThreeDayCycle() {}

    public static int dayIndex(long worldTime) {
        return (int) Math.floorMod(Math.floorDiv(worldTime, TICKS_PER_DAY), 3L);
    }

    public static int dayNumber(long worldTime) {
        return dayIndex(worldTime) + 1;
    }

    public static long tickOfDay(long worldTime) {
        return Math.floorMod(worldTime, TICKS_PER_DAY);
    }

    public static long tickOfCycle(long worldTime) {
        return Math.floorMod(worldTime, TICKS_PER_CYCLE);
    }

    public static boolean isFinalDay(long worldTime) {
        return dayIndex(worldTime) == 2;
    }
}
