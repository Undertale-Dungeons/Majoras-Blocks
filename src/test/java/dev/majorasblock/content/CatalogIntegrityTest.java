package dev.majorasblock.content;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CatalogIntegrityTest {
    @Test
    void allCrossCatalogueReferencesAreValid() {
        assertDoesNotThrow(CatalogIntegrity::validate);
    }

    @Test
    void originalBossCountIsExactlyFive() {
        assertEquals(5, BossCatalog.original().size());
    }
}
