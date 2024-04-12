package lib.command;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

  private static final Map<String, BotCommand> commands = new HashMap<>();

  public static void registerCommand(BotCommand command) {
    CommandManager.commands.put(command.data.getName(), command);
  }

  public static Collection<BotCommand> getAllCommands() {
    return Collections.unmodifiableCollection(commands.values());
  }

  public static BotCommand getCommand(String label) {
    return commands.get(label);
  }

  public static void loadCommandsFromPackage(String packageName) {
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      String path = packageName.replace(".", "/");
      Enumeration<URL> resources = classLoader.getResources(path);

      while (resources.hasMoreElements()) {
        URL resource = resources.nextElement();
        File dir = new File(resource.getFile());

        if (dir.isDirectory()) {
          scanDirectory(dir, packageName);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void scanDirectory(File dir, String packageName) throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    for (File file : dir.listFiles()) {
      if (file.isFile() && file.getName().endsWith(".class")) {
        String className = packageName + "." + file.getName().replace(".class", "");
        Class<?> clazz = Class.forName(className);

        if (BotCommand.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
          Constructor<?> constructor = clazz.getConstructor();
          BotCommand command = (BotCommand) constructor.newInstance();

          registerCommand(command);
        }
      }
    }
  }
}
