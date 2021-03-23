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
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SethomeCommand implements CommandExecutor, TabCompleter {

    private final Component prefix;
    private final IStorage storage;

    public SethomeCommand(SurvivalPlay survivalPlay) {
        prefix = Component.text("[Home] ").color(TextColor.fromHexString("#0091ff"));
        storage = survivalPlay.getStorage();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // check if sender is player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ComponentMessages.NO_PLAYER.get());
            return true;
        }
        // check if args long enough
        if (args.length != 1) {
            sender.sendMessage(Component.text()
                    .append(prefix)
                    .append(Component.text("Bitte benutze /sethome <public/private>").color(NamedTextColor.RED))
                    .build());
            return true;
        }
        // cast sender to player
        Player player = (Player) sender;
        // get players location
        Location location = player.getLocation();
        // public home
        if (args[0].equalsIgnoreCase("public")) {
            // set public home
            storage.setHome(false, player, location);
            storage.save();
            // send confirm message
            player.sendMessage(Component.text()
                    .append(prefix)
                    .append(Component.text("Public home wurde gesetzt.").color(Colors.HOME.getTextColor()))
                    .build());
            return true;
        }
        // private home
        if (args[0].equalsIgnoreCase("private")) {
            // set private home
            storage.setHome(true, player, location);
            storage.save();
            // send confirm message
            player.sendMessage(Component.text()
                    .append(prefix)
                    .append(Component.text("Private home wurde gesetzt.").color(Colors.HOME.getTextColor()))
                    .build());
            return true;
        }
        // send wrong arguments
        sender.sendMessage(Component.text()
                .append(prefix)
                .append(Component.text("Bitte benutze /sethome <public/private>").color(NamedTextColor.RED))
                .build());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completion = new ArrayList<>();
        if (args.length == 1) {
            if ("private".startsWith(args[0])) completion.add("private");
            if ("public".startsWith(args[0])) completion.add("public");
        }
        return completion;
    }
}
