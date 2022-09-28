package de.dal3x.degenbot.twitch;

/** Contains all the stream information to transfer between components */
public class TwitchStream {

    /** The channel name */
    String name;
    /** The stream title */
    String title;
    /** The stream category */
    String game;
    /** A link to the streamer's profile */
    String link;
    /** A URL for the stream thumbnail */
    String pictureURL;

    /** Creates a TwitchStream object to encapsulate all given variables */
    public TwitchStream(String name, String title, String game, String link, String pictureURL) {
        this.name = name;
        this.title = title;
        this.game = game;
        this.link = link;
        this.pictureURL = pictureURL;
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
