package core.commands;

import java.io.IOException;
import java.util.jar.Manifest;

import lib.discord.command.BotCommand;
import lib.discord.command.GenericCommandEvent;
import lib.java.JarUtils;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class VersionCommand extends BotCommand {

  public VersionCommand() {
    super(Commands.slash("version", "See the bot version."));
  }

  @Override
  public void execute(GenericCommandEvent event) {
    String version = System.getProperty("version");

    if (JarUtils.isRunningOnJar()) {
      try {
        Manifest manifest = JarUtils.getManifest();
        version = manifest.getMainAttributes().getValue("Implementation-Version");
      } catch (IOException e) {
        System.out.println(e);
        event
            .reply("An error occurred while reading the bot version.");
        return;
      }
    }

    if (version == null) {
      event
          .reply("An error occurred while reading the bot version.").queue();
      return;
    }

    event
        .reply(version).queue();
  }
}
