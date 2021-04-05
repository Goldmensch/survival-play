package de.nick.survivalplay.commands;

import de.nick.smartclans.api.SmartclansAPI;
import de.nick.survivalplay.Colors;
import de.nick.survivalplay.ComponentMessages;
import de.nick.survivalplay.SurvivalPlay;
import de.nick.survivalplay.raid.RaidManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RaidCommand implements CommandExecutor {

    private final Component prefix;
    private final SmartclansAPI smartclansAPI;
    private final SurvivalPlay survivalPlay;
    private final RaidManager raidManager;

    private final Map<String, String> raids;

    public RaidCommand(SurvivalPlay survivalPlay) {
        prefix = Component.text("[Raid] ").color(TextColor.fromHexString("#a83e32"));
        smartclansAPI = new SmartclansAPI();
        raids = new HashMap<>();
        this.survivalPlay = survivalPlay;
        raidManager = new RaidManager(survivalPlay);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length != 2) {
                sender.sendMessage(Component.text()
                        .append(prefix)
                        .append(Component.text("Bitte benutze /raid <check/start/stop> <clan>").color(NamedTextColor.RED))
                        .build());
                return true;
            }

            Player player = (Player) sender;
            if (!smartclansAPI.getPlayerapi().isInClan(player)) {
                player.sendMessage(Component.text()
                        .append(prefix)
                        .append(Component.text("Du bist in keinem Clan!").color(NamedTextColor.RED))
                        .build());
                return true;
            }

            String clan = args[1];
            String raidersClan = smartclansAPI.getPlayerapi().getClannameAsString(player);

            // check if command is correct
            if (args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("stop")) {
                if (!smartclansAPI.getClanapi().exists(clan)) {
                    player.sendMessage(Component.text()
                            .append(prefix)
                            .append(Component.text("Der Clan ").color(NamedTextColor.RED))
                            .append(Component.text(clan).color(Colors.PLAYER_ERROR.getTextColor()))
                            .append(Component.text(" existiert nicht.").color(NamedTextColor.RED))
                            .build());
                    return true;
                }
            } else {
                sender.sendMessage(Component.text()
                        .append(prefix)
                        .append(Component.text("Bitte benutze /raid <check/start/stop> <clan>").color(NamedTextColor.RED))
                        .build());
                return true;
            }

            List<UUID> targets = new ArrayList<>();
            List<UUID> raiders = new ArrayList<>();
            // targets
            for (String current : smartclansAPI.getClanapi().getMembersAsUUIDList(clan)) {
                UUID uuid = UUID.fromString(current);
                Player target = Bukkit.getPlayer(uuid);
                if (target != null) targets.add(uuid);
            }
            // raiders
            for (String current : smartclansAPI.getClanapi().getMembersAsUUIDList(raidersClan)) {
                UUID uuid = UUID.fromString(current);
                Player target = Bukkit.getPlayer(uuid);
                if (target != null) raiders.add(uuid);
            }

            if (raids.containsKey(clan.toLowerCase()) || raids.containsValue(clan.toLowerCase())) {
                player.sendMessage(Component.text()
                        .append(prefix)
                        .append(Component.text("Dieser Clan ist bereits in einem raid.").color(Colors.RAID.getTextColor()))
                        .build());
                return true;
            }

            if (raids.containsKey(raidersClan.toLowerCase()) || raids.containsValue(raidersClan.toLowerCase())) {
                player.sendMessage(Component.text()
                        .append(Component.text("Dein Clan ist bereits in einem Raid.").color(Colors.RAID.getTextColor()))
                        .build());
                return true;
            }

            //sub commands
            if (args[0].equalsIgnoreCase("check")) {
                checkSub(raidersClan, clan, raiders.size(), targets.size(), player, false);
                return true;
            }
            // start sub
            if (args[0].equalsIgnoreCase("start")) {
                if (checkSub(raidersClan, clan, raiders.size(), targets.size(), player, true)) {
                    String targetTimeOverMsg = "§cDer Raid gegen %c ist abgelaufen!";
                    String raiderTimeOverMsg = "§cDer Raid von %c gegen euch ist abgelaufen!";
                    String timeLeftMsg = "§aEs sind noch %m Minuten und %s sekunden des raids übrig.";
                    raidManager.startRaid(raidersClan, clan, targetTimeOverMsg, raiderTimeOverMsg, timeLeftMsg);
                }
            }
            return false;
        } else {
            sender.sendMessage(ComponentMessages.NO_PLAYER.get());
        }
        return true;
    }

    private boolean checkSub(String raidersClan, String clan, int onlineRaiders, int onlineTargets, Player player, boolean noMessage) {
        if ((onlineRaiders - 1) <= onlineTargets) {
            if (noMessage) return true;
            player.sendMessage(Component.text()
                    .append(prefix)
                    .append(Component.text("Es sind ").color(Colors.RAID.getTextColor()))
                    .append(Component.text("genug").color(NamedTextColor.GREEN))
                    .append(Component.text(" Leute von ").color(Colors.RAID.getTextColor()))
                    .append(Component.text(raidersClan).color(Colors.RAID_CLAN.getTextColor()))
                    .append(Component.text(" online").color(Colors.RAID.getTextColor()))
                    .build());
            return true;
        } else {
            player.sendMessage(Component.text()
                    .append(prefix)
                    .append(Component.text("Es sind ").color(Colors.RAID.getTextColor()))
                    .append(Component.text("nicht genug").color(NamedTextColor.RED))
                    .append(Component.text(" Leute von ").color(Colors.RAID.getTextColor()))
                    .append(Component.text(raidersClan).color(Colors.RAID_CLAN.getTextColor()))
                    .append(Component.text(" online").color(Colors.RAID.getTextColor()))
                    .build());
        }
        return false;
    }
}
