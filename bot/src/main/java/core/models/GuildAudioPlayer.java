package core.models;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import lib.lavaplayer.JdaAudioSendHandler;

public class GuildAudioPlayer {

  public AudioPlayer audioPlayer;
  public TrackScheduler scheduler;

  public JdaAudioSendHandler sendHandler;

  public GuildAudioPlayer(AudioPlayerManager playerManager) {
    this.audioPlayer = playerManager.createPlayer();
    this.sendHandler = new JdaAudioSendHandler(this.audioPlayer);
    this.scheduler = new TrackScheduler(this.audioPlayer);
    this.audioPlayer.addListener(scheduler);
  }
}