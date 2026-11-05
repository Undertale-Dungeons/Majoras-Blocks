package dev.majorasblock.economy;

import dev.majorasblock.content.ModItems;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

/** Adds a small randomized supply cache when a vanilla decorated pot is broken. */
public final class PotLootRuntime {
    private PotLootRuntime() {}

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((Level world, Player player, BlockPos pos,
                                               BlockState state, net.minecraft.world.level.block.entity.BlockEntity blockEntity) -> {
            if (!(world instanceof ServerLevel level) || !state.is(Blocks.DECORATED_POT)) return;
            int roll = level.getRandom().nextInt(100);
            Item item;
            int count;
            if (roll < 25) {
                item = ModItems.RUPEE;
                count = 2 + level.getRandom().nextInt(5);
            } else if (roll < 47) {
                item = Items.STICK;
                count = 1 + level.getRandom().nextInt(3);
            } else if (roll < 65) {
                item = ModItems.DEKU_NUT;
                count = 1 + level.getRandom().nextInt(2);
            } else if (roll < 80) {
                item = Items.WHEAT_SEEDS;
                count = 1 + level.getRandom().nextInt(3);
            } else if (roll < 92) {
                item = Items.BREAD;
                count = 1;
            } else if (roll < 98) {
                item = ModItems.GOLDEN_DUST;
                count = 1;
            } else {
                item = ModItems.SPIRIT_SHARD;
                count = 1;
            }
            drop(level, pos, item, count);
        });
    }

    private static void drop(ServerLevel level, BlockPos pos, Item item, int count) {
        ItemEntity entity = new ItemEntity(level,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                new ItemStack(item, count));
        level.addFreshEntity(entity);
    }
}
