package core.commands;

import lib.discord.ChannelUtils;
import lib.discord.command.BotCommand;
import lib.discord.command.GenericCommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand extends BotCommand {

  public LeaveCommand() {
    super(Commands
        .slash("leave", "Disconnect the bot from the voice channel."));
  }

  @Override
  public void execute(GenericCommandEvent event) {
    VoiceChannel voiceChannel = ChannelUtils.getVoiceChannel(event);

    if (voiceChannel == null) {
      event
          .reply("You are not connected to a voice channel!")
          .queue();
      return;
    }

    Guild guild = event.getGuild();

    AudioChannelUnion connectedChannel = guild.getSelfMember().getVoiceState().getChannel();

    if (connectedChannel == null) {
      event
          .reply("I'm not connected to a voice channel.")
          .queue();
      return;
    }

    AudioManager audioManager = guild.getAudioManager();

    audioManager.closeAudioConnection();

    event
        .reply("Disconnected from the voice channel!")
        .queue();
  }
}
