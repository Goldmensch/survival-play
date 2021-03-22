package de.nick.survivalplay.commands;

import de.nick.survivalplay.Colors;
import de.nick.survivalplay.Messages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class GamemodeCommand implements CommandExecutor, TabCompleter {

    private final TextComponent prefix;

    public GamemodeCommand() {
        prefix = new TextComponent("[Gamemode] ");
        prefix.setColor(ChatColor.of("#a30093"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player target;
        // sender checks
        // if args lengh is 1
        if (args.length == 1) {
            // check permission for change self gamemode
            if (!sender.hasPermission("gamemode.self")) {
                // if not send no perm msg
                sender.spigot().sendMessage(Messages.NO_PERMISSONS.get());
                return true;
            }
            // check if the sender is a player
            if (sender instanceof Player) {
                // define sender as target
                target = (Player) sender;
            } else {
                // if no player, send error msg
                sender.spigot().sendMessage(new ComponentBuilder(prefix)
                        .append("Bitte benutze /gamemode <gamemode> <spieler>").color(ChatColor.RED)
                        .create());
                return true;
            }
            // if args lengh is 2
        } else if (args.length == 2) {
            // check permission for change someothers gamemode
            if (!sender.hasPermission("gamemode.other")) {
                // if not send no perm msg
                sender.spigot().sendMessage(Messages.NO_PERMISSONS.get());
                return true;
            }
            // init target as player in args1
            target = Bukkit.getPlayer(args[1]);
            // if target not online send not online msg
            if (target == null) {
                sender.spigot().sendMessage(new ComponentBuilder(prefix)
                        .append("Der Spieler ").color(ChatColor.RED)
                        .append(args[1]).color(Colors.PLAYER_ERROR.get())
                        .append(" muss online sein.").color(ChatColor.RED)
                        .create());
                return true;
            }
        } else {
            // send no permission msg if no permission
            if (!(sender.hasPermission("gamemode.self") || sender.hasPermission("gamemode.other"))) {
                sender.spigot().sendMessage(Messages.NO_PERMISSONS.get());
                return true;
            }
            // send wrong arguments
            sender.spigot().sendMessage(new ComponentBuilder(prefix)
                    .append("Bitte benutze /gamemode <gamemode> (spieler)").color(ChatColor.RED)
                    .create());
            return false;
        }
        // setgamemode of target
        switch (args[0].toLowerCase()) {
            // set target gamemode survival
            case "survival":
            case "0":
                if (!sender.hasPermission("gamemode.survival")) {
                    sender.spigot().sendMessage(Messages.NO_PERMISSONS.get());
                    return true;
                }
                target.setGameMode(GameMode.SURVIVAL);
                break;
            // set target gamemode creative
            case "creative":
            case "1":
                if (!sender.hasPermission("gamemode.creative")) {
                    sender.spigot().sendMessage(Messages.NO_PERMISSONS.get());
                    return true;
                }
                target.setGameMode(GameMode.CREATIVE);
                break;
            // set target gamemode adventure
            case "adventure":
            case "2":
                if (!sender.hasPermission("gamemode.adventure")) {
                    sender.spigot().sendMessage(Messages.NO_PERMISSONS.get());
                    return true;
                }
                target.setGameMode(GameMode.ADVENTURE);
                break;
            // set target gamemode spectator
            case "spectator":
            case "3":
                if (!sender.hasPermission("gamemode.spectator")) {
                    sender.spigot().sendMessage(Messages.NO_PERMISSONS.get());
                    return true;
                }
                target.setGameMode(GameMode.ADVENTURE);
                break;
            // send if gamemode is invalid
            default:
                sender.spigot().sendMessage(new ComponentBuilder(prefix)
                        .append("Es sind folgende Gamemodes verfügbar: survival(0), creative(1), adventure(2), spectator(3)").color(ChatColor.RED)
                        .create());
                return true;
        }

        // send sender confirm msg
        if (target.getName().equals(sender.getName())) {
            // send if sender change his own gamemode
            sender.spigot().sendMessage(new ComponentBuilder(prefix)
                    .append("Dein Gamemode wurde zu ").color(Colors.GAMEMODE.get())
                    .append(target.getGameMode().toString().toLowerCase()).color(Colors.GAMEMODE_GAMEMODE.get())
                    .append(" geändert.").color(Colors.GAMEMODE.get())
                    .create());
        } else {
            // send if sender change someone else Gamemode
            sender.spigot().sendMessage(new ComponentBuilder(prefix)
                    .append("Der Gamemode von ").color(Colors.GAMEMODE.get())
                    .append(target.getName()).color(Colors.GAMEMODE_PLAYER.get())
                    .append(" wurde zu ").color(Colors.GAMEMODE.get())
                    .append(target.getGameMode().toString().toLowerCase()).color(Colors.GAMEMODE_GAMEMODE.get())
                    .append(" geändert.").color(Colors.GAMEMODE.get())
                    .create());
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        // gamemodemodes
        if (args.length == 1) {
            // check if player has permission for gamemode creative
            if (sender.hasPermission("gamemode.creative")) {
                if ("creative".startsWith(args[0])) completions.add("creative");
                if ("1".startsWith(args[0])) completions.add("1");
            }
            // check if player has permission for gamemode survival
            if (sender.hasPermission("gamemode.survival")) {
                if ("survival".startsWith(args[0])) completions.add("survival");
                if ("0".startsWith(args[0])) completions.add("0");
            }
            // check if player has permission for gamemode adventure
            if (sender.hasPermission("gamemode.adventure")) {
                if ("adventure".startsWith(args[0])) completions.add("adventure");
                if ("2".startsWith(args[0])) completions.add("2");
            }
            // check if player has permission for gamemode specture
            if (sender.hasPermission("gamemode.spectator")) {
                if ("spectator".startsWith(args[0])) completions.add("spectator");
                if ("3".startsWith(args[0])) completions.add("3");
            }
        }
        // player
        if (args.length == 2) {
            // check if sender has permission for change others gamemode
            if (sender.hasPermission("gamemode.other")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().startsWith(args[1])) completions.add(player.getName());
                }
            }
        }
        return completions;
    }
}
