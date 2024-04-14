package core.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import core.Bot;
import core.managers.PlayerManager;
import core.models.GuildAudioPlayer;
import core.models.TrackScheduler;
import lib.command.BotCommand;
import lib.discord.ChannelUtils;
import lib.player.JdaAudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand extends BotCommand {

  public PlayCommand() {
    super(Commands
        .slash("play", "Play a song.")
        .addOption(OptionType.STRING, "identifier",
            "Track identifier for media source (e.g. YouTube video ID, file path).", true));
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    VoiceChannel voiceChannel = ChannelUtils.getVoiceChannel(event);

    if (voiceChannel == null) {
      event
          .reply("You are not connected to a voice channel!")
          .queue();
      return;
    }

    Guild guild = event.getGuild();
    AudioManager audioManager = guild.getAudioManager();

    audioManager.openAudioConnection(voiceChannel);

    PlayerManager playerManager = PlayerManager.getInstance();
    GuildAudioPlayer guildAudioPlayer = playerManager.getGuildAudioPlayer(guild);

    TrackScheduler trackScheduler = guildAudioPlayer.scheduler;

    String url = event.getOption("identifier").getAsString();
    String urlWithoutApiKey = url;

    if(url.startsWith(Bot.env.get("JELLYFIN_URL"))) {
      url = url + "?api_key=" + Bot.env.get("JELLYFIN_API_KEY");
    }

    String identifier = url;

    playerManager
        .getPlayerManager()
        .loadItemOrdered(guildAudioPlayer.audioPlayer, identifier, new AudioLoadResultHandler() {

          @Override
          public void trackLoaded(AudioTrack track) {
            event
                .reply("The bot started playing: " + urlWithoutApiKey)
                .queue();

            trackScheduler.queue(track);
          }

          @Override
          public void playlistLoaded(AudioPlaylist playlist) {
            for (AudioTrack track : playlist.getTracks()) {
              trackScheduler.queue(track);
            }
          }

          @Override
          public void noMatches() {
            event
                .reply("No matches!")
                .queue();
          }

          @Override
          public void loadFailed(FriendlyException exception) {
            if (exception.severity == FriendlyException.Severity.COMMON) {
              event
                  .reply(
                      "The track is unavailable, for example, the YouTube track is blocked or not available in your area.")
                  .queue();

              return;
            }

            System.out.println(exception.getMessage());

            event
                .reply("Load failed!")
                .queue();
          }
        });
  }
}
