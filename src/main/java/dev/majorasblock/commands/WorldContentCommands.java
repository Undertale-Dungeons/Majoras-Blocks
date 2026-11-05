package dev.majorasblock.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.majorasblock.content.BossCatalog;
import dev.majorasblock.content.CreaturePrototypeService;
import dev.majorasblock.content.WanderingCreatureCatalog;
import dev.majorasblock.dungeon.LandmarkBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.Permissions;

import java.util.stream.Collectors;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/** Commands for world landmarks, boss memorials, and harmless creature prototypes. */
public final class WorldContentCommands {
    private WorldContentCommands(){}
    public static void register(){
        CommandRegistrationCallback.EVENT.register((dispatcher,registries,environment)->
            dispatcher.register(literal("majorasblock")
                .then(literal("landmark")
                    .then(literal("list").executes(context->{
                        String list=LandmarkBuilder.ids().stream().collect(Collectors.joining(", "));
                        context.getSource().sendSuccess(()->Component.literal("Landmarks: "+list),false);
                        return LandmarkBuilder.ids().size();
                    }))
                    .then(literal("build").requires(s->s.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                        .then(argument("id",StringArgumentType.word()).executes(context->{
                            var player=context.getSource().getPlayerOrException();
                            var result=LandmarkBuilder.build((ServerLevel)context.getSource().getLevel(),
                                player.blockPosition(),StringArgumentType.getString(context,"id"));
                            context.getSource().sendSuccess(()->Component.literal("Built "+result.name()+" at "
                                +result.origin().toShortString()+" with "+result.blocksPlaced()+" blocks."),true);
                            return 1;
                        }))))
                .then(literal("boss")
                    .then(literal("list").executes(context->{
                        String list=BossCatalog.all().stream().map(b->b.id()+" — "+b.name()+" ("+b.dungeonId()+")")
                            .collect(Collectors.joining("\n"));
                        context.getSource().sendSuccess(()->Component.literal("Boss catalogue:\n"+list),false);
                        return BossCatalog.all().size();
                    }))
                    .then(literal("memorial").requires(s->s.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                        .then(argument("id",StringArgumentType.word()).executes(context->{
                            var player=context.getSource().getPlayerOrException();
                            var result=LandmarkBuilder.buildBossMemorial((ServerLevel)context.getSource().getLevel(),
                                player.blockPosition(),StringArgumentType.getString(context,"id"));
                            context.getSource().sendSuccess(()->Component.literal("Built "+result.name()+" at "
                                +result.origin().toShortString()+" with "+result.blocksPlaced()+" blocks."),true);
                            return 1;
                        }))))
                .then(literal("creature")
                    .then(literal("list").executes(context->{
                        String list=WanderingCreatureCatalog.all().stream()
                            .map(c->c.id()+" — "+c.name()+" ("+c.region()+")")
                            .collect(Collectors.joining("\n"));
                        context.getSource().sendSuccess(()->Component.literal("Ambient creature prototypes:\n"+list),false);
                        return WanderingCreatureCatalog.all().size();
                    }))
                    .then(literal("summon").then(argument("id",StringArgumentType.word()).executes(context->{
                        var player=context.getSource().getPlayerOrException();
                        var pos=player.blockPosition().relative(player.getDirection(),3).above();
                        var result=CreaturePrototypeService.spawn((ServerLevel)context.getSource().getLevel(),
                            pos,StringArgumentType.getString(context,"id"));
                        if(!result.success()){context.getSource().sendFailure(Component.literal(result.message()));return 0;}
                        context.getSource().sendSuccess(()->Component.literal("Spawned "+result.message()
                            +" visual prototype. Custom models are still in development."),false);
                        return 1;
                    }))))
            )
        );
    }
}
