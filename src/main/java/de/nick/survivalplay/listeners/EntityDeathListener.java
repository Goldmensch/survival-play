package de.nick.survivalplay.listeners;

import de.nick.survivalplay.SurvivalPlay;
import de.nick.survivalplay.farmlimiter.WitherskullLimiter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    private final WitherskullLimiter witherskullLimiter;

    public EntityDeathListener(SurvivalPlay main) {
        witherskullLimiter = new WitherskullLimiter(main);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        witherskullLimiter.callDeath(event.getEntity(), event.getDrops());
    }

}
