package de.nick.survivalplay.listeners;

import de.nick.survivalplay.SurvivalPlay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final SurvivalPlay main;

    public PlayerMoveListener(SurvivalPlay main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        // tpa - playermove check
        if ((event.getFrom().getY() != event.getTo().getY()) || (event.getFrom().getBlockZ() != event.getFrom().getBlockZ())
                || (event.getFrom().getBlockX() != event.getTo().getBlockX())) {
            main.getTpaCommand().cancelTeleport(event.getPlayer());
            main.getSpawnCommand().cancelTeleport(event.getPlayer());
            main.getHomeCommand().cancelTeleport(event.getPlayer());
        }

    }

}
