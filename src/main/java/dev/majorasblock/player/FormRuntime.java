package dev.majorasblock.player;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session-scoped Deku, Goron, and Zora form powers.
 * The forms provide distinct exploration abilities without changing player
 * dimensions, hitboxes, or world data.
 */
public final class FormRuntime {
    public enum Form { NONE, DEKU, GORON, ZORA }

    private static final Map<UUID, Form> ACTIVE_FORMS = new ConcurrentHashMap<>();
    private static boolean registered;

    private FormRuntime() {}

    public static synchronized void register() {
        if (registered) return;
        registered = true;
        ServerTickEvents.END_SERVER_TICK.register(FormRuntime::tick);
    }

    public static void activate(ServerPlayer player, Form form) {
        if (player == null || form == null || form == Form.NONE) return;
        ACTIVE_FORMS.put(player.getUUID(), form);
        apply(player, form, 100);
    }

    public static Form active(UUID playerId) {
        return playerId == null ? Form.NONE : ACTIVE_FORMS.getOrDefault(playerId, Form.NONE);
    }

    public static void clear(UUID playerId) {
        if (playerId != null) ACTIVE_FORMS.remove(playerId);
    }

    private static void tick(MinecraftServer server) {
        Set<UUID> online = new HashSet<>();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            UUID id = player.getUUID();
            online.add(id);
            if (server.overworld().getGameTime() % 40L != 0L) continue;
            Form form = ACTIVE_FORMS.get(id);
            if (form != null) apply(player, form, 80);
        }
        ACTIVE_FORMS.keySet().removeIf(id -> !online.contains(id));
    }

    private static void apply(ServerPlayer player, Form form, int duration) {
        for (Holder<MobEffect> effect : effects(form)) {
            int amplifier = form == Form.GORON && effect == MobEffects.RESISTANCE ? 1 : 0;
            player.addEffect(new MobEffectInstance(effect, duration, amplifier));
        }
    }

    private static List<Holder<MobEffect>> effects(Form form) {
        return switch (form) {
            case DEKU -> List.of(MobEffects.JUMP_BOOST, MobEffects.SLOW_FALLING, MobEffects.SPEED);
            case GORON -> List.of(MobEffects.RESISTANCE, MobEffects.FIRE_RESISTANCE);
            case ZORA -> List.of(MobEffects.WATER_BREATHING, MobEffects.NIGHT_VISION, MobEffects.DOLPHINS_GRACE);
            case NONE -> List.of();
        };
    }
}
