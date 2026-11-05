package dev.majorasblock.economy;

import dev.majorasblock.content.ModItems;
import dev.majorasblock.dungeon.LandmarkBuilder;
import dev.majorasblock.player.FormRuntime;
import dev.majorasblock.time.ThreeDayCycle;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session-scoped blacksmith orders. A finished heirloom is a collectible/display
 * item, not a weapon and has no attack or damage behavior.
 */
public final class BlacksmithService {
    public static final int PRICE_RUPEES = 100;

    public enum Blueprint {
        HERO("hero", "Hero's Sword", ModItems.HERO_SWORD, FormRuntime.Form.NONE),
        DEKU("deku", "Deku Sword", ModItems.DEKU_SWORD, FormRuntime.Form.DEKU),
        GORON("goron", "Goron Sword", ModItems.GORON_SWORD, FormRuntime.Form.GORON),
        ZORA("zora", "Zora Sword", ModItems.ZORA_SWORD, FormRuntime.Form.ZORA);

        private final String id;
        private final String displayName;
        private final Item item;
        private final FormRuntime.Form requiredForm;

        Blueprint(String id, String displayName, Item item, FormRuntime.Form requiredForm) {
            this.id = id;
            this.displayName = displayName;
            this.item = item;
            this.requiredForm = requiredForm;
        }

        public String id() { return id; }
        public String displayName() { return displayName; }
        public Item item() { return item; }
        public FormRuntime.Form requiredForm() { return requiredForm; }

        public static Optional<Blueprint> byId(String id) {
            if (id == null) return Optional.empty();
            for (Blueprint blueprint : values()) {
                if (blueprint.id.equalsIgnoreCase(id)) return Optional.of(blueprint);
            }
            return Optional.empty();
        }
    }

    public enum OrderResult {
        ORDERED,
        UNKNOWN_BLUEPRINT,
        NOT_AT_FORGE,
        ALREADY_ORDERED,
        WRONG_FORM,
        NOT_ENOUGH_RUPEES,
        CLOCK_UNAVAILABLE
    }

    public enum CollectResult {
        COLLECTED,
        NO_ORDER,
        NOT_READY,
        NOT_AT_FORGE,
        INVENTORY_FULL,
        CLOCK_UNAVAILABLE
    }

    public record OrderStatus(Blueprint blueprint, long ticksRemaining, boolean ready) {}

    private record Order(Blueprint blueprint, long completionTick) {}

    private static final Map<UUID, Order> ORDERS = new ConcurrentHashMap<>();

    private BlacksmithService() {}

    public static OrderResult order(ServerPlayer player, MinecraftServer server, String blueprintId) {
        Optional<Blueprint> match = Blueprint.byId(blueprintId);
        if (match.isEmpty()) return OrderResult.UNKNOWN_BLUEPRINT;

        if (!(player.level() instanceof ServerLevel level)
                || !LandmarkBuilder.isNearBlacksmith(level, player.blockPosition(), 12)) {
            return OrderResult.NOT_AT_FORGE;
        }

        if (ORDERS.containsKey(player.getUUID())) return OrderResult.ALREADY_ORDERED;

        Blueprint blueprint = match.get();
        if (blueprint.requiredForm() != FormRuntime.Form.NONE
                && FormRuntime.active(player.getUUID()) != blueprint.requiredForm()) {
            return OrderResult.WRONG_FORM;
        }

        if (countRupees(player) < PRICE_RUPEES) return OrderResult.NOT_ENOUGH_RUPEES;

        Optional<Long> now = currentClockTick(server);
        if (now.isEmpty()) return OrderResult.CLOCK_UNAVAILABLE;

        removeRupees(player, PRICE_RUPEES);
        ORDERS.put(player.getUUID(), new Order(blueprint, now.get() + ThreeDayCycle.TICKS_PER_DAY));
        return OrderResult.ORDERED;
    }

    public static Optional<OrderStatus> status(ServerPlayer player, MinecraftServer server) {
        Order order = ORDERS.get(player.getUUID());
        if (order == null) return Optional.empty();

        Optional<Long> now = currentClockTick(server);
        if (now.isEmpty()) return Optional.empty();

        long remaining = Math.max(0L, order.completionTick() - now.get());
        return Optional.of(new OrderStatus(order.blueprint(), remaining, remaining == 0L));
    }

    public static CollectResult collect(ServerPlayer player, MinecraftServer server) {
        Order order = ORDERS.get(player.getUUID());
        if (order == null) return CollectResult.NO_ORDER;

        if (!(player.level() instanceof ServerLevel level)
                || !LandmarkBuilder.isNearBlacksmith(level, player.blockPosition(), 12)) {
            return CollectResult.NOT_AT_FORGE;
        }

        Optional<Long> now = currentClockTick(server);
        if (now.isEmpty()) return CollectResult.CLOCK_UNAVAILABLE;
        if (now.get() < order.completionTick()) return CollectResult.NOT_READY;

        ItemStack reward = new ItemStack(order.blueprint().item());
        if (!player.getInventory().add(reward)) return CollectResult.INVENTORY_FULL;

        ORDERS.remove(player.getUUID());
        return CollectResult.COLLECTED;
    }

    public static int countRupees(ServerPlayer player) {
        int total = 0;
        var inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.is(ModItems.RUPEE)) total += stack.getCount();
        }
        return total;
    }

    public static boolean grantRupees(ServerPlayer player, int amount) {
        if (amount <= 0) return false;
        ItemStack stack = new ItemStack(ModItems.RUPEE, amount);
        return player.getInventory().add(stack);
    }

    private static void removeRupees(ServerPlayer player, int amount) {
        int remaining = amount;
        var inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize() && remaining > 0; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.is(ModItems.RUPEE)) continue;
            int taken = Math.min(remaining, stack.getCount());
            stack.shrink(taken);
            remaining -= taken;
        }
        if (remaining != 0) throw new IllegalStateException("Rupee balance changed during blacksmith order.");
    }

    private static Optional<Long> currentClockTick(MinecraftServer server) {
        Optional<Holder<WorldClock>> clock = server.overworld().dimensionType().defaultClock();
        return clock.map(server.clockManager()::getTotalTicks);
    }
}
