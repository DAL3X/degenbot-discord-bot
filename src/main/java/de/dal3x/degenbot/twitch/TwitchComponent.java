package de.dal3x.degenbot.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import de.dal3x.degenbot.main.DegenBot;

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

    /** Builds the Twitch Component by opening a connection to twitch with the token using the given provider. */
    public TwitchComponent(DegenBot bot, String provider, String token, String url) {
        this.degenbot = bot;
        OAuth2Credential credential = new OAuth2Credential(provider, token);
        this.component = TwitchClientBuilder.builder()
                .withDefaultAuthToken(credential)
                .withEnableHelix(true)
                //.withEnableChat(true)
                //.withChatAccount(credential)
                .withDefaultEventHandler(SimpleEventHandler.class)
                .build();
        this.twitchURL = url;
        activateLiveListener();
    }

    /** Activates the live listener. Only call this once on startup. */
    private void activateLiveListener() {
        this.component.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelGoLiveEvent.class,event -> {
            String link = this.twitchURL + event.getChannel().getName();
            TwitchStream streamInfo = new TwitchStream(event.getChannel().getName(), event.getStream().getTitle(), event.getStream().getGameName(), link, event.getStream().getThumbnailUrl());
            this.degenbot.postLiveChannel(streamInfo);
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
