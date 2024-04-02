package Bot;

import Bot.commands;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.List;

public class Bot {
    private final ShardManager shardManager;
    //private final Dotenv config;

    public Bot() throws LoginException {

        //This file, is how we create an instance of our bot
        //Shards allow the bot to be able to be run on multiple servers

        //This is from an API in the pom.xml file
        //This makes it possible to upload onto github
        //The token is important because if someone else has your token they can do anything with your bot
        //But currently not implementing it because the .env can possibly be pushed on accident.
        //config = Dotenv.configure().load();
        //String token = config.get("TOKEN");


        String token = "";
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);

        //Bot information: Online status/activity and what we allow the bot to see in our server
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("the calendar"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES);

        //Build our bot
        shardManager = builder.build();
        shardManager.addEventListener(new commands());


    }

    //public Dotenv getConfig(){
    //    return config;
    //}
    //public ShardManager getShardManager(){
    //    return getShardManager();
    //}
    public static void main(String[] args){

        //Try building our bot
        try{
            Bot bot = new Bot();

        }catch(LoginException e){
            System.out.println("ERROR: Provided bot token is invalid");
        }

    }
}



