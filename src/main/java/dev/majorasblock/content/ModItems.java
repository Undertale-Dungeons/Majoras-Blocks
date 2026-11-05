package dev.majorasblock.content;

import dev.majorasblock.MajorasBlock;
import dev.majorasblock.items.OcarinaItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/** Registered original adventure instrument. */
public final class ModItems {
    public static final Item OCARINA = registerOcarina();

    private ModItems() {}

    private static Item registerOcarina() {
        Identifier identifier = Identifier.fromNamespaceAndPath(MajorasBlock.MOD_ID, "ocarina");
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        return Registry.register(BuiltInRegistries.ITEM, key,
                new OcarinaItem(new Item.Properties().stacksTo(1).setId(key)));
    }

    public static void register() {
        MajorasBlock.LOGGER.info("Registered the original ocarina item");
    }
}
