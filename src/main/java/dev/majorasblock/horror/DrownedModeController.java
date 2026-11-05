package dev.majorasblock.horror;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Optional horror atmosphere. All effects are transient presentation events;
 * this type has no filesystem access and can never create or alter saves.
 */
public final class DrownedModeController {
    public enum Intensity { OFF, SUBTLE, UNEASY, FULL }
    public enum Effect {
        DISTANT_FOOTSTEPS, AMBIENT_WHISPER, REFLECTION_DELAY, FARAWAY_SILHOUETTE,
        CLOCK_FACE_FLICKER, NON_INTERACTIVE_TEXT_GLITCH, UNFAMILIAR_DUAL_FOOTSTEPS,
        BRIEF_ROOM_TONE_DROP, UNATTENDED_LANTERN, DISTANT_PLAYER_SHAPE,
        FALSE_DOOR_SHADOW, SINGLE_FRAME_SKY_FLICKER, NAMELESS_CHAT_ECHO,
        QUIET_WATER_RIPPLE, MUSIC_NOTE_DETUNE
    }

    private static final List<Effect> EFFECTS = List.of(Effect.values());
    private Intensity intensity = Intensity.OFF;
    private long lastEffectTick = Long.MIN_VALUE;

    public synchronized void setIntensity(Intensity intensity) {
        this.intensity = intensity == null ? Intensity.OFF : intensity;
        if (this.intensity == Intensity.OFF) {
            lastEffectTick = Long.MIN_VALUE;
        }
    }

    public synchronized Intensity intensity() { return intensity; }
    public synchronized boolean isEnabled() { return intensity != Intensity.OFF; }
    public synchronized boolean mayModifySaveFiles() { return false; }
    public synchronized boolean mayCreateAlternateWorlds() { return false; }
    public synchronized boolean mayDeleteOrRenameWorlds() { return false; }
    public synchronized boolean mayImpersonateOnlinePlayers() { return false; }

    /** Returns a rare atmosphere cue; rendering/audio is handled by a client effect layer. */
    public synchronized Effect pollEffect(long worldTick) {
        if (!isEnabled()) return null;
        long interval = switch (intensity) {
            case OFF -> Long.MAX_VALUE;
            case SUBTLE -> 7_200L;
            case UNEASY -> 3_600L;
            case FULL -> 1_800L;
        };
        if (lastEffectTick != Long.MIN_VALUE && worldTick - lastEffectTick < interval) {
            return null;
        }
        lastEffectTick = worldTick;
        int allowed = intensity == Intensity.SUBTLE ? 5
                : intensity == Intensity.UNEASY ? 10 : EFFECTS.size();
        return EFFECTS.get(ThreadLocalRandom.current().nextInt(allowed));
    }
}
