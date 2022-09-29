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
            .addOptions(new OptionData(OptionType.STRING, "channel-id", CommandDescriptions.add).setRequired(true))
            .setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true));
        commands.addCommands(Commands.slash("degen_remove", CommandDescriptions.remove)
                .addOptions(new OptionData(OptionType.STRING, "channel-id", CommandDescriptions.remove).setRequired(true))
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true));
        commands.addCommands(Commands.slash("degen_mark", CommandDescriptions.remove)
                .addOptions(new OptionData(OptionType.STRING, "text-channel-id", CommandDescriptions.mark).setRequired(true))
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
                String addChannelName = event.getOption("channel-id").getAsString();
                bot.addToTrackingList(addChannelName);
                event.reply("Streamer added to the notification list!").queue();
                break;
            case "degen_remove":
                String removeChannelName = event.getOption("channel-id").getAsString();
                bot.removeFromTrackingList(removeChannelName);
                event.reply("Streamer removed from the notification list!").queue();
                break;
            case "degen_mark":
                String markChannelID = event.getOption("text-channel-id").getAsString();
                bot.updateDiscordTarget(markChannelID);
                event.reply("Text channel marked as go live notification channel!").queue();
                break;
            case "degen_list":
                event.reply("Streamer on the notification list: " + bot.getInfoPacket().getTracking().toString()).queue();
                break;
            default:
                // Do nothing
        }
    }

}
