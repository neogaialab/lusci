package core;

import core.listeners.ReadyListener;
import io.github.cdimascio.dotenv.Dotenv;
import lib.command.CommandUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Bot {

  public static JDA api;
  public static Dotenv env;

  public static void main(String[] args) {
    env = Dotenv.configure().directory("../").load();
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
