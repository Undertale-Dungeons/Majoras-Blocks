package dev.majorasblock.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

/**
 * Client-only Z-style focus camera.
 *
 * Focus can be acquired whenever the player is in the world. Candidates must
 * be alive, visible, and within 64 blocks. This controller changes view only;
 * it does not implement weapon attacks, damage, or hit handling.
 */
public final class MajorasBlockClient implements ClientModInitializer {
    private static final double LOCK_RANGE = 64.0;
    private static final double LOCK_RANGE_SQUARED = LOCK_RANGE * LOCK_RANGE;
    private static final float MAX_TURN_PER_TICK = 14.0F;

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("majorasblock", "controls"));

    private static final KeyMapping FOCUS_KEY = KeyBindingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.majorasblock.focus",
                    InputConstants.Type.KEYSYM,
                    InputConstants.KEY_R,
                    CATEGORY
            ));

    private static final KeyMapping NEXT_TARGET_KEY = KeyBindingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.majorasblock.focus_next",
                    InputConstants.Type.KEYSYM,
                    InputConstants.KEY_V,
                    CATEGORY
            ));

    private static LivingEntity focusedEntity;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(MajorasBlockClient::tick);
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.HOTBAR,
                Identifier.fromNamespaceAndPath("majorasblock", "focus_status"),
                MajorasBlockClient::renderFocusStatus
        );
    }

    private static void tick(Minecraft client) {
        if (client.player == null || client.level == null || client.gui.screen() != null) {
            focusedEntity = null;
            return;
        }

        while (FOCUS_KEY.consumeClick()) {
            focusedEntity = focusedEntity == null ? findFocusTarget(client, null) : null;
        }

        while (NEXT_TARGET_KEY.consumeClick()) {
            if (focusedEntity != null) focusedEntity = findFocusTarget(client, focusedEntity);
        }

        if (focusedEntity == null) return;

        Player player = client.player;
        if (focusedEntity.isRemoved()
                || !focusedEntity.isAlive()
                || player.distanceToSqr(focusedEntity) > LOCK_RANGE_SQUARED
                || !player.hasLineOfSight(focusedEntity)) {
            focusedEntity = null;
            return;
        }

        rotateToward(player, focusedEntity);
    }

    private static LivingEntity findFocusTarget(Minecraft client, LivingEntity current) {
        Player player = client.player;
        Vec3 look = player.getLookAngle();

        List<Candidate> candidates = client.level.getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(LOCK_RANGE),
                entity -> entity != player
                        && !(entity instanceof Player)
                        && entity.isAlive()
                        && !entity.isRemoved()
                        && !entity.isSpectator()
                        && player.hasLineOfSight(entity)
                        && player.distanceToSqr(entity) <= LOCK_RANGE_SQUARED
        ).stream()
                .map(entity -> new Candidate(entity, score(player, look, entity)))
                .sorted(Comparator.comparingDouble(Candidate::score).reversed())
                .toList();

        if (candidates.isEmpty()) return null;
        if (current == null) return candidates.getFirst().entity();

        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i).entity() == current) {
                return candidates.get((i + 1) % candidates.size()).entity();
            }
        }
        return candidates.getFirst().entity();
    }

    private static double score(Player player, Vec3 look, LivingEntity target) {
        Vec3 toward = target.getEyePosition().subtract(player.getEyePosition());
        double distance = toward.length();
        if (distance < 1.0E-6) return 1.0;
        double alignment = look.dot(toward.scale(1.0 / distance));
        double proximity = 1.0 - (distance / LOCK_RANGE);
        // Aim alignment is preferred, while visible targets behind the player
        // remain eligible and can be selected with the next-target key.
        return alignment * 0.65 + proximity * 0.35;
    }

    private static void rotateToward(Player player, LivingEntity target) {
        Vec3 toward = target.getEyePosition().subtract(player.getEyePosition());
        double horizontalLength = Math.sqrt(toward.x * toward.x + toward.z * toward.z);

        float desiredYaw = (float) (Math.atan2(toward.z, toward.x) * (180.0 / Math.PI)) - 90.0F;
        float desiredPitch = (float) (-(Math.atan2(toward.y, horizontalLength) * (180.0 / Math.PI)));
        desiredPitch = Math.max(-85.0F, Math.min(85.0F, desiredPitch));

        float yawDelta = wrapDegrees(desiredYaw - player.getYRot());
        float pitchDelta = desiredPitch - player.getXRot();

        player.setYRot(player.getYRot() + clamp(yawDelta, -MAX_TURN_PER_TICK, MAX_TURN_PER_TICK));
        player.setXRot(player.getXRot() + clamp(pitchDelta, -MAX_TURN_PER_TICK, MAX_TURN_PER_TICK));
    }

    private static void renderFocusStatus(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();
        LivingEntity target = focusedEntity;
        if (target == null || client.player == null || target.isRemoved()) return;

        String label = "Z TARGET  " + target.getDisplayName().getString()
                + "  " + Math.round(client.player.distanceTo(target)) + "m";
        int x = (client.getWindow().getGuiScaledWidth() - client.font.width(label)) / 2;
        int y = client.getWindow().getGuiScaledHeight() / 2 + 28;

        graphics.fill(x - 5, y - 3, x + client.font.width(label) + 5, y + 12, 0x88000000);
        graphics.text(client.font, label, x, y, 0xFFFFD36E, true);
    }

    private static float wrapDegrees(float value) {
        value %= 360.0F;
        if (value >= 180.0F) value -= 360.0F;
        if (value < -180.0F) value += 360.0F;
        return value;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private record Candidate(LivingEntity entity, double score) {}
}
