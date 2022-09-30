package de.dal3x.degenbot.structures;

import java.util.HashSet;
import java.util.Set;

/** A serializable object holding information to be saved and loaded in the YAML format. */
public class InfoPacket {

    /** The set of tracked streamer IDs. */
    private Set<String> tracking;

    /** The channel ID of the discord channel to post notifications in.  */
    private String discordTarget;

    /** Creates an empty InfoPacket. */
    public InfoPacket() {
        this.tracking = new HashSet<>();
        this.discordTarget = "";
    }

    /** Returns the set of tracked streamer IDs. */
    public Set<String> getTracking() {
        return tracking;
    }

    /** Sets the set of tracked streamer IDs. */
    public void setTracking(Set<String> tracking) {
        this.tracking = tracking;
    }

    /** Returns the channel ID of the discord channel to post notifications in. */
    public String getDiscordTarget() {
        return discordTarget;
    }

    /** Sets the channel ID of the discord channel to post notifications in. */
    public void setDiscordTarget(String discordTarget) {
        this.discordTarget = discordTarget;
    }
}
