package de.nick.survivalplay.text;

import de.nick.smartclans.api.SmartclansAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class MessageFormatter {

    private final SmartclansAPI smartclansAPI;
    private final RankHandler rankHandler;

    public MessageFormatter(SmartclansAPI smartclansAPI) {
        this.smartclansAPI = smartclansAPI;
        rankHandler = new RankHandler(smartclansAPI);
    }

    public TextComponent getUpdatedChatPrefix(Player player) {
        // check if player is in clan
        if(smartclansAPI.getPlayerapi().isInClan(player)) {
            // return Prefix with Clanname and Playername
            return Component.text()
                    .append(Component.text("[").color(NamedTextColor.DARK_GREEN))
                    .append(Component.text(smartclansAPI.getPlayerapi().getClannameAsString(player)).color(NamedTextColor.DARK_GREEN))
                    .append(Component.text("] ").color(NamedTextColor.DARK_GREEN))
                    .append(Component.text(player.getName())).color(rankHandler.getTeamColor(player))
                    .build();

        }else {
            // return Prefix with "Wilder" as Prefix and Playername
            return Component.text()
                    .append(Component.text("[ohne Clan] ").color(NamedTextColor.DARK_GREEN))
                    .append(Component.text(player.getName())).color(rankHandler.getTeamColor(player))
                    .build();
        }
    }

}
