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
     *With this new remove feature a notable flaw in our bot was found. If we wanted to remove a reminder we would have to type the entire string out which seems like a hassle
     *So we implemeneted an ID that is generated when you create a reminder
     *Though this seems like a simple fix it caused a lot of changes to the other functions too.
     * Changing our user from a string to the type User
     * Our data structure is a hashmap with a hashmap inside rather than just a list
     * Since we're accessing a hashmap reading our list is a little different
     * Our reminder class took in more arguments to help
     *
     * Things we added in this new sprint:
     * made all command calls private (noone can see it besides the user calling it)
     * Formatted our reminder list so that it looks better
     * removereminder feature
     * @param event
     */


    private final HashMap<String, HashMap<Integer,reminder>> map;
    int id = 0;

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

            //Turning them into a usable type
            //String user = event.getUser().toString();
            User user = event.getUser();
            int days = arg1.getAsInt();
            int hours = arg2.getAsInt();
            int minutes = arg3.getAsInt();
            String reminder = arg4.getAsString();


            //Test cases
            //Negative time
            if(days < 0 || hours < 0 || minutes < 0){
                event.reply("Cannot enter negative time").setEphemeral(true).queue();
            }
            //Providing a time larger than 60 minutes
            else if (minutes > 60){
                event.reply("Invalid time for minutes must be at or below 60 minutes").setEphemeral(true).queue();
            }

            //Otherwise we shoould create the reminder
            else{
                String str = String.format("Reminder created for you in %d days %d hours %d minutes for %s", days, hours, minutes, reminder);

                //When creating a reminder we should convert our time to millis
                long millisTime = (days * 24 * 60 * 60 * 1000) + (hours * 60 * 60 * 1000) + (minutes * 60 * 1000);
                Timer timer = new Timer();

                //Create an instance of our reminder class. It should have the user's reminder, the user and their hash map, and the channel the message was sent in, their id for the reminder and a timer
                //Each element required in an instance so that it can be recalled and used when the timer is up
                //For example
                //User: when we want to privately message, the bot has a hard time remembering who. So when your timer is up for your reminder it looks at the user
                //ID: Similar to user it makes it easier on the bot when your timer is up to remove it from the list

                reminder remind = new reminder(reminder,map, user,channel, id, timer);

                //We should add this reminder to the user's reminders (key->value:  id , reminder) hashtable.
                //Then add this updated list to their user unique id

                HashMap<Integer, reminder> innerMap = map.getOrDefault(user.toString(), new HashMap<Integer, reminder>());
                innerMap.put(id, remind);
                map.put(user.toString(), innerMap);
                //Testing
                // System.out.println(id);
                //System.out.println("Size of your reminder list " + innerMap.size() );

                //Update ID value so we always have a unique id and then start the timer
                id++;
                remind.setTimer(millisTime);

                //Emphemeral allows us for only the user calling a function to see it
                event.reply(str).setEphemeral(true).queue();

            }

        }
        else if (command.equals("reminderlist")) {

            String user = event.getUser().toString();
            HashMap<Integer, reminder> innerMap = map.get(user);

            EmbedBuilder embed = readReminders(user);
            event.reply("Fetched your list!").setEphemeral(true).addEmbeds(embed.build()).queue();

        }
        else if(command.equals("removereminder")){

            String user = event.getUser().toString();


            //Takes the first argument in the command and turns it into an int
            OptionMapping arg1 = event.getOption("reminderid");
            int requestedID = arg1.getAsInt();

            //retreive their hashmap
            HashMap<Integer, reminder> innerMap = map.get(user);

            //If the user attempts to remove a command that doesn't exist
            if(!innerMap.containsKey(requestedID)){
                event.reply("ID doesn't exist").setEphemeral(true).queue();
            }

            //Other wise we will remove the reminder here
            //Cancel the timer and remove it from the list

            Timer timer = innerMap.get(requestedID).timer;
            timer.cancel();
            //this works too
            //reminder remind = innerMap.get(requestedID);
            //remind.cancelTimer();
            String reminder = innerMap.get(requestedID).getReminder();
            innerMap.remove(requestedID);
            event.reply("Removed your reminder for " + reminder ).setEphemeral(true).queue();



        }


    }

    public class reminder extends TimerTask{
        //Data we need to create a single reminder.
    private String reminder;
    private User user;

    //Key : User
        //Value : A hashmap of their reminders to be able to remove with an id rather than the whole string
    private HashMap<String, HashMap<Integer, reminder>> map;
    private TextChannel channel;
    private long scheduledTime;
    private int reminderID;
    private Timer timer;

        //Instance of our bot
        reminder(String reminder, HashMap<String, HashMap<Integer, reminder>> map, User user, TextChannel channel, int reminderID, Timer timer){
            //this.user = user etc..
            this.reminder = reminder;
            this.map = map;
            this.user = user;
            this.channel = channel;
            this.reminderID = reminderID;
            this.timer = timer;


        }

        public String getReminder() {
            return reminder;
        }

        public long getScheduledTime() {
            return scheduledTime;
        }
        public void cancelTimer(){
            timer.cancel();
        }

        //run is a default function when you extend timertask
        //Basically the code here runs when the timer is done
        @Override
        public void run() {
            //This is when the timer is up we will send a private message to the user
            //While this occurs we should remove this reminder since the time is up
            user.openPrivateChannel().queue(privateChannel ->
                    privateChannel.sendMessage("Reminder for " + reminder).queue(message -> {
                        //cancelTimer();
                        HashMap<Integer, reminder> innerMap = map.get(user.toString());
                        innerMap.remove(this.reminderID);


                    })
                    );
        }

        void setTimer(long timeMillis){
            //Set's the timer in millis
            scheduledTime = System.currentTimeMillis() + timeMillis; // makes it easier to display remaining time for reminder list
            timer = new Timer();
            timer.schedule(this, timeMillis);

        }


    }

    //This is how we create our formatted message and use it in our case where we call our reminderlist
    EmbedBuilder readReminders(String userId){
        HashMap<Integer, reminder> list = map.get(userId);
        EmbedBuilder embed = new EmbedBuilder();


        //Checks the list if it's empty or not
        if(list == null || list.size() == 0){
            embed.setTitle("Reminders");
            embed.setDescription("No reminders currently");
            return embed;
        }
        //Starting to create our embed, providing a title then string concatenating
        embed.setTitle("Reminders");
        //Sets up description by having a loop read each reminder in the list then string builds it
        StringBuilder strb = new StringBuilder();
        for(Map.Entry<Integer, reminder> entry : list.entrySet()){
            //Calculte the time for each reminder
            long currentTimeMillis = System.currentTimeMillis();
            long scheduledTimeMillis = entry.getValue().scheduledExecutionTime();
            long timeRemainingMillis = scheduledTimeMillis - currentTimeMillis;
            long millis = timeRemainingMillis;
            long seconds = millis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            hours %= 24;
            minutes %= 60;
            seconds %= 60;

            //Once calculated we format it all into one line then append it for each reminder
            String str = String.format("%d days %02d : %02d : %02d\n", days, hours, minutes, seconds);
            strb.append(String.format("**ID:** %s **Reminder:** %s **Time:** %s",entry.getValue().reminderID, entry.getValue().reminder, str));



        }
        embed.setDescription(strb.toString());

        return embed;


    }




    //How we make the commands show up
    @Override
    public void onGuildReady(GuildReadyEvent event) {


        List<CommandData> commandData = new ArrayList<>();
        //commandData.add(Commands.slash("remind", "Set a reminder"));
        commandData.add(Commands.slash("createreminder", "Create a reminder in...").addOptions(new OptionData(OptionType.INTEGER, "days", "How many days from now?", true ), new OptionData(OptionType.INTEGER, "hours", "How many hours from now?", true ), new OptionData(OptionType.INTEGER, "minutes", "How many minutes from now?", true ), new OptionData(OptionType.STRING, "description", "What do you want to be reminded of", true )));
        commandData.add(Commands.slash("reminderlist", "View your current reminders"));
        commandData.add(Commands.slash("removereminder", "Remove a reminder").addOptions(new OptionData(OptionType.INTEGER, "reminderid", "Given the ID of the reminder this reminder will be removed", true)));

        event.getGuild().updateCommands().addCommands(commandData).queue();

    }




}

