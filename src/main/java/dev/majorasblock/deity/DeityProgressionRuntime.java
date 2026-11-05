package dev.majorasblock.deity;

import dev.majorasblock.dungeon.LandmarkBuilder;
import dev.majorasblock.player.AdventureProgressionRuntime;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Turns built Deity Fountains into once-per-session spirit blessing locations. */
public final class DeityProgressionRuntime {
    public enum ClaimResult { CLAIMED, UNKNOWN_SPIRIT, NOT_AT_FOUNTAIN, ALREADY_CLAIMED }

    private static final int BLESSING_DURATION = 20 * 60 * 20;
    private static final Map<UUID, DeityProgression> PLAYERS = new ConcurrentHashMap<>();

    private DeityProgressionRuntime() {}

    public static ClaimResult claim(ServerPlayer player, String spiritId) {
        if (player == null) return ClaimResult.NOT_AT_FOUNTAIN;
        DeitySpiritCatalog.Spirit spirit = DeitySpiritCatalog.byId(spiritId);
        if (spirit == null) return ClaimResult.UNKNOWN_SPIRIT;
        if (!LandmarkBuilder.isNearDeityFountain((net.minecraft.server.level.ServerLevel) player.level(),
                player.blockPosition(), 12)) {
            return ClaimResult.NOT_AT_FOUNTAIN;
        }

        DeityProgression progression = PLAYERS.computeIfAbsent(player.getUUID(), ignored -> new DeityProgression());
        Optional<DeitySpiritCatalog.Power> power = progression.collect(spiritId);
        if (power.isEmpty()) return ClaimResult.ALREADY_CLAIMED;

        for (Holder<MobEffect> effect : effects(power.get())) {
            player.addEffect(new MobEffectInstance(effect, BLESSING_DURATION, 0));
        }
        AdventureProgressionRuntime.award(player, 50, "Deity Spirit");
        player.sendSystemMessage(Component.literal(spirit.name()
                + " has blessed you. This fountain's gift lasts for this server session."));
        return ClaimResult.CLAIMED;
    }

    public static List<String> collected(UUID playerId) {
        DeityProgression progression = playerId == null ? null : PLAYERS.get(playerId);
        return progression == null ? List.of() : progression.snapshot().stream().sorted().toList();
    }

    private static List<Holder<MobEffect>> effects(DeitySpiritCatalog.Power power) {
        return switch (power) {
            case SWIFT_STEP -> List.of(MobEffects.SPEED, MobEffects.JUMP_BOOST);
            case MOON_VEIL -> List.of(MobEffects.RESISTANCE, MobEffects.SLOW_FALLING);
            case TIDE_BREATH -> List.of(MobEffects.WATER_BREATHING, MobEffects.DOLPHINS_GRACE);
            case ROOTED_STANCE -> List.of(MobEffects.RESISTANCE, MobEffects.SLOW_FALLING);
            case ECHO_FOCUS -> List.of(MobEffects.NIGHT_VISION);
            case SUNWARD_SHELL -> List.of(MobEffects.FIRE_RESISTANCE);
            case SKYBOUND_GRACE -> List.of(MobEffects.SLOW_FALLING, MobEffects.JUMP_BOOST);
            case QUIET_PULSE -> List.of(MobEffects.INVISIBILITY);
        };
    }
}
