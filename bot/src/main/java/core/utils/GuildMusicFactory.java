package core.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import core.managers.TrackScheduler;
import core.models.GuildMusic;
import lib.player.JdaAudioSendHandler;

public class GuildMusicFactory {

  public static GuildMusic from(AudioPlayerManager audioPlayerManager) {
    GuildMusic guildMusic = new GuildMusic();
    AudioPlayer audioPlayer = audioPlayerManager.createPlayer();
    TrackScheduler trackScheduler = new TrackScheduler(audioPlayer);
    JdaAudioSendHandler audioSendHandler = new JdaAudioSendHandler(audioPlayer);

    guildMusic.setAudioPlayer(audioPlayer);
    guildMusic.setScheduler(trackScheduler);
    audioPlayer.addListener(trackScheduler);
    guildMusic.setSendHandler(audioSendHandler);

    return guildMusic;
  }
}
