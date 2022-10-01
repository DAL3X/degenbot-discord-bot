package de.dal3x.degenbot.structures;

/** Contains all the stream information to transfer between components.
 * This class is the interface to transfer data between the twitch and discord components of the bot.
 */
public class TwitchStream {

    /** The channel name */
    String name;
    /** The channel id */
    String channelID;
    /** The stream title */
    String title;
    /** The stream category */
    String game;
    /** A link to the streamer's profile */
    String link;
    /** A URL for the stream thumbnail */
    String pictureURL;
    /** A URL to the streams category */
    String categoryURL;

    /** Creates a TwitchStream object to encapsulate all given variables */
    public TwitchStream(String name, String channelID, String title, String game, String link, String pictureURL, String categoryURL) {
        this.name = name;
        this.channelID = channelID;
        this.title = title;
        this.game = game;
        this.link = link;
        this.pictureURL = pictureURL;
        this.categoryURL = categoryURL;
    }

    /** Returns the channel name */
    public String getName() {
        return this.name;
    }

    /** Returns the stream title */
    public String getTitle() {
        return title;
    }

    /** Returns the stream category */
    public String getGame() {
        return game;
    }

    /** Returns a link to the streamer's profile */
    public String getLink() {
        return link;
    }

    /** Returns a URL for the stream thumbnail */
    public String getPictureURL() {
        return pictureURL;
    }
}
