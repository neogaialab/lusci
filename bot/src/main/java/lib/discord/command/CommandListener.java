package lib.discord.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    String commandLabel = event.getName();
    BotCommand command = CommandManager.getCommand(commandLabel);

    if (command == null) {
      System.out.printf("Unknown command %s used by %#s%n", event.getName(), event.getUser());
      return;
    }

    try {
      command.execute(new GenericCommandEvent() {
        @Override
        public Guild getGuild() {
          return event.getGuild();
        }

        @Override
        public String getSubcommandName() {
          return event.getSubcommandName();
        }

        @Override
        public GenericMessageCreate reply(String message) {
          return new GenericMessageCreate() {
            @Override
            public void queue() {
              event.reply(message).queue();
            }
          };
        }

        @Override
        public Member getMember() {
          return event.getMember();
        }

        @Override
        public String getOptionAsString(String name) {
          return event.getOption(name).getAsString();
        }

        @Override
        public int getOptionAsInt(String name) {
          return event.getOption(name).getAsInt();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      event
          .reply("An error occurred while executing the command.")
          .queue();
    }
  }
}
