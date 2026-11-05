package dev.majorasblock.horror;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrownedModeControllerTest {
    @Test
    void disabledModeHasNoCueAndCannotTouchSaveFiles() {
        DrownedModeController controller = new DrownedModeController();
        assertNull(controller.pollEffect(1));
        assertFalse(controller.mayModifySaveFiles());
        assertFalse(controller.mayCreateAlternateWorlds());
        assertFalse(controller.mayDeleteOrRenameWorlds());
    }

    @Test
    void enablingModeProducesOnlyKnownEffects() {
        DrownedModeController controller = new DrownedModeController();
        controller.setIntensity(DrownedModeController.Intensity.FULL);
        assertNotNull(controller.pollEffect(1));
    }
}
