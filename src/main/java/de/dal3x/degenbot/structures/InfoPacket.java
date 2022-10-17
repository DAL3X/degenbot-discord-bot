package de.dal3x.degenbot.structures;

import java.util.HashMap;
import java.util.Map;

/** A serializable object holding information to be saved and loaded in the YAML format. */
public class InfoPacket {

    /** The set of tracked streamer IDs. */
    private Map<String, TrackingInfo> tracking;

    /** The channel ID of the discord channel to post notifications in.  */
    private String discordDefaultTarget;

    /** Creates an empty InfoPacket. */
    public InfoPacket() {
        this.tracking = new HashMap<>();
        this.discordDefaultTarget = "";
    }

    /** Returns the set of tracked streamer IDs. */
    public Map<String, TrackingInfo> getTracking() {
        return tracking;
    }

    /** Sets the set of tracked streamer IDs. */
    public void setTracking(Map<String, TrackingInfo> tracking) {
        this.tracking = tracking;
    }

    /** Returns the channel ID of the discord channel to post notifications in. */
    public String getDiscordDefaultTarget() {
        return discordDefaultTarget;
    }

    /** Sets the channel ID of the discord channel to post notifications in. */
    public void setDiscordDefaultTarget(String discordTarget) {
        this.discordDefaultTarget = discordTarget;
    }
}
