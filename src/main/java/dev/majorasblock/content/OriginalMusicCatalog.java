package dev.majorasblock.content;

import java.util.List;
import java.util.Optional;

/** Original note sequences composed for Majora's Block. */
public final class OriginalMusicCatalog {
    public record Note(int midiPitch, double beats) {}
    public record Theme(String id, String title, int tempoBpm, String instrument, List<Note> notes) {
        public Theme {
            notes = List.copyOf(notes);
        }
    }

    private static final List<Theme> THEMES = List.of(
            new Theme("song_of_friendship", "Song of Friendship", 112, "celesta", List.of(
                    new Note(72,.5), new Note(76,.5), new Note(79,.75), new Note(76,.5),
                    new Note(74,.5), new Note(77,.75), new Note(79,.5), new Note(84,1),
                    new Note(79,.5), new Note(77,.5), new Note(76,.75), new Note(74,.5),
                    new Note(72,1), new Note(76,.5), new Note(79,.5), new Note(84,1.25)
            )),
            new Theme("song_of_inverted_time", "Song of Inverted Time", 84, "soft_bell", List.of(
                    new Note(79,.75), new Note(76,.75), new Note(72,1),
                    new Note(79,.75), new Note(76,.75), new Note(72,1),
                    new Note(74,.5), new Note(72,.5), new Note(67,1.5)
            )),
            new Theme("song_of_chosen_hour", "Song of the Chosen Hour", 120, "clock_chime", List.of(
                    new Note(60,.5), new Note(67,.5), new Note(72,.5), new Note(76,.5),
                    new Note(79,1), new Note(76,.5), new Note(72,.5),
                    new Note(67,.5), new Note(72,.5), new Note(79,1.5)
            )),
            new Theme("song_of_double_time", "Song of Double Time", 148, "bright_chime", List.of(
                    new Note(72,.5), new Note(72,.5), new Note(79,.5), new Note(79,.5),
                    new Note(81,.5), new Note(81,.5), new Note(84,1),
                    new Note(79,.5), new Note(76,.5), new Note(72,1)
            )),
            new Theme("song_of_healing", "Song of Healing", 92, "warm_bell", List.of(
                    new Note(69,.75), new Note(72,.5), new Note(76,.75),
                    new Note(74,.5), new Note(72,.75), new Note(69,.75),
                    new Note(67,1), new Note(72,1), new Note(76,1.5)
            ))
    );

    private OriginalMusicCatalog() {}

    public static Theme friendship() {
        return require("song_of_friendship");
    }

    public static List<Theme> all() {
        return THEMES;
    }

    public static Optional<Theme> byId(String id) {
        return THEMES.stream().filter(theme -> theme.id().equals(id)).findFirst();
    }

    public static Theme require(String id) {
        return byId(id).orElseThrow(() -> new IllegalArgumentException("Unknown original theme: " + id));
    }
}
