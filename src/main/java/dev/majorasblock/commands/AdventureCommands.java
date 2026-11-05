package dev.majorasblock.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.majorasblock.dungeon.DungeonStructureBuilder;
import dev.majorasblock.content.EnemyPrototypeService;
import dev.majorasblock.music.OriginalSongPlaybackController;
import dev.majorasblock.horror.DrownedModeController;
import dev.majorasblock.horror.DrownedRuntime;
import dev.majorasblock.time.AdventureTimeRuntime;
import dev.majorasblock.time.ThreeDayCycle;
import dev.majorasblock.songs.FriendshipArrivalRuntime;
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
                            .then(literal("drowned")
                                    .then(argument("intensity", StringArgumentType.word())
                                            .executes(context -> {
                                                var player = context.getSource().getPlayerOrException();
                                                String value = StringArgumentType.getString(context, "intensity");
                                                DrownedModeController.Intensity intensity;
                                                try {
                                                    intensity = DrownedModeController.Intensity.valueOf(value.toUpperCase());
                                                } catch (IllegalArgumentException exception) {
                                                    context.getSource().sendFailure(Component.literal(
                                                            "Use off, subtle, uneasy, or full."));
                                                    return 0;
                                                }
                                                DrownedRuntime.setIntensity(player.getUUID(), intensity);
                                                context.getSource().sendSuccess(
                                                        () -> Component.literal("DROWNED atmosphere set to "
                                                                + intensity.name().toLowerCase() + "."),
                                                        false
                                                );
                                                return 1;
                                            })))
                            .then(literal("enemy")
                                    .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                                    .then(literal("spawn")
                                            .then(argument("enemy", StringArgumentType.word())
                                                    .executes(context -> {
                                                        var player = context.getSource().getPlayerOrException();
                                                        String enemyId = StringArgumentType.getString(context, "enemy");
                                                        var spawnPos = player.blockPosition().relative(player.getDirection(), 3).above();
                                                        var result = EnemyPrototypeService.spawn(
                                                                context.getSource().getLevel(), spawnPos, enemyId);
                                                        if (!result.success()) {
                                                            context.getSource().sendFailure(Component.literal(result.message()));
                                                            return 0;
                                                        }
                                                        context.getSource().sendSuccess(
                                                                () -> Component.literal("Spawned " + result.message()
                                                                        + " prototype. Custom AI and model are still in development."),
                                                                true
                                                        );
                                                        return 1;
                                                    }))))
                            .then(literal("dungeon")
                                    .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
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
                            .then(literal("friendship")
                                    .then(literal("accept").executes(context -> {
                                        var player = context.getSource().getPlayerOrException();
                                        FriendshipArrivalRuntime.setConsent(player.getUUID(), true);
                                        context.getSource().sendSuccess(
                                                () -> Component.literal("Song of Friendship arrivals are now enabled for this session."),
                                                false
                                        );
                                        return 1;
                                    }))
                                    .then(literal("deny").executes(context -> {
                                        var player = context.getSource().getPlayerOrException();
                                        FriendshipArrivalRuntime.setConsent(player.getUUID(), false);
                                        context.getSource().sendSuccess(
                                                () -> Component.literal("Song of Friendship arrivals are now disabled."),
                                                false
                                        );
                                        return 1;
                                    })))
                            .then(literal("song")
                                    .then(literal("friendship")
                                            .then(argument("friend", StringArgumentType.word())
                                                    .executes(context -> {
                                                        var traveller = context.getSource().getPlayerOrException();
                                                        String friendName = StringArgumentType.getString(context, "friend");
                                                        var friend = context.getSource().getServer()
                                                                .getPlayerList().getPlayerByName(friendName);
                                                        if (friend == null) {
                                                            context.getSource().sendFailure(Component.literal("That player is not online."));
                                                            return 0;
                                                        }
                                                        if (friend == traveller) {
                                                            context.getSource().sendFailure(Component.literal("Choose another player."));
                                                            return 0;
                                                        }
                                                        if (!FriendshipArrivalRuntime.accepts(friend.getUUID())) {
                                                            context.getSource().sendFailure(Component.literal(
                                                                    friend.getGameProfile().name() + " has not enabled Friendship arrivals."));
                                                            return 0;
                                                        }
                                                        if (!FriendshipArrivalRuntime.teleport(traveller, friend)) {
                                                            context.getSource().sendFailure(Component.literal(
                                                                    "No safe arrival space was found near that player."));
                                                            return 0;
                                                        }
                                                        playSong(context, "song_of_friendship");
                                                        context.getSource().sendSuccess(
                                                                () -> Component.literal("You soared to " + friend.getGameProfile().name() + "."),
                                                                false
                                                        );
                                                        return 1;
                                                    }))
                                            .executes(context -> playSong(context, "song_of_friendship")))
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
                                    .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
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
