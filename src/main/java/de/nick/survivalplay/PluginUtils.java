package de.nick.survivalplay;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.List;

public class PluginUtils {

    // checks if the player is in vanish
    public static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    public static List<Player> getVisiblePlayers() {
        List<Player> players = new ArrayList<>();
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!isVanished(target)) {
                players.add(target);
            }
        }
        return players;
    }

}
