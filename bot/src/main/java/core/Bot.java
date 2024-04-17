package core;

import core.listeners.ReadyListener;
import io.github.cdimascio.dotenv.Dotenv;
import lib.discord.command.CommandUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import okhttp3.OkHttpClient;

public class Bot {

  public static JDA api;
  public static Dotenv env;
  public static OkHttpClient httpClient = new OkHttpClient();

  static {
    env = Dotenv.configure().directory("../").ignoreIfMissing().load();
  }

  public static void main(String[] args) {
    String token = env.get("DISCORD_BOT_TOKEN");

    if (token == null) {
      System.out.println("DISCORD_BOT_TOKEN not set");
      System.exit(1);
    }

    api = JDABuilder.createDefault(token)
        .addEventListeners(new ReadyListener())
        .build();
    
    CommandUtils.addCommands(api, "core.commands");
  }
}
