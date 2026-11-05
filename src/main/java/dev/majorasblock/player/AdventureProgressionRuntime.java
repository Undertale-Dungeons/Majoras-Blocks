package dev.majorasblock.player;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Session-scoped adventure XP and level rewards earned through exploration. */
public final class AdventureProgressionRuntime {
    public record Status(int level, int experience, int nextLevelAt) {}

    private static final int MAX_LEVEL = 20;
    private static final int REWARD_DURATION = 20 * 60 * 5;
    private static final Map<UUID, Progress> PLAYERS = new ConcurrentHashMap<>();

    private AdventureProgressionRuntime() {}

    public static Status status(UUID playerId) {
        Progress progress = playerId == null ? null : PLAYERS.get(playerId);
        return progress == null
                ? new Status(1, 0, 100)
                : new Status(progress.level, progress.experience, progress.level * 100);
    }

    public static void award(ServerPlayer player, int amount, String source) {
        if (player == null || amount <= 0) return;
        Progress progress = PLAYERS.computeIfAbsent(player.getUUID(), ignored -> new Progress());
        int oldLevel = progress.level;
        progress.experience += amount;
        while (progress.level < MAX_LEVEL && progress.experience >= progress.level * 100) {
            progress.level++;
        }
        player.sendSystemMessage(Component.literal("Adventure +" + amount + " XP (" + source + "). "
                + "Level " + progress.level + " • " + progress.experience + " XP"));
        if (progress.level > oldLevel) {
            grantLevelReward(player, progress.level);
            player.sendSystemMessage(Component.literal("Adventure level " + progress.level
                    + " reached. A new exploration blessing is unlocked."));
        }
    }

    public static void remove(UUID playerId) {
        if (playerId != null) PLAYERS.remove(playerId);
    }

    private static void grantLevelReward(ServerPlayer player, int level) {
        Holder<MobEffect> reward = switch (level) {
            case 2 -> MobEffects.SPEED;
            case 3 -> MobEffects.NIGHT_VISION;
            case 4 -> MobEffects.WATER_BREATHING;
            case 5 -> MobEffects.SLOW_FALLING;
            case 6 -> MobEffects.RESISTANCE;
            case 7 -> MobEffects.FIRE_RESISTANCE;
            case 8 -> MobEffects.REGENERATION;
            default -> null;
        };
        if (reward != null) {
            player.addEffect(new MobEffectInstance(reward, REWARD_DURATION, 0));
        }
    }

    private static final class Progress {
        private int level = 1;
        private int experience;
    }
}
