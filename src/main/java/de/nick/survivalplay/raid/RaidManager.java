package de.nick.survivalplay.raid;

import de.nick.smartclans.api.SmartclansAPI;
import de.nick.survivalplay.PluginUtils;
import de.nick.survivalplay.SurvivalPlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.util.*;

public class RaidManager {

    private final Map<Integer, Raid> raids;
    private final Map<String, Integer> targetClans;
    private final Map<String, Integer> raiderClans;

    private final SurvivalPlay survivalPlay;
    private final SmartclansAPI smartclansAPI;

    public RaidManager(SurvivalPlay survivalPlay) {
        raids = new HashMap<>();
        targetClans = new HashMap<>();
        raiderClans = new HashMap<>();

        this.survivalPlay = survivalPlay;
        this.smartclansAPI = survivalPlay.getSmartclansAPI();
    }

    public RaidStartTerminations startRaid(String raidersClan, String targetsClan, String targetTimerOverMsg, String raiderTimerOverMsg, String timeleftMsg) {
        // check if target clan is in a raid
        if (targetClans.containsKey(raidersClan) || raiderClans.containsKey(raidersClan)) {
            return RaidStartTerminations.RAIDER_ALREADY_IN_RAID;
        }
        // check if raider clan is in a raid
        if (targetClans.containsKey(targetsClan) || raiderClans.containsKey(targetsClan)) {
            return RaidStartTerminations.TARGET_ALREADY_IN_RAID;
        }
        Raid raid = new Raid(targetsClan, raidersClan, this);
        // put raid in maps
        putRaidIn(raid);
        // start raid timer
        startTimer(survivalPlay.getConfigHandler().getRaidMaxTime(), survivalPlay.getConfigHandler().getRaidtimeLeftMessageTimes(), raid, targetTimerOverMsg,
                raiderTimerOverMsg, timeleftMsg);
        return RaidStartTerminations.RAID_STARTED;
    }

    private void startTimer(String time, List<String> timeLeftIntervals, Raid raid, String targetTimerOverMsg, String raiderTimerOverMsg, String timeleftMsg) {
        String raiderClan = raid.getRaiderClan();
        String targetClan = raid.getTargetClan();

        // create runnable
        Runnable runnable = () -> {
            long sleepTime = Duration.parse(PluginUtils.parseISOISO8601Time(time)).toMillis();
            try {
                if (timeLeftIntervals.size() != 0) {
                    for (String current : timeLeftIntervals) {
                        if (!raids.containsKey(raid.getId())) {
                            return;
                        }
                        long thisTime = Duration.parse(PluginUtils.parseISOISO8601Time(current)).toMillis();
                        sleepTime = sleepTime - thisTime;
                        long minutes = (sleepTime / 1000) / 60;
                        long seconds = (sleepTime / 1000) % 60;
                        Component msg = LegacyComponentSerializer.legacyAmpersand().deserialize(timeleftMsg.replace("%m", String.valueOf(minutes))
                                .replace("%s", String.valueOf(seconds)));
                        sendMessageToClan(raiderClan, msg);
                        sendMessageToClan(targetClan, msg);
                        Thread.sleep(sleepTime);
                    }
                } else {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (raids.containsKey(raid.getId())) {
                raids.remove(raid.getId());
                // deserialize legacy text
                Component targetMsg = LegacyComponentSerializer.legacyAmpersand().deserialize(targetTimerOverMsg.replace("%c", targetClan));
                Component raiderMsg = LegacyComponentSerializer.legacyAmpersand().deserialize(raiderTimerOverMsg.replace("%c", raiderClan));

                sendMessageToClan(raiderClan, targetMsg);
                sendMessageToClan(targetClan, raiderMsg);
            }
        };
        // start thread
        Thread t = new Thread(runnable);
        t.setName("raidtimer: " + raid.getId());
        t.start();

    }

    public int getFreeID() {
        List<Integer> ids = Arrays.asList(raids.keySet().toArray(new Integer[0]));
        for (int i = 0; true; i++) {
            if (!ids.contains(i)) {
                return i;
            }
        }
    }

    private void putRaidIn(Raid raid) {
        int id = raid.getId();
        raids.put(id, raid);
        targetClans.put(raid.getTargetClan(), id);
        raiderClans.put(raid.getRaiderClan(), id);
    }

    private void sendMessageToClan(String clan, Component msg) {
        List<String> member = smartclansAPI.getClanapi().getMembersAsUUIDList(clan);
        for (String current : member) {
            UUID uuid = UUID.fromString(current);
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(msg);
        }
    }


}
