package dev.majorasblock.deity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/** Per-player spirit collection model. Snapshot ids can be stored by the save layer. */
public final class DeityProgression {
    private final Set<String> collectedSpiritIds = new HashSet<>();

    public synchronized Optional<DeitySpiritCatalog.Power> collect(String spiritId) {
        DeitySpiritCatalog.Spirit spirit = DeitySpiritCatalog.byId(spiritId);
        if (spirit == null || !collectedSpiritIds.add(spiritId)) {
            return Optional.empty();
        }
        return Optional.of(spirit.power());
    }

    public synchronized boolean hasSpirit(String spiritId) {
        return collectedSpiritIds.contains(spiritId);
    }

    public synchronized Set<String> snapshot() {
        return Collections.unmodifiableSet(new HashSet<>(collectedSpiritIds));
    }

    public synchronized void loadSnapshot(Set<String> spiritIds) {
        collectedSpiritIds.clear();
        if (spiritIds == null) {
            return;
        }
        for (String spiritId : spiritIds) {
            if (DeitySpiritCatalog.byId(spiritId) != null) {
                collectedSpiritIds.add(spiritId);
            }
        }
    }
}
