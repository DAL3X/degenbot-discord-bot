package de.dal3x.degenbot.discord;

import de.dal3x.degenbot.main.DegenBot;
import de.dal3x.degenbot.structures.TwitchStream;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/**
 * The discord component of the bot. It is responsible all for interactions with the discord API.
 */
public class DiscordComponent {

    /** The JDA component, which characterizes the connection to discord. */
    private final JDA component;

    /** Builds the Discord Component by opening a connection with a bot-token and activity for the online bot. */
    public DiscordComponent(DegenBot bot, String token, String activity) {
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setActivity(Activity.playing(activity));
        CommandExecutor executor = new CommandExecutor(bot);
        builder.addEventListeners(executor);
        MessageListener messageListener = new MessageListener(bot);
        builder.addEventListeners(messageListener);
        this.component = builder.build();
        try {
            this.component.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.registerCommands(this.component);
    }

    /** Posts the live notification for a given stream without a message. */
    public void postLiveNotification(String targetChannel, TwitchStream stream) {
        TextChannel channel = component.getTextChannelById(targetChannel);
        if (channel != null) {
            MessageEmbed embed = EmbedFactory.createEmbed(stream);
            channel.sendMessageEmbeds(embed).queue();
        }
    }

    /** Posts the live notification for a given stream with a message. */
    public void postLiveNotification(String targetChannel, TwitchStream stream, String message) {
        TextChannel channel = component.getTextChannelById(targetChannel);
        if (channel != null) {
            MessageEmbed embed = EmbedFactory.createEmbed(stream);
            MessageCreateData messageData = new MessageCreateBuilder()
                    .setContent(message)
                    .setEmbeds(embed)
                    .build();
            channel.sendMessage(messageData).queue();
        }
    }

}
