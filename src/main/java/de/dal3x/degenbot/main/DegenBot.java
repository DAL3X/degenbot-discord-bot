package de.dal3x.degenbot.main;

import de.dal3x.degenbot.config.DefaultValues;
import de.dal3x.degenbot.discord.DiscordComponent;
import de.dal3x.degenbot.twitch.TwitchComponent;
import de.dal3x.degenbot.twitch.TwitchStream;
import io.github.cdimascio.dotenv.Dotenv;

/** The main part of the bot. This class creates the link between the discord and twitch module */
public class DegenBot {

    /** The part of the bot responsible for handling discord data */
    private final DiscordComponent discord;

    /** The part of the bot responsible for handling twitch data */
    private final TwitchComponent twitch;


    public DegenBot() {
        Dotenv environment = loadEnv();
        this.twitch = new TwitchComponent(this, environment.get("TWITCH_PROVIDER"),environment.get("TWITCH_TOKEN"));
        twitch.registerLiveListener("drhazuul_vr");
        twitch.registerLiveListener("dal3xx");
        this.discord = new DiscordComponent(this, environment.get("DISCORD_TOKEN"), environment.get("ACTIVITY"));
    }

    private Dotenv loadEnv() {
        return Dotenv.configure().directory(DefaultValues.envPath).filename(DefaultValues.envFile).load();
    }

    public static void main(String[] args) {
        new DegenBot();
    }

    public void postLiveChannel(TwitchStream stream) {
        discord.postLiveNotification(stream);
    }
}
