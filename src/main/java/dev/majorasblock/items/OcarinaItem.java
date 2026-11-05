package dev.majorasblock.items;

import dev.majorasblock.music.OriginalSongPlaybackController;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/** Right-click instrument that plays the mod's original ocarina themes. */
public final class OcarinaItem extends Item {
    public OcarinaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            OriginalSongPlaybackController.playFriendship(serverPlayer);
        }
        return InteractionResult.SUCCESS;
    }
}
