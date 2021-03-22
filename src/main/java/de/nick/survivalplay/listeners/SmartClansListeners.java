package de.nick.survivalplay.listeners;

import de.nick.smartclans.api.SmartclansAPI;
import de.nick.smartclans.api.events.*;
import de.nick.survivalplay.text.RankHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SmartClansListeners implements Listener {

    private RankHandler rankHandler;

    public SmartClansListeners(SmartclansAPI smartclansAPI) {
        rankHandler = new RankHandler(smartclansAPI);
    }

    @EventHandler
    public void onClanCreate(ClanCreateCompleteEvent event) {
        rankHandler.updateTabName(event.getCreator());
    }

    @EventHandler
    public void onCoLeaderAdd(AddCoLeaderEvent event) {
        rankHandler.updateTabName(event.getPlayer());
    }

    @EventHandler
    public void onClanDelete(ClanDeleteEvent event) {
        for(Player current : Bukkit.getOnlinePlayers()) {
            rankHandler.updateTabName(current);
        }
    }

    @EventHandler
    public void onMemberJoin(MemberAddEvent event) {
        rankHandler.updateTabName(event.getPlayer());
    }

    @EventHandler
    public void onMemberLeave(MemberRemoveEvent event) {
        rankHandler.updateTabName(event.getPlayer());
    }

    @EventHandler
    public void onCoLeaderRemove(RemoveCoLeaderEvent event) {
        rankHandler.updateTabName(event.getPlayer());
    }
}
