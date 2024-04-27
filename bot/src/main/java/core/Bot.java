package core;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.listeners.ReadyListener;
import core.listeners.TtcListener;
import io.github.cdimascio.dotenv.Dotenv;
import lib.discord.command.CommandUtils;
import lib.java.FileUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import okhttp3.OkHttpClient;

public class Bot {

  public static JDA api;
  public static Dotenv env;
  public static OkHttpClient httpClient = new OkHttpClient();
  public static JsonNode functionDeclarationsNode;

  static {
    env = Dotenv.configure().directory("../").ignoreIfMissing().load();

    try {
      var functionDeclarations = FileUtils.readResourceFile("function_declarations.json");
      functionDeclarationsNode = new ObjectMapper().readTree(functionDeclarations);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException {
    String token = env.get("DISCORD_BOT_TOKEN");

    if (token == null) {
      System.out.println("DISCORD_BOT_TOKEN not set");
      System.exit(1);
    }

    api = JDABuilder.createDefault(token)
        .addEventListeners(new ReadyListener())
        .addEventListeners(new TtcListener())
        .build();

    Message.suppressContentIntentWarning();

    CommandUtils.addCommands(api, "core.commands");
  }
}
