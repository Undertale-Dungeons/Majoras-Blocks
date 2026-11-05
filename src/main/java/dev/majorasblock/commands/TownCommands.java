package dev.majorasblock.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.majorasblock.content.BossCatalog;
import dev.majorasblock.content.ModItems;
import dev.majorasblock.dungeon.LandmarkBuilder;
import dev.majorasblock.economy.PersistentRewardStore;
import dev.majorasblock.economy.ShopService;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/** Clock Town shopping, one-time hidden collectibles, and boss reward dispatch. */
public final class TownCommands {
    private static final Map<String, String> TREASURES = Map.of(
            "spirit_grove", "spirit",
            "spirit_spring", "spirit",
            "heart_grove", "heart",
            "heart_cave", "heart"
    );

    private TownCommands() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registries, environment) ->
                dispatcher.register(literal("majorasblock")
                        .then(literal("shop")
                                .then(literal("list").executes(context -> {
                                    String listings = ShopService.offers().stream()
                                            .map(offer -> offer.id() + " — " + offer.name() + ": "
                                                    + offer.price() + " rupees (" + offer.shopId() + ")")
                                            .collect(java.util.stream.Collectors.joining("\n"));
                                    context.getSource().sendSuccess(() -> Component.literal(
                                            "Clock Town shops. Buy with /majorasblock shop buy <item> [amount]:\n"
                                                    + listings), false);
                                    return ShopService.offers().size();
                                }))
                                .then(literal("buy")
                                        .then(argument("item", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    for (ShopService.Offer offer : ShopService.offers()) {
                                                        builder.suggest(offer.id());
                                                    }
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> buy(context, 1))
                                                .then(argument("amount", IntegerArgumentType.integer(1, 16))
                                                        .executes(context -> buy(context,
                                                                IntegerArgumentType.getInteger(context, "amount")))))))
                        .then(literal("treasure")
                                .then(literal("claim")
                                        .then(argument("location", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    TREASURES.keySet().forEach(builder::suggest);
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> claimTreasure(context)))))
                        .then(literal("boss")
                                .then(literal("reward")
                                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                                        .then(argument("id", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    BossCatalog.all().forEach(boss -> builder.suggest(boss.id()));
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> dropBossReward(context)))))
                )
        );
    }

    private static int buy(
            com.mojang.brigadier.context.CommandContext<net.minecraft.commands.CommandSourceStack> context,
            int amount) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        String id = StringArgumentType.getString(context, "item");
        ShopService.PurchaseResult result = ShopService.buy(player, id, amount);
        switch (result) {
            case BOUGHT -> context.getSource().sendSuccess(() -> Component.literal(
                    "Bought " + amount + " × " + ShopService.offer(id).orElseThrow().name() + "."), false);
            case UNKNOWN_OFFER -> context.getSource().sendFailure(Component.literal("That item is not sold in Clock Town."));
            case WRONG_SHOP -> context.getSource().sendFailure(Component.literal(
                    "Find the matching Clock Town stall and its shopkeeper first."));
            case NOT_ENOUGH_RUPEES -> context.getSource().sendFailure(Component.literal("You need more rupees."));
            case INVENTORY_FULL -> context.getSource().sendFailure(Component.literal("Make room in your inventory."));
            case INVALID_AMOUNT -> context.getSource().sendFailure(Component.literal("Choose an amount from 1 to 16."));
        }
        return result == ShopService.PurchaseResult.BOUGHT ? 1 : 0;
    }

    private static int claimTreasure(
            com.mojang.brigadier.context.CommandContext<net.minecraft.commands.CommandSourceStack> context)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        String id = StringArgumentType.getString(context, "location");
        String rewardType = TREASURES.get(id);
        if (rewardType == null) {
            context.getSource().sendFailure(Component.literal(
                    "Unknown treasure location. Choose spirit_grove, spirit_spring, heart_grove, or heart_cave."));
            return 0;
        }
        if (!(player.level() instanceof ServerLevel level)
                || !LandmarkBuilder.isNearLandmark(level, player.blockPosition(), id, 10)) {
            context.getSource().sendFailure(Component.literal("You are not close enough to that hidden shrine."));
            return 0;
        }
        if (!PersistentRewardStore.claimLocation(context.getSource().getServer(), player.getUUID(), id)) {
            context.getSource().sendFailure(Component.literal("You have already collected this treasure in this world."));
            return 0;
        }

        ItemStack reward = rewardType.equals("heart")
                ? new ItemStack(ModItems.LIFE_CRYSTAL)
                : new ItemStack(ModItems.SPIRIT_SHARD);
        if (!player.getInventory().add(reward)) player.drop(reward, false);
        context.getSource().sendSuccess(() -> Component.literal(
                rewardType.equals("heart")
                        ? "You found a Life Crystal."
                        : "You found a Spirit Shard."), false);
        return 1;
    }

    private static int dropBossReward(
            com.mojang.brigadier.context.CommandContext<net.minecraft.commands.CommandSourceStack> context)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        String bossId = StringArgumentType.getString(context, "id");
        var boss = BossCatalog.byId(bossId);
        if (boss.isEmpty()) {
            context.getSource().sendFailure(Component.literal("Unknown boss id: " + bossId));
            return 0;
        }
        if (!PersistentRewardStore.claimBoss(context.getSource().getServer(), bossId)) {
            context.getSource().sendFailure(Component.literal(
                    "This boss has already awarded its Life Crystal drop in this world."));
            return 0;
        }
        int count = context.getSource().getServer().getPlayerList().getPlayerCount() > 1 ? 2 : 1;
        ItemEntity drop = new ItemEntity((ServerLevel) player.level(),
                player.getX(), player.getY() + 0.5, player.getZ(),
                new ItemStack(ModItems.LIFE_CRYSTAL, count));
        ((ServerLevel) player.level()).addFreshEntity(drop);
        context.getSource().sendSuccess(() -> Component.literal(
                boss.get().name() + " reward: " + count + " Life Crystal(s). "
                        + "This world can claim this boss reward only once."), false);
        return 1;
    }
}
