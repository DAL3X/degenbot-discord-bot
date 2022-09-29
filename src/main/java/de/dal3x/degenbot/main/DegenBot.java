package de.dal3x.degenbot.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import de.dal3x.degenbot.config.DefaultValues;
import de.dal3x.degenbot.discord.DiscordComponent;
import de.dal3x.degenbot.serializable.InfoPacket;
import de.dal3x.degenbot.twitch.TwitchComponent;
import de.dal3x.degenbot.twitch.TwitchStream;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * The main part of the bot. This class creates the link between the discord and twitch module.
 */
public class DegenBot {

    /** The part of the bot responsible for handling discord data. */
    private final DiscordComponent discord;

    /** The part of the bot responsible for handling twitch data. */
    private final TwitchComponent twitch;

    /** The info packet containing all tracked channels and target discord channels. */
    private InfoPacket infoPacket;

    /** Creates a bot instance. Should only be called once on startup. */
    public DegenBot() {
        Dotenv environment = loadEnv();
        this.twitch = new TwitchComponent(this, environment.get("TWITCH_PROVIDER"), environment.get("TWITCH_TOKEN"), environment.get("TWITCH_URL"));
        loadInfoPacket();
        this.discord = new DiscordComponent(this, environment.get("DISCORD_TOKEN"), environment.get("ACTIVITY"));
    }

    /** Loads the environment to obtain all needed constants and tokens. */
    private Dotenv loadEnv() {
        return Dotenv.configure().directory(DefaultValues.envPath).filename(DefaultValues.envFile).load();
    }

    /** Main method creates a bot instance on start. */
    public static void main(String[] args) {
        new DegenBot();
    }

    /** Delegates the live post to the discord component. */
    public void postLiveChannel(TwitchStream stream) {
        discord.postLiveNotification(this.infoPacket.getDiscordTarget(), stream);
    }

    /** Loads all tracked channels from a file and enables their tracking. Should only be called on start once. */
    private void loadInfoPacket() {
        File trackingFile = new File(DefaultValues.trackingFile);
        if(!trackingFile.exists()) {
            this.infoPacket = new InfoPacket();
            saveInfoPacket();
            return;
        }
        ObjectMapper mapper = new YAMLMapper();
        try {
            this.infoPacket = mapper.readValue(trackingFile, InfoPacket.class);
            for (String channel : this.infoPacket.getTracking()){
                twitch.registerLiveListener(channel);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Adds a channel with the given name to the list of tracked channels and saves it. */
    public void addToTrackingList(String name) {
        Set<String> tracking = this.infoPacket.getTracking();
        tracking.add(name);
        this.infoPacket.setTracking(tracking);
        this.twitch.registerLiveListener(name);
        saveInfoPacket();
    }

    /** Removes a channel with the given name from the list of tracked channels and saves it. */
    public void removeFromTrackingList(String name) {
        Set<String> tracking = this.infoPacket.getTracking();
        tracking.remove(name);
        this.infoPacket.setTracking(tracking);
        this.twitch.unregisterLiveListener(name);
        saveInfoPacket();
    }

    /** Serializes and saves the list of all tracked channels. */
    private void saveInfoPacket() {
        File trackingFile = new File(DefaultValues.trackingFile);
        ObjectMapper mapper = new YAMLMapper();
        try {
            mapper.writeValue(trackingFile, this.infoPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Changes and saves the channel ID of the discord channel to post notifications in. */
    public void updateDiscordTarget(String id) {
        this.infoPacket.setDiscordTarget(id);
        saveInfoPacket();
    }

}
