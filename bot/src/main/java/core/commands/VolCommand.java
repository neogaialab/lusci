package core.commands;

import core.managers.PlayerManager;
import lib.discord.command.BotCommand;
import lib.discord.command.GenericCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class VolCommand extends BotCommand {

  public VolCommand() {
    super(Commands
        .slash("vol", "Change the bot's volume.")
        .addSubcommands(
            new SubcommandData("up", "Increase the volume."),
            new SubcommandData("down", "Decrease the volume."),
            new SubcommandData("mute", "Mute the bot's audio."),
            new SubcommandData("unmute", "Unmute the bot's audio."),
            new SubcommandData("set", "Set the volume.")
                .addOption(OptionType.INTEGER, "volume", "Volume level to set.", true),
            new SubcommandData("get", "Retrieve the current volume.")));
  }

  @Override
  public void execute(GenericCommandEvent event) {
    var guild = event.getGuild();
    var playerManager = PlayerManager.getInstance();
    var guildAudioPlayer = playerManager.getGuildAudioPlayer(guild);
    var audioPlayer = guildAudioPlayer.audioPlayer;

    var vol = audioPlayer.getVolume();
    var subCommandName = event.getSubcommandName();

    switch (subCommandName) {
      case "get":
        event
            .reply("The volume is: " + vol)
            .queue();
        break;
      case "set":
        var newVol = event.getOptionAsInt("volume");

        if(newVol > 100) {
          event
            .reply("The volume can't be higher than 100.")
            .queue();
          break;
        }

        audioPlayer.setVolume(newVol);
        event
            .reply("The volume has been updated to " + vol + ".")
            .queue();
        break;
      case "up":
        if(vol + 10 > 100) {
          event
            .reply("The volume can't be higher than 100.")
            .queue();
          break;
        }

        audioPlayer.setVolume(vol + 10);
        event
            .reply("The volume has been turned up to " + vol + ".")
            .queue();
        break;
      case "down":
        audioPlayer.setVolume(vol - 10);
        event
            .reply("The volume has been turned down to " + vol + ".")
            .queue();
        break;
      case "mute":
        audioPlayer.setVolume(0);
        event
            .reply("The volume has been muted.")
            .queue();
        break;
      case "unmute":
        audioPlayer.setVolume(80);
        event
            .reply("The volume has been unmuted.")
            .queue();
        break;
    }

  }
}
