package dev.majorasblock;

import dev.majorasblock.commands.AdventureCommands;
import dev.majorasblock.content.DungeonCatalog;
import dev.majorasblock.content.EnemyCatalog;
import dev.majorasblock.content.SongCatalog;
import dev.majorasblock.deity.DeitySpiritCatalog;
import dev.majorasblock.time.AdventureTimeRuntime;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Main entrypoint for Majora's Block. */
public final class MajorasBlock implements ModInitializer {
    public static final String MOD_ID = "majorasblock";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        AdventureTimeRuntime.register();
        AdventureCommands.register();

        LOGGER.info(
                "Majora's Block {} initialised: {} dungeons, {} original enemy types, {} songs, {} deity spirits catalogued",
                MOD_ID,
                DungeonCatalog.all().size(),
                EnemyCatalog.all().size(),
                SongCatalog.all().size(),
                DeitySpiritCatalog.all().size()
        );
    }
}
