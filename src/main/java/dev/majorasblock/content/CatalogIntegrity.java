package dev.majorasblock.content;

import dev.majorasblock.deity.DeitySpiritCatalog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Fast startup validation for content links that would otherwise fail in play. */
public final class CatalogIntegrity {
    private CatalogIntegrity() {}

    public static void validate() {
        requireUnique("dungeon", DungeonCatalog.all().stream().map(DungeonCatalog.Dungeon::id).toList());
        requireUnique("boss", BossCatalog.all().stream().map(BossCatalog.Boss::id).toList());
        requireUnique("enemy", EnemyCatalog.all().stream().map(EnemyCatalog.Enemy::id).toList());
        requireUnique("song", SongCatalog.all().stream().map(SongCatalog.Song::id).toList());
        requireUnique("spirit", DeitySpiritCatalog.all().stream().map(DeitySpiritCatalog.Spirit::id).toList());
        requireUnique("music theme", OriginalMusicCatalog.all().stream().map(OriginalMusicCatalog.Theme::id).toList());

        if (DungeonCatalog.legacy().size() != 4 || DungeonCatalog.original().size() != 5
                || DungeonCatalog.all().size() != 9) {
            throw new IllegalStateException("Expected four returning and five original dungeons.");
        }
        if (BossCatalog.original().size() != 5 || BossCatalog.all().size() != 9) {
            throw new IllegalStateException("Expected four returning and five original bosses.");
        }

        for (DungeonCatalog.Dungeon dungeon : DungeonCatalog.all()) {
            BossCatalog.Boss boss = BossCatalog.byId(dungeon.bossId()).orElse(null);
            if (boss == null || !boss.dungeonId().equals(dungeon.id())) {
                throw new IllegalStateException("Dungeon-to-boss link is invalid: " + dungeon.id());
            }
        }
        for (BossCatalog.Boss boss : BossCatalog.all()) {
            if (DungeonCatalog.byId(boss.dungeonId()) == null) {
                throw new IllegalStateException("Boss references missing dungeon: " + boss.id());
            }
        }
        if (EnemyPrototypeService.prototypeCount() != EnemyCatalog.all().size()) {
            throw new IllegalStateException("Every catalogued enemy must have a prototype spawn mapping.");
        }
        for (EnemyCatalog.Enemy enemy : EnemyCatalog.all()) {
            if (enemy.health() <= 0 || enemy.movementSpeed() <= 0 || enemy.movementSpeed() > 1) {
                throw new IllegalStateException("Invalid enemy tuning: " + enemy.id());
            }
        }
        for (OriginalMusicCatalog.Theme theme : OriginalMusicCatalog.all()) {
            if (theme.tempoBpm() <= 0 || theme.notes().isEmpty()) {
                throw new IllegalStateException("Empty or invalid music theme: " + theme.id());
            }
            for (OriginalMusicCatalog.Note note : theme.notes()) {
                if (note.midiPitch() < 0 || note.midiPitch() > 127 || note.beats() <= 0) {
                    throw new IllegalStateException("Invalid note in theme: " + theme.id());
                }
            }
        }
        if (DeitySpiritCatalog.all().size() != 8) {
            throw new IllegalStateException("Expected eight deity spirits.");
        }
    }

    private static void requireUnique(String kind, List<String> ids) {
        Set<String> unique = new HashSet<>(ids);
        if (unique.size() != ids.size()) {
            throw new IllegalStateException("Duplicate " + kind + " id in content catalogue.");
        }
        for (String id : ids) {
            if (id == null || id.isBlank()) {
                throw new IllegalStateException("Blank " + kind + " id in content catalogue.");
            }
        }
    }
}
