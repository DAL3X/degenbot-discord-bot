package de.dal3x.degenbot.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import de.dal3x.degenbot.main.DegenBot;
import de.dal3x.degenbot.structures.TwitchStream;

/**
 * The twitch component of the bot. It is responsible all for interactions with the twitch API.
 */
public class TwitchComponent {

    /** The TwitchClient component, which characterizes the connection to twitch. */
    private final TwitchClient component;

    /** An instance of the bot which this component belongs to. */
    private final DegenBot degenbot;

    /** The url used to access the twitch servers. */
    private final String twitchURL;

    /** The default cooldown length. */
    private final int defaultCoolDown;

    /** Builds the Twitch Component by opening a connection to twitch with the token using the given provider. */
    public TwitchComponent(DegenBot bot, String provider, String token, String url, String defaultCD) {
        this.degenbot = bot;
        this.defaultCoolDown = Integer.parseInt(defaultCD);
        OAuth2Credential credential = new OAuth2Credential(provider, token);
        this.component = TwitchClientBuilder.builder()
                .withDefaultAuthToken(credential)
                .withEnableHelix(true)
                //.withEnableChat(true)
                //.withChatAccount(credential)
                .withDefaultEventHandler(SimpleEventHandler.class)
                .build();
        this.twitchURL = url;
        activateListener();
    }

    /** Activates the live listener. Only call this once on startup. */
    private void activateListener() {
        this.component.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelGoLiveEvent.class,event -> {
            if (this.degenbot.hasCooldown(event.getChannel().getName())) {
                // If on cooldown, just ignore everything
                return;
            }
            String link = this.twitchURL + event.getChannel().getName().toLowerCase();
            String category = this.twitchURL + "directory/game/" + event.getStream().getGameName();
            TwitchStream streamInfo = new TwitchStream(event.getChannel().getName(), event.getChannel().getId(),
                    event.getStream().getTitle(), event.getStream().getGameName(),  link,
                    event.getStream().getThumbnailUrl(1280, 720)+"?t="+System.currentTimeMillis(), category);
            this.degenbot.postLiveChannel(streamInfo);
        });

        this.component.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelGoOfflineEvent.class, event -> {
            String name = event.getChannel().getName();
            if (!this.degenbot.hasCooldown(name)) {
                this.degenbot.setCooldown(name, this.defaultCoolDown);
            }
        });
    }

    /** Registers an event listener if the channel with the provided name goes live. */
    public void registerLiveListener(String channel) {
        this.component.getClientHelper().enableStreamEventListener(channel);
    }

    /** Unregisters an event listener if the channel with the provided name goes live. */
    public void unregisterLiveListener(String channel) {
        this.component.getClientHelper().disableStreamEventListener(channel);
    }

}
