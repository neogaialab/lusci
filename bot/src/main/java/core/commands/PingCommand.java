package core.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import lib.BotCommand;

public class PingCommand extends BotCommand {

  public PingCommand() {
    super(Commands.slash("ping", "Calculate ping of the bot."));
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    long time = System.currentTimeMillis();

    event
        .reply("Pong!")
        .setEphemeral(true)
        .flatMap(v -> event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time))
        .queue();
  }
}
