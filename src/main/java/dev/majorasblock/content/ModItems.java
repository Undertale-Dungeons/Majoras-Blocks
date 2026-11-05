package dev.majorasblock.content;

import dev.majorasblock.MajorasBlock;
import dev.majorasblock.items.OcarinaItem;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.BlocksAttacks;

import java.util.List;
import java.util.Optional;

/** Registered inventory items for the first functional equipment pass. */
public final class ModItems {
    public static final Item HERO_SWORD = register("hero_sword",
            new Item.Properties().sword(ToolMaterial.IRON, 3.0f, -2.4f).durability(420));
    public static final Item RAZOR_SWORD = register("razor_sword",
            new Item.Properties().sword(ToolMaterial.IRON, 4.0f, -2.4f).durability(100));
    public static final Item GILDED_SWORD = register("gilded_sword",
            new Item.Properties().sword(ToolMaterial.DIAMOND, 5.0f, -2.4f).durability(900));
    public static final Item HERO_SHIELD = register("hero_shield",
            shieldProperties(420, 100.0f));
    public static final Item MIRROR_SHIELD = register("mirror_shield",
            shieldProperties(560, 120.0f));
    public static final Item SHEATH = register("sword_sheath",
            new Item.Properties().stacksTo(1));
    public static final Item OCARINA = registerOcarina();

    private ModItems() {}

    private static Item.Properties shieldProperties(int durability, float blockingAngle) {
        BlocksAttacks blocksAttacks = new BlocksAttacks(
                0.2f,
                0.5f,
                List.of(new BlocksAttacks.DamageReduction(
                        blockingAngle, Optional.empty(), 0.0f, 1.0f)),
                new BlocksAttacks.ItemDamageFunction(1.0f, 1.0f, 0.0f),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
        return new Item.Properties()
                .durability(durability)
                .equippable(EquipmentSlot.OFFHAND)
                .component(DataComponents.BLOCKS_ATTACKS, blocksAttacks);
    }

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
