package de.nick.survivalplay.listeners;

import de.nick.survivalplay.SurvivalPlay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    private final SurvivalPlay survivalPlay;

    public PlayerLeaveListener(SurvivalPlay survivalPlay) {
        this.survivalPlay = survivalPlay;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        survivalPlay.getRtpCommand().checkAndRemoveLast(event.getPlayer());
    }

}
