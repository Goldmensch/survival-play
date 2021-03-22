package de.nick.survivalplay.storage;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IStorage {

    void setSpawn(Location location);

    Location getSpawn();

    void save();

    void setHome(boolean isprivate, Player player, Location location);

    Location getHome(boolean isprivate, Player player);

    void setPlayerData(Player player);

    String getUUID(String playername);
}
