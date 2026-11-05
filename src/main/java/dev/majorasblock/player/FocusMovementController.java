package dev.majorasblock.player;

/**
 * Third-person target-focus movement mapping.
 * Offensive actions are intentionally kept outside this movement controller.
 */
public final class FocusMovementController {
    public enum Move {
        NONE,
        BACKFLIP,
        SIDE_DASH_LEFT,
        SIDE_DASH_RIGHT,
        BACKSTEP
    }

    public enum CameraMode {
        FIRST_PERSON,
        THIRD_PERSON_BACK,
        THIRD_PERSON_FRONT
    }

    private FocusMovementController() {}

    public static Move resolveMove(
            boolean focusLocked,
            boolean backwardHeld,
            boolean leftHeld,
            boolean rightHeld,
            boolean jumpPressed
    ) {
        if (!focusLocked) {
            return Move.NONE;
        }
        if (jumpPressed && backwardHeld) {
            return Move.BACKFLIP;
        }
        if (jumpPressed && leftHeld) {
            return Move.SIDE_DASH_LEFT;
        }
        if (jumpPressed && rightHeld) {
            return Move.SIDE_DASH_RIGHT;
        }
        if (backwardHeld) {
            return Move.BACKSTEP;
        }
        return Move.NONE;
    }

    public static CameraMode defaultCameraMode() {
        return CameraMode.THIRD_PERSON_BACK;
    }

    public static CameraMode nextCameraMode(CameraMode current) {
        return switch (current) {
            case FIRST_PERSON -> CameraMode.THIRD_PERSON_BACK;
            case THIRD_PERSON_BACK -> CameraMode.THIRD_PERSON_FRONT;
            case THIRD_PERSON_FRONT -> CameraMode.FIRST_PERSON;
        };
    }
}
