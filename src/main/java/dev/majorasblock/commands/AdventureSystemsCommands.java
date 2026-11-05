package dev.majorasblock.commands;

import dev.majorasblock.deity.DeityProgressionRuntime;
import dev.majorasblock.player.AdventureProgressionRuntime;
import dev.majorasblock.player.FormRuntime;
import dev.majorasblock.time.ThreeDayCycle;
import dev.majorasblock.world.MoonRealmService;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.clock.WorldClock;

import java.util.Optional;

import static net.minecraft.commands.Commands.literal;

/** Commands for forms, progression, the lunar destination, and the three-day clock. */
public final class AdventureSystemsCommands {
    private AdventureSystemsCommands() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registries, environment) ->
                dispatcher.register(literal("majorasblock")
                        .then(literal("moon")
                                .then(literal("travel").executes(context -> {
                                    var player = context.getSource().getPlayerOrException();
                                    boolean moved = MoonRealmService.travelToMoon(context.getSource().getServer(), player);
                                    if (!moved) {
                                        context.getSource().sendFailure(Component.literal("The Moon route could not be opened."));
                                        return 0;
                                    }
                                    context.getSource().sendSuccess(() -> Component.literal(
                                            "You arrive at the Moon. Use /majorasblock moon return to go back."), false);
                                    return 1;
                                }))
                                .then(literal("return").executes(context -> {
                                    var player = context.getSource().getPlayerOrException();
                                    boolean moved = MoonRealmService.returnFromMoon(context.getSource().getServer(), player);
                                    if (!moved) {
                                        context.getSource().sendFailure(Component.literal("No safe return point was found."));
                                        return 0;
                                    }
                                    context.getSource().sendSuccess(() -> Component.literal("You return from the Moon."), false);
                                    return 1;
                                })))
                        .then(literal("form")
                                .then(literal("status").executes(context -> {
                                    var player = context.getSource().getPlayerOrException();
                                    FormRuntime.Form form = FormRuntime.active(player.getUUID());
                                    context.getSource().sendSuccess(() -> Component.literal(
                                            "Current form: " + form.name().toLowerCase()), false);
                                    return 1;
                                }))
                                .then(literal("clear").executes(context -> {
                                    var player = context.getSource().getPlayerOrException();
                                    FormRuntime.clear(player.getUUID());
                                    context.getSource().sendSuccess(() -> Component.literal(
                                            "Your active form has been cleared. Existing short effects will fade shortly."), false);
                                    return 1;
                                })))
                        .then(literal("progress").executes(context -> {
                            var player = context.getSource().getPlayerOrException();
                            AdventureProgressionRuntime.Status status =
                                    AdventureProgressionRuntime.status(player.getUUID());
                            context.getSource().sendSuccess(() -> Component.literal(
                                    "Adventure level " + status.level() + " • " + status.experience()
                                            + " XP • next level at " + status.nextLevelAt() + " XP"), false);
                            return 1;
                        }))
                        .then(literal("cycle").executes(context -> {
                            var server = context.getSource().getServer();
                            ServerLevel overworld = server.overworld();
                            Optional<Holder<WorldClock>> clock = overworld.dimensionType().defaultClock();
                            if (clock.isEmpty()) {
                                context.getSource().sendFailure(Component.literal("This world has no adventure clock."));
                                return 0;
                            }
                            long ticks = server.clockManager().getTotalTicks(clock.get());
                            int day = ThreeDayCycle.dayNumber(ticks);
                            long dayTick = ThreeDayCycle.tickOfDay(ticks);
                            long remaining = ThreeDayCycle.TICKS_PER_DAY - dayTick;
                            long remainingMinutes = remaining * 60L / 1_000L;
                            context.getSource().sendSuccess(() -> Component.literal(
                                    "Day " + day + " of 3 • " + remainingMinutes
                                            + " adventure minutes until the next day."), false);
                            return 1;
                        }))
                        .then(literal("spirit")
                                .then(literal("claim")
                                        .then(net.minecraft.commands.Commands.argument("id",
                                                com.mojang.brigadier.arguments.StringArgumentType.word())
                                                .executes(context -> {
                                                    var player = context.getSource().getPlayerOrException();
                                                    String id = com.mojang.brigadier.arguments.StringArgumentType.getString(
                                                            context, "id");
                                                    DeityProgressionRuntime.ClaimResult result =
                                                            DeityProgressionRuntime.claim(player, id);
                                                    switch (result) {
                                                        case CLAIMED -> context.getSource().sendSuccess(
                                                                () -> Component.literal("The fountain answers your call."), false);
                                                        case UNKNOWN_SPIRIT -> context.getSource().sendFailure(
                                                                Component.literal("Unknown spirit id: " + id));
                                                        case NOT_AT_FOUNTAIN -> context.getSource().sendFailure(
                                                                Component.literal("Build a Deity Fountain and stand near its center first."));
                                                        case ALREADY_CLAIMED -> context.getSource().sendFailure(
                                                                Component.literal("You have already claimed that spirit this session."));
                                                    }
                                                    return result == DeityProgressionRuntime.ClaimResult.CLAIMED ? 1 : 0;
                                                }))))
                )
        );
    }
}
