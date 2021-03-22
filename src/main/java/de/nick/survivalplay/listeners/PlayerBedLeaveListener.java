package de.nick.survivalplay.listeners;

import de.nick.survivalplay.SurvivalPlay;
import de.nick.survivalplay.sleepskip.NightSkipHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class PlayerBedLeaveListener implements Listener {

    private final NightSkipHandler nightSkipHandler;

    public PlayerBedLeaveListener(SurvivalPlay survivalPlay) {
        nightSkipHandler = survivalPlay.getNightSkipHandler();
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        nightSkipHandler.onBedLeave();
    }
}
