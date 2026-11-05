package dev.majorasblock.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.majorasblock.economy.BlacksmithService;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/** Rupee wallet and blacksmith order commands. */
public final class EconomyCommands {
    private EconomyCommands() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registries, environment) ->
                dispatcher.register(literal("majorasblock")
                        .then(literal("rupees")
                                .then(literal("balance").executes(context -> {
                                    var player = context.getSource().getPlayerOrException();
                                    int balance = BlacksmithService.countRupees(player);
                                    context.getSource().sendSuccess(
                                            () -> Component.literal("Wallet: " + balance + " rupees."), false);
                                    return 1;
                                }))
                                .then(literal("grant")
                                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                                        .then(argument("amount", IntegerArgumentType.integer(1, 640))
                                                .executes(context -> {
                                                    var player = context.getSource().getPlayerOrException();
                                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                                    if (!BlacksmithService.grantRupees(player, amount)) {
                                                        context.getSource().sendFailure(Component.literal(
                                                                "There is not enough room in your inventory."));
                                                        return 0;
                                                    }
                                                    context.getSource().sendSuccess(
                                                            () -> Component.literal("Added " + amount + " rupees."), false);
                                                    return 1;
                                                }))))
                        .then(literal("smith")
                                .then(literal("order")
                                        .then(argument("blueprint", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    builder.suggest("hero");
                                                    builder.suggest("deku");
                                                    builder.suggest("goron");
                                                    builder.suggest("zora");
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> {
                                                    var player = context.getSource().getPlayerOrException();
                                                    String blueprint = StringArgumentType.getString(context, "blueprint");
                                                    BlacksmithService.OrderResult result =
                                                            BlacksmithService.order(player, context.getSource().getServer(), blueprint);
                                                    switch (result) {
                                                        case ORDERED -> context.getSource().sendSuccess(
                                                                () -> Component.literal("The blacksmith accepts 100 rupees. "
                                                                        + "Your heirloom will be ready after one in-game day. "
                                                                        + "Use /majorasblock smith collect at the forge."), false);
                                                        case UNKNOWN_BLUEPRINT -> context.getSource().sendFailure(Component.literal(
                                                                "Choose hero, deku, goron, or zora."));
                                                        case NOT_AT_FORGE -> context.getSource().sendFailure(Component.literal(
                                                                "You need to be beside a built Blacksmith Forge."));
                                                        case ALREADY_ORDERED -> context.getSource().sendFailure(Component.literal(
                                                                "You already have an unfinished blacksmith order."));
                                                        case WRONG_FORM -> context.getSource().sendFailure(Component.literal(
                                                                "Wear the matching Deku, Goron, or Zora form mask before ordering that heirloom."));
                                                        case NOT_ENOUGH_RUPEES -> context.getSource().sendFailure(Component.literal(
                                                                "The blacksmith needs 100 rupees."));
                                                        case CLOCK_UNAVAILABLE -> context.getSource().sendFailure(Component.literal(
                                                                "The adventure clock is unavailable in this world."));
                                                    }
                                                    return result == BlacksmithService.OrderResult.ORDERED ? 1 : 0;
                                                })))
                                .then(literal("status").executes(context -> {
                                    var player = context.getSource().getPlayerOrException();
                                    var status = BlacksmithService.status(player, context.getSource().getServer());
                                    if (status.isEmpty()) {
                                        context.getSource().sendFailure(Component.literal(
                                                "You do not have an active blacksmith order."));
                                        return 0;
                                    }
                                    var order = status.get();
                                    long ticks = order.ticksRemaining();
                                    long minutes = (ticks * 24L + 999L) / 1000L;
                                    String state = order.ready() ? "Ready for collection"
                                            : minutes + " adventure minutes remaining";
                                    context.getSource().sendSuccess(() -> Component.literal(
                                            order.blueprint().displayName() + ": " + state + "."), false);
                                    return 1;
                                }))
                                .then(literal("collect").executes(context -> {
                                    var player = context.getSource().getPlayerOrException();
                                    BlacksmithService.CollectResult result =
                                            BlacksmithService.collect(player, context.getSource().getServer());
                                    switch (result) {
                                        case COLLECTED -> context.getSource().sendSuccess(
                                                () -> Component.literal("The blacksmith hands over your finished heirloom."), false);
                                        case NO_ORDER -> context.getSource().sendFailure(
                                                Component.literal("You have no blacksmith order."));
                                        case NOT_READY -> context.getSource().sendFailure(
                                                Component.literal("The blacksmith is still working. Check /majorasblock smith status."));
                                        case NOT_AT_FORGE -> context.getSource().sendFailure(
                                                Component.literal("Return to the Blacksmith Forge to collect your order."));
                                        case INVENTORY_FULL -> context.getSource().sendFailure(
                                                Component.literal("Make room in your inventory before collecting."));
                                        case CLOCK_UNAVAILABLE -> context.getSource().sendFailure(
                                                Component.literal("The adventure clock is unavailable in this world."));
                                    }
                                    return result == BlacksmithService.CollectResult.COLLECTED ? 1 : 0;
                                })))
                )
        );
    }
}
