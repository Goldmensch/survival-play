package de.nick.survivalplay;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;

public class Broadcaster {

    private final SurvivalPlay survivalPlay;

    public Broadcaster(SurvivalPlay survivalPlay) {
        this.survivalPlay = survivalPlay;
    }

    private Component buildDiscordComponent() {
        return Component.text()
                .append(Component.text("[Discord] ").color(TextColor.fromHexString("#4868d9")))
                .append(Component.text("Du hast keine Freunde? Kein Problem! Joinen einfach unserem Discord und du wirst bestimmt welche finden. " +
                        "Wenn du ein Problem hast kannst du dich dort auch an den Support wenden. Link: ").color(Colors.DISCORD.getTextColor()))
                .append(Component.text(survivalPlay.getConfigHandler().getDiscordLink()).color(Colors.DISCORD_LINK.getTextColor())
                        .clickEvent(ClickEvent.openUrl(survivalPlay.getConfigHandler().getDiscordLink())))
                .build();
    }

    private Component buildDonationComponent() {
        return Component.text()
                .append(Component.text("[Spenden] ").color(TextColor.fromHexString("#a88c00")))
                .append(Component.text("Dir gefällt der Server und du möchtest uns freiwillig unterstützen? Dann spende doch mal ein paar Euro. " +
                        "Ihr bekommt dadurch natürlich keine Vorteile, aber uns hilft es den Server weiterzufinanzieren. Link: ")
                .color(NamedTextColor.GOLD))
                .append(Component.text(survivalPlay.getConfigHandler().getDonationLink()).color(Colors.DONATION_LINK.getTextColor())
                        .clickEvent(ClickEvent.openUrl(survivalPlay.getConfigHandler().getDonationLink())))
                .build();
    }

    public void start(){
        Component disord = buildDiscordComponent();
        Component donation = buildDonationComponent();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(survivalPlay, () -> Bukkit.getServer().sendMessage(disord),0, 36000);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(survivalPlay, () -> Bukkit.getServer().sendMessage(donation),12000, 36000);
    }

}
