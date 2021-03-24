package de.nick.survivalplay.storage;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class YamlStorage implements IStorage {

    private final FileConfiguration config;
    private final File file;

    // constructor
    public YamlStorage(File file) {
        this.config = YamlConfiguration.loadConfiguration(file);
        this.file = file;
    }

    // get spawn location
    @Override
    public Location getSpawn() {
        return config.getLocation(YamlStoragePaths.SPAWN.getPath());
    }

    // set world spawn and for /spawn command
    @Override
    public void setSpawn(Location location) {
        config.set(YamlStoragePaths.SPAWN.getPath(), location);
    }

    // saves file to disk
    @Override
    public void save() {
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // sets a private or public home
    @Override
    public void setHome(boolean isprivate, Player player, Location location) {
        if (isprivate) {
            config.set(YamlStoragePaths.PRIVATE_HOME.getPath() + "." + player.getUniqueId().toString(), location);
        } else {
            config.set(YamlStoragePaths.PUBLIC_HOME.getPath() + "." + player.getUniqueId().toString(), location);
        }
    }


    @Override
    public Location getHome(boolean isprivate, String uuid) {
        if (isprivate) {
            return config.getLocation(YamlStoragePaths.PRIVATE_HOME.getPath() + "." + uuid);
        } else {
            return config.getLocation(YamlStoragePaths.PUBLIC_HOME.getPath() + "." + uuid);
        }
    }

    @Override
    public Location getHome(boolean isprivate, Player player) {
        if (isprivate) {
            return config.getLocation(YamlStoragePaths.PRIVATE_HOME.getPath() + "." + player.getUniqueId().toString());
        } else {
            return config.getLocation(YamlStoragePaths.PUBLIC_HOME.getPath() + "." + player.getUniqueId().toString());
        }
    }

    @Override
    public void setPlayerData(Player player) {
        config.set(YamlStoragePaths.PLAYER_DATA_UUID.getPath() + "." + player.getUniqueId().toString() + ".name", player.getName());
        config.set(YamlStoragePaths.PLAYER_DATA_NAME.getPath() + "." + player.getName() + ".uuid", player.getUniqueId().toString());
    }

    @Override
    public String getUUID(String playername) {
        for(String name : Objects.requireNonNull(config.getConfigurationSection(YamlStoragePaths.PLAYER_DATA_NAME.getPath())).getKeys(false)) {
            if(name.equalsIgnoreCase(playername)) {
                return config.getString(YamlStoragePaths.PLAYER_DATA_NAME.getPath() + "." + name + ".uuid");
            }
        }
        return null;
    }

    @Override
    public Set<String> getAllUuidsWith(boolean privatehome) {
        if (privatehome) {
            return Objects.requireNonNull(config.getConfigurationSection(YamlStoragePaths.PRIVATE_HOME.getPath())).getKeys(false);
        } else {
            return Objects.requireNonNull(config.getConfigurationSection(YamlStoragePaths.PUBLIC_HOME.getPath())).getKeys(false);
        }
    }

    @Override
    public String getName(String uuid) {
        return config.getString(YamlStoragePaths.PLAYER_DATA_UUID.getPath() + "." + uuid + ".name");
    }
}
