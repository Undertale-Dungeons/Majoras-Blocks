package dev.majorasblock.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeSongControllerTest {
    @Test
    void invertedTimeCarriesFractionalTicks() {
        TimeSongController controller = new TimeSongController();
        controller.activateInvertedTime();
        assertEquals(0, controller.scaledDayTicks(1));
        assertEquals(1, controller.scaledDayTicks(1));
        assertEquals(0.5, controller.dayClockScale());
    }

    @Test
    void fastForwardUsesTheNextOccurrence() {
        assertEquals(1_000, TimeSongController.fastForwardTo(1, 7, 0));
        assertEquals(24_000, TimeSongController.fastForwardTo(0, 6, 0));
        assertEquals(48_000, TimeSongController.fastForwardTo(24_000, 6, 0));
    }

    @Test
    void invalidTimesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> TimeSongController.fastForwardTo(0, 24, 0));
        assertThrows(IllegalArgumentException.class, () -> TimeSongController.fastForwardTo(0, 12, 60));
    }
}
