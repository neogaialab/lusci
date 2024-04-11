package core.listeners;

import lib.BotCommand;
import lib.CommandManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
  
  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    String commandLabel = event.getName();
    BotCommand command = CommandManager.getCommand(commandLabel);

    if(command == null) {
      System.out.printf("Unknown command %s used by %#s%n", event.getName(), event.getUser());
      return;
    }

    command.execute(event);
  }
}
