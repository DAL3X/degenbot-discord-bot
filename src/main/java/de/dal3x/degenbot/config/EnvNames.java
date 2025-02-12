package de.dal3x.degenbot.config;

/** Encapsulates all the config labels statically */
public enum EnvNames {
    TWITCH_PROVIDER("TWITCH_PROVIDER"),
    TWITCH_TOKEN("TWITCH_TOKEN"),
    TWITCH_URL("TWITCH_URL"),
    COOLDOWN("COOLDOWN"),
    DISCORD_TOKEN("DISCORD_TOKEN"),
    ACTIVITY("ACTIVITY");

    public final String label;

    EnvNames(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
