package de.nick.survivalplay.commands;

import de.nick.survivalplay.Colors;
import de.nick.survivalplay.SurvivalPlay;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class DiscordCommand implements CommandExecutor {

    private final SurvivalPlay survivalPlay;

    private final TextComponent prefix;

    public DiscordCommand(SurvivalPlay survivalPlay) {
        prefix = new TextComponent("[Discord] ");
        prefix.setColor(ChatColor.of("#4868d9"));
        this.survivalPlay = survivalPlay;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // send player discord link
        sender.sendMessage(new ComponentBuilder(prefix)
                .append("Unser discord: ").color(Colors.DISCORD.get())
                .append(survivalPlay.getConfigHandler().getDiscordLink()).color(Colors.DISCORD_LINK.get())
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, survivalPlay.getConfigHandler().getDiscordLink()))
                .create());
        return false;
    }
}
