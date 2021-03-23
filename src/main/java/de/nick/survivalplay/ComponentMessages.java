package de.nick.survivalplay;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum ComponentMessages {

    //NO_PERMISSONS("Dazu hast du keine Berechtigungen!", NamedTextColor.RED),
    NO_PLAYER("Dieser Befehl muss von einem Spieler ausgeführt werden!", NamedTextColor.RED);

    private final String msg;
    private final TextColor textColor;

    ComponentMessages(String msg, NamedTextColor color) {
        this.msg = msg;
        this.textColor = color;
    }

    public Component get() {
        return Component.text(msg).color(textColor);
    }
}
