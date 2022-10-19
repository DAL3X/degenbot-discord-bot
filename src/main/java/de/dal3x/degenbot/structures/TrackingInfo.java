package de.dal3x.degenbot.structures;

/** Contains the tracking information for a twitch channel. Is serializable and gets saved. */
public class TrackingInfo {

    /** The discord channel id for posting when this channel goes online. */
    private String channel;

    /** An optional message that gets send when the channel goes live. */
    private String message;

    /** Default constructor for deserialization. */
    public TrackingInfo() {}

    /** Create a new TrackingInfo object with the given parameters */
    public TrackingInfo(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    /** Sets the discord channel id for posting when this channel goes online. */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /** Sets the optional message that gets send when the channel goes live. */
    public void setMessage(String message) {
        this.message = message;
    }

    /** Returns the discord channel id for posting when this channel goes online. */
    public String getChannel() {
        return channel;
    }

    /** Returns the optional message that gets send when the channel goes live. */
    public String getMessage() {
        return message;
    }
}
