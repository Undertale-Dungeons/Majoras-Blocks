package dev.majorasblock.content;

import dev.majorasblock.MajorasBlock;
import dev.majorasblock.items.OcarinaItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

/** Registered inventory items for the first equipment implementation pass. */
public final class ModItems {
    public static final Item HERO_SWORD = register("hero_sword",
            new Item.Properties().durability(420));
    public static final Item RAZOR_SWORD = register("razor_sword",
            new Item.Properties().durability(100));
    public static final Item GILDED_SWORD = register("gilded_sword",
            new Item.Properties().durability(900));
    public static final Item HERO_SHIELD = register("hero_shield",
            new Item.Properties().equippable(EquipmentSlot.OFFHAND).stacksTo(1));
    public static final Item MIRROR_SHIELD = register("mirror_shield",
            new Item.Properties().equippable(EquipmentSlot.OFFHAND).stacksTo(1));
    public static final Item SHEATH = register("sword_sheath",
            new Item.Properties().stacksTo(1));
    public static final Item OCARINA = registerOcarina();

    private ModItems() {}

    private static Item register(String path, Item.Properties properties) {
        Identifier identifier = Identifier.fromNamespaceAndPath(MajorasBlock.MOD_ID, path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        Item item = new Item(properties.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    private static Item registerOcarina() {
        Identifier identifier = Identifier.fromNamespaceAndPath(MajorasBlock.MOD_ID, "ocarina");
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        return Registry.register(BuiltInRegistries.ITEM, key,
                new OcarinaItem(new Item.Properties().stacksTo(1).setId(key)));
    }

    public static void register() {
        MajorasBlock.LOGGER.info("Registered {} Majora's Block equipment items", 7);
    }
}
