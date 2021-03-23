package de.nick.survivalplay.commands.msg;

import de.nick.survivalplay.Colors;
import de.nick.survivalplay.PluginUtils;
import de.nick.survivalplay.SurvivalPlay;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class RCommand implements CommandExecutor {

    private final TextComponent prefix;
    private final SurvivalPlay survivalPlay;

    public RCommand(SurvivalPlay survivalPlay) {
        prefix = new TextComponent("[MSG] ");
        prefix.setColor(ChatColor.of("#00fff7"));
        this.survivalPlay = survivalPlay;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // check args
        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder(prefix)
                    .append("Bitte benutze /r <nachricht>").color(ChatColor.RED)
                    .create());
            return true;
        }
        // set targetName
        String targetName = survivalPlay.getMsgCommand().getPartners().get(sender.getName());
        // check if sender can answer anyone
        if (targetName != null) {
            // set target
            CommandSender target;
            if (targetName.equalsIgnoreCase("console")) {
                target = Bukkit.getConsoleSender();
            } else {
                target = Bukkit.getPlayer(targetName);
            }
            // check if target still online or not vanish
            if(sender instanceof Player) {
                if ((target == null) || (PluginUtils.isVanished((Player) sender))) {
                    sender.sendMessage(new ComponentBuilder(prefix)
                            .append("Der Spieler ").color(ChatColor.RED)
                            .append(args[0]).color(Colors.PLAYER_ERROR.get())
                            .append(" ist nicht mehr online!").color(ChatColor.RED)
                            .create());
                    survivalPlay.getMsgCommand().getPartners().remove(sender.getName());
                    return true;
                }
            }
            // build message
            StringBuilder msg = new StringBuilder(256);
            for (String arg : args) {
                msg.append(arg);
                msg.append(" ");
            }
            // send to sender
            sender.sendMessage(new ComponentBuilder(prefix)
                    .append("du > ").color(Colors.MSG_INFO.get())
                    .append(targetName).color(Colors.MSG_INFO.get())
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
            survivalPlay.getMsgCommand().getPartners().put(target.getName(), sender.getName());
        } else {
            sender.sendMessage(new ComponentBuilder(prefix)
                    .append("Du kannst Niemandem antworten!").color(ChatColor.RED)
                    .create());
            return true;
        }
        return false;
    }
}
