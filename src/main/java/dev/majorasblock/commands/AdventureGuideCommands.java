package dev.majorasblock.commands;

import dev.majorasblock.content.DungeonCatalog;
import dev.majorasblock.content.EnemyCatalog;
import dev.majorasblock.deity.DeitySpiritCatalog;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.chat.Component;

import java.util.stream.Collectors;

import static net.minecraft.commands.Commands.literal;

/** Discoverable help and catalog commands so players can find the mod content. */
public final class AdventureGuideCommands {
    private AdventureGuideCommands() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("majorasblock")
                        .then(literal("help").executes(context -> {
                            context.getSource().sendSuccess(() -> Component.literal(
                                    "Majora's Block commands:\n"
                                            + "/majorasblock status\n"
                                            + "/majorasblock drowned off|subtle|uneasy|full\n"
                                            + "/majorasblock friendship accept|deny\n"
                                            + "/majorasblock song friendship [player]\n"
                                            + "/majorasblock song healing\n"
                                            + "/majorasblock song inverted_time|double_time\n"
                                            + "/majorasblock song chosen_hour <0-23> <0-59>\n"
                                            + "/majorasblock dungeon list\n"
                                            + "/majorasblock spirit list\n"
                                            + "Admin: /majorasblock dungeon build <id> and /majorasblock enemy list|spawn <id>."
                            ), false);
                            return 1;
                        }))
                        .then(literal("dungeon").then(literal("list").executes(context -> {
                            String list = DungeonCatalog.all().stream()
                                    .map(dungeon -> dungeon.id() + " — " + dungeon.name())
                                    .collect(Collectors.joining("\n"));
                            context.getSource().sendSuccess(() -> Component.literal("Dungeon catalogue:\n" + list), false);
                            return DungeonCatalog.all().size();
                        })))
                        .then(literal("enemy").then(literal("list").executes(context -> {
                            String list = EnemyCatalog.all().stream()
                                    .map(enemy -> enemy.id() + " — " + enemy.name() + " (" + enemy.threat().name().toLowerCase() + ")")
                                    .collect(Collectors.joining("\n"));
                            context.getSource().sendSuccess(() -> Component.literal("Enemy prototypes:\n" + list), false);
                            return EnemyCatalog.all().size();
                        })))
                        .then(literal("spirit").then(literal("list").executes(context -> {
                            String list = DeitySpiritCatalog.all().stream()
                                    .map(spirit -> spirit.id() + " — " + spirit.name() + ": " + spirit.description())
                                    .collect(Collectors.joining("\n"));
                            context.getSource().sendSuccess(() -> Component.literal("Spirit catalogue:\n" + list), false);
                            return DeitySpiritCatalog.all().size();
                        })))
                )
        );
    }
}
