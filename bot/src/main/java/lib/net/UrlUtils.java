package lib.net;

import java.net.URI;

public class UrlUtils {
  public static boolean isUrl(String url) {
    try {
      new URI(url).toURL();
      return true;
    } catch (Exception err) {
      return false;
    }
  }
}
