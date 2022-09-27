package de.dal3x.degenbot.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

/**
 * The twitch component of the bot. It is responsible all for interactions with the twitch API.
 */
public class TwitchComponent {

    /** The TwitchClient component, which characterizes the connection to twitch. */
    private TwitchClient component;

    /** Builds the Twitch Component by opening a connection to twitch with the token using the given provider. */
    public TwitchComponent(String provider, String token) {
        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withDefaultAuthToken(new OAuth2Credential(provider, token))
                .withEnableHelix(true)
                .withDefaultEventHandler(SimpleEventHandler.class)
                .build();
    }

    /** Returns the TwitchClient component. */
    public TwitchClient getComponent() {
        return component;
    }
}
