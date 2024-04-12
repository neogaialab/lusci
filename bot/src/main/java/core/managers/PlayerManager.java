package core.managers;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import core.models.GuildMusic;
import core.utils.GuildMusicFactory;
import net.dv8tion.jda.api.entities.Guild;

public class PlayerManager {
  private static PlayerManager INSTANCE;

  private Map<Long, GuildMusic> musicManagers;
  private AudioPlayerManager audioPlayerManager;

  public PlayerManager() {
    this.musicManagers = new HashMap<>();
    this.audioPlayerManager = new DefaultAudioPlayerManager();

    AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
    AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
  }

  public GuildMusic getGuildMusic(Guild guild) {
    return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
      GuildMusic guildMusicManager = GuildMusicFactory.from(this.audioPlayerManager);

      guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

      return guildMusicManager;
    });
  }

  public AudioPlayerManager getAudioPlayerManager() {
    return this.audioPlayerManager;
  }

  public static PlayerManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new PlayerManager();
    }

    return INSTANCE;
  }
}