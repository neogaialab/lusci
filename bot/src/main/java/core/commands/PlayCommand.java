package core.commands;

import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import core.Bot;
import core.managers.PlayerManager;
import core.models.GuildAudioPlayer;
import core.models.TrackScheduler;
import lib.command.BotCommand;
import lib.discord.ChannelUtils;
import lib.jellyfin.Search;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.managers.AudioManager;
import okhttp3.HttpUrl;
import okhttp3.Request;

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

    audioManager.openAudioConnection(voiceChannel);

    PlayerManager playerManager = PlayerManager.getInstance();
    GuildAudioPlayer guildAudioPlayer = playerManager.getGuildAudioPlayer(guild);

    TrackScheduler trackScheduler = guildAudioPlayer.scheduler;

    String query = event.getOption("query").getAsString();
    String originalQuery = query;

    if (query.startsWith(Bot.env.get("JELLYFIN_URL"))) {
      query = query + "?api_key=" + Bot.env.get("JELLYFIN_API_KEY");
    }

    String trackName = originalQuery;

    try {
      new URI(query).toURL();
    } catch (Exception e) {
      if (e instanceof IllegalArgumentException) {
        var httpUrl = HttpUrl
            .parse(Bot.env.get("JELLYFIN_URL") + "/Search/Hints")
            .newBuilder()
            .addQueryParameter("apiKey", Bot.env.get("JELLYFIN_API_KEY"))
            .addQueryParameter("searchTerm", query)
            .addQueryParameter("includeItemTypes", "Audio")
            .build();

        var request = new Request.Builder()
            .url(httpUrl)
            .build();

        try (var response = Bot.httpClient.newCall(request).execute()) {
          var responseBody = response.body().string();

          ObjectMapper objectMapper = new ObjectMapper();
          Search jsonResult = objectMapper.readValue(responseBody, Search.class);

          if (jsonResult.TotalRecordCount() == 0) {
            event
                .reply("No matches!")
                .queue();

            return;
          }

          var firstHint = jsonResult.SearchHints()[0];

          trackName = firstHint.Name();
          query = Bot.env.get("JELLYFIN_URL") + "/Items/" + firstHint.ItemId() + "/Download?apiKey="
              + Bot.env.get("JELLYFIN_API_KEY");
        } catch (Exception e2) {
          e2.printStackTrace();
        }
      }
    }

    String identifier = query;
    String finalTrackName = trackName;

    playerManager
        .getPlayerManager()
        .loadItemOrdered(guildAudioPlayer.audioPlayer, identifier, new AudioLoadResultHandler() {

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
