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

import javax.security.auth.login.LoginException;
import javax.xml.soap.Text;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class commands extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        //Variables
        String command = event.getName();
        TextChannel channel = (TextChannel) event.getChannel();

        if(command.equals("")){
            //This is where we implement how we do what we want

        }
        else if(command.equals("")){

        }


    }

    //Instant update make sure Bot.commands are lower case
    @Override
    public void onGuildReady(GuildReadyEvent event) {


        List<CommandData> commandData = new ArrayList<>();
        //commandData.add(Commands.slash("remind", "Set a reminder"));
        commandData.add(Commands.slash("command", "This is a command"));
        commandData.add(Commands.slash("test", "This is a test"));
        event.getGuild().updateCommands().addCommands(commandData).queue();

    }




}

