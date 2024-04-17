package core.commands;

import core.managers.PlayerManager;
import core.models.GuildAudioPlayer;
import lib.discord.command.BotCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class SkipCommand extends BotCommand {

  public SkipCommand() {
    super(Commands
        .slash("skip", "Play the next song."));
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    Guild guild = event.getGuild();

    PlayerManager playerManager = PlayerManager.getInstance();
    GuildAudioPlayer guildAudioPlayer = playerManager.getGuildAudioPlayer(guild);
    guildAudioPlayer.scheduler.nextTrack();

    event
        .reply("The bot started the next track.")
        .queue();
  }
}
