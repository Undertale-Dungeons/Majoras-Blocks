package dev.majorasblock;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Main entrypoint for Majora's Block. */
public final class MajorasBlock implements ModInitializer {
    public static final String MOD_ID = "majorasblock";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Majora's Block {} is starting", MOD_ID);
    }
}
