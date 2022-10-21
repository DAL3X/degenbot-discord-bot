package de.dal3x.degenbot.discord;

import de.dal3x.degenbot.structures.TwitchStream;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

/** Creates discord embeds that can send in text channels. */
public class EmbedFactory {

    /** Create and returns a go live embed to post in a discord text channel out of a TwitchStream data object. */
    public static MessageEmbed createEmbed(TwitchStream stream) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(stream.getDisplayName() + " just went live!", stream.getLink(), stream.getLogoURL());
        builder.setThumbnail(stream.getLogoURL());
        builder.setColor(new Color(96,20,196));
        builder.setTitle(stream.getTitle(), stream.getLink());
        builder.setImage(stream.getPictureURL());
        builder.addField("Category: ", stream.getGame(), false);
        builder.setFooter(stream.getDescription());
        return builder.build();
    }

}
