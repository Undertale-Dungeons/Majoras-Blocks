package dev.majorasblock.deity;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeityProgressionTest {
    @Test
    void spiritsCanOnlyBeCollectedOnce() {
        DeityProgression progress = new DeityProgression();
        assertTrue(progress.collect("spirit_of_motion").isPresent());
        assertTrue(progress.hasSpirit("spirit_of_motion"));
        assertTrue(progress.collect("spirit_of_motion").isEmpty());
        assertTrue(progress.collect("not_a_spirit").isEmpty());
    }

    @Test
    void snapshotsIgnoreUnknownSpiritIds() {
        DeityProgression progress = new DeityProgression();
        progress.loadSnapshot(Set.of("spirit_of_motion", "not_a_spirit"));
        assertEquals(Set.of("spirit_of_motion"), progress.snapshot());
    }
}
