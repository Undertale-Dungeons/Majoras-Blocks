package dev.majorasblock.content;

import java.util.List;
import java.util.Optional;

/** Song metadata. Actual note input and sound assets are added by the client layer. */
public final class SongCatalog {
    public enum Effect {
        TELEPORT_TO_FRIEND,
        SLOW_DAY_CLOCK,
        FAST_FORWARD_TO_CHOSEN_TIME,
        ADVANCE_TO_NEXT_DAY,
        RETURN_TO_HUMAN_FORM,
        TRAVEL_TO_KNOWN_POINT,
        CHANGE_WEATHER,
        RESTORE
    }

    public record Song(String id, String name, Effect effect, String description) {}

    private static final List<Song> SONGS = List.of(
            new Song("song_of_friendship", "Song of Friendship", Effect.TELEPORT_TO_FRIEND,
                    "Request a safe arrival beside an online friend who allows it"),
            new Song("song_of_inverted_time", "Song of Inverted Time", Effect.SLOW_DAY_CLOCK,
                    "Slows the adventure day clock without slowing entities or game simulation"),
            new Song("song_of_chosen_hour", "Song of the Chosen Hour", Effect.FAST_FORWARD_TO_CHOSEN_TIME,
                    "Moves the adventure clock forward to a time selected by the player"),
            new Song("song_of_double_time", "Song of Double Time", Effect.ADVANCE_TO_NEXT_DAY,
                    "Advances the adventure clock to the next morning"),
            new Song("song_of_healing", "Song of Healing", Effect.RETURN_TO_HUMAN_FORM,
                    "Restores the player's original form"),
            new Song("song_of_soaring", "Song of Soaring", Effect.TRAVEL_TO_KNOWN_POINT,
                    "Opens travel to a previously discovered point"),
            new Song("song_of_storms", "Song of Storms", Effect.CHANGE_WEATHER,
                    "Changes local weather where the world rules allow it"),
            new Song("song_of_restoration", "Song of Restoration", Effect.RESTORE,
                    "Restores a limited amount of adventure energy")
    );

    private SongCatalog() {}

    public static List<Song> all() {
        return SONGS;
    }

    public static Optional<Song> byId(String id) {
        return SONGS.stream().filter(song -> song.id().equals(id)).findFirst();
    }
}
