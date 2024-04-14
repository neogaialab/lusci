package core.managers;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import core.models.GuildAudioPlayer;
import net.dv8tion.jda.api.entities.Guild;

public class PlayerManager {
  private static PlayerManager INSTANCE;

  private Map<Long, GuildAudioPlayer> musicManagers;
  private AudioPlayerManager playerManager;

  public PlayerManager() {
    this.musicManagers = new HashMap<>();
    this.playerManager = new DefaultAudioPlayerManager();

    AudioSourceManagers.registerRemoteSources(this.playerManager);
  }

  public GuildAudioPlayer getGuildAudioPlayer(Guild guild) {
    return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
      GuildAudioPlayer guildMusicManager = new GuildAudioPlayer(playerManager);

      guild.getAudioManager().setSendingHandler(guildMusicManager.sendHandler);

      return guildMusicManager;
    });
  }

  public AudioPlayerManager getPlayerManager() {
    return this.playerManager;
  }

  public static PlayerManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new PlayerManager();
    }

    return INSTANCE;
  }
}