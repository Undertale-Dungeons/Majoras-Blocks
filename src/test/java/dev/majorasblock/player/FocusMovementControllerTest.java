package dev.majorasblock.player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FocusMovementControllerTest {
    @Test
    void mapsFocusedDefensiveMovement() {
        assertEquals(FocusMovementController.Move.BACKFLIP,
                FocusMovementController.resolveMove(true, true, false, false, true));
        assertEquals(FocusMovementController.Move.SIDE_DASH_LEFT,
                FocusMovementController.resolveMove(true, false, true, false, true));
        assertEquals(FocusMovementController.Move.NONE,
                FocusMovementController.resolveMove(false, true, false, false, true));
    }

    @Test
    void cameraModeCyclesAndHandlesNull() {
        var mode = FocusMovementController.defaultCameraMode();
        assertEquals(FocusMovementController.CameraMode.THIRD_PERSON_FRONT,
                FocusMovementController.nextCameraMode(mode));
        assertEquals(FocusMovementController.CameraMode.FIRST_PERSON,
                FocusMovementController.nextCameraMode(FocusMovementController.CameraMode.THIRD_PERSON_FRONT));
        assertEquals(FocusMovementController.CameraMode.THIRD_PERSON_BACK,
                FocusMovementController.nextCameraMode(null));
    }
}
