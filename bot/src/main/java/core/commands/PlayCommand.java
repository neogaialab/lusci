package core.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import core.managers.PlayerManager;
import core.models.GuildAudioPlayer;
import core.models.TrackScheduler;
import lib.command.BotCommand;
import lib.discord.ChannelUtils;
import lib.jellyfin.Errors;
import lib.jellyfin.services.ItemSearch;
import lib.jellyfin.utils.ItemUrlUtils;
import lib.net.UrlUtils;
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
        .addOption(OptionType.STRING, "query",
            "URL or search query to use.", true));
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

    PlayerManager playerManager = PlayerManager.getInstance();
    GuildAudioPlayer guildAudioPlayer = playerManager.getGuildAudioPlayer(guild);

    TrackScheduler trackScheduler = guildAudioPlayer.scheduler;

    String query = event.getOption("query").getAsString();
    String trackName = query;

    if (ItemUrlUtils.isJellyfinUrl(query)) {
      query = ItemUrlUtils.addAuthorizationToUrl(query);
      trackName = ItemUrlUtils.removeAuthorizationFromUrl(query);
    }
    else if(!UrlUtils.isUrl(query)) {
      try {
        var searchResult = ItemSearch.searchMusic(query);

        trackName = searchResult.name();
        query = searchResult.url();
      } catch (Exception err2) {
        if (err2 instanceof Errors.NoMatchesError) {
          event
            .reply("No track found!")
            .queue();

          return;
        }

        if (err2 instanceof Errors.SearchError) {
          event
            .reply("An error occurred while searching!")
            .queue();

          return;
        }

        event
          .reply("An unknown error occurred!")
          .queue();

        return;
      }
    }

    audioManager.openAudioConnection(voiceChannel);

    String finalUrl = query;
    String finalTrackName = trackName;

    playerManager
        .getPlayerManager()
        .loadItemOrdered(guildAudioPlayer.audioPlayer, finalUrl, new AudioLoadResultHandler() {

          @Override
          public void trackLoaded(AudioTrack track) {
            if (guildAudioPlayer.audioPlayer.getPlayingTrack() != null) {
              event
                .reply("Track added to the queue: " + finalTrackName)
                .queue();
            } else {
              event
                .reply("The bot started playing: " + finalTrackName)
                .queue();
            }

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
