package lib.discord.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public abstract class GenericCommandEvent {
  public abstract Guild getGuild();
  public abstract String getOptionAsString(String name);
  public abstract int getOptionAsInt(String name);
  public abstract GenericMessageCreate reply(String message);
  public abstract Member getMember();
  public abstract String getSubcommandName();
  public abstract long getPing();
  public abstract long getGatewayPing();
}
