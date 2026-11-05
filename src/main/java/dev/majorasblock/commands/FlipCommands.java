package dev.majorasblock.commands;

import dev.majorasblock.player.FlipMobilityRuntime;
import dev.majorasblock.player.FlipMovementController;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.literal;

/** Directional flip mobility, independent from target focus and combat. */
public final class FlipCommands {
    private FlipCommands() {}

    private static int perform(ServerPlayer player, FlipMovementController.Direction direction,
                               net.minecraft.commands.CommandSourceStack source) {
        if (!FlipMobilityRuntime.perform(player, direction)) {
            source.sendFailure(Component.literal("Flip is cooling down or unavailable while riding."));
            return 0;
        }
        String label = switch (direction) {
            case FRONT -> "front flip";
            case BACK -> "back flip";
            case LEFT -> "left side flip";
            case RIGHT -> "right side flip";
        };
        source.sendSuccess(() -> Component.literal("You perform a " + label + "."), false);
        return 1;
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registries, environment) ->
            dispatcher.register(literal("majorasblock")
                .then(literal("flip")
                    .then(literal("front").executes(c -> perform(c.getSource().getPlayerOrException(),
                        FlipMovementController.Direction.FRONT, c.getSource())))
                    .then(literal("back").executes(c -> perform(c.getSource().getPlayerOrException(),
                        FlipMovementController.Direction.BACK, c.getSource())))
                    .then(literal("left").executes(c -> perform(c.getSource().getPlayerOrException(),
                        FlipMovementController.Direction.LEFT, c.getSource())))
                    .then(literal("right").executes(c -> perform(c.getSource().getPlayerOrException(),
                        FlipMovementController.Direction.RIGHT, c.getSource()))))
            )
        );
    }
}
