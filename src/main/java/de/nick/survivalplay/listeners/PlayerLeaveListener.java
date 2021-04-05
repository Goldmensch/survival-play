package de.nick.survivalplay.listeners;

import de.nick.survivalplay.SurvivalPlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        event.quitMessage(Component.text()
                .append(Component.text(event.getPlayer().getName()).color(NamedTextColor.GOLD))
                .append(Component.text(" war wohl doch nicht so wild und ist gegangen.").color(NamedTextColor.YELLOW))
                .build());
    }

}
