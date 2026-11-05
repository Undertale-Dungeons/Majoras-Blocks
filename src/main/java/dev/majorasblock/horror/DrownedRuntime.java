package dev.majorasblock.horror;

import dev.majorasblock.horror.DrownedModeController.Effect;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-player transient horror cues. This class deliberately has no filesystem
 * access and never creates, reads, changes, renames, or deletes world saves.
 */
public final class DrownedRuntime {
    private static final Map<UUID, DrownedModeController> MODES = new ConcurrentHashMap<>();
    private static boolean registered;

    private DrownedRuntime() {}

    public static synchronized void register() {
        if (registered) return;
        registered = true;
        ServerTickEvents.END_SERVER_TICK.register(DrownedRuntime::tick);
    }

    public static void setIntensity(UUID playerId, DrownedModeController.Intensity intensity) {
        if (playerId == null) return;
        controller(playerId).setIntensity(intensity);
    }

    public static DrownedModeController.Intensity intensity(UUID playerId) {
        if (playerId == null) return DrownedModeController.Intensity.OFF;
        return controller(playerId).intensity();
    }

    public static void clear(UUID playerId) {
        if (playerId != null) MODES.remove(playerId);
    }

    private static DrownedModeController controller(UUID playerId) {
        return MODES.computeIfAbsent(playerId, ignored -> new DrownedModeController());
    }

    private static void tick(MinecraftServer server) {
        long worldTick = server.overworld().getGameTime();
        Set<UUID> onlinePlayers = new HashSet<>();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            UUID playerId = player.getUUID();
            onlinePlayers.add(playerId);
            DrownedModeController mode = MODES.get(playerId);
            if (mode == null || !mode.isEnabled()) continue;
            Effect effect = mode.pollEffect(worldTick);
            if (effect != null) dispatch(player, effect, worldTick);
        }
        MODES.keySet().removeIf(playerId -> !onlinePlayers.contains(playerId));
    }

    private static void dispatch(ServerPlayer player, Effect effect, long tick) {
        switch (effect) {
            case DISTANT_FOOTSTEPS, UNFAMILIAR_DUAL_FOOTSTEPS ->
                    playCue(player, SoundEvents.AMBIENT_CAVE, SoundSource.AMBIENT, 0.28f, 0.72f, tick);
            case AMBIENT_WHISPER, BRIEF_ROOM_TONE_DROP ->
                    playCue(player, SoundEvents.AMBIENT_CAVE, SoundSource.AMBIENT, 0.18f, 0.52f, tick);
            case MUSIC_NOTE_DETUNE ->
                    playCue(player, SoundEvents.NOTE_BLOCK_CHIME, SoundSource.RECORDS, 0.22f, 0.56f, tick);
            case REFLECTION_DELAY, QUIET_WATER_RIPPLE ->
                    player.sendOverlayMessage(Component.literal("The water moved a moment too late."));
            case FARAWAY_SILHOUETTE, DISTANT_PLAYER_SHAPE ->
                    player.sendOverlayMessage(Component.literal("A figure stands at the edge of sight."));
            case CLOCK_FACE_FLICKER ->
                    player.sendOverlayMessage(Component.literal("The clock skips one quiet beat."));
            case NON_INTERACTIVE_TEXT_GLITCH, NAMELESS_CHAT_ECHO ->
                    player.sendOverlayMessage(Component.literal("...  /  ..."));
            case UNATTENDED_LANTERN ->
                    player.sendOverlayMessage(Component.literal("A light is burning with nobody near it."));
            case FALSE_DOOR_SHADOW ->
                    player.sendOverlayMessage(Component.literal("There is a doorway where the wall should be."));
            case SINGLE_FRAME_SKY_FLICKER ->
                    player.sendOverlayMessage(Component.literal("For an instant, the sky looks unfamiliar."));
        }
    }

    private static void playCue(ServerPlayer player, net.minecraft.core.Holder<SoundEvent> sound,
                                SoundSource source, float volume, float pitch, long tick) {
        player.connection.send(new ClientboundSoundPacket(
                sound, source,
                player.getX() + 6.0, player.getY() + 1.0, player.getZ() - 5.0,
                volume, pitch, tick ^ player.getUUID().getLeastSignificantBits()
        ));
    }
}
