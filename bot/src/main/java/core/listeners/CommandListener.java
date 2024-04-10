package core.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
  
  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    switch (event.getName()) {
      case "ping":
        long time = System.currentTimeMillis();

        event
            .reply("Pong!")
            .setEphemeral(true)
            .flatMap(v -> event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time))
            .queue();

        break;
      default:
        System.out.printf("Unknown command %s used by %#s%n", event.getName(), event.getUser());
    }
  }
}
