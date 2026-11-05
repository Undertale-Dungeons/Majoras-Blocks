package dev.majorasblock.items;

import dev.majorasblock.content.MaskCatalog;
import dev.majorasblock.content.RelicCatalog;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.List;

/** Usable mask and relic. Each entry grants its own temporary exploration power. */
public final class MaskItem extends Item {
    private static final int DURATION = 20 * 60 * 5;
    private final MaskCatalog.Power power;
    private final String title;
    private final String description;

    public MaskItem(Properties properties, MaskCatalog.Mask mask) {
        this(properties, mask.power(), mask.name(), mask.description());
    }

    public MaskItem(Properties properties, RelicCatalog.Relic relic) {
        this(properties, relic.power(), relic.name(), relic.description());
    }

    private MaskItem(Properties properties, MaskCatalog.Power power, String title, String description) {
        super(properties);
        this.power = power;
        this.title = title;
        this.description = description;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            for (Holder<MobEffect> effect : effects(power)) {
                serverPlayer.addEffect(new MobEffectInstance(effect, DURATION, 0));
            }
            serverPlayer.sendSystemMessage(Component.literal(title + ": " + description));
        }
        return InteractionResult.SUCCESS;
    }

    private static List<Holder<MobEffect>> effects(MaskCatalog.Power power) {
        return switch (power) {
            case DEKU_FORM -> List.of(MobEffects.JUMP_BOOST, MobEffects.SLOW_FALLING);
            case GORON_FORM -> List.of(MobEffects.RESISTANCE, MobEffects.FIRE_RESISTANCE);
            case ZORA_FORM -> List.of(MobEffects.WATER_BREATHING, MobEffects.NIGHT_VISION);
            case TRAVERSAL -> List.of(MobEffects.SPEED, MobEffects.SLOW_FALLING);
            case AQUATIC -> List.of(MobEffects.WATER_BREATHING, MobEffects.NIGHT_VISION);
            case RESILIENT -> List.of(MobEffects.RESISTANCE, MobEffects.FIRE_RESISTANCE);
            case VISION -> List.of(MobEffects.NIGHT_VISION);
            case RECOVERY -> List.of(MobEffects.REGENERATION);
            case AGILITY -> List.of(MobEffects.SPEED, MobEffects.JUMP_BOOST);
            case QUIET -> List.of(MobEffects.INVISIBILITY);
            case ECHO -> List.of(MobEffects.NIGHT_VISION, MobEffects.SPEED);
            case BALANCED -> List.of(MobEffects.SPEED, MobEffects.RESISTANCE);
        };
    }
}
