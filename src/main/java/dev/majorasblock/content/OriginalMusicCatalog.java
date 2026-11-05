package dev.majorasblock.content;

import java.util.List;

/** Original note sequence data; the client sequencer will synthesize playback. */
public final class OriginalMusicCatalog {
    public record Note(int midiPitch, double beats) {}
    public record Theme(String id, String title, int tempoBpm, String instrument, List<Note> notes) {
        public Theme {
            notes = List.copyOf(notes);
        }
    }

    private static final Theme FRIENDSHIP = new Theme(
            "song_of_friendship", "Song of Friendship", 112, "celesta",
            List.of(
                    new Note(72, .5), new Note(76, .5), new Note(79, .75), new Note(76, .5),
                    new Note(74, .5), new Note(77, .75), new Note(79, .5), new Note(84, 1),
                    new Note(79, .5), new Note(77, .5), new Note(76, .75), new Note(74, .5),
                    new Note(72, 1), new Note(76, .5), new Note(79, .5), new Note(84, 1.25)
            )
    );

    private OriginalMusicCatalog() {}

    public static Theme friendship() {
        return FRIENDSHIP;
    }

    public static List<Theme> all() {
        return List.of(FRIENDSHIP);
    }
}
