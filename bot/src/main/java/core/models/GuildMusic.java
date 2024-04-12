package core.models;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import core.managers.TrackScheduler;
import lib.player.JdaAudioSendHandler;

public class GuildMusic {

  public AudioPlayer audioPlayer;
  public TrackScheduler scheduler;

  private JdaAudioSendHandler sendHandler;

  public GuildMusic() {
  }

  public AudioPlayer getAudioPlayer() {
    return this.audioPlayer;
  }

  public void setAudioPlayer(AudioPlayer audioPlayer) {
    this.audioPlayer = audioPlayer;
  }

  public TrackScheduler getScheduler() {
    return this.scheduler;
  }

  public void setScheduler(TrackScheduler scheduler) {
    this.scheduler = scheduler;
  }

  public JdaAudioSendHandler getSendHandler() {
    return this.sendHandler;
  }

  public void setSendHandler(JdaAudioSendHandler sendHandler) {
    this.sendHandler = sendHandler;
  }
}