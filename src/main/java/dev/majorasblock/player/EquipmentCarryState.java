package dev.majorasblock.player;

import dev.majorasblock.content.ShieldCatalog;
import dev.majorasblock.content.WeaponCatalog;

/**
 * Per-player carry state for back-mounted weapons and shields.
 * The render layer reads this state to swap between sheath/back and hand poses.
 */
public final class EquipmentCarryState {
    public enum Attachment { BACK_SHEATH, BACK_MOUNT, RIGHT_HAND, LEFT_ARM, NONE }

    private String activeWeaponId = "hero_sword";
    private String activeShieldId = "hero_shield";
    private boolean weaponDrawn;
    private boolean shieldRaised;

    public synchronized String activeWeaponId() {
        return activeWeaponId;
    }

    public synchronized String activeShieldId() {
        return activeShieldId;
    }

    public synchronized boolean weaponDrawn() {
        return weaponDrawn;
    }

    public synchronized boolean shieldRaised() {
        return shieldRaised;
    }

    public synchronized void equipWeapon(String weaponId) {
        WeaponCatalog.require(weaponId);
        activeWeaponId = weaponId;
        weaponDrawn = false;
    }

    public synchronized void equipShield(String shieldId) {
        ShieldCatalog.byId(shieldId).orElseThrow(
                () -> new IllegalArgumentException("Unknown shield: " + shieldId));
        activeShieldId = shieldId;
        shieldRaised = false;
    }

    public synchronized boolean drawWeapon() {
        WeaponCatalog.Weapon weapon = WeaponCatalog.require(activeWeaponId);
        if (weapon.weaponClass() == WeaponCatalog.Class.TOOL
                || weapon.weaponClass() == WeaponCatalog.Class.RANGED
                || weapon.canBeSheathed()) {
            weaponDrawn = true;
            return true;
        }
        return false;
    }

    public synchronized void sheatheWeapon() {
        weaponDrawn = false;
    }

    public synchronized void toggleWeapon() {
        if (weaponDrawn) {
            sheatheWeapon();
        } else {
            drawWeapon();
        }
    }

    public synchronized void raiseShield() {
        shieldRaised = activeShieldId != null;
    }

    public synchronized void lowerShield() {
        shieldRaised = false;
    }

    public synchronized void toggleShield() {
        shieldRaised = !shieldRaised && activeShieldId != null;
    }

    public synchronized Attachment weaponAttachment() {
        if (activeWeaponId == null) {
            return Attachment.NONE;
        }
        if (weaponDrawn) {
            return Attachment.RIGHT_HAND;
        }
        WeaponCatalog.Weapon weapon = WeaponCatalog.require(activeWeaponId);
        return weapon.weaponClass() == WeaponCatalog.Class.TWO_HANDED_SWORD
                ? Attachment.BACK_MOUNT : Attachment.BACK_SHEATH;
    }

    public synchronized Attachment shieldAttachment() {
        if (activeShieldId == null) {
            return Attachment.NONE;
        }
        return shieldRaised ? Attachment.LEFT_ARM : Attachment.BACK_MOUNT;
    }

    public synchronized Snapshot snapshot() {
        return new Snapshot(activeWeaponId, activeShieldId, weaponDrawn, shieldRaised);
    }

    public synchronized void load(Snapshot snapshot) {
        if (snapshot == null) {
            return;
        }
        WeaponCatalog.byId(snapshot.weaponId()).ifPresent(weapon -> activeWeaponId = weapon.id());
        ShieldCatalog.byId(snapshot.shieldId()).ifPresent(shield -> activeShieldId = shield.id());
        weaponDrawn = snapshot.weaponDrawn() && activeWeaponId != null;
        shieldRaised = snapshot.shieldRaised() && activeShieldId != null;
    }

    public record Snapshot(String weaponId, String shieldId,
                           boolean weaponDrawn, boolean shieldRaised) {}
}
