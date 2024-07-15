package core.listeners;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import core.Bot;
import lib.discord.command.CommandManager;
import lib.discord.command.GenericCommandEvent;
import lib.discord.command.GenericMessageCreate;
import lib.gemini.FunctionCalling;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class TtcListener implements EventListener {

  @Override
  public void onEvent(GenericEvent _e) {
    if (_e instanceof MessageReceivedEvent event) {
      var botName = Bot.api.getSelfUser().getId();
      var message = event.getMessage().getContentRaw();

      System.out.println(message);

      var reference = event.getMessage().getReferencedMessage();

      var isTaggingBot = message.contains("<@" + botName + ">");
      var isReplyingBot = reference != null && reference.getAuthor().getId().equals(botName);

      if (isTaggingBot || isReplyingBot) {
        var prompt = message.toString().replace("<@" + botName + ">", "");

        if (Bot.functionDeclarationsNode == null) {
          event.getChannel().sendMessage("Sorry, text to command isn't available right now.").queue();
          System.out.println("[ERROR] functionDeclarationsNode is null.");
          return;
        }

        try {
          var res = FunctionCalling.generate(prompt, Bot.functionDeclarationsNode);

          var functionCall = res
              .get("candidates")
              .get(0)
              .get("content")
              .get("parts")
              .get(0)
              .get("functionCall");

          var name = functionCall.get("name").asText();
          var args = functionCall.get("args");

          var commands = name.split("_");
          var mainCommand = commands[0];

          var command = CommandManager.getCommandByName(mainCommand);

          if (command == null) {
            event.getChannel().sendMessage("Sorry, something went wrong.").queue();
            System.out.println("[WARN] No command found for " + name);
            return;
          }

          System.out.println("[TTC] Executing command " + name + " with args " + args);

          command.execute(new GenericCommandEvent() {
            @Override
            public String getSubcommandName() {
              var subcommandName = commands[1];
              return subcommandName;
            }

            @Override
            public Guild getGuild() {
              return event.getGuild();
            }

            @Override
            public GenericMessageCreate reply(String message) {
              return new GenericMessageCreate() {
                @Override
                public void queue() {
                  event.getMessage().reply(message).queue();
                }
              };
            }

            @Override
            public Member getMember() {
              return event.getMember();
            }

            @Override
            public String getOptionAsString(String name) {
              return args.get(name).asText();
            }

            @Override
            public int getOptionAsInt(String name) {
              return args.get(name).asInt();
            }

            @Override
            public long getPing() {
              return event.getMessage().getTimeCreated().until(OffsetDateTime.now(), ChronoUnit.MILLIS) / 1000;
            }

            @Override
            public long getGatewayPing() {
              return event.getJDA().getGatewayPing();
            }
          });
        } catch (Exception er) {
          event.getChannel().sendMessage("Sorry, something went wrong. Try again.").queue();
          er.printStackTrace();
        }
      }
    }
  }
}
