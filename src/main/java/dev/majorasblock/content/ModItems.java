package dev.majorasblock.content;

import dev.majorasblock.MajorasBlock;
import dev.majorasblock.items.OcarinaItem;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** Registered adventure items and their visible Creative inventory tab. */
public final class ModItems {
    public static final Item OCARINA = registerOcarina();

    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(MajorasBlock.MOD_ID, "adventure")
    );

    public static final CreativeModeTab CREATIVE_TAB = FabricCreativeModeTab.builder()
            .title(Component.translatable("itemGroup.majorasblock"))
            .icon(() -> new ItemStack(OCARINA))
            .displayItems((parameters, output) -> output.accept(OCARINA))
            .build();

    private ModItems() {}

    private static Item registerOcarina() {
        Identifier identifier = Identifier.fromNamespaceAndPath(MajorasBlock.MOD_ID, "ocarina");
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        return Registry.register(BuiltInRegistries.ITEM, key,
                new OcarinaItem(new Item.Properties().stacksTo(1).setId(key)));
    }

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CREATIVE_TAB_KEY, CREATIVE_TAB);
        MajorasBlock.LOGGER.info("Registered the Ocarina and Majora's Block Creative tab");
    }
}
