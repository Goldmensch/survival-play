package de.nick.survivalplay;

import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;

public enum Colors {

    PLAYER_ERROR("#bf4b39"),
    TPA("#f7d600"),
    TPA_PLAYER("#bab700"),
    ACCEPT("#73ff00"),
    GAMEMODE("#ff00e6"),
    GAMEMODE_PLAYER("#fc5bed"),
    GAMEMODE_GAMEMODE("#9e3694"),
    SPAWN("#fff600"),
    MSG("#66fffa"),
    MSG_INFO("#00aba5"),
    DISCORD("#7289da"),
    DISCORD_LINK("#4c64c7"),
    RTP("#ff5c5c"),
    RTP_PLAYER("#b50000"),
    NIGHTSKIP("#45ff57"),
    NIGHTSKIP_NUMBER("#00bd12"),
    HOME("#4db2ff"),
    INFO("#4affc0"),
    INFO_LIST("#00cc85"),
    DONATION_LINK("#ffdf3d"),
    RAID("#f7847c"),
    RAID_CLAN("#bf0d00");


    private final String hex;

    Colors(String of) {
        this.hex = of;
    }

    // gets the Chatcolor of the hex code
    public ChatColor get() {
        return ChatColor.of(hex);
    }

    // gets the textcolor of the hex code
    public TextColor getTextColor() {
        return TextColor.fromHexString(hex);
    }
}
