package de.nick.survivalplay.commands.msg;

import de.nick.survivalplay.Colors;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class MsgCommand implements CommandExecutor {

    private final TextComponent prefix;
    private final Map<String, String> partners;

    public MsgCommand() {
        prefix = new TextComponent("[MSG] ");
        prefix.setColor(ChatColor.of("#00fff7"));
        partners = new HashMap<>();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // check argslenght
        if (args.length < 2) {
            sender.sendMessage(new ComponentBuilder(prefix)
                    .append("Bitte benutze /msg <spieler> <nachricht>").color(ChatColor.RED)
                    .create());
            return true;
        }
        // define receiver
        Player target = Bukkit.getPlayer(args[0]);
        // check if receiver is online
        if (target == null) {
            sender.sendMessage(new ComponentBuilder(prefix)
                    .append("Der Spieler ").color(ChatColor.RED)
                    .append(args[0]).color(Colors.PLAYER_ERROR.get())
                    .append(" muss online sein!").color(ChatColor.RED)
                    .create());
            return true;
        }
        // build message
        StringBuilder msg = new StringBuilder(256 - args[0].length());
        for (int i = 1; i < (args.length); i++) {
            msg.append(args[i]);
            msg.append(" ");
        }
        // send to sender
        sender.sendMessage(new ComponentBuilder(prefix)
                .append("du > ").color(Colors.MSG_INFO.get())
                .append(target.getName()).color(Colors.MSG_INFO.get())
                .append(": ").color(Colors.MSG.get())
                .append(msg.toString()).color(Colors.MSG.get())
                .create());
        // send to target
        target.sendMessage(new ComponentBuilder(prefix)
                .append(sender.getName()).color(Colors.MSG_INFO.get())
                .append(" > dir").color(Colors.MSG_INFO.get())
                .append(": ").color(Colors.MSG.get())
                .append(msg.toString()).color(Colors.MSG.get())
                .create());
        partners.put(target.getName(), sender.getName());
        return true;
    }

    public Map<String, String> getPartners() {
        return partners;
    }
}
