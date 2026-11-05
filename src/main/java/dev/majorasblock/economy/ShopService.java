package dev.majorasblock.economy;

import dev.majorasblock.content.ModItems;
import dev.majorasblock.dungeon.LandmarkBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Clock Town's rupee shops. All offers are exploration, display, or utility goods. */
public final class ShopService {
    public record Offer(String id, String name, String shopId, Item item, int price) {}

    public enum PurchaseResult {
        BOUGHT, UNKNOWN_OFFER, WRONG_SHOP, NOT_ENOUGH_RUPEES, INVENTORY_FULL, INVALID_AMOUNT
    }

    private static final Map<String, Offer> OFFERS = Map.ofEntries(
            Map.entry("shield", new Offer("shield", "Shield", "clocktown_market", Items.SHIELD, 75)),
            Map.entry("bread", new Offer("bread", "Bread", "clocktown_market", Items.BREAD, 5)),
            Map.entry("torch", new Offer("torch", "Torch", "clocktown_market", Items.TORCH, 3)),
            Map.entry("lantern", new Offer("lantern", "Lantern", "clocktown_market", Items.LANTERN, 14)),
            Map.entry("stick", new Offer("stick", "Kindling Sticks", "clocktown_market", Items.STICK, 2)),
            Map.entry("deku_nut", new Offer("deku_nut", "Deku Nut", "clocktown_market", ModItems.DEKU_NUT, 8)),

            Map.entry("deku_photo", new Offer("deku_photo", "Deku Photograph", "curio_shop", ModItems.DEKU_PHOTOGRAPH, 25)),
            Map.entry("golden_dust", new Offer("golden_dust", "Golden Dust", "curio_shop", ModItems.GOLDEN_DUST, 55)),
            Map.entry("odolwa_portrait", new Offer("odolwa_portrait", "Odolwa Portrait", "curio_shop", ModItems.ODOLWA_PORTRAIT, 35)),

            Map.entry("glowstone", new Offer("glowstone", "Glowstone Dust", "spark_shop", Items.GLOWSTONE_DUST, 9)),
            Map.entry("redstone", new Offer("redstone", "Redstone Dust", "spark_shop", Items.REDSTONE, 8)),
            Map.entry("candle", new Offer("candle", "Candle", "spark_shop", Items.CANDLE, 5)),
            Map.entry("soul_lantern", new Offer("soul_lantern", "Soul Lantern", "spark_shop", Items.SOUL_LANTERN, 18)),
            Map.entry("campfire", new Offer("campfire", "Campfire", "spark_shop", Items.CAMPFIRE, 22)),

            Map.entry("decorated_pot", new Offer("decorated_pot", "Decorated Pot", "pottery_shop", Items.DECORATED_POT, 12)),
            Map.entry("flower_pot", new Offer("flower_pot", "Flower Pot", "pottery_shop", Items.FLOWER_POT, 7)),
            Map.entry("clay", new Offer("clay", "Clay Ball", "pottery_shop", Items.CLAY_BALL, 2)),
            Map.entry("brick", new Offer("brick", "Brick", "pottery_shop", Items.BRICK, 2)),

            Map.entry("spirit_shard", new Offer("spirit_shard", "Spirit Shard", "spirit_shop", ModItems.SPIRIT_SHARD, 45)),
            Map.entry("moonlight_charm", new Offer("moonlight_charm", "Spirit Charm of Moonlight", "spirit_shop", ModItems.SPIRIT_OF_MOONLIGHT, 65)),
            Map.entry("tide_charm", new Offer("tide_charm", "Spirit Charm of Tides", "spirit_shop", ModItems.SPIRIT_OF_TIDES, 65))
    );

    private ShopService() {}

    public static List<Offer> offers() {
        return OFFERS.values().stream().sorted((a, b) -> a.id().compareTo(b.id())).toList();
    }

    public static Optional<Offer> offer(String id) {
        return Optional.ofNullable(OFFERS.get(id));
    }

    public static PurchaseResult buy(ServerPlayer player, String offerId, int amount) {
        if (amount < 1 || amount > 16) return PurchaseResult.INVALID_AMOUNT;
        Offer offer = OFFERS.get(offerId);
        if (offer == null) return PurchaseResult.UNKNOWN_OFFER;
        if (!(player.level() instanceof ServerLevel level)
                || !LandmarkBuilder.isNearLandmark(level, player.blockPosition(), offer.shopId(), 12)) {
            return PurchaseResult.WRONG_SHOP;
        }

        int totalCost = Math.multiplyExact(offer.price(), amount);
        if (BlacksmithService.countRupees(player) < totalCost) return PurchaseResult.NOT_ENOUGH_RUPEES;

        ItemStack goods = new ItemStack(offer.item(), amount);
        if (!player.getInventory().add(goods)) return PurchaseResult.INVENTORY_FULL;
        if (!BlacksmithService.spendRupees(player, totalCost)) {
            player.drop(goods, false);
            return PurchaseResult.NOT_ENOUGH_RUPEES;
        }
        return PurchaseResult.BOUGHT;
    }
}
