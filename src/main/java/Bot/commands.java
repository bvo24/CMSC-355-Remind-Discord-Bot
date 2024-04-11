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

import javax.xml.soap.Text;
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


    private final HashMap<String, List<reminder>> map;

    public commands() {
        map = new HashMap<>();
    }


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
                long millisTime = (days * 24 * 60 * 60 * 1000) + (hours * 60 * 60 * 1000) + (minutes * 60 * 1000);

                //Create an instance of our reminder class. It should have the user's reminder, the user and their hash map, and the channel the message was sent in
                //We need their hashmap so that when the timer is done we remove it from their list. That's why it's in the remindertask
                reminder remind = new reminder(reminder,map, user,channel);

                //Once we create a instance we should get their hashmap and their list and add this reminder
                //And we can schedule this reminder too with a function already created.
                List<reminder> list = map.getOrDefault(user, new ArrayList<reminder>());
                list.add(remind);
                map.put(user, list);
                remind.setTimer(millisTime);


                //Example
                //reminder task = new reminder( map, user, what their reminded)
                //task.schedule( time in millis)





                event.reply(str).queue();

            }


            //This is where we implement the logic for the command






        }
        else if (command.equals("reminderlist")) {
            String user = event.getUser().toString();
            List<reminder> reminders = map.get(user);

            if (reminders == null || reminders.isEmpty()) {
                event.reply("You have no reminders set.").queue();
            } else {
                StringBuilder reminderList = new StringBuilder("Here are your reminders:\n"); // All reminders just get added to one big string
                for (int i = 0; i < reminders.size(); i++) {
                    reminder remind = reminders.get(i);
                    long timeRemaining = remind.getScheduledTime() - System.currentTimeMillis();
                    // This all just takes the remaining time in milliseconds and converts it back to day, hour, minute format
                    long days = timeRemaining / (24 * 60 * 60 * 1000);
                    timeRemaining %= (24 * 60 * 60 * 1000);
                    long hours = timeRemaining / (60 * 60 * 1000);
                    timeRemaining %= (60 * 60 * 1000);
                    long minutes = timeRemaining / (60 * 1000);
                    timeRemaining %= (60 * 1000);
                    long seconds = timeRemaining / 1000;

                    String timeString;
                    // Also display seconds remaining if there is less than a minute left
                    // That way it doesn't just show 0 days, 0 hours, 0 minutes when there are still some seconds left
                    if (days == 0 && hours == 0 && minutes == 0) {
                        timeString = String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
                    } else {
                        timeString = String.format("%d days, %d hours, %d minutes", days, hours, minutes);
                    }

                    reminderList.append(i + 1).append(". ").append(remind.getReminder()).append(" - ").append(timeString).append(" remaining\n");
                }

                event.reply(reminderList.toString()).queue();
            }
        }


    }

    public class reminder extends TimerTask{
        //Data we need to create a single reminder.
    private String reminder;
    private String user;
    private HashMap<String, List<reminder>> map;
    private TextChannel channel;
    private long scheduledTime;

        //Instance of our bot
        reminder(String reminder, HashMap<String, List<reminder>> map, String user, TextChannel channel){
            //this.user = user etc..
            this.reminder = reminder;
            this.map = map;
            this.user = user;
            this.channel = channel;


        }

        public String getReminder() {
            return reminder;
        }

        public long getScheduledTime() {
            return scheduledTime;
        }

        //run is a default function when you extend timertask
        //Basically the code here runs when the timer is done
        @Override
        public void run() {
            //Currently we should keep it simple and just send a message in the chat

            //Once complete the list still has the reminder. We have their hashmap and can get their list so
            channel.sendMessage("Here's your reminder for " + reminder).queue();
            List<reminder> list = map.get(user);
            list.remove(this);



        }

        void setTimer(long timeMillis){
            //Set's the timer
            scheduledTime = System.currentTimeMillis() + timeMillis; // makes it easier to display remaining time for reminder list
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

