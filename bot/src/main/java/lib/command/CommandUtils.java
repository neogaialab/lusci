package lib.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandUtils {
  
  public static void addCommands(JDA api, String packageName) {
    api.addEventListener(new CommandListener());

    CommandManager.loadCommandsFromPackage(packageName);

    Collection<BotCommand> commands = CommandManager.getAllCommands();

    List<SlashCommandData> commandData = new ArrayList<>();
    for (BotCommand command : commands) {
      commandData.add(command.data);
    }

    api.updateCommands()
        .addCommands(commandData)
        .queue();
  }
}
