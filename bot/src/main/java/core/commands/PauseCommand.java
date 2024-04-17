package core.commands;

import core.managers.PlayerManager;
import core.models.GuildAudioPlayer;
import lib.discord.command.BotCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class PauseCommand extends BotCommand {

  public PauseCommand() {
    super(Commands
        .slash("pause", "Pause the current song."));
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    Guild guild = event.getGuild();

    PlayerManager playerManager = PlayerManager.getInstance();
    GuildAudioPlayer guildAudioPlayer = playerManager.getGuildAudioPlayer(guild);

    if(guildAudioPlayer.audioPlayer.isPaused()) {
      event
        .reply("The current track is already paused.")
        .queue();
      return;
    }

    guildAudioPlayer.audioPlayer.setPaused(true);

    event
        .reply("The bot paused the current track.")
        .queue();
  }
}
