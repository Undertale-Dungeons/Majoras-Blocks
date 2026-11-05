package dev.majorasblock.items;

import dev.majorasblock.music.OriginalSongPlaybackController;
import dev.majorasblock.time.AdventureTimeRuntime;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/** A reusable exploration item that advances the adventure clock to sunrise. */
public final class ClockworkHourglassItem extends Item {
    public ClockworkHourglassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer
                && level instanceof ServerLevel serverLevel) {
            AdventureTimeRuntime.fastForwardToChosenHour(serverLevel.getServer(), 6, 0);
            OriginalSongPlaybackController.playTheme(serverPlayer, "song_of_chosen_hour");
            serverPlayer.sendSystemMessage(Component.literal("The Clockwork Hourglass turns the day toward sunrise."));
        }
        return InteractionResult.SUCCESS;
    }
}
