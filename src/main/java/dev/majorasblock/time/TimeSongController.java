package dev.majorasblock.time;

/**
 * Adventure-clock math for the time songs.
 *
 * The scale applies only to the adventure day clock. It must not be applied to
 * Minecraft's server tick rate, entity AI, redstone, or player movement.
 */
public final class TimeSongController {
    public static final long TICKS_PER_DAY = ThreeDayCycle.TICKS_PER_DAY;

    private double dayClockScale = 1.0;
    private double fractionalTickCarry;

    public synchronized void activateInvertedTime() {
        dayClockScale = 0.5;
    }

    public synchronized void restoreNormalTime() {
        dayClockScale = 1.0;
        fractionalTickCarry = 0.0;
    }

    public synchronized double dayClockScale() {
        return dayClockScale;
    }

    /**
     * Returns how many adventure-clock ticks should pass during the supplied
     * number of server ticks. A 0.5 scale means two server ticks advance one
     * adventure-clock tick.
     */
    public synchronized long scaledDayTicks(long serverTicks) {
        if (serverTicks < 0) {
            throw new IllegalArgumentException("serverTicks must not be negative");
        }
        double exactTicks = serverTicks * dayClockScale + fractionalTickCarry;
        long wholeTicks = (long) Math.floor(exactTicks);
        fractionalTickCarry = exactTicks - wholeTicks;
        return wholeTicks;
    }

    /**
     * Finds the next occurrence of a chosen 24-hour clock time.
     * Minecraft time 0 is treated as 06:00, matching the vanilla day cycle.
     */
    public static long fastForwardTo(long currentWorldTime, int hour, int minute) {
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Choose a valid 24-hour time");
        }

        long currentDayStart = Math.floorDiv(currentWorldTime, TICKS_PER_DAY) * TICKS_PER_DAY;
        long currentDayTick = Math.floorMod(currentWorldTime, TICKS_PER_DAY);
        long targetDayTick = Math.floorMod((hour - 6L) * 1_000L + minute * (1_000L / 60L), TICKS_PER_DAY);
        long target = currentDayStart + targetDayTick;

        if (target <= currentWorldTime || targetDayTick <= currentDayTick) {
            target += TICKS_PER_DAY;
        }
        return target;
    }
}
