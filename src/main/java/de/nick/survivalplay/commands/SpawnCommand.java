package de.nick.survivalplay.commands;

import de.nick.survivalplay.Colors;
import de.nick.survivalplay.Messages;
import de.nick.survivalplay.SurvivalPlay;
import de.nick.survivalplay.storage.IStorage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("deprecation")
public class SpawnCommand implements CommandExecutor, TabCompleter {

    private final SurvivalPlay survivalPlay;

    private final TextComponent prefix;

    private final List<String> confirmedTeleports;

    public SpawnCommand(SurvivalPlay survivalPlay) {
        this.survivalPlay = survivalPlay;
        prefix = new TextComponent("[Spawn] ");
        prefix.setColor(ChatColor.GOLD);
        confirmedTeleports = new ArrayList<>();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.spigot().sendMessage(Messages.NO_PLAYER.get());
            return true;
        }
        if (args.length == 0) {
            // get location and storage
            IStorage storage = survivalPlay.getStorage();
            Location location = storage.getSpawn();
            // check if spawn is set
            if (location == null) {
                sender.spigot().sendMessage(new ComponentBuilder(prefix)
                        .append("Der Spawn wurde noch nicht gesetzt.").color(ChatColor.RED)
                        .create());
                return true;
            }
            // define target (sender)
            Player player = (Player) sender;

            // check if target is in a teleportation
            if (confirmedTeleports.contains(player.getName())) {
                return true;
            }

            //set in confirmed teleports
            confirmedTeleports.add(player.getName());

            //send teleport message
            player.sendMessage(new ComponentBuilder(prefix)
                    .append("Du wirst in 5s zum Spawn teleportiert. Nicht bewegen!").color(Colors.SPAWN.get())
                    .create());

            // teleport
            Bukkit.getScheduler().runTaskLater(survivalPlay, () -> {
                // check if teleport is cancelled
                if (confirmedTeleports.contains(player.getName())) {
                    // teleport - async
                    CompletableFuture<Boolean> task = player.teleportAsync(location);

                    // after teleport
                    task.thenRun(() -> {
                        // remove from teleports array
                        confirmedTeleports.remove(player.getName());

                        // send confirm message
                        player.spigot().sendMessage(new ComponentBuilder(prefix)
                                .append("Du wurdest erfolgreich zum spawn teleportiert.").color(Colors.SPAWN.get())
                                .create());
                    });
                }
            }, 100);
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("spawn.set")) {
                    // define target (sender)
                    Player target = (Player) sender;
                    // get storage
                    IStorage storage = survivalPlay.getStorage();
                    // set spawn and save file
                    storage.setSpawn(target.getLocation());
                    storage.save();
                    // send confirm message
                    target.spigot().sendMessage(new ComponentBuilder(prefix)
                            .append("Der Spawn wurde erfolgreich gesetzt").color(ChatColor.GREEN)
                            .create());
                } else {
                    sender.spigot().sendMessage(Messages.NO_PERMISSONS.get());
                }
                return true;
            }
        }
        // check if sender has set spawn permission
        if (sender.hasPermission("spawn.set")) {
            sender.spigot().sendMessage(new ComponentBuilder(prefix)
                    .append("Bitte benutze /spawn (set)").color(ChatColor.RED)
                    .create());
            // if not send normal usage
        } else {
            sender.spigot().sendMessage(new ComponentBuilder(prefix)
                    .append("Bitte benutze /spawn").color(ChatColor.RED)
                    .create());
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completion = new ArrayList<>();
        if (sender.hasPermission("spawn.set")) {
            if (args.length == 1) {
                if ("set".startsWith(args[0].toLowerCase())) completion.add("set");
            }
        }
        return completion;
    }

    public void cancelTeleport(Player player) {
        // check if the player in a teleport
        if (confirmedTeleports.contains(player.getName())) {
            confirmedTeleports.remove(player.getName());
            // send player cancel message
            player.spigot().sendMessage(new ComponentBuilder(prefix)
                    .append("Der Teleport wurde abgebrochen!").color(ChatColor.RED)
                    .create());
        }
    }
}
