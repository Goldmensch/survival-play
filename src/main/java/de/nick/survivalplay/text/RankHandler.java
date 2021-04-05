package de.nick.survivalplay.text;

import de.nick.smartclans.api.SmartclansAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

public class RankHandler {

    private final SmartclansAPI smartclansAPI;

    public RankHandler(SmartclansAPI smartclansAPI) {
        this.smartclansAPI = smartclansAPI;
    }

    // check rank and return prefix as text component
    private Component getRankPrefix(Player player) {
        if (player.hasPermission("server.owner")) {
            return Component.text("[Owner] ").color(NamedTextColor.DARK_RED);
        }
        if (player.hasPermission("server.admin")) {
            return Component.text("[Admin] ").color(NamedTextColor.RED);
        }
        if (player.hasPermission("server.dev")) {
            return Component.text("[Dev] ").color(NamedTextColor.AQUA);
        }
        if (player.hasPermission("server.mod")) {
            return Component.text("[Mod] ").color(NamedTextColor.GREEN);
        }
        if (player.hasPermission("server.sup")) {
            return Component.text("[Sup] ").color(NamedTextColor.YELLOW);
        }
        if(player.hasPermission("server.manager")) {
            return Component.text("[Manager] ").color(TextColor.fromHexString("#ff575c"));
        }
        if (player.hasPermission("server.builder")) {
            return Component.text("[Builder] ").color(NamedTextColor.DARK_AQUA);
        }
        if (player.hasPermission("server.vip")) {
            return Component.text("[VIP] ").color(TextColor.fromHexString("#63ffed"));
        }
        return Component.text("[Spieler] ").color(NamedTextColor.GRAY);
    }

    // gets the color of the rank
    public TextColor getRankColor(Player player) {
        if (player.hasPermission("server.owner")) {
            return NamedTextColor.DARK_RED;
        }
        if (player.hasPermission("server.admin")) {
            return NamedTextColor.RED;
        }
        if (player.hasPermission("server.dev")) {
            return NamedTextColor.AQUA;
        }
        if (player.hasPermission("server.mod")) {
            return NamedTextColor.GREEN;
        }
        if (player.hasPermission("server.sup")) {
            return NamedTextColor.YELLOW;
        }
        if(player.hasPermission("server.manager")) {
            return TextColor.fromHexString("#ff575c");
        }
        if (player.hasPermission("server.builder")) {
            return NamedTextColor.DARK_AQUA;
        }
        if (player.hasPermission("server.vip")) {
            return TextColor.fromHexString("#ff575c");
        }
        return NamedTextColor.GRAY;
    }

    public TextColor getTeamColor(Player player) {
        if (getRankColor(player) != NamedTextColor.GRAY) {
            return NamedTextColor.GREEN;
        } else {
            return NamedTextColor.GRAY;
        }
    }

    // update the tablist name of the Player
    public void updateTabName(Player player) {
        Component name = Component.text()
                .append(getRankPrefix(player))
                .append(Component.text(player.getName()).color(getRankColor(player)))
                .build();
        // check if player is in clan if not set only prefix
        if (smartclansAPI.getPlayerapi().isInClan(player)) {
            Component clanrank;
            switch (smartclansAPI.getPlayerapi().getPosition(player)) {
                case "member":
                    clanrank = Component.text(" [Mitglied | ").color(NamedTextColor.DARK_GREEN);
                    break;
                case "coleader":
                    clanrank = Component.text(" [Co-Leitung | ").color(NamedTextColor.DARK_GREEN);
                    break;
                case "leader":
                    clanrank = Component.text(" [Leitung | ").color(NamedTextColor.DARK_GREEN);
                    break;
                default:
                    clanrank = Component.text("").color(NamedTextColor.DARK_GREEN);
                    break;
            }
            player.playerListName(Component.text()
                    .append(name)
                    .append(clanrank)
                    .append(Component.text(smartclansAPI.getPlayerapi().getClannameAsString(player)).color(NamedTextColor.DARK_GREEN))
                    .append(Component.text("]").color(NamedTextColor.DARK_GREEN))
                    .build());
        } else {
            player.playerListName(name);
        }
    }


}
