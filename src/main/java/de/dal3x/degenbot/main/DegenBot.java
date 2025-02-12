package de.dal3x.degenbot.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import de.dal3x.degenbot.config.DefaultValues;
import de.dal3x.degenbot.config.EnvNames;
import de.dal3x.degenbot.discord.DiscordComponent;
import de.dal3x.degenbot.structures.InfoPacket;
import de.dal3x.degenbot.structures.TrackingInfo;
import de.dal3x.degenbot.twitch.TwitchComponent;
import de.dal3x.degenbot.structures.TwitchStream;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The main part of the bot. This class creates the link between the discord and twitch module.
 */
public class DegenBot {

    /** Determines if the bot is in testing mode. If this is true, the bot will post events without cool-downs. */
    public static final boolean testMode = false;

    private final boolean useLocalEnvFile = true;

    /** Set of all channel ids that are on cool-down. */
    private final Set<String> cooldown;

    /** The Executor responsible for handling time sensitive events. */
    private final ScheduledExecutorService executor;

    /** The part of the bot responsible for handling discord data. */
    private final DiscordComponent discord;

    /** The part of the bot responsible for handling twitch data. */
    private final TwitchComponent twitch;

    /** The info packet containing all tracked channels and target discord channels. */
    private InfoPacket infoPacket;


    /** Creates a bot instance. Should only be called once on startup. */
    public DegenBot() {
        this.cooldown = new HashSet<>();
        this.executor = Executors.newSingleThreadScheduledExecutor();
        String provider, twitchToken, url, cooldown, discordToken, activity;
        if (useLocalEnvFile) {
            Dotenv environment = loadEnv();
            provider = environment.get(EnvNames.TWITCH_PROVIDER.toString());
            twitchToken = environment.get(EnvNames.TWITCH_TOKEN.toString());
            url = environment.get(EnvNames.TWITCH_URL.toString());
            cooldown = environment.get(EnvNames.COOLDOWN.toString());
            discordToken = environment.get(EnvNames.DISCORD_TOKEN.toString());
            activity = environment.get(EnvNames.ACTIVITY.toString());
        }
        else {
            provider = System.getenv().get(EnvNames.TWITCH_PROVIDER.toString());
            twitchToken = System.getenv().get(EnvNames.TWITCH_TOKEN.toString());
            url = System.getenv().get(EnvNames.TWITCH_URL.toString());
            cooldown = System.getenv().get(EnvNames.COOLDOWN.toString());
            discordToken = System.getenv().get(EnvNames.DISCORD_TOKEN.toString());
            activity = System.getenv().get(EnvNames.ACTIVITY.toString());
        }
        this.twitch = new TwitchComponent(this, provider, twitchToken, url, cooldown);
        loadInfoPacket();
        this.discord = new DiscordComponent(this, discordToken, activity);

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
        List<TrackingInfo> trackList = this.infoPacket.getTracking().get(stream.getName().toLowerCase());
        for (TrackingInfo track : trackList) {
            String targetChannel = track.getChannel();
            if (!track.getMessage().isEmpty()) {
                discord.postLiveNotification(targetChannel, stream, track.getMessage());
            } else {
                discord.postLiveNotification(targetChannel, stream);
            }
        }
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
            for (String channel : this.infoPacket.getTracking().keySet()){
                twitch.registerLiveListener(channel);
            }
        } catch (IOException e) {
            this.infoPacket = new InfoPacket();
        }
    }

    /** Start bacon tracking a given user id with the given server id */
    public void addToBaconTrackingList(String id, String server) {
        this.infoPacket.addBaconTracking(id, server);
        saveInfoPacket();
    }

    /** Stop bacon tracking a given user id with the given server id */
    public void removeFromBaconTrackingList(String id, String server) {
        Map<String, Set<String>> tracking = this.infoPacket.getBaconTracking();
        if (tracking.containsKey(id)) {
            Set<String> servers = tracking.get(id);
            servers.remove(server);
            if (servers.isEmpty()) {
                tracking.remove(id);
            }
            else {
                tracking.put(id, servers);
            }
        }
        this.infoPacket.setBaconTracking(tracking);
        saveInfoPacket();
    }

    /** Adds a channel with the given name, target channel and optional message to the list of tracked channels and saves it. */
    public void addToTrackingList(String name, String server, String channel, String message) {
        if (!this.infoPacket.getTracking().containsKey(name.toLowerCase())) {
            this.twitch.registerLiveListener(name);
        }
        this.infoPacket.addTracking(name.toLowerCase(), new TrackingInfo(server, channel, message));
        saveInfoPacket();
    }

    /** Removes a channel with the given name from the list of tracked channels and saves it. */
    public void removeFromTrackingList(String name, String server) {
        Map<String, List<TrackingInfo>> tracking = this.infoPacket.getTracking();
        List<TrackingInfo> list = tracking.get(name);
        List<Integer> toRemove = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getServer().equalsIgnoreCase(server)) {
                toRemove.add(i);
            }
        }
        for (int remove : toRemove) {
            list.remove(remove);
        }
        if (list.isEmpty()) {
            this.cooldown.remove(name.toLowerCase());
            this.twitch.unregisterLiveListener(name);
        }
        tracking.put(name, list);
        this.infoPacket.setTracking(tracking);
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

    /** Checks if a channel is on cooldown. */
    public boolean hasCooldown(String channelID) {
        return this.cooldown.contains(channelID.toLowerCase());
    }

    /** Sets a channel cooldown to a specified number of minutes. */
    public void setCooldown(String name, int minutes) {
        if (testMode) {
            return;
        }
        this.cooldown.add(name.toLowerCase());
        Runnable task = () -> this.cooldown.remove(name.toLowerCase());
        this.executor.schedule(task, minutes, TimeUnit.MINUTES);
    }

    /** Returns the InfoPacket containing all life values for this bot */
    public InfoPacket getInfoPacket() {
        return infoPacket;
    }
}
