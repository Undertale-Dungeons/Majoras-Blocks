package dev.majorasblock.items;

import dev.majorasblock.music.OriginalSongPlaybackController;
import dev.majorasblock.songs.FriendshipArrivalRuntime;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/**
 * The Ocarina is a real usable item: normal use plays Friendship and can
 * travel to a nearby opted-in friend; sneaking plays the Healing melody.
 */
public final class OcarinaItem extends Item {
    public OcarinaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.isShiftKeyDown()) {
                OriginalSongPlaybackController.playTheme(serverPlayer, "song_of_healing");
                serverPlayer.sendSystemMessage(Component.literal("You play the Song of Healing."));
            } else {
                ServerPlayer friend = FriendshipArrivalRuntime.nearestAcceptingFriend(serverPlayer, 64.0);
                if (friend != null && FriendshipArrivalRuntime.teleport(serverPlayer, friend)) {
                    OriginalSongPlaybackController.playFriendship(serverPlayer);
                    serverPlayer.sendSystemMessage(Component.literal(
                            "The Song of Friendship carries you to " + friend.getGameProfile().name() + "."));
                    friend.sendSystemMessage(Component.literal(
                            serverPlayer.getGameProfile().name() + " arrived through the Song of Friendship."));
                } else {
                    OriginalSongPlaybackController.playFriendship(serverPlayer);
                    serverPlayer.sendSystemMessage(Component.literal(
                            "You play the Song of Friendship. Nearby arrivals require /majorasblock friendship accept."));
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}
