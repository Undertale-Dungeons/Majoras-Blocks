package dev.majorasblock.content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

/** Non-hostile vanilla-animal stand-ins for the original ambient creature roster. */
public final class CreaturePrototypeService {
    public record SpawnResult(boolean success,String message,Entity entity){}
    private CreaturePrototypeService(){}
    public static SpawnResult spawn(ServerLevel level,BlockPos pos,String creatureId){
        if(level==null||pos==null||creatureId==null)return new SpawnResult(false,"Missing level, position, or creature id.",null);
        WanderingCreatureCatalog.Creature creature=WanderingCreatureCatalog.byId(creatureId);
        if(creature==null)return new SpawnResult(false,"Unknown creature id: "+creatureId,null);
        Optional<EntityType<?>> type=BuiltInRegistries.ENTITY_TYPE.getOptional(Identifier.parse("minecraft:"+creature.vanillaType()));
        if(type.isEmpty())return new SpawnResult(false,"Missing vanilla creature type: "+creature.vanillaType(),null);
        Entity entity=type.get().spawn(level,pos,EntitySpawnReason.COMMAND);
        if(entity==null)return new SpawnResult(false,"Could not spawn the creature at this position.",null);
        entity.setCustomName(Component.literal(creature.name()+" (visual prototype)"));
        entity.setCustomNameVisible(true);
        entity.setInvulnerable(true);
        return new SpawnResult(true,creature.name(),entity);
    }
}
