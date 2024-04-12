package lib.discord;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ChannelUtils {

  public static VoiceChannel getVoiceChannel(SlashCommandInteractionEvent event) {
    Member member = event.getMember();
    if (member == null) {
      event
          .reply("Can't get the guild-specific information related to you. Try executing the command inside the guild.")
          .queue();
      return null;
    }

    GuildVoiceState voiceState = member.getVoiceState();
    AudioChannelUnion audioChannelUnion = voiceState.getChannel();

    if(audioChannelUnion == null) {
      return null;
    }

    return audioChannelUnion.asVoiceChannel();
  }
}
