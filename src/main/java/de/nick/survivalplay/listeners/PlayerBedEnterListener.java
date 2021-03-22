package de.nick.survivalplay.listeners;

import de.nick.survivalplay.SurvivalPlay;
import de.nick.survivalplay.sleepskip.NightSkipHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class PlayerBedEnterListener implements Listener {

    private final NightSkipHandler nightSkipHandler;

    public PlayerBedEnterListener(SurvivalPlay survivalPlay) {
        nightSkipHandler = survivalPlay.getNightSkipHandler();
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        nightSkipHandler.onBedEnter();
    }

}
