package dev.majorasblock;

import dev.majorasblock.commands.AdventureCommands;
import dev.majorasblock.commands.AdventureGuideCommands;
import dev.majorasblock.commands.CollectionCommands;
import dev.majorasblock.commands.WorldContentCommands;
import dev.majorasblock.commands.AdventureSystemsCommands;
import dev.majorasblock.commands.FlipCommands;
import dev.majorasblock.commands.EconomyCommands;
import dev.majorasblock.commands.TownCommands;
import dev.majorasblock.commands.BlackKnightTrialCommands;
import dev.majorasblock.economy.PersistentRewardStore;
import dev.majorasblock.economy.PotLootRuntime;
import dev.majorasblock.player.FormRuntime;
import dev.majorasblock.player.LedgeHangRuntime;
import dev.majorasblock.time.ThreeDayCycleRuntime;
import dev.majorasblock.content.DungeonCatalog;
import dev.majorasblock.content.CatalogIntegrity;
import dev.majorasblock.content.EnemyCatalog;
import dev.majorasblock.content.ModItems;
import dev.majorasblock.music.OriginalSongPlaybackController;
import dev.majorasblock.time.AdventureTimeRuntime;
import dev.majorasblock.horror.DrownedRuntime;
import dev.majorasblock.content.SongCatalog;
import dev.majorasblock.deity.DeitySpiritCatalog;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MajorasBlock implements ModInitializer {
    public static final String MOD_ID = "majorasblock";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        CatalogIntegrity.validate();
        ModItems.register();
        OriginalSongPlaybackController.register();
        AdventureTimeRuntime.register();
        DrownedRuntime.register();
        FormRuntime.register();
        ThreeDayCycleRuntime.register();
        AdventureCommands.register();
        AdventureGuideCommands.register();
        CollectionCommands.register();
        WorldContentCommands.register();
        AdventureSystemsCommands.register();
        FlipCommands.register();
        LedgeHangRuntime.register();
        EconomyCommands.register();
        TownCommands.register();
        BlackKnightTrialCommands.register();
        PersistentRewardStore.register();
        PotLootRuntime.register();

        LOGGER.info("Majora's Block initialized: {} masks, {} relics, {} dungeons, {} bosses, {} enemy concepts, {} ambient creatures, {} songs, {} spirits",
                dev.majorasblock.content.MaskCatalog.all().size(),
                dev.majorasblock.content.RelicCatalog.all().size(),
                DungeonCatalog.all().size(),
                dev.majorasblock.content.BossCatalog.all().size(),
                EnemyCatalog.all().size(),
                dev.majorasblock.content.WanderingCreatureCatalog.all().size(),
                SongCatalog.all().size(),
                DeitySpiritCatalog.all().size());
    }
}
