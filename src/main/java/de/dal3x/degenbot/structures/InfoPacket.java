package de.dal3x.degenbot.structures;

import java.util.*;

/** A serializable object holding information to be saved and loaded in the YAML format. */
public class InfoPacket {

    /** The map of tracked streamer IDs and their TrackingInfo List. */
    private Map<String, List<TrackingInfo>> tracking;

    /** The list of bacon tracked user IDs */
    private Map<String, Set<String>> baconTracking;

    /** Creates an empty InfoPacket. */
    public InfoPacket() {
        this.tracking = new HashMap<>();
        this.baconTracking = new HashMap<>();
    }

    /** Sets the bacon tracked user IDs */
    public Map<String, Set<String>> getBaconTracking() {
        return baconTracking;
    }

    /** Gets the bacon tracked user IDs */
    public void setBaconTracking(Map<String, Set<String>> baconTracking) {
        this.baconTracking = baconTracking;
    }

    /** Returns the set of tracked streamer IDs. */
    public Map<String, List<TrackingInfo>> getTracking() {
        return tracking;
    }

    /** Sets the set of tracked streamer IDs. */
    public void setTracking(Map<String, List<TrackingInfo>> tracking) {
        this.tracking = tracking;
    }

    /** Adds a single new element to tracking info map or adds to entries if present */
    public void addTracking(String channelID, TrackingInfo info) {
        List<TrackingInfo> list = new LinkedList<>();
        if (this.tracking.containsKey(channelID)) {
            list = this.tracking.get(channelID);
        }
        list.add(info);
        this.tracking.put(channelID, list);
    }

    /** Adds a single new item to bacon tracking or adds to entry of present */
    public void addBaconTracking(String id, String server) {
        Set<String> servers = new HashSet<>();
        if (this.baconTracking.containsKey(id)) {
            servers = this.baconTracking.get(id);
        }
        servers.add(server);
        this.baconTracking.put(id, servers);
    }

}
