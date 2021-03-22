package de.nick.survivalplay.commands.home;

import de.nick.survivalplay.ComponentMessages;
import de.nick.survivalplay.SurvivalPlay;
import de.nick.survivalplay.storage.IStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeCommand implements CommandExecutor {

    private final IStorage storage;
    private final Component prefix;

    public HomeCommand(SurvivalPlay survivalPlay) {
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
        Player player = (Player) sender;
        return true;
    }
}
