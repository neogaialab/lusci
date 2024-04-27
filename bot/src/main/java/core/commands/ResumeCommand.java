package core.commands;

import core.managers.PlayerManager;
import core.models.GuildAudioPlayer;
import lib.discord.command.BotCommand;
import lib.discord.command.GenericCommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ResumeCommand extends BotCommand {

  public ResumeCommand() {
    super(Commands
        .slash("resume", "Resume the current song."));
  }

  @Override
  public void execute(GenericCommandEvent event) {
    Guild guild = event.getGuild();

    PlayerManager playerManager = PlayerManager.getInstance();
    GuildAudioPlayer guildAudioPlayer = playerManager.getGuildAudioPlayer(guild);

    if(!guildAudioPlayer.audioPlayer.isPaused()) {
      event
        .reply("The current track is already playing.")
        .queue();
      return;
    }

    guildAudioPlayer.audioPlayer.setPaused(false);

    event
        .reply("The bot resumed the current track.")
        .queue();
  }
}
