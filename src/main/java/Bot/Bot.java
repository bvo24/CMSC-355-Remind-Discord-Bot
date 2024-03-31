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

        //Essentially this is how we get our bot to run, we get a token we generated
        //Shards is basically how to get your bot on multiple servers
        //config = Dotenv.configure().load();
        //String token = config.get("TOKEN");
        String token = "";
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);

        //Bot information: Online status/activity and what we allow the bot to see in our server
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("the calendar"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES);

        //Used for checking members statuses (Online, offline, etc..)
        //builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        //builder.setChunkingFilter(ChunkingFilter.ALL);
        //builder.enableCache(CacheFlag.ONLINE_STATUS);

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



