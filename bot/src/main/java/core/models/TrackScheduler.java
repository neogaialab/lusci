package core.models;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackScheduler extends AudioEventAdapter {

  private final AudioPlayer player;
  private final BlockingQueue<AudioTrack> queue;

  public TrackScheduler(AudioPlayer player) {
    this.player = player;
    this.queue = new LinkedBlockingQueue<>();
  }

  public void queue(AudioTrack track) {
    Boolean started = this.player.startTrack(track, true);

    if (!started) {
      this.queue.offer(track);
    }
  }

  public boolean isAudioPlaying() {
    return !this.player.isPaused();
  }

  public void nextTrack() {
    this.player.startTrack(this.queue.poll(), false);
  }

  @Override
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    if (endReason.mayStartNext) {
      this.nextTrack();
    }
  }

  public BlockingQueue<AudioTrack> getQueue() {
    return this.queue;
  }
}