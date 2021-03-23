package de.nick.survivalplay.sleepskip;

import de.nick.survivalplay.Colors;
import de.nick.survivalplay.PluginUtils;
import de.nick.survivalplay.SurvivalPlay;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class NightSkipHandler {

    private final SurvivalPlay survivalPlay;
    private final SmoothNightSkip smoothNightSkip;
    private final TextComponent prefix;
    private int sleepingPlayers = 0;
    private boolean isSkipping = false;


    public NightSkipHandler(SurvivalPlay survivalPlay) {
        this.survivalPlay = survivalPlay;
        prefix = new TextComponent("[NightSkip] ");
        prefix.setColor(ChatColor.of("#00fc19"));
        smoothNightSkip = new SmoothNightSkip(survivalPlay);
    }

    // check and skip night
    public void onBedEnter() {
        // add 1 to sleeping players
        sleepingPlayers++;
        // check if enough players are sleeping
        if (enoughSleeping()) {
            // check if night is currently skipping
            if (isSkipping) {
                return;
            }
            // stop thunder (if its enable in config)
            if (survivalPlay.getConfigHandler().stopThunderEnable()) {
                if (Objects.requireNonNull(Bukkit.getServer().getWorld("world")).hasStorm()) {
                    Objects.requireNonNull(Bukkit.getServer().getWorld("world")).setStorm(false);
                    Bukkit.broadcast(new ComponentBuilder(prefix)
                            .append("Der Sturm wurde beendet").color(Colors.NIGHTSKIP.get())
                            .create());
                }
            }
            if (Objects.requireNonNull(Bukkit.getServer().getWorld("world")).getTime() <= 12541) {
                return;
            }
            // skip night
            smoothNightSkip.skip(survivalPlay.getConfigHandler().getSkipTo());
            // set sleeping players to 0
            sleepingPlayers = 0;
            // no - sends message
        } else {
            Bukkit.broadcast(new ComponentBuilder(prefix)
                    .append("Es schalfen gerade ").color(Colors.NIGHTSKIP.get())
                    .append(String.valueOf(sleepingPlayers)).color(Colors.NIGHTSKIP_NUMBER.get())
                    .append(" Spieler, zum überspringen fehlen noch ").color(Colors.NIGHTSKIP.get())
                    .append(String.valueOf(needToSleep())).color(Colors.NIGHTSKIP.get())
                    .append(" Spieler.").color(Colors.NIGHTSKIP.get())
                    .create());
        }
    }

    // reduce sleepingplayers by 1
    public void onBedLeave() {
        sleepingPlayers--;
    }

    // check if enough players sleept
    private boolean enoughSleeping() {
        float percent = survivalPlay.getConfigHandler().getHowMuchPlayerMustSleept() / 100F;
        int mustSleep = Math.round(percent * PluginUtils.getVisiblePlayers().size());
        return sleepingPlayers >= mustSleep;
    }

    // returns how many players must sleep
    public int needToSleep() {
        float percent = survivalPlay.getConfigHandler().getHowMuchPlayerMustSleept() / 100F;
        int mustSleep = Math.round(percent * PluginUtils.getVisiblePlayers().size());
        return sleepingPlayers - mustSleep;
    }

    // set isSkipping
    public void setIsSkipping(boolean skipping) {
        isSkipping = skipping;
    }
}
