package de.nick.survivalplay.listeners;

import de.nick.survivalplay.SurvivalPlay;
import de.nick.survivalplay.storage.IStorage;
import de.nick.survivalplay.text.RankHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final RankHandler rankHandler;
    private final IStorage storage;

    public PlayerJoinListener(SurvivalPlay survivalPlay) {
        rankHandler = new RankHandler(survivalPlay.getSmartclansAPI());
        storage = survivalPlay.getStorage();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        rankHandler.updateTabName(event.getPlayer());
        storage.setPlayerData(event.getPlayer());
        storage.save();
    }

}
