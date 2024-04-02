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
            OptionMapping arg1 = event.getOption("days");
            OptionMapping arg2 = event.getOption("hours");
            OptionMapping arg3 = event.getOption("minutes");
            OptionMapping arg4 = event.getOption("description");

            int days = arg1.getAsInt();
            int hours = arg2.getAsInt();
            int minutes = arg3.getAsInt();
            String reminder = arg4.getAsString();
            if(days < 0 || hours < 0 || minutes < 0){
                event.reply("Cannot enter negative time").queue();
            }
            else if (minutes > 60){
                event.reply("Invalid time for minutes must be at or below 60 minutes").queue();
            }
            else{
                String str = String.format("Reminder created for you in %d days %d hours %d minutes for %s", days, hours, minutes, reminder);
                //event.reply("Created a reminder in %d").queue();
                event.reply(str).queue();

            }


            //This is where we implement the logic for the command






        }
        else if(command.equals("reminderlist")){
            event.reply("Here are your reminders:").queue();

        }


    }

    //How we make the commands show up
    @Override
    public void onGuildReady(GuildReadyEvent event) {


        List<CommandData> commandData = new ArrayList<>();
        //commandData.add(Commands.slash("remind", "Set a reminder"));
        commandData.add(Commands.slash("createreminder", "Create a reminder in...").addOptions(new OptionData(OptionType.INTEGER, "days", "How many days from now?", true ), new OptionData(OptionType.INTEGER, "hours", "How many hours from now?", true ), new OptionData(OptionType.INTEGER, "minutes", "How many minutes from now?", true ), new OptionData(OptionType.STRING, "description", "What do you want to be reminded of", true )));
        commandData.add(Commands.slash("reminderlist", "View your current reminders"));
        event.getGuild().updateCommands().addCommands(commandData).queue();

    }




}

