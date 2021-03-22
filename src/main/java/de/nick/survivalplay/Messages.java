package de.nick.survivalplay;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public enum Messages {

    NO_PERMISSONS("Dazu hast du keine Berechtigungen!", ChatColor.RED),
    NO_PLAYER("Dieser Befehl muss von einem Spieler ausgeführt werden!", ChatColor.RED);

    private final String msg;
    private final ChatColor color;

    Messages(String msg, ChatColor color) {
        this.msg = msg;
        this.color = color;
    }

    // gets the message as a TextComponent
    public TextComponent get() {
        TextComponent component = new TextComponent(this.msg);
        component.setColor(color);
        return component;
    }
}
