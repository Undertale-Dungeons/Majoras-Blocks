package dev.majorasblock.horror;

import java.util.List;

/**
 * Optional atmosphere-only horror profile.
 *
 * This system never creates, deletes, renames, corrupts, or rewrites world or
 * player save files. Effects are transient client presentation events only.
 */
public final class DrownedModeController {
    public enum Intensity { OFF, SUBTLE, UNEASY, FULL }
    public enum Effect {
        DISTANT_FOOTSTEPS,
        AMBIENT_WHISPER,
        BRIEF_REFLECTION_DELAY,
        FARAWAY_SILHOUETTE,
        CLOCK_FACE_FLICKER,
        NON_INTERACTIVE_TEXT_GLITCH
    }

    private static final List<Effect> EFFECTS = List.of(
            Effect.DISTANT_FOOTSTEPS,
            Effect.AMBIENT_WHISPER,
            Effect.BRIEF_REFLECTION_DELAY,
            Effect.FARAWAY_SILHOUETTE,
            Effect.CLOCK_FACE_FLICKER,
            Effect.NON_INTERACTIVE_TEXT_GLITCH
    );

    private Intensity intensity = Intensity.OFF;

    public synchronized void setIntensity(Intensity intensity) {
        this.intensity = intensity == null ? Intensity.OFF : intensity;
    }

    public synchronized Intensity intensity() {
        return intensity;
    }

    public synchronized boolean isEnabled() {
        return intensity != Intensity.OFF;
    }

    public synchronized boolean mayModifySaveFiles() {
        return false;
    }

    public synchronized boolean mayCreateAlternateWorlds() {
        return false;
    }

    public synchronized boolean mayDeleteOrRenameWorlds() {
        return false;
    }

    public synchronized Effect chooseAmbientEffect(long worldTime, long worldSeed) {
        if (!isEnabled()) {
            return null;
        }
        int offset = intensity.ordinal() * 7;
        int index = Math.floorMod(worldTime + worldSeed + offset, EFFECTS.size()) > Integer.MAX_VALUE
                ? 0
                : (int) Math.floorMod(worldTime + worldSeed + offset, EFFECTS.size());
        return EFFECTS.get(index);
    }
}
