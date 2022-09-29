package de.dal3x.degenbot.discord;

import de.dal3x.degenbot.main.DegenBot;
import de.dal3x.degenbot.twitch.TwitchStream;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

/**
 * The discord component of the bot. It is responsible all for interactions with the discord API.
 */
public class DiscordComponent {

    /** The JDA component, which characterizes the connection to discord. */
    private final JDA component;

    /** An instance of the bot which this component belongs to. */
    private final DegenBot degenbot;

    /** Builds the Discord Component by opening a connection with a bot-token and activity for the online bot. */
    public DiscordComponent(DegenBot bot, String token, String activity) {
        this.degenbot = bot;
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setActivity(Activity.playing(activity));
        CommandExecutor executor = new CommandExecutor(bot);
        builder.addEventListeners(executor);
        this.component = builder.build();
        try {
            this.component.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.registerCommands(this.component);
    }

    /** Posts the live notification for a given stream. */
    public void postLiveNotification(String targetChannel, TwitchStream stream) {
        TextChannel channel = component.getTextChannelById(targetChannel);
        channel.sendMessage(stream.getName() + " just went live playing [" + stream.getGame() + "]: \n" + stream.getTitle()).queue();
        channel.sendMessage(stream.getLink()).queue();
    }

}
