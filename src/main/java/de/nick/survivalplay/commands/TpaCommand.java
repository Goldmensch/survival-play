package de.nick.survivalplay.commands;

import de.nick.survivalplay.Colors;
import de.nick.survivalplay.Messages;
import de.nick.survivalplay.SurvivalPlay;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("deprecation")
public class TpaCommand implements CommandExecutor {

    private final TextComponent prefix;
    private final SurvivalPlay main;

    private final Map<String, Long> cooldown;
    private final Map<String, String> teleports;
    private final List<String> confirmedTeleports;

    public TpaCommand(SurvivalPlay main) {
        prefix = new TextComponent("[TPA] ");
        prefix.setColor(ChatColor.of("#ff8800"));
        cooldown = new HashMap<>();
        teleports = new HashMap<>();
        confirmedTeleports = new ArrayList<>();
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // check if sender is a player
        if (!(sender instanceof Player)) {
            sender.spigot().sendMessage(Messages.NO_PLAYER.get());
            return true;
        }

        // check if the command is complete
        if (args.length != 1) {
            sender.spigot().sendMessage(new ComponentBuilder(prefix)
                    .append("Bitte benutze /tpa <spieler>")
                    .color(ChatColor.RED).create());
            return true;
        }

        // accept -- section
        if (args[0].equalsIgnoreCase("accept")) {
            Player player = (Player) sender;

            // check if player have a tpa
            if (teleports.containsKey(player.getName())) {
                Player target = Bukkit.getPlayer(teleports.get(player.getName()));

                // check if sender is still on
                if (target == null) {
                    player.spigot().sendMessage(new ComponentBuilder(prefix)
                            .append("Der Spieler ").color(ChatColor.RED)
                            .append(teleports.get(player.getName())).color(Colors.PLAYER_ERROR.get())
                            .append(" ist nicht mehr online.").color(ChatColor.RED)
                            .create());
                    teleports.remove(player.getName());
                    return true;
                }

                // send sender tpa accept message
                target.spigot().sendMessage(new ComponentBuilder(prefix)
                        .append("Der Spieler ").color(Colors.TPA.get())
                        .append(player.getName()).color(Colors.TPA_PLAYER.get())
                        .append(" hat deine TPA angenommen und du wirst in 5s zu ihm teleportiert. Nicht bewegen!").color(Colors.TPA.get())
                        .create());

                // send tpa info to player
                player.spigot().sendMessage(new ComponentBuilder(prefix)
                        .append("Du hast die TPA von ").color(Colors.TPA.get())
                        .append(target.getName()).color(Colors.TPA_PLAYER.get())
                        .append(" angenommen, er wird in 5s zu dir teleportiert").color(Colors.TPA.get())
                        .create());

                // add player to confirmedTeleports
                confirmedTeleports.add(target.getName());
                // remove from teleports
                teleports.remove(player.getName());

                // teleport
                Bukkit.getScheduler().runTaskLater(main, () -> {
                    // check if teleport is cancelled
                    if (confirmedTeleports.contains(target.getName())) {
                        // teleport - async
                        CompletableFuture<Boolean> task = target.teleportAsync(player.getLocation());

                        // after teleport
                        task.thenRun(() -> {
                            // send player message
                            target.spigot().sendMessage(new ComponentBuilder(prefix)
                                    .append("Du wurdest zu ").color(Colors.TPA.get())
                                    .append(player.getName()).color(Colors.TPA_PLAYER.get())
                                    .append(" teleportiert.").color(Colors.TPA.get())
                                    .create());

                            // send sender message
                            player.spigot().sendMessage(new ComponentBuilder(prefix)
                                    .append("Der Spieler ").color(Colors.TPA.get())
                                    .append(target.getName()).color(Colors.TPA_PLAYER.get())
                                    .append(" wurde zu dir teleportiert.").color(Colors.TPA.get())
                                    .create());
                            confirmedTeleports.remove(player.getName());
                        });
                    }
                }, 100);
            } else {
                player.spigot().sendMessage(new ComponentBuilder(prefix)
                        .append("Du hast keine offenen TPAs!").color(ChatColor.RED)
                        .create());
            }
            return true;
        }
        // make Player instances
        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);

