package de.nick.survivalplay.raid;

public class Raid {

    private final String targetClan;
    private final String raiderClan;
    private final int id;

    public Raid(String targetClan, String raiderClan, RaidManager raidManager) {
        this.raiderClan = raiderClan;
        this.targetClan = targetClan;
        id = raidManager.getFreeID();
    }

    public String getRaiderClan() {
        return raiderClan;
    }

    public String getTargetClan() {
        return targetClan;
    }

    public int getId() {
        return id;
    }
}
