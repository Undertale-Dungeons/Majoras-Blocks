package dev.majorasblock.content;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class AdventureCollectionCatalogTest {
    @Test
    void maskCollectionContainsFiftyPlusUniqueEntriesAndAllThreeForms() {
        assertTrue(MaskCatalog.all().size() >= 50);
        assertEquals(MaskCatalog.all().size(),
                new HashSet<>(MaskCatalog.all().stream().map(MaskCatalog.Mask::id).toList()).size());
        assertNotNull(MaskCatalog.byId("deku_form_mask"));
        assertNotNull(MaskCatalog.byId("goron_form_mask"));
        assertNotNull(MaskCatalog.byId("zora_form_mask"));
        assertNotNull(MaskCatalog.byId("sponge_mask"));
        assertNotNull(MaskCatalog.byId("peabert_mask"));
    }

    @Test
    void relicsAndAmbientCreaturesHaveUniqueEntries() {
        assertTrue(RelicCatalog.all().size() >= 20);
        assertTrue(WanderingCreatureCatalog.all().size() >= 20);
        assertEquals(RelicCatalog.all().size(),
                new HashSet<>(RelicCatalog.all().stream().map(RelicCatalog.Relic::id).toList()).size());
        assertEquals(WanderingCreatureCatalog.all().size(),
                new HashSet<>(WanderingCreatureCatalog.all().stream().map(WanderingCreatureCatalog.Creature::id).toList()).size());
    }

    @Test
    void everyCataloguedCreatureUsesAnExplicitVanillaPrototype() {
        assertTrue(WanderingCreatureCatalog.all().stream()
                .allMatch(creature -> creature.vanillaType() != null && !creature.vanillaType().isBlank()));
    }
}
