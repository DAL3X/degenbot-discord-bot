package de.dal3x.degenbot.main;

import de.dal3x.degenbot.config.DefaultValues;
import de.dal3x.degenbot.discord.DiscordComponent;
import de.dal3x.degenbot.twitch.TwitchComponent;
import io.github.cdimascio.dotenv.Dotenv;

public class DegenBot {

    private DiscordComponent discord;
    private TwitchComponent twitch;

    public DegenBot() {
        Dotenv environment = loadEnv();
        this.twitch = new TwitchComponent(environment.get("TWITCH_PROVIDER"),environment.get("TWITCH_TOKEN"));
        this.twitch.registerEvents();
        this.discord = new DiscordComponent(environment.get("DISCORD_TOKEN"), environment.get("ACTIVITY"));
    }

    private Dotenv loadEnv() {
        return Dotenv.configure().directory(DefaultValues.envPath).filename(DefaultValues.envFile).load();
    }

    public static void main(String[] args) {
        new DegenBot();
    }
}
