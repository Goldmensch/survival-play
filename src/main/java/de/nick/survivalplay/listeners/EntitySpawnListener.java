package de.nick.survivalplay.listeners;

import de.nick.survivalplay.SurvivalPlay;
import de.nick.survivalplay.farmlimiter.WitherskullLimiter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnListener implements Listener {

    private final WitherskullLimiter witherskullLimiter;

    public EntitySpawnListener(SurvivalPlay main) {
        witherskullLimiter = new WitherskullLimiter(main);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        witherskullLimiter.callSpawn(event.getEntity(), event.getLocation());
    }
}
