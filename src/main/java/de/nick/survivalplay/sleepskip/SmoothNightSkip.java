package de.nick.survivalplay.sleepskip;

import de.nick.survivalplay.SurvivalPlay;
import org.bukkit.Bukkit;

import java.util.Objects;

public class SmoothNightSkip {

    private final SurvivalPlay survivalPlay;

    private int taskID;

    public SmoothNightSkip(SurvivalPlay survivalPlay) {
        this.survivalPlay = survivalPlay;
    }

    public void skip(long stop) {
        // set isSkipping true
        survivalPlay.getNightSkipHandler().setIsSkipping(true);
        // set how much time should skip every tick
        long skipTime = survivalPlay.getConfigHandler().getSkipTime();
        // start schedula
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(survivalPlay, () -> {
            // calculate time
            long time = Objects.requireNonNull(Bukkit.getWorld("world")).getTime() + skipTime;
            // set time
            Objects.requireNonNull(Bukkit.getWorld("world")).setTime(time);
            // check if time is enough
            if (time <= stop) {
                // cancel scheduler
                Bukkit.getScheduler().cancelTask(taskID);
                survivalPlay.getNightSkipHandler().setIsSkipping(false);
            }
        }, 0, 1);
    }

}
