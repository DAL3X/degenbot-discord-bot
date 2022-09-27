package de.dal3x.degenbot.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

/**
 * The discord component of the bot. It is responsible all for interactions with the discord API.
 */
public class DiscordComponent {

    /** The JDA component, which characterizes the connection to discord. */
    private JDA component;

    /** Builds the Discord Component by opening a connection with a bot-token and activity for the online bot. */
    public DiscordComponent(String token, String activity) {
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setActivity(Activity.playing(activity));
        this.component = builder.build();
    }

    /** Returns the JDA component. */
    public JDA getComponent() {
        return this.component;
    }
}
