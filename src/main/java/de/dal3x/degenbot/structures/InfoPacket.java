package de.dal3x.degenbot.structures;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** A serializable object holding information to be saved and loaded in the YAML format. */
public class InfoPacket {

    /** The map of tracked streamer IDs and their TrackingInfo List. */
    private Map<String, List<TrackingInfo>> tracking;


    /** Creates an empty InfoPacket. */
    public InfoPacket() {
        this.tracking = new HashMap<>();
    }

    /** Returns the set of tracked streamer IDs. */
    public Map<String, List<TrackingInfo>> getTracking() {
        return tracking;
    }

    /** Sets the set of tracked streamer IDs. */
    public void setTracking(Map<String, List<TrackingInfo>> tracking) {
        this.tracking = tracking;
    }

    public void addTracking(String channelID, TrackingInfo info) {
        List<TrackingInfo> list = new LinkedList<TrackingInfo>();
        if (this.tracking.containsKey(channelID)) {
            list = this.tracking.get(channelID);
        }
        list.add(info);
        this.tracking.put(channelID, list);
    }

}
