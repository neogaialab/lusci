package core.commands;

import lib.discord.command.BotCommand;
import lib.discord.command.GenericCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class PingCommand extends BotCommand {

  public PingCommand() {
    super(Commands.slash("ping", "Calculate ping of the bot."));
  }

  @Override
  public void execute(GenericCommandEvent event) {
    long time = System.currentTimeMillis();

    event
        .reply(String.format("Pong: %d ms", System.currentTimeMillis() - time))
        .queue();
  }
}
