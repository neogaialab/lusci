package lib.java;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

public class JarUtils {

  public static boolean isRunningOnJar() {
    return JarUtils.class.getProtectionDomain()
        .getCodeSource()
        .getLocation()
        .getPath()
        .endsWith(".jar");
  }

  public static Manifest getManifest() throws IOException {
    Enumeration<URL> resources = ClassLoader.getSystemClassLoader()
        .getResources("META-INF/MANIFEST.MF");

    while (resources.hasMoreElements()) {
      Manifest manifest = new Manifest(resources.nextElement().openStream());
      
      return manifest;
    }

    return null;
  }
}