        // check if cooldown over
        if (cooldown.containsKey(player.getName())) {
            long difference = (System.currentTimeMillis() / 1000) - cooldown.get(player.getName());
            if (difference < 180) {
                long minutes = (180 - difference) / 60;
                long seconds = (180 - difference) % 60;
                player.spigot().sendMessage(new ComponentBuilder(prefix)
                        .append("Bitte warte noch ").color(ChatColor.RED)
                        .append(String.valueOf(minutes)).color(ChatColor.of("#ff0000"))
                        .append(" Minuten und ").color(ChatColor.RED)
                        .append(String.valueOf(seconds)).color(ChatColor.of("#ff0000"))
                        .append(" Sekunden bis du wieder eine TPA schicken kannst.").color(ChatColor.RED)
                        .create());
                return true;
            }
        }

        // check if target is online
        if (target == null) {
            player.spigot().sendMessage(new ComponentBuilder(prefix)
                    .append("Der Spieler ").color(ChatColor.RED)
                    .append(args[0]).color(Colors.PLAYER_ERROR.get())
                    .append(" ist nicht online.").color(ChatColor.RED)
                    .create());
            return false;
        }

        // check if target is in a teleportation
        if (confirmedTeleports.contains(target.getName())) {
            player.spigot().sendMessage(new ComponentBuilder(prefix)
                    .append("Der Spieler ").color(ChatColor.RED)
                    .append(target.getName()).color(Colors.PLAYER_ERROR.get())
                    .append(" befindet sich gerade in einer teleportation.").color(ChatColor.RED)
                    .create());
            return true;
        }

        // send tpa request
        sendRequest(target, player);

        // send tpa confirm to sender
        player.spigot().sendMessage(new ComponentBuilder(prefix)
                .append("Du hast dem Spieler ").color(Colors.TPA.get())
                .append(target.getName()).color(Colors.TPA_PLAYER.get())
                .append(" eine TPA geschickt. Sie läuft in 30s ab.").color(Colors.TPA.get())
                .create());

        // make teleport entry
        teleports.put(target.getName(), player.getName());
        cooldown.put(player.getName(), System.currentTimeMillis() / 1000);

        // expiration after 30s
        Bukkit.getScheduler().runTaskLater(main, () -> {
            if (confirmedTeleports.contains(player.getName())) return;
            if (teleports.containsKey(player.getName()) && teleports.get(player.getName()).equalsIgnoreCase(target.getName())) {
                player.spigot().sendMessage(new ComponentBuilder(prefix)
                        .append("Die TPA von ").color(ChatColor.RED)
                        .append(target.getName()).color(Colors.PLAYER_ERROR.get())
                        .append(" ist gerade abgelaufen.").color(ChatColor.RED)
                        .create());
                teleports.remove(player.getName());
            }
        }, 600);
        return false;
    }

    private void sendRequest(Player target, Player player) {
        // create base message
        ComponentBuilder base = new ComponentBuilder(prefix);
        base.append("Der Spieler ").color(Colors.TPA.get());
        base.append(player.getName()).color(Colors.TPA_PLAYER.get());
        base.append(" hat dir eine TPA geschickt.").color(Colors.TPA.get());
        base.append("\nSie läuft in 30s ab. ").create();

        // create accept "button"
        TextComponent accept = new TextComponent("Annehmen");
        accept.setColor(Colors.ACCEPT.get());
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Teleportanfrage annehmen")));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept"));

        // merge both together
        base.append(accept);

        // send message
        target.spigot().sendMessage(base.create());
    }

    public void cancelTeleport(Player player) {
        // check if the player in a teleport
        if (confirmedTeleports.contains(player.getName())) {
            // remove player from confirmed teleports
            confirmedTeleports.remove(player.getName());
            // send player cancel message
            player.spigot().sendMessage(new ComponentBuilder(prefix)
                    .append("Der Teleport wurde abgebrochen!").color(ChatColor.RED)
                    .create());
        }
    }
}
