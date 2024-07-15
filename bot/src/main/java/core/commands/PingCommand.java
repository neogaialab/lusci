package core.commands;

import lib.discord.command.BotCommand;
import lib.discord.command.GenericCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class PingCommand extends BotCommand {

  public PingCommand() {
    super(Commands.slash("ping", "Checks the bot's latency."));
  }

  @Override
  public void execute(GenericCommandEvent event) {
    var ping = event.getPing();
    var gatewayPing = event.getGatewayPing();

    event
        .reply(String.format("Pong: %d ms | WebSocket: %d ms", ping, gatewayPing))
        .queue();
  }
}
