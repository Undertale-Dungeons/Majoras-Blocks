package dev.majorasblock.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.majorasblock.dungeon.DungeonStructureBuilder;
import dev.majorasblock.music.OriginalSongPlaybackController;
import dev.majorasblock.player.EquipmentCarryRegistry;
import dev.majorasblock.player.EquipmentCarryState;
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

    private static int playSong(com.mojang.brigadier.context.CommandContext<net.minecraft.commands.CommandSourceStack> context, String themeId) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        boolean played = OriginalSongPlaybackController.playTheme(player, themeId);
        if (!played) {
            context.getSource().sendFailure(Component.literal("Unknown original song: " + themeId));
            return 0;
        }
        context.getSource().sendSuccess(() -> Component.literal("Playing " + themeId.replace('_', ' ') + "."), false);
        return 1;
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("majorasblock")
                            .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                            .then(literal("dungeon")
                                    .then(literal("build")
                                            .then(argument("dungeon", StringArgumentType.word())
                                                    .executes(context -> {
                                                        var player = context.getSource().getPlayerOrException();
                                                        String dungeonId = StringArgumentType.getString(context, "dungeon");
                                                        var result = DungeonStructureBuilder.build(
                                                                context.getSource().getLevel(),
                                                                player.blockPosition(),
                                                                dungeonId
                                                        );
                                                        context.getSource().sendSuccess(
                                                                () -> Component.literal("Built " + result.dungeonName()
                                                                        + " test structure. Placed " + result.blocksPlaced()
                                                                        + " blocks in air at " + result.origin().toShortString() + "."),
                                                                true
                                                        );
                                                        return 1;
                                                    }))))
                            .then(literal("song")
                                    .then(literal("friendship").executes(context -> playSong(context, "song_of_friendship")))
                                    .then(literal("inverted_time").executes(context -> {
                                        AdventureTimeRuntime.activateInvertedTime(context.getSource().getServer());
                                        playSong(context, "song_of_inverted_time");
                                        context.getSource().sendSuccess(() -> Component.literal("The day clock now moves at half speed."), false);
                                        return 1;
                                    }))
                                    .then(literal("double_time").executes(context -> {
                                        AdventureTimeRuntime.fastForwardToChosenHour(context.getSource().getServer(), 6, 0);
                                        playSong(context, "song_of_double_time");
                                        return 1;
                                    }))
                                    .then(literal("chosen_hour")
                                            .then(argument("hour", IntegerArgumentType.integer(0, 23))
                                                    .then(argument("minute", IntegerArgumentType.integer(0, 59))
                                                            .executes(context -> {
                                                                int hour = IntegerArgumentType.getInteger(context, "hour");
                                                                int minute = IntegerArgumentType.getInteger(context, "minute");
                                                                AdventureTimeRuntime.fastForwardToChosenHour(
                                                                        context.getSource().getServer(), hour, minute);
                                                                playSong(context, "song_of_chosen_hour");
                                                                context.getSource().sendSuccess(
                                                                        () -> Component.literal("The clock advanced to "
                                                                                + String.format("%02d:%02d", hour, minute) + "."),
                                                                        false);
                                                                return 1;
                                                            }))))
                                    .then(literal("healing").executes(context -> playSong(context, "song_of_healing")))
                            )
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
            dispatcher.register(
                    literal("gear")
                            .then(literal("status").executes(context -> {
                                var player = context.getSource().getPlayerOrException();
                                EquipmentCarryState state = EquipmentCarryRegistry.get(player.getUUID());
                                context.getSource().sendSuccess(
                                        () -> Component.literal("Weapon: " + state.activeWeaponId()
                                                + " (" + state.weaponAttachment() + ")"
                                                + " | Shield: " + state.activeShieldId()
                                                + " (" + state.shieldAttachment() + ")"),
                                        false
                                );
                                return 1;
                            }))
                            .then(literal("draw").executes(context -> {
                                var player = context.getSource().getPlayerOrException();
                                EquipmentCarryState state = EquipmentCarryRegistry.get(player.getUUID());
                                state.drawWeapon();
                                context.getSource().sendSuccess(
                                        () -> Component.literal("Weapon drawn."),
                                        false
                                );
                                return 1;
                            }))
                            .then(literal("sheathe").executes(context -> {
                                var player = context.getSource().getPlayerOrException();
                                EquipmentCarryRegistry.get(player.getUUID()).sheatheWeapon();
                                context.getSource().sendSuccess(() -> Component.literal("Weapon sheathed."), false);
                                return 1;
                            }))
                            .then(literal("shield")
                                    .then(literal("raise").executes(context -> {
                                        var player = context.getSource().getPlayerOrException();
                                        EquipmentCarryRegistry.get(player.getUUID()).raiseShield();
                                        context.getSource().sendSuccess(() -> Component.literal("Shield raised."), false);
                                        return 1;
                                    }))
                                    .then(literal("lower").executes(context -> {
                                        var player = context.getSource().getPlayerOrException();
                                        EquipmentCarryRegistry.get(player.getUUID()).lowerShield();
                                        context.getSource().sendSuccess(() -> Component.literal("Shield lowered."), false);
                                        return 1;
                                    })))
            );
        });
    }
}
