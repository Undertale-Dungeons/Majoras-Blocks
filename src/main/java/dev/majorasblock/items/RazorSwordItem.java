package dev.majorasblock.items;

import dev.majorasblock.content.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Collectible heirloom item with 100 ceremonial uses.
 * It does not add attack damage or weapon behavior. Right-clicking spends one
 * display/use charge; after 100 uses the item returns to the Hero's Sword.
 */
public final class RazorSwordItem extends Item {
    public RazorSwordItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        ItemStack held = player.getItemInHand(hand);
        int nextDamage = held.getDamageValue() + 1;
        if (nextDamage >= held.getMaxDamage()) {
            player.setItemInHand(hand, new ItemStack(ModItems.HERO_SWORD));
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(Component.literal(
                        "The Razor Sword has dulled and returned to the Hero's Sword."));
            }
        } else {
            held.setDamageValue(nextDamage);
            if (player instanceof ServerPlayer serverPlayer && nextDamage % 10 == 0) {
                serverPlayer.sendSystemMessage(Component.literal(
                        "Razor Sword durability: " + (held.getMaxDamage() - nextDamage) + "/100."));
            }
        }
        return InteractionResult.SUCCESS;
    }
}
