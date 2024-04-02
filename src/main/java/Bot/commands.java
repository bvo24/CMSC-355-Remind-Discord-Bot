package Bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.sharding.ShardManager;
import Bot.Bot;

import java.util.*;

public class commands extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        //Variables
        String command = event.getName();
        TextChannel channel = (TextChannel) event.getChannel();

        if(command.equals("createreminder")){
            //This is where we implement the logic for the command
            event.reply("Created a reminder").queue();

        }
        else if(command.equals("reminderlist")){
            event.reply("Here are your reminders").queue();

        }


    }

    //Instant update make sure Bot.commands are lower case
    @Override
    public void onGuildReady(GuildReadyEvent event) {


        List<CommandData> commandData = new ArrayList<>();
        //commandData.add(Commands.slash("remind", "Set a reminder"));
        commandData.add(Commands.slash("createreminder", "Create a reminder in...").addOptions(new OptionData(OptionType.INTEGER, "days", "How many days from now?", true ), new OptionData(OptionType.INTEGER, "hours", "How many hours from now?", true ), new OptionData(OptionType.INTEGER, "minutes", "How many minutes from now?", true ), new OptionData(OptionType.STRING, "description", "What do you want to be reminded of", true )));
        commandData.add(Commands.slash("reminderlist", "View your current reminders"));
        event.getGuild().updateCommands().addCommands(commandData).queue();

    }




}

