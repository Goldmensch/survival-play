package de.nick.survivalplay.commands.home;

import de.nick.survivalplay.Colors;
import de.nick.survivalplay.ComponentMessages;
import de.nick.survivalplay.SurvivalPlay;
import de.nick.survivalplay.storage.IStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DelhomeCommand implements CommandExecutor {

    private final Component prefix;
    private final IStorage storage;

    public DelhomeCommand(SurvivalPlay survivalPlay) {
        prefix = Component.text("[Home] ").color(TextColor.fromHexString("#0091ff"));
        storage = survivalPlay.getStorage();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // check if sender is player
        if(!(sender instanceof Player)) {
            sender.sendMessage(ComponentMessages.NO_PLAYER.get());
            return true;
        }
        // check if args long enough
        if(args.length != 1) {
            sender.sendMessage(Component.text()
                    .append(prefix)
                    .append(Component.text("Bitte benutze /delhome <public/private>").color(NamedTextColor.RED))
                    .build());
            return true;
        }
        // cast sender to player
        Player player = (Player) sender;
        // public home
        if(args[0].equalsIgnoreCase("public")) {
            Location location = storage.getHome(false, player);
            // check if home is set
            if(location == null) {
                player.sendMessage(Component.text()
                        .append(prefix)
                        .append(Component.text("Du hast noch kein public home gesetzt!").color(NamedTextColor.RED))
                        .build());
                return true;

            }
            // set public home
            storage.setHome(false, player, null);
            storage.save();
            // send confirm message
            player.sendMessage(Component.text()
                    .append(prefix)
                    .append(Component.text("Public home wurde gelöscht.").color(Colors.HOME.getTextColor()))
                    .build());
            return true;
        }
        // private home
        if(args[0].equalsIgnoreCase("private")) {
            Location location = storage.getHome(true, player);
            // check if home is set
            if(location == null) {
                player.sendMessage(Component.text()
                        .append(prefix)
                        .append(Component.text("Du hast noch kein private home gesetzt!").color(NamedTextColor.RED))
                        .build());
                return true;

            }
            // set private home
            storage.setHome(true, player, null);
            storage.save();
            // send confirm message
            player.sendMessage(Component.text()
                    .append(prefix)
                    .append(Component.text("Private home wurde gelöscht.").color(Colors.HOME.getTextColor()))
                    .build());
            return true;
        }
        // send wrong arguments
        sender.sendMessage(Component.text()
                .append(prefix)
                .append(Component.text("Bitte benutze /delhome <public/private>").color(NamedTextColor.RED))
                .build());
        return true;
    }
}