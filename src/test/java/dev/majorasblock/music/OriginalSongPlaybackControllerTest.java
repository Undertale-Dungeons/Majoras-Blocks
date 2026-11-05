package dev.majorasblock.music;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OriginalSongPlaybackControllerTest {
    @Test
    void convertsBeatDurationsToTwentyTickPerSecondIntervals() {
        assertEquals(10, OriginalSongPlaybackController.ticksForNote(1.0, 120));
        assertEquals(5, OriginalSongPlaybackController.ticksForNote(0.5, 120));
        assertEquals(11, OriginalSongPlaybackController.ticksForNote(1.0, 112));
    }

    @Test
    void rejectsInvalidNoteDurations() {
        assertThrows(IllegalArgumentException.class,
                () -> OriginalSongPlaybackController.ticksForNote(0, 120));
        assertThrows(IllegalArgumentException.class,
                () -> OriginalSongPlaybackController.ticksForNote(1, 0));
        assertThrows(IllegalArgumentException.class,
                () -> OriginalSongPlaybackController.ticksForNote(Double.NaN, 120));
    }
}
