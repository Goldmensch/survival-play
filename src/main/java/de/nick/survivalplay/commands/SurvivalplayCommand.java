package de.nick.survivalplay.commands;

import de.nick.survivalplay.Messages;
import de.nick.survivalplay.SurvivalPlay;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class SurvivalplayCommand implements CommandExecutor, TabCompleter {

    private final SurvivalPlay survivalPlay;

    public SurvivalplayCommand(SurvivalPlay survivalPlay) {
        this.survivalPlay = survivalPlay;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // check if args[0] is reload
        if (args[0].equalsIgnoreCase("reload")) {
            reload(sender);
            return true;
        }
        sender.sendMessage(new ComponentBuilder(survivalPlay.getPrefix())
                .append("Survival-play.de Server System, Version: ").color(ChatColor.RED)
                .append(survivalPlay.getDescription().getVersion()).color(ChatColor.DARK_RED)
                .create());
        return false;
    }

    private void reload(CommandSender sender) {
        // check if sender has Permission
        if (sender.hasPermission("survivalplay.reload")) {
            // simulate restart
            survivalPlay.getLogger().info("simulate restart... || please ignore: unregistered PluginClassLoader");
            // > disable Plugin
            Bukkit.getPluginManager().disablePlugin(survivalPlay);
            // > enable Plugin
            Bukkit.getPluginManager().enablePlugin(survivalPlay);
            sender.sendMessage(new ComponentBuilder(survivalPlay.getPrefix())
                    .append("Plugin erfolgreich reloaded!").color(ChatColor.GREEN)
                    .create());
        } else {
            sender.sendMessage(Messages.NO_PERMISSONS.get());
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("survivalplay.reload") && "reload".startsWith(args[0].toLowerCase())) completions.add("reload");
        }
        return completions;
    }
}
