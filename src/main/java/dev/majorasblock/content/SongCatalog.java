package dev.majorasblock.content;

import java.util.List;
import java.util.Optional;

/** Song metadata. Sound files and instrument-note input are client-side assets/systems. */
public final class SongCatalog {
    public enum Effect {
        TELEPORT_TO_FRIEND, SLOW_DAY_CLOCK, FAST_FORWARD_TO_CHOSEN_TIME,
        ADVANCE_TO_NEXT_DAY, RETURN_TO_HUMAN_FORM, TRAVEL_TO_KNOWN_POINT,
        CHANGE_WEATHER, RESTORE, SUMMON_MOUNT, REVEAL_HIDDEN_THINGS,
        CALM_HOSTILE_CREATURES, OPEN_TEMPORARY_PASSAGE
    }

    public record Song(String id, String name, Effect effect, String description,
                       boolean requiresServerValidation) {}

    private static final List<Song> SONGS = List.of(
            new Song("song_of_friendship", "Song of Friendship", Effect.TELEPORT_TO_FRIEND, "Request an arrival beside a consenting online friend", true),
            new Song("song_of_inverted_time", "Song of Inverted Time", Effect.SLOW_DAY_CLOCK, "Slows only the adventure day clock", true),
            new Song("song_of_chosen_hour", "Song of the Chosen Hour", Effect.FAST_FORWARD_TO_CHOSEN_TIME, "Advances the clock to the next chosen hour", true),
            new Song("song_of_double_time", "Song of Double Time", Effect.ADVANCE_TO_NEXT_DAY, "Advances to the next morning", true),
            new Song("song_of_healing", "Song of Healing", Effect.RETURN_TO_HUMAN_FORM, "Restores the player's original form", true),
            new Song("song_of_soaring", "Song of Soaring", Effect.TRAVEL_TO_KNOWN_POINT, "Opens travel to a discovered point", true),
            new Song("song_of_storms", "Song of Storms", Effect.CHANGE_WEATHER, "Changes local weather where permitted", true),
            new Song("song_of_restoration", "Song of Restoration", Effect.RESTORE, "Restores a limited amount of adventure energy", true),
            new Song("eponas_song", "Epona's Song", Effect.SUMMON_MOUNT, "Calls the player's registered mount", true),
            new Song("song_of_truth", "Song of Truth", Effect.REVEAL_HIDDEN_THINGS, "Reveals nearby hidden clues for a short time", true),
            new Song("song_of_calm", "Song of Calm", Effect.CALM_HOSTILE_CREATURES, "Briefly calms eligible creatures", true),
            new Song("song_of_opening", "Song of Opening", Effect.OPEN_TEMPORARY_PASSAGE, "Activates marked song locks", true)
    );

    private SongCatalog() {}

    public static List<Song> all() {
        return SONGS;
    }

    public static Optional<Song> byId(String id) {
        return SONGS.stream().filter(song -> song.id().equals(id)).findFirst();
    }
}
