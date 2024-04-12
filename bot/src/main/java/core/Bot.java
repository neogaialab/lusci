package core;

import core.listeners.ReadyListener;
import io.github.cdimascio.dotenv.Dotenv;
import lib.command.CommandUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Bot {

  public static JDA api;

  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.configure().directory("../").load();
    String token = dotenv.get("DISCORD_BOT_TOKEN");

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
