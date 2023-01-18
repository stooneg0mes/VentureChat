package mineverse.Aust1n46.chat.database;

import mineverse.Aust1n46.chat.api.MineverseChatAPI;
import mineverse.Aust1n46.chat.api.SynchronizedMineverseChatPlayer;
import mineverse.Aust1n46.chat.command.mute.MuteContainer;
import mineverse.Aust1n46.chat.proxy.VentureChatProxySource;
import mineverse.Aust1n46.chat.utilities.UUIDFetcher;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class for reading and writing proxy player data.
 *
 * @author Aust1n46
 */
public class ProxyPlayerData {
    public static void loadLegacyBungeePlayerData(File dataFolder, VentureChatProxySource source) {
        File sync = new File(dataFolder, "BungeePlayers.yml");
        if (!sync.exists()) {
            return;
        }
        try {
            source.sendConsoleMessage("&8[&eVentureChat&8]&c - Detected Legacy Player Data!");
            source.sendConsoleMessage("&8[&eVentureChat&8]&c - Converting to new structure and deleting old BungeePlayers.yml file!");
            Configuration playerData = ConfigurationProvider.getProvider(YamlConfiguration.class).load(sync);
            for (String uuidString : playerData.getKeys()) {
                UUID uuid = UUID.fromString(uuidString);
                if (UUIDFetcher.shouldSkipOfflineUUIDProxy(uuid, source)) {
                    source.sendConsoleMessage("&8[&eVentureChat&8]&c - Skipping Offline UUID: " + uuid);
                    continue;
                }
                Set<String> listening = new HashSet<String>();
                StringTokenizer l = new StringTokenizer(playerData.getString(uuidString + ".channels"), ",");
                while (l.hasMoreTokens()) {
                    String channel = l.nextToken();
                    listening.add(channel);
                }
                HashMap<String, MuteContainer> mutes = new HashMap<String, MuteContainer>();
                StringTokenizer m = new StringTokenizer(playerData.getString(uuidString + ".mutes"), ",");
                while (m.hasMoreTokens()) {
                    String[] parts = m.nextToken().split(":");
                    String channelName = parts[0];
                    mutes.put(channelName, new MuteContainer(channelName, Long.parseLong(parts[1])));
                }
                HashSet<UUID> ignores = new HashSet<UUID>();
                StringTokenizer n = new StringTokenizer(playerData.getString(uuidString + ".ignores"), ",");
                while (n.hasMoreTokens()) {
                    String ignore = n.nextToken();
                    ignores.add(UUID.fromString(ignore));
                }
                boolean spy = playerData.getBoolean(uuidString + ".spy");
                boolean messageToggle = playerData.getBoolean(uuidString + ".messagetoggle");
                MineverseChatAPI.addSynchronizedMineverseChatPlayerToMap(new SynchronizedMineverseChatPlayer(uuid, listening, mutes, ignores, spy, messageToggle));
            }
        } catch (Exception e) {
            MineverseChatAPI.clearProxyPlayerMap();
            source.sendConsoleMessage("&8[&eVentureChat&8]&c - Error Loading Legacy Player Data!");
            source.sendConsoleMessage("&8[&eVentureChat&8]&c - Deleted BungeePlayers.yml file!");
        } finally {
            sync.delete();
        }
    }

