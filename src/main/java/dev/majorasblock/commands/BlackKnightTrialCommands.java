package dev.majorasblock.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.majorasblock.content.ModItems;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/**
 * Non-combat constellation trial for the Black Knight story encounter.
 * The Knight is represented by a buildable shrine and narrative entry, not an attack-capable mob.
 */
public final class BlackKnightTrialCommands {
    private static final List<String> STAR_SEQUENCE = List.of("north", "east", "west");
    private static final Map<UUID, Integer> ACTIVE_TRIALS = new ConcurrentHashMap<>();
    private static final Set<UUID> SHARD_CLAIMS = ConcurrentHashMap.newKeySet();

    private BlackKnightTrialCommands() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registries, environment) ->
            dispatcher.register(literal("majorasblock")
                .then(literal("knight")
                    .then(literal("lore").executes(context -> {
                        context.getSource().sendSuccess(() -> Component.literal(
                            "The Black Knight is the trial immediately before Majora.\n"
                            + "Tatl: you see stars\n"
                            + "Tatl: you feel lightheaded\n"
                            + "Tatl: your breath catches in your throat\n"
                            + "Tatl: you felt something hovering close behind your head...\n"
                            + "Tatl: your chest feels tight"
                        ), false);
                        return 1;
                    }))
                    .then(literal("trial").executes(context -> begin(context.getSource().getPlayerOrException())))
                    .then(literal("align")
                        .then(argument("direction", StringArgumentType.word())
                            .executes(context -> align(
                                context.getSource().getPlayerOrException(),
                                StringArgumentType.getString(context, "direction")
                            ))))
                ))
        );
    }

    private static int begin(ServerPlayer player) {
        ACTIVE_TRIALS.put(player.getUUID(), 0);
        player.sendSystemMessage(Component.literal(
            "The Black Knight's white stars begin to turn.\n"
            + "Tatl: you see stars\n"
            + "Tatl: you feel lightheaded\n"
            + "Tatl: your breath catches in your throat\n"
            + "Tatl: you felt something hovering close behind your head...\n"
            + "Tatl: your chest feels tight\n"
            + "Trial clue: face the North Star, turn toward the rising sun, then face where the sun disappears.\n"
            + "Use /majorasblock knight align north, then east, then west."
        ));
        return 1;
    }

    private static int align(ServerPlayer player, String rawDirection) {
        UUID id = player.getUUID();
        Integer step = ACTIVE_TRIALS.get(id);
        if (step == null) {
            player.sendSystemMessage(Component.literal("Begin the Black Knight trial first with /majorasblock knight trial."));
            return 0;
        }

        String direction = rawDirection.toLowerCase(Locale.ROOT);
        if (step >= STAR_SEQUENCE.size() || !STAR_SEQUENCE.get(step).equals(direction)) {
            ACTIVE_TRIALS.put(id, 0);
            player.sendSystemMessage(Component.literal("The stars scatter. The alignment resets. Begin again at north."));
            return 0;
        }

        int next = step + 1;
        if (next < STAR_SEQUENCE.size()) {
            ACTIVE_TRIALS.put(id, next);
            player.sendSystemMessage(Component.literal("The constellation holds. Continue with the next direction."));
            return 1;
        }

        ACTIVE_TRIALS.remove(id);
        player.sendSystemMessage(Component.literal(
            "The stars settle into a white crown. The Black Knight lowers its head and leaves the court. "
            + "A dark shard remains as proof that you passed the trial before Majora."
        ));
        if (SHARD_CLAIMS.add(id)) {
            player.getInventory().add(new ItemStack(ModItems.BLACK_KNIGHT_SHARD));
            player.sendSystemMessage(Component.literal("You received the Black Knight Shard. It is a keepsake, not a combat weapon."));
        } else {
            player.sendSystemMessage(Component.literal("You have already claimed the Black Knight Shard this session."));
        }
        return 1;
    }
}
