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
                        + "/majorasblock song healing|inverted_time|double_time\n"
                        + "/majorasblock song chosen_hour <0-23> <0-59>\n"
                        + "/majorasblock mask list|grant <id>\n"
                        + "/majorasblock relic list|grant <id>\n"
                        + "/majorasblock dungeon list\n"
                        + "/majorasblock boss list\n"
                        + "/majorasblock spirit list\n"
                        + "/majorasblock creature list|summon <id>\n"
                        + "/majorasblock moon travel|return\n"
                        + "/majorasblock form status|clear\n"
                        + "/majorasblock progress\n"
                        + "/majorasblock rupees balance | grant <amount> (admin)\n"
                        + "/majorasblock smith order hero|deku|goron|zora|razor|gilded\n"
                        + "/majorasblock smith status|collect\n"
                        + "/majorasblock shop list|buy <item> [amount]\n"
                        + "/majorasblock treasure claim spirit_grove|spirit_spring|heart_grove|heart_cave\n"
                        + "/majorasblock boss reward <id> (admin, once per world)\n"
                        + "/majorasblock flip front|back|left|right\n"
                        + "/majorasblock cycle\n"
                        + "/majorasblock spirit claim <id> (near a built Deity Fountain)\n"
                        + "Admin: /majorasblock dungeon build <id>, /majorasblock landmark build <id>, "
                        + "/majorasblock boss memorial <id>, /majorasblock enemy spawn <id>."
                    ), false);
                    return 1;
                }))
                .then(literal("dungeon").then(literal("list").executes(context -> {
                    String list=DungeonCatalog.all().stream().map(d->d.id()+" — "+d.name()).collect(Collectors.joining("\n"));
                    context.getSource().sendSuccess(()->Component.literal("Dungeon catalogue:\n"+list),false);
                    return DungeonCatalog.all().size();
                })))
                .then(literal("enemy").then(literal("list").executes(context -> {
                    String list=EnemyCatalog.all().stream().map(e->e.id()+" — "+e.name()+" ("+e.threat().name().toLowerCase()+")")
                        .collect(Collectors.joining("\n"));
                    context.getSource().sendSuccess(()->Component.literal("Enemy concepts (prototypes):\n"+list),false);
                    return EnemyCatalog.all().size();
                })))
                .then(literal("spirit").then(literal("list").executes(context -> {
                    String list=DeitySpiritCatalog.all().stream().map(s->s.id()+" — "+s.name()+": "+s.description())
                        .collect(Collectors.joining("\n"));
                    context.getSource().sendSuccess(()->Component.literal("Deity spirits:\n"+list),false);
                    return DeitySpiritCatalog.all().size();
                })))
            )
        );
    }
}
