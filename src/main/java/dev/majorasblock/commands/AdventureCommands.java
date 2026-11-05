package dev.majorasblock.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.majorasblock.time.AdventureTimeRuntime;
import dev.majorasblock.time.ThreeDayCycle;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.clock.WorldClock;

import java.util.Optional;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/** Operator commands for testing shared adventure-clock behavior. */
public final class AdventureCommands {
    private AdventureCommands() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("majorasblock")
                            .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                            .then(literal("status").executes(context -> {
                                ServerLevel overworld = context.getSource().getServer().overworld();
                                Optional<Holder<WorldClock>> clock = overworld.dimensionType().defaultClock();
                                long time = clock.map(value ->
                                        context.getSource().getServer().clockManager().getTotalTicks(value)
                                ).orElse(0L);
                                int day = ThreeDayCycle.dayNumber(time);
                                int tick = (int) ThreeDayCycle.tickOfDay(time);
                                int hour = Math.floorMod(6 + tick / 1_000, 24);
                                int minute = (tick % 1_000) * 60 / 1_000;
                                context.getSource().sendSuccess(
                                        () -> Component.literal("Adventure day " + day
                                                + " | " + String.format("%02d:%02d", hour, minute)
                                                + " | day clock " + AdventureTimeRuntime.dayClockScale(
                                                        context.getSource().getServer()) + "x"),
                                        false
                                );
                                return 1;
                            }))
                            .then(literal("time")
                                    .then(literal("slow").executes(context -> {
                                        AdventureTimeRuntime.activateInvertedTime(context.getSource().getServer());
                                        context.getSource().sendSuccess(
                                                () -> Component.literal("The adventure day clock now runs at half speed."),
                                                false
                                        );
                                        return 1;
                                    }))
                                    .then(literal("normal").executes(context -> {
                                        AdventureTimeRuntime.restoreNormalTime(context.getSource().getServer());
                                        context.getSource().sendSuccess(
                                                () -> Component.literal("The adventure day clock is back to normal."),
                                                false
                                        );
                                        return 1;
                                    }))
                                    .then(literal("set")
                                            .then(argument("hour", IntegerArgumentType.integer(0, 23))
                                                    .then(argument("minute", IntegerArgumentType.integer(0, 59))
                                                            .executes(context -> {
                                                                int hour = IntegerArgumentType.getInteger(context, "hour");
                                                                int minute = IntegerArgumentType.getInteger(context, "minute");
                                                                long target = AdventureTimeRuntime.fastForwardToChosenHour(
                                                                        context.getSource().getServer(), hour, minute
                                                                );
                                                                context.getSource().sendSuccess(
                                                                        () -> Component.literal("The adventure clock advanced to "
                                                                                + String.format("%02d:%02d", hour, minute)
                                                                                + " (clock ticks " + target + ")."),
                                                                        false
                                                                );
                                                                return 1;
                                                            })))))
            );
        });
    }
}
