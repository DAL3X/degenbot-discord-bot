package de.dal3x.degenbot.discord;

import de.dal3x.degenbot.config.CommandDescriptions;
import de.dal3x.degenbot.main.DegenBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

/** The command listener and executor for discord commands. */
public class CommandExecutor extends ListenerAdapter {

    /** Instance of the bot that created this listener. */
    private final DegenBot bot;

    /** Creates a new CommandExecutor responding to the provided bot. */
    public CommandExecutor(DegenBot bot) {
        this.bot = bot;
    }

    /** Registers the commands on a discord server level. */
    public void registerCommands(JDA jda) {
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(Commands.slash("degen_add", CommandDescriptions.add)
            .addOptions(new OptionData(OptionType.STRING, "twitch-channel-id", CommandDescriptions.add).setRequired(true))
            .addOptions(new OptionData(OptionType.STRING, "discord-channel-id", CommandDescriptions.add).setRequired(true))
            .addOptions(new OptionData(OptionType.STRING, "message", CommandDescriptions.add).setRequired(false))
            .setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true));
        commands.addCommands(Commands.slash("degen_remove", CommandDescriptions.remove)
                .addOptions(new OptionData(OptionType.STRING, "twitch-channel-id", CommandDescriptions.remove).setRequired(true))
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true));
        commands.addCommands(Commands.slash("degen_list", CommandDescriptions.list)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true));
        commands.queue();
    }
    /** Executes commands when detected. */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            return; // Needs to have administrator permissions
        }
        switch(event.getName()) {
            case "degen_add":
                String addChannelName = event.getOption("twitch-channel-id").getAsString();
                String addDiscordChannel = "";
                String addMessage = "";
                if (event.getOption("discord-channel-id") != null) {
                    addDiscordChannel = event.getOption("discord-channel-id").getAsString();
                }
                if (event.getOption("message") != null) {
                    addMessage = event.getOption("message").getAsString();
                }
                bot.addToTrackingList(addChannelName, event.getGuild().getId(), addDiscordChannel, addMessage);
                event.reply("Streamer added to the notification list!").queue();
                break;
            case "degen_remove":
                String removeChannelName = event.getOption("twitch-channel-id").getAsString();
                String serverID = event.getGuild().getId();
                bot.removeFromTrackingList(removeChannelName, serverID);
                event.reply("Streamer removed from the notification list!").queue();
                break;
            case "degen_list":
                String reply = "";
                for (String key : bot.getInfoPacket().getTracking().keySet()) {
                    for (int i = 0; i < bot.getInfoPacket().getTracking().get(key).size(); i++) {
                        if (bot.getInfoPacket().getTracking().get(key).get(i).getServer() == event.getGuild().getId()) {
                            reply = reply + " " + key;
                        }
                    }
                }
                // Use "```" to enforce chat style and prevent styling errors
                event.reply("Streamer on the notification list: " + "```" + reply + "```").queue();
                break;
            default:
                // Do nothing
        }
    }

}
