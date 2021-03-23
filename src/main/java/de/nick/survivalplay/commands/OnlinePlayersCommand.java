package de.nick.survivalplay.commands;

import de.nick.survivalplay.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OnlinePlayersCommand implements CommandExecutor {

    private final Component prefix;

    public OnlinePlayersCommand() {
        prefix = Component.text("[Info] ").color(TextColor.fromHexString("#00ffa6"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> playernames = new ArrayList<>();
        for (Player current : Bukkit.getOnlinePlayers()) {
            playernames.add(current.getName());
        }
        sender.sendMessage(Component.text()
                .append(prefix)
                .append(Component.text("Es sind gerade ").color(Colors.INFO.getTextColor()))
                .append(Component.text(playernames.size()).color(Colors.INFO_LIST.getTextColor()))
                .append(Component.text(" Spieler online: ").color(Colors.INFO.getTextColor()))
                .append(Component.text(playernames.toString()).color(Colors.INFO_LIST.getTextColor()))
                .build());
        return false;
    }
}
