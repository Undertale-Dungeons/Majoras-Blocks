package dev.majorasblock.player;

/** Input-to-action mapping for the target-focused combat control scheme. */
public final class FocusedCombatController {
    public enum Action { NONE, BACKFLIP, SIDE_DASH_LEFT, SIDE_DASH_RIGHT, BACKSTEP, STAB, SLASH, SHIELD_BLOCK, PERFECT_GUARD }

    private FocusedCombatController() {}

    public static Action resolve(
            boolean targetLocked,
            boolean backward,
            boolean left,
            boolean right,
            boolean forward,
            boolean jumpPressed,
            boolean attackPressed,
            boolean shieldHeld,
            boolean shieldPressed
    ) {
        if (shieldHeld) {
            return shieldPressed ? Action.PERFECT_GUARD : Action.SHIELD_BLOCK;
        }
        if (!targetLocked) {
            return attackPressed ? Action.SLASH : Action.NONE;
        }
        if (jumpPressed && backward) return Action.BACKFLIP;
        if (jumpPressed && left) return Action.SIDE_DASH_LEFT;
        if (jumpPressed && right) return Action.SIDE_DASH_RIGHT;
        if (attackPressed && backward) return Action.STAB;
        if (attackPressed) return Action.SLASH;
        if (backward) return Action.BACKSTEP;
        return Action.NONE;
    }
}
