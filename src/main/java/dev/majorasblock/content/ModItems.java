package dev.majorasblock.content;

import dev.majorasblock.MajorasBlock;
import dev.majorasblock.items.ClockworkHourglassItem;
import dev.majorasblock.items.OcarinaItem;
import dev.majorasblock.items.SpiritCharmItem;
import dev.majorasblock.items.MaskItem;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** All registered items for the adventure, masks, relics, and the custom tab. */
public final class ModItems {
    public static final Item OCARINA = registerOcarina();
    public static final Item CLOCKWORK_HOURGLASS = registerHourglass();

    public static final Item SPIRIT_OF_MOTION = registerSpiritCharm("spirit_of_motion_charm", MobEffects.SPEED);
    public static final Item SPIRIT_OF_MOONLIGHT = registerSpiritCharm("spirit_of_moonlight_charm", MobEffects.RESISTANCE);
    public static final Item SPIRIT_OF_TIDES = registerSpiritCharm("spirit_of_tides_charm", MobEffects.WATER_BREATHING);
    public static final Item SPIRIT_OF_STONE = registerSpiritCharm("spirit_of_stone_charm", MobEffects.JUMP_BOOST);
    public static final Item SPIRIT_OF_ECHOES = registerSpiritCharm("spirit_of_echoes_charm", MobEffects.NIGHT_VISION);
    public static final Item SPIRIT_OF_SUNRISE = registerSpiritCharm("spirit_of_sunrise_charm", MobEffects.FIRE_RESISTANCE);
    public static final Item SPIRIT_OF_WIND = registerSpiritCharm("spirit_of_wind_charm", MobEffects.SLOW_FALLING);
    public static final Item SPIRIT_OF_STILLNESS = registerSpiritCharm("spirit_of_stillness_charm", MobEffects.REGENERATION);

    public static final Map<String, Item> MASK_ITEMS = registerMasks();
    public static final Map<String, Item> RELIC_ITEMS = registerRelics();

    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(MajorasBlock.MOD_ID, "adventure")
    );

    public static final CreativeModeTab CREATIVE_TAB = FabricCreativeModeTab.builder()
            .title(net.minecraft.network.chat.Component.translatable("itemGroup.majorasblock"))
            .icon(() -> new ItemStack(OCARINA))
            .displayItems((parameters, output) -> {
                output.accept(OCARINA);
                output.accept(CLOCKWORK_HOURGLASS);
                output.accept(SPIRIT_OF_MOTION);
                output.accept(SPIRIT_OF_MOONLIGHT);
                output.accept(SPIRIT_OF_TIDES);
                output.accept(SPIRIT_OF_STONE);
                output.accept(SPIRIT_OF_ECHOES);
                output.accept(SPIRIT_OF_SUNRISE);
                output.accept(SPIRIT_OF_WIND);
                output.accept(SPIRIT_OF_STILLNESS);
                MASK_ITEMS.values().forEach(output::accept);
                RELIC_ITEMS.values().forEach(output::accept);
            })
            .build();

    private ModItems() {}

    private static Item registerOcarina() {
        ResourceKey<Item> key = itemKey("ocarina");
        return Registry.register(BuiltInRegistries.ITEM, key,
                new OcarinaItem(new Item.Properties().stacksTo(1).setId(key)));
    }

    private static Item registerHourglass() {
        ResourceKey<Item> key = itemKey("clockwork_hourglass");
        return Registry.register(BuiltInRegistries.ITEM, key,
                new ClockworkHourglassItem(new Item.Properties().stacksTo(1).setId(key)));
    }

    private static Item registerSpiritCharm(String id, Holder<MobEffect> effect) {
        ResourceKey<Item> key = itemKey(id);
        return Registry.register(BuiltInRegistries.ITEM, key,
                new SpiritCharmItem(new Item.Properties().stacksTo(16).setId(key), effect));
    }

    private static Map<String, Item> registerMasks() {
        Map<String, Item> items = new LinkedHashMap<>();
        for (MaskCatalog.Mask mask : MaskCatalog.all()) {
            ResourceKey<Item> key = itemKey(mask.id());
            Item item = Registry.register(BuiltInRegistries.ITEM, key,
                    new MaskItem(new Item.Properties().stacksTo(1).setId(key), mask));
            items.put(mask.id(), item);
        }
        return Collections.unmodifiableMap(items);
    }

    private static Map<String, Item> registerRelics() {
        Map<String, Item> items = new LinkedHashMap<>();
        for (RelicCatalog.Relic relic : RelicCatalog.all()) {
            ResourceKey<Item> key = itemKey(relic.id());
            Item item = Registry.register(BuiltInRegistries.ITEM, key,
                    new MaskItem(new Item.Properties().stacksTo(1).setId(key), relic));
            items.put(relic.id(), item);
        }
        return Collections.unmodifiableMap(items);
    }

    private static ResourceKey<Item> itemKey(String path) {
        return ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(MajorasBlock.MOD_ID, path));
    }

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CREATIVE_TAB_KEY, CREATIVE_TAB);
        MajorasBlock.LOGGER.info(
                "Registered {} masks, {} relics, eight spirit charms, the Ocarina, and the Clockwork Hourglass",
                MASK_ITEMS.size(), RELIC_ITEMS.size()
        );
    }
}
