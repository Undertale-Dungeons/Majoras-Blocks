package dev.majorasblock.player;

import java.util.Set;

/** The intended quest/forge route from the Hero's Sword to the Gilded Sword. */
public final class SwordsmithProgression {
    public enum Stage { NO_SWORD, HERO_SWORD, RAZOR_SWORD, GILDED_SWORD }
    public enum Result { SUCCESS, WRONG_STAGE, MISSING_MATERIALS, FORGE_NOT_READY }

    public record Materials(int moonsteel, int goldDust, int clockworkParts, int rupees) {
        public boolean hasRazorMaterials() {
            return moonsteel >= 3 && clockworkParts >= 1 && rupees >= 100;
        }
        public boolean hasGildedMaterials() {
            return goldDust >= 1 && moonsteel >= 2 && rupees >= 150;
        }
    }

    private Stage stage = Stage.NO_SWORD;
    private long razorForgeCompletionTick = -1L;

    public synchronized Stage stage() {
        return stage;
    }

    public synchronized void awardHeroSword() {
        if (stage == Stage.NO_SWORD) {
            stage = Stage.HERO_SWORD;
        }
    }

    public synchronized Result beginRazorForge(Materials materials) {
        if (stage != Stage.HERO_SWORD) {
            return Result.WRONG_STAGE;
        }
        if (materials == null || !materials.hasRazorMaterials()) {
            return Result.MISSING_MATERIALS;
        }
        stage = Stage.RAZOR_SWORD;
        return Result.SUCCESS;
    }

    public synchronized Result beginGildedForge(Materials materials, long currentAdventureTick,
                                                  boolean goldDustTempered) {
        if (stage != Stage.RAZOR_SWORD) {
            return Result.WRONG_STAGE;
        }
        if (materials == null || !materials.hasGildedMaterials()) {
            return Result.MISSING_MATERIALS;
        }
        if (!goldDustTempered) {
            return Result.FORGE_NOT_READY;
        }
        razorForgeCompletionTick = currentAdventureTick + 24_000L;
        return Result.SUCCESS;
    }

    public synchronized Result claimGildedSword(long currentAdventureTick) {
        if (stage != Stage.RAZOR_SWORD) {
            return Result.WRONG_STAGE;
        }
        if (razorForgeCompletionTick < 0 || currentAdventureTick < razorForgeCompletionTick) {
            return Result.FORGE_NOT_READY;
        }
        stage = Stage.GILDED_SWORD;
        return Result.SUCCESS;
    }

    public synchronized void load(Stage savedStage, long forgeCompletionTick) {
        if (savedStage != null) {
            stage = savedStage;
        }
        razorForgeCompletionTick = Math.max(-1L, forgeCompletionTick);
    }
}
