package de.nick.survivalplay.storage;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;

public interface IStorage {

    Location getSpawn();

    void setSpawn(Location location);

    void save();

    void setHome(boolean isprivate, Player player, Location location);

    Location getHome(boolean isprivate, String uuid);

    Location getHome(boolean isprivate, Player player);

    void setPlayerData(Player player);

    String getUUID(String playername);

    Set<String> getAllUuidsWith(boolean privatehome);

    String getName(String uuid);
}
