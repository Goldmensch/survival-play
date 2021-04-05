package de.nick.survivalplay.listeners;

import de.nick.smartclans.api.SmartclansAPI;
import de.nick.survivalplay.text.MessageFormatter;
import de.nick.survivalplay.text.RankHandler;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@SuppressWarnings("deprecation")
public class ChatListener implements Listener {

    private final MessageFormatter messageFormatter;
    private final RankHandler rankHandler;

    public ChatListener(SmartclansAPI smartclansAPI) {
        messageFormatter = new MessageFormatter(smartclansAPI);
        rankHandler = new RankHandler(smartclansAPI);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.formatter((displayName, message) -> Component.text()
                .append(messageFormatter.getUpdatedChatPrefix(event.getPlayer()))
                .append(Component.text(": ").color(rankHandler.getTeamColor(event.getPlayer())))
                .append(getFormattedMessage(event.message(), event.getPlayer()))
                .build());
    }

    private Component getFormattedMessage(Component message, Player player) {
        if(player.hasPermission("colorcodes.use")) {
            String msgString = LegacyComponentSerializer.legacyAmpersand().serialize(message);
            return LegacyComponentSerializer.legacyAmpersand().deserialize(msgString);
        }
        return message;
    }

}
