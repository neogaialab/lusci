package lib.discord.command;

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class BotCommand {

  public SlashCommandData data;

  public BotCommand(SlashCommandData data) {
    this.data = data;
  }
  
  public abstract void execute(GenericCommandEvent event);

}

