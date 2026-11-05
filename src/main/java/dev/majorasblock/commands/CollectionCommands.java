package dev.majorasblock.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.majorasblock.content.MaskCatalog;
import dev.majorasblock.content.ModItems;
import dev.majorasblock.content.RelicCatalog;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Collectors;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/** Discovery and self-grant commands for the complete collectible catalogue. */
public final class CollectionCommands {
    private CollectionCommands(){}
    public static void register(){
        CommandRegistrationCallback.EVENT.register((dispatcher,registries,environment)->
            dispatcher.register(literal("majorasblock")
                .then(literal("mask")
                    .then(literal("list").executes(context->{
                        String list=MaskCatalog.all().stream().map(m->m.id()+" — "+m.name()).collect(Collectors.joining("\n"));
                        context.getSource().sendSuccess(()->Component.literal("Mask collection ("+MaskCatalog.all().size()+"):\n"+list),false);
                        return MaskCatalog.all().size();
                    }))
                    .then(literal("grant").then(argument("id",StringArgumentType.word()).executes(context->{
                        String id=StringArgumentType.getString(context,"id"); Item item=ModItems.MASK_ITEMS.get(id);
                        if(item==null){context.getSource().sendFailure(Component.literal("Unknown mask id: "+id));return 0;}
                        var player=context.getSource().getPlayerOrException();
                        if(!player.getInventory().add(new ItemStack(item))){context.getSource().sendFailure(Component.literal("Your inventory is full."));return 0;}
                        context.getSource().sendSuccess(()->Component.literal("Added "+MaskCatalog.byId(id).name()+"."),false);return 1;
                    }))))
                .then(literal("relic")
                    .then(literal("list").executes(context->{
                        String list=RelicCatalog.all().stream().map(r->r.id()+" — "+r.name()).collect(Collectors.joining("\n"));
                        context.getSource().sendSuccess(()->Component.literal("Adventure gear ("+RelicCatalog.all().size()+"):\n"+list),false);
                        return RelicCatalog.all().size();
                    }))
                    .then(literal("grant").then(argument("id",StringArgumentType.word()).executes(context->{
                        String id=StringArgumentType.getString(context,"id"); Item item=ModItems.RELIC_ITEMS.get(id);
                        if(item==null){context.getSource().sendFailure(Component.literal("Unknown relic id: "+id));return 0;}
                        var player=context.getSource().getPlayerOrException();
                        if(!player.getInventory().add(new ItemStack(item))){context.getSource().sendFailure(Component.literal("Your inventory is full."));return 0;}
                        String name=RelicCatalog.all().stream().filter(x->x.id().equals(id)).findFirst().orElseThrow().name();
                        context.getSource().sendSuccess(()->Component.literal("Added "+name+"."),false);return 1;
                    }))))
            )
        );
    }
}
