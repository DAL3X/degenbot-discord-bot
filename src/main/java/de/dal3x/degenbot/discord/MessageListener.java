package de.dal3x.degenbot.discord;

import de.dal3x.degenbot.main.DegenBot;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Message;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Listens to discord messages and reacts with a bacon emoji is author is tracked user
 */
public class MessageListener extends ListenerAdapter {

    /** Instance of the running bot. Required to get up-to-date tracking info */
    DegenBot bot;

    /** Map of cooldowns for server ids per user */
    Map<String, Set<String>> cooldowns;

    public MessageListener(DegenBot bot) {
        this.bot = bot;
        cooldowns = new HashMap<>();
    }

    /** Checks if the given user id is on cooldown for the given server */
    private boolean hasCooldown(String id, String server) {
        if (cooldowns.containsKey(id)) {
            return cooldowns.get(id).contains(server);
        }
        return false;
    }

    /** Sets bacon cooldown for a user id for the given server */
    private void setCooldown(String id, String server) {
        Set<String> servers;
        if (cooldowns.containsKey(id)) {
            servers = cooldowns.get(id);
            servers.add(server);
        }
        else {
            servers = new HashSet<>();
        }
        servers.add(server);
        this.cooldowns.put(id, servers);
        Runnable task = () -> this.cooldowns.remove(id);
        Executors.newSingleThreadScheduledExecutor().schedule(task, 1440, TimeUnit.MINUTES); //60*24=1440 minutes equals 24 hours
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Map<String, Set<String>> trackingInfo = bot.getInfoPacket().getBaconTracking();
        String id = event.getAuthor().getId();
        if (trackingInfo.containsKey(id)) {
            System.out.println(event.getMessage().getContentRaw());
            if (trackingInfo.get(id).contains(event.getGuild().getId()) && (!hasCooldown(id, event.getGuild().getId())) || DegenBot.testMode) {
                Message message = event.getMessage();
                message.addReaction(Emoji.fromUnicode("\uD83E\uDD53")).queue();
                setCooldown(id, event.getGuild().getId()); //60*24=1440 minutes equals 24 hours
            }
        }
    }
}