    public static void loadProxyPlayerData(File dataFolder, VentureChatProxySource source) {
        try {
            File playerDataDirectory = dataFolder;
            if (!playerDataDirectory.exists()) {
                playerDataDirectory.mkdirs();
            }
            Files.walk(Paths.get(dataFolder.getAbsolutePath()))
                .filter(Files::isRegularFile)
                .forEach((path) -> readProxyPlayerDataFile(path, source));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readProxyPlayerDataFile(Path path, VentureChatProxySource source) {
        SynchronizedMineverseChatPlayer smcp;
        File proxyPlayerDataFile = path.toFile();
        if (!proxyPlayerDataFile.exists()) {
            return;
        }
        try {
            Configuration proxyPlayerDataFileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(proxyPlayerDataFile);
            String uuidString = proxyPlayerDataFile.getName().replace(".yml", "");
            UUID uuid = UUID.fromString(uuidString);
            if (UUIDFetcher.shouldSkipOfflineUUIDProxy(uuid, source)) {
                source.sendConsoleMessage("&8[&eVentureChat&8]&c - Skipping Offline UUID: " + uuid);
                source.sendConsoleMessage("&8[&eVentureChat&8]&c - File will be skipped and deleted.");
                proxyPlayerDataFile.delete();
                return;
            }
            Set<String> listening = new HashSet<String>();
            StringTokenizer l = new StringTokenizer(proxyPlayerDataFileConfiguration.getString("channels"), ",");
            while (l.hasMoreTokens()) {
                String channel = l.nextToken();
                listening.add(channel);
            }
            HashMap<String, MuteContainer> mutes = new HashMap<String, MuteContainer>();
            Configuration muteSection = proxyPlayerDataFileConfiguration.getSection("mutes");
            for (String channelName : muteSection.getKeys()) {
                Configuration channelSection = muteSection.getSection(channelName);
                mutes.put(channelName, new MuteContainer(channelName, channelSection.getLong("time"), channelSection.getString("reason")));
            }
            HashSet<UUID> ignores = new HashSet<UUID>();
            StringTokenizer n = new StringTokenizer(proxyPlayerDataFileConfiguration.getString("ignores"), ",");
            while (n.hasMoreTokens()) {
                String ignore = n.nextToken();
                ignores.add(UUID.fromString(ignore));
            }
            boolean spy = proxyPlayerDataFileConfiguration.getBoolean("spy");
            boolean messageToggle = proxyPlayerDataFileConfiguration.getBoolean("messagetoggle");
            smcp = new SynchronizedMineverseChatPlayer(uuid, listening, mutes, ignores, spy, messageToggle);
        } catch (Exception e) {
            source.sendConsoleMessage("&8[&eVentureChat&8]&c - Error Loading Data File: " + proxyPlayerDataFile.getName());
            source.sendConsoleMessage("&8[&eVentureChat&8]&c - File will be skipped and deleted.");
            proxyPlayerDataFile.delete();
            return;
        }
        if (smcp != null) {
            MineverseChatAPI.addSynchronizedMineverseChatPlayerToMap(smcp);
        }
    }

    public static void saveProxyPlayerData(File dataFolder, VentureChatProxySource source) {
        try {
            for (SynchronizedMineverseChatPlayer p : MineverseChatAPI.getSynchronizedMineverseChatPlayers()) {
                if (UUIDFetcher.shouldSkipOfflineUUIDProxy(p.getUUID(), source)) {
                    return;
                }
                File proxyPlayerDataFile = new File(dataFolder.getAbsolutePath(), p.getUUID() + ".yml");
                if (!proxyPlayerDataFile.exists()) {
                    proxyPlayerDataFile.createNewFile();
                }
                Configuration proxyPlayerDataFileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(proxyPlayerDataFile);

                String listen = "";
                for (String s : p.getListening())
                    listen += s + ",";
                String ignore = "";
                for (UUID s : p.getIgnores())
                    ignore += s.toString() + ",";
                if (listen.length() > 0)
                    listen = listen.substring(0, listen.length() - 1);
                if (ignore.length() > 0)
                    ignore = ignore.substring(0, ignore.length() - 1);
                proxyPlayerDataFileConfiguration.set("channels", listen);
                Configuration muteSection = createSection(proxyPlayerDataFileConfiguration, "mutes");
                for (MuteContainer mute : p.getMutes()) {
                    Configuration channelSection = createSection(muteSection, mute.getChannel());
                    channelSection.set("time", mute.getDuration());
                    channelSection.set("reason", mute.getReason());
                }
                proxyPlayerDataFileConfiguration.set("ignores", ignore);
                proxyPlayerDataFileConfiguration.set("spy", p.isSpy());
                proxyPlayerDataFileConfiguration.set("messagetoggle", p.getMessageToggle());

                ConfigurationProvider.getProvider(YamlConfiguration.class).save(proxyPlayerDataFileConfiguration, proxyPlayerDataFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new {@link Configuration} section.
     *
     * @param configurationSection
     * @param sectionKey
     * @return Configuration
     */
    private static Configuration createSection(Configuration configurationSection, String sectionKey) {
        configurationSection.set(sectionKey, null);
        return configurationSection.getSection(sectionKey);
    }
}
