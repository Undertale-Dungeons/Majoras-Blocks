package dev.majorasblock.items;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/** Reusable spirit relic that grants a short, non-offensive exploration blessing. */
public final class SpiritCharmItem extends Item {
    private static final int BLESSING_DURATION_TICKS = 20 * 60 * 5;
    private final Holder<MobEffect> effect;

    public SpiritCharmItem(Properties properties, Holder<MobEffect> effect) {
        super(properties);
        this.effect = effect;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.addEffect(new MobEffectInstance(effect, BLESSING_DURATION_TICKS, 0));
            serverPlayer.sendSystemMessage(Component.literal("A spirit's blessing settles over you."));
        }
        return InteractionResult.SUCCESS;
    }
}
