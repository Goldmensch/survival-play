package de.nick.survivalplay.commands;

import de.nick.survivalplay.Colors;
import de.nick.survivalplay.Messages;
import de.nick.survivalplay.PluginUtils;
import de.nick.survivalplay.SurvivalPlay;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class RtpCommand implements CommandExecutor {

    private final TextComponent prefix;

    private final List<String> lastPlayers;
    private String lastPlayer;

    private final SurvivalPlay survivalPlay;

    public RtpCommand(SurvivalPlay survivalPlay) {
        prefix = new TextComponent("[RTP] ");
        prefix.setColor(ChatColor.of("#ff0000"));
        lastPlayers = new ArrayList<>();
        lastPlayer = "";
        this.survivalPlay = survivalPlay;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // check if sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.NO_PLAYER.get());
            return true;
        }
        // check if sender has permission to use
        if (sender.hasPermission("rtp.use")) {
            // get player
            Player player = (Player) sender;
            // get list of visible players
            List<Player> visiblePlayers = PluginUtils.getVisiblePlayers();
            // if 1 or less playes visibel online - break
            if (visiblePlayers.size() <= 1) {
                sender.sendMessage(new ComponentBuilder(prefix)
                        .append("Zu wenig Spieler online!").color(ChatColor.RED)
                        .create());
                return true;
            }
            // definie target
            Player target;
            // define and init boolean for loop
            boolean loop = false;
            // check and define subtract variable
            int subtract;
            if (PluginUtils.isVanished(player)) {
                subtract = 0;
            } else {
                subtract = 1;
            }
            // check if less visible or players online as reqired for lastteleports in config // loop until player is ok
            if (visiblePlayers.size() - subtract <= survivalPlay.getConfigHandler().getLastPlayersAmount()) {
                // yes - less players // do random algo with only 1 last player
                do {
                    // get random player
                    target = getRandomVisiblePlayer(visiblePlayers);
                    // check if player is last player -no -> set loop to false
                    if (!lastPlayer.equals(target.getName())) {
                        loop = false;
                    }
                    // check if player is sender -yes -> set loop to true
                    if (target.getName().equals(sender.getName())) {
                        loop = true;
                    }
                } while (loop);
                // no - enough player // do new random algo with all last players (in config)
            } else {
                do {
                    // get random player
                    target = getRandomVisiblePlayer(visiblePlayers);
                    // check if last players contain player -no -> set loop to false
                    if (!lastPlayers.contains(target.getName())) {
                        loop = false;
                    }
                    // check if player is sender -yes -> set loop to true
                    if (target.getName().equals(sender.getName())) {
                        loop = true;
                    }
                } while (loop);
            }
            // set last player
            lastPlayer = target.getName();
            // add to last players
            lastPlayers.add(target.getName());
            // check if last players bigger than allowed -yes remove oldest player
            if (lastPlayers.size() > survivalPlay.getConfigHandler().getLastPlayersAmount()) {
                lastPlayers.remove(0);
            }
            // define efective finalTarget (for lambda)
            Player finalTarget = target;
            // teleport player async
            player.teleportAsync(target.getLocation()).thenRun(() -> player.sendMessage(new ComponentBuilder(prefix)
                    .append("Du wurdest zu ").color(Colors.RTP.get())
                    .append(finalTarget.getName()).color(Colors.RTP_PLAYER.get())
                    .append(" teleportiert.").color(Colors.RTP.get())
                    .create()));
        } else {
            sender.sendMessage(Messages.NO_PERMISSONS.get());
        }
        return false;
    }

    // gets an random visible Player
    private Player getRandomVisiblePlayer(List<Player> players) {
        int randomInt = new Random().nextInt(players.size());
        return (Player) players.toArray()[randomInt];
    }

    // check and remove player from last player(s)
    public void checkAndRemoveLast(Player player) {
        if (lastPlayer.equals(player.getName())) {
            lastPlayer = "";
        }
        lastPlayers.remove(player.getName());
    }
}
