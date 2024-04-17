package lib.discord.command;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
      ClassLoader classLoader = CommandManager.class.getClassLoader();
      String path = packageName.replace(".", "/");
      Enumeration<URL> resources = classLoader.getResources(path);

      while (resources.hasMoreElements()) {
        URL resource = resources.nextElement();

        if (resource.getProtocol().equals("jar")) {
          loadClassesFromJar(resource, path, packageName);
        } else {
          File dir = new File(resource.toURI());

          if (dir.isDirectory()) {
            scanDirectory(dir, packageName);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void loadClassesFromJar(URL resource, String path, String packageName) throws IOException,
      ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException,
      InvocationTargetException {
    String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
    JarFile jarFile = new JarFile(jarPath);
    Enumeration<JarEntry> entries = jarFile.entries();

    while (entries.hasMoreElements()) {
      JarEntry entry = entries.nextElement();
      String entryName = entry.getName();

      if (entryName.startsWith(path) && entryName.endsWith(".class")) {
        String className = entryName.substring(0, entryName.length() - 6)
            .replace("/", ".");
        Class<?> clazz = Class.forName(className);

        if (BotCommand.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
          Constructor<?> constructor = clazz.getConstructor();
          BotCommand command = (BotCommand) constructor.newInstance();

          registerCommand(command);
        }
      }
    }

    jarFile.close();
  }

  private static void scanDirectory(File dir, String packageName) throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    File[] files = dir.listFiles();

    if (files != null) {
      for (File file : files) {
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
}
