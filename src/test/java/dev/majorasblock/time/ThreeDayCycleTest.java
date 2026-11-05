package dev.majorasblock.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreeDayCycleTest {
    @Test
    void cyclesAcrossThreeDays() {
        assertEquals(1, ThreeDayCycle.dayNumber(0));
        assertEquals(1, ThreeDayCycle.dayNumber(23_999));
        assertEquals(2, ThreeDayCycle.dayNumber(24_000));
        assertEquals(3, ThreeDayCycle.dayNumber(48_000));
        assertTrue(ThreeDayCycle.isFinalDay(48_000));
        assertEquals(1, ThreeDayCycle.dayNumber(72_000));
        assertEquals(3, ThreeDayCycle.dayNumber(-1));
    }
}
