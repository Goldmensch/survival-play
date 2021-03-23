package de.nick.survivalplay.commands.home;

import de.nick.survivalplay.Colors;
import de.nick.survivalplay.ComponentMessages;
import de.nick.survivalplay.SurvivalPlay;
import de.nick.survivalplay.storage.IStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
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
import java.util.Set;

public class HomeCommand implements CommandExecutor, TabCompleter {

    private final IStorage storage;
    private final Component prefix;

    private final SurvivalPlay survivalPlay;

    private final List<String> confirmedTeleports;

    public HomeCommand(SurvivalPlay survivalPlay) {
        this.survivalPlay = survivalPlay;
        prefix = Component.text("[Home] ").color(TextColor.fromHexString("#0091ff"));
        storage = survivalPlay.getStorage();
        confirmedTeleports = new ArrayList<>();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // check if sender is player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ComponentMessages.NO_PLAYER.get());
            return true;
        }
        Player player = (Player) sender;
        String targetUUID;
        if (args.length == 1) {
            targetUUID = player.getUniqueId().toString();
        } else if (args.length == 2) {
            if (!args[0].equalsIgnoreCase("public")) {
                player.sendMessage(Component.text()
                        .append(prefix)
                        .append(Component.text("Bitte benutze /home <public/private> (spieler)").color(NamedTextColor.RED))
                        .build());
                return false;
            }
            targetUUID = storage.getUUID(args[1]);
            if (targetUUID == null) { // check if target has a public home
                player.sendMessage(Component.text()
                        .append(prefix)
                        .append(Component.text("Der Spieler ").color(NamedTextColor.RED))
                        .append(Component.text(args[1]).color(Colors.PLAYER_ERROR.getTextColor()))
                        .append(Component.text(" besitzt kein public home!").color(NamedTextColor.RED))
                        .build());
                return true;
            }
        } else {
            player.sendMessage(Component.text()
                    .append(prefix)
                    .append(Component.text("Bitte benutze /home <public/private> (spieler)").color(NamedTextColor.RED))
                    .build());
            return false;
        }
        // check if player is in a teleportation
        if (confirmedTeleports.contains(player.getName())) {
            return true;
        }

        // private home
        if (args[0].equalsIgnoreCase("private")) {
            Location location = storage.getHome(true, player);
            // check if private home exists
            if (location == null) {
                player.sendMessage(Component.text()
                        .append(prefix)
                        .append(Component.text("Du hast noch kein private home gesetzt!").color(NamedTextColor.RED))
                        .build());
                return true;
            }
            // send confirm teleport message
            player.sendMessage(Component.text()
                    .append(prefix)
                    .append(Component.text("Du wirst in 5s zu deinem private Home teleportiert. Nicht bewegen!").color(Colors.HOME.getTextColor()))
                    .build());

            //set in confirmed teleports
            confirmedTeleports.add(player.getName());

            // teleport
            Bukkit.getScheduler().runTaskLater(survivalPlay, () -> {
                // check if teleport is cancelled
                if (confirmedTeleports.contains(player.getName())) {
                    // teleport - async
                    player.teleportAsync(location).thenRun(() -> {
                        // remove from teleports array
                        confirmedTeleports.remove(player.getName());

                        // send confirm message
                        player.sendMessage(Component.text()
                                .append(prefix)
                                .append(Component.text("Du wurdest zu deinem private home teleportiert.").color(Colors.HOME.getTextColor()))
                                .build());
                    });
                }
            }, 100);
            return true;
        }
        // public home
        if (args[0].equalsIgnoreCase("public")) {
            Location location = storage.getHome(false, targetUUID);
            // check if home exists
            if (location == null) {
                if (targetUUID.equals(player.getUniqueId().toString())) {
                    player.sendMessage(Component.text()
                            .append(prefix)
                            .append(Component.text("Du hast noch kein public home gesetzt!").color(NamedTextColor.RED))
                            .build());
                } else {
                    player.sendMessage(Component.text()
                            .append(prefix)
                            .append(Component.text("Der Spieler ").color(NamedTextColor.RED))
                            .append(Component.text(args[1]).color(NamedTextColor.RED))
                            .append(Component.text(" hat noch kein public Home gesetzt! ").color(NamedTextColor.RED))
                            .build());
                }
                return true;
            }
            // send confirm teleport message
            if (targetUUID.equals(player.getUniqueId().toString())) {
                player.sendMessage(Component.text()
                        .append(prefix)
                        .append(Component.text("Du wirst in 5s zu deinem public Home teleportiert. Nicht bewegen!").color(Colors.HOME.getTextColor()))
                        .build());
            } else {
                player.sendMessage(Component.text()
                        .append(prefix)
                        .append(Component.text("Du wirst in 5s zu dem public Home von ").color(Colors.HOME.getTextColor()))
                        .append(Component.text(args[1]).color(NamedTextColor.RED))
                        .append(Component.text(" teleportiert. Nicht bewegen!").color(Colors.HOME.getTextColor()))
                        .build());
            }

            //set in confirmed teleports
            confirmedTeleports.add(player.getName());

            // teleport
            Bukkit.getScheduler().runTaskLater(survivalPlay, () -> {
                // check if teleport is cancelled
                if (confirmedTeleports.contains(player.getName())) {
                    // teleport - async
                    player.teleportAsync(location).thenRun(() -> {
                        // remove from teleports array
                        confirmedTeleports.remove(player.getName());

                        // send confirm message
                        if (targetUUID.equals(player.getUniqueId().toString())) {
                            player.sendMessage(Component.text()
                                    .append(prefix)
                                    .append(Component.text("Du wurdest zu deinem private Home teleportiert.").color(Colors.HOME.getTextColor()))
                                    .build());
                        } else {
                            player.sendMessage(Component.text()
                                    .append(prefix)
                                    .append(Component.text("Du wurdest zu dem public Home von ").color(Colors.HOME.getTextColor()))
                                    .append(Component.text(args[1]).color(NamedTextColor.RED))
                                    .append(Component.text(" teleportiert.").color(Colors.HOME.getTextColor()))
                                    .build());
                        }
                    });
                }
            }, 100);
            return true;
        }
        player.sendMessage(Component.text()
                .append(prefix)
                .append(Component.text("Bitte benutze /home <public/private> (spieler)").color(NamedTextColor.RED))
                .build());
        return false;
    }

    public void cancelTeleport(Player player) {
        // check if the player in a teleport
        if (confirmedTeleports.contains(player.getName())) {
            confirmedTeleports.remove(player.getName());
            // send player cancel message
            player.sendMessage(Component.text()
                    .append(prefix)
                    .append(Component.text("Der Teleport wurde abgebrochen!").color(NamedTextColor.RED))
                    .build());
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completion = new ArrayList<>();
        if (args.length == 1) {
            if ("private".startsWith(args[0])) completion.add("private");
            if ("public".startsWith(args[0])) completion.add("public");
        }
        if ((args.length == 2) && args[0].equalsIgnoreCase("public")) {
            Set<String> uuids = storage.getAllUuidsWith(false);
            for (String uuid : uuids) {
                String name = storage.getName(uuid);
                if (name.startsWith(args[1])) completion.add(name);
            }
        }
        return completion;
    }
}
