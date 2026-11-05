package dev.majorasblock.economy;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import dev.majorasblock.MajorasBlock;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.UUID;

/**
 * World-persistent one-time rewards. Boss rewards are global per world so a
 * memorial command cannot be repeated to farm extra crystals.
 */
public final class PersistentRewardStore {
    private static final Properties DATA = new Properties();
    private static Path file;

    private PersistentRewardStore() {}

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(PersistentRewardStore::load);
        ServerLifecycleEvents.SERVER_STOPPING.register(PersistentRewardStore::save);
    }

    private static synchronized void load(MinecraftServer server) {
        DATA.clear();
        file = server.getWorldPath(LevelResource.ROOT)
                .resolve("data")
                .resolve("majorasblock_rewards.properties");
        if (!Files.exists(file)) return;
        try (InputStream input = Files.newInputStream(file)) {
            DATA.load(input);
        } catch (IOException exception) {
            MajorasBlock.LOGGER.error("Could not load Majora's Block reward history.", exception);
        }
    }

    private static synchronized void save(MinecraftServer server) {
        if (file == null) {
            file = server.getWorldPath(LevelResource.ROOT)
                    .resolve("data")
                    .resolve("majorasblock_rewards.properties");
        }
        try {
            Files.createDirectories(file.getParent());
            try (OutputStream output = Files.newOutputStream(file)) {
                DATA.store(output, "Majora's Block one-time treasure and boss rewards");
            }
        } catch (IOException exception) {
            server.getLogger().error("Could not save Majora's Block reward history.", exception);
        }
    }

    public static synchronized boolean claimLocation(
            MinecraftServer server, UUID playerId, String locationId) {
        String key = "player." + playerId + ".location." + locationId;
        if (Boolean.parseBoolean(DATA.getProperty(key, "false"))) return false;
        DATA.setProperty(key, "true");
        save(server);
        return true;
    }

    public static synchronized boolean claimBoss(MinecraftServer server, String bossId) {
        String key = "world.boss." + bossId;
        if (Boolean.parseBoolean(DATA.getProperty(key, "false"))) return false;
        DATA.setProperty(key, "true");
        save(server);
        return true;
    }

    public static synchronized boolean hasClaimedBoss(String bossId) {
        return Boolean.parseBoolean(DATA.getProperty("world.boss." + bossId, "false"));
    }
}
