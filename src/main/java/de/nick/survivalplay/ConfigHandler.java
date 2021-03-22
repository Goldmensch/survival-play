package de.nick.survivalplay;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStreamReader;
import java.util.Objects;

public class ConfigHandler {

    private final SurvivalPlay main;

    public ConfigHandler(SurvivalPlay main) {
        this.main = main;
    }

    // build and check the config line by line
    public void checkAndBuild() {
        // get config.yml from jar
        FileConfiguration jarconfig = YamlConfiguration.loadConfiguration(new InputStreamReader(Objects.requireNonNull(main.getResource("config.yml"))));
        // reload config
        main.reloadConfig();
        // get config from main class
        FileConfiguration config = main.getConfig();
        // check and build config
        for (String current : jarconfig.getKeys(true)) {
            if (!config.getKeys(true).contains(current)) {
                config.set(current, jarconfig.get(current));
            }
        }
        // remove useless options
        for (String current : config.getKeys(true)) {
            if (!jarconfig.getKeys(true).contains(current)) {
                if (!current.startsWith(".")) {
                    config.set(current, null);
                }
            }
        }
        // save config
        main.saveConfig();

    }

    // return true if witherskullfarms are disble in the config
    public boolean witherskullfarmDisable() {
        return main.getConfig().getBoolean("farmlimiter.witherskullfarm-disable");
    }

    // return discord link
    public String getDiscordLink() {
        return main.getConfig().getString("links.discord");

    }

    //return rtp last players
    public int getLastPlayersAmount() {
        return main.getConfig().getInt("rtp.lastteleports");
    }

    // return how much players must sleep in %
    public int getHowMuchPlayerMustSleept() {
        return main.getConfig().getInt("sleepskip.mustSleepInPercent");
    }

    // returns the time that it skips to
    public long getSkipTo() {
        return main.getConfig().getLong("sleepskip.skipTo");
    }

    // return the time that should add every tick
    public long getSkipTime() {
        return main.getConfig().getLong("sleepskip.skipTime");
    }

    // return true if stopThunder is enable
    public boolean stopThunderEnable() {
        return main.getConfig().getBoolean("sleepskip.stopThunder");
    }

}
