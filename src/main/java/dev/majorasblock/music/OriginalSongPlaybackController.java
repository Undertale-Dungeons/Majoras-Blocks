package dev.majorasblock.music;

import dev.majorasblock.content.OriginalMusicCatalog;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/** Server-authoritative playback of original note sequences through vanilla instruments. */
public final class OriginalSongPlaybackController {
    private static final Map<UUID, Session> ACTIVE = new HashMap<>();
    private static boolean registered;

    private OriginalSongPlaybackController() {}

    public static synchronized void register() {
        if (registered) return;
        registered = true;
        ServerTickEvents.END_SERVER_TICK.register(OriginalSongPlaybackController::tick);
    }

    public static synchronized void playFriendship(ServerPlayer player) {
        playTheme(player, "song_of_friendship");
    }

    public static synchronized boolean playTheme(ServerPlayer player, String themeId) {
        var theme = OriginalMusicCatalog.byId(themeId);
        if (theme.isEmpty()) {
            return false;
        }
        ACTIVE.put(player.getUUID(), new Session(theme.get()));
        return true;
    }

    public static synchronized void stop(UUID playerId) {
        ACTIVE.remove(playerId);
    }

    private static synchronized void tick(MinecraftServer server) {
        Iterator<Map.Entry<UUID, Session>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Session> entry = iterator.next();
            ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
            if (player == null) {
                iterator.remove();
                continue;
            }

            Session session = entry.getValue();
            if (session.ticksUntilNextNote > 0) {
                session.ticksUntilNextNote--;
                if (session.ticksUntilNextNote > 0) {
                    continue;
                }
            }

            if (session.noteIndex >= session.theme.notes().size()) {
                iterator.remove();
                continue;
            }

            OriginalMusicCatalog.Note note = session.theme.notes().get(session.noteIndex++);
            float pitch = (float) Math.pow(2.0, (note.midiPitch() - 72.0) / 12.0);
            player.level().playSound(
                    null,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    SoundEvents.NOTE_BLOCK_CHIME,
                    SoundSource.RECORDS,
                    0.65f,
                    Math.max(0.5f, Math.min(2.0f, pitch))
            );

            session.ticksUntilNextNote = ticksForNote(note.beats(), session.theme.tempoBpm());
        }
    }

    static int ticksForNote(double beats, int tempoBpm) {
        if (!Double.isFinite(beats) || beats <= 0 || tempoBpm <= 0) {
            throw new IllegalArgumentException("Beats and tempo must be positive finite values.");
        }
        return Math.max(1, (int) Math.round(beats * 1_200.0 / tempoBpm));
    }

    private static final class Session {
        private final OriginalMusicCatalog.Theme theme;
        private int noteIndex;
        private int ticksUntilNextNote;

        private Session(OriginalMusicCatalog.Theme theme) {
            this.theme = theme;
        }
    }
}
