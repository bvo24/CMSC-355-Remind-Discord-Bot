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
    /**
     *
     * Ok so currently how we're going to implement this is with a hashmap.
     * <Key,Value> Key is the userid and value should be a list of their reminders(which should be a class there's a lot of data in the reminder)
     *
     *
     *
     * @param event
     */







    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        //Variables
        String command = event.getName();
        TextChannel channel = (TextChannel) event.getChannel();




        if(command.equals("createreminder")){

            //Breaks down the arguments for this command
            OptionMapping arg1 = event.getOption("days");
            OptionMapping arg2 = event.getOption("hours");
            OptionMapping arg3 = event.getOption("minutes");
            OptionMapping arg4 = event.getOption("description");


            String user = event.getUser().toString();
            int days = arg1.getAsInt();
            int hours = arg2.getAsInt();
            int minutes = arg3.getAsInt();
            String reminder = arg4.getAsString();

            //Test cases
            if(days < 0 || hours < 0 || minutes < 0){
                event.reply("Cannot enter negative time").queue();
            }
            else if (minutes > 60){
                event.reply("Invalid time for minutes must be at or below 60 minutes").queue();
            }


            else{
                String str = String.format("Reminder created for you in %d days %d hours %d minutes for %s", days, hours, minutes, reminder);
                //event.reply("Created a reminder in %d").queue();

                //When creating a reminder we should convert our time to millis

                //Then we should create a reminder class. It should have the user's reminder, the user and their hash map.
                //We need their hashmap so that when the timer is done we remove it from their list. That's why it's in the remindertask

                //Once we create a instance we should get their hashmap and their list and add this reminder
                //And we can schedule this reminder too with a function already created.

                //Example
                //reminder task = new reminder( map, user, what their reminded)
                //task.schedule( time in millis)





                event.reply(str).queue();

            }


            //This is where we implement the logic for the command






        }
        else if(command.equals("reminderlist")){
            event.reply("Here are your reminders:").queue();

            //this one is fairly simple
            //we should get the users list and just iterate through


        }


    }

    public class reminder extends TimerTask{
        //Data we need to create a single reminder.
        //private


        //Instance of our bot
        reminder(){
            //this.user = user etc..

        }


        //run is a default thing when you extend timertask
        //Basically the code here runs when the timer is done
        @Override
        public void run() {
            //Currently we should keep it simple and just send a message in the chat

            //Once complete the list still has the reminder. We have their hashmap and can get their list so



        }

        void setTimer(long timeMillis){
            //Set's the timer
            Timer time = new Timer();
            time.schedule(this, timeMillis);

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

