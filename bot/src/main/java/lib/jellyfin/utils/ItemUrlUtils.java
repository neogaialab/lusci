package lib.jellyfin.utils;

import core.Bot;
import okhttp3.HttpUrl;

public class ItemUrlUtils {
  public static String getItemStreamUrl(String itemId) {
    var url = Bot.env.get("JELLYFIN_URL") + "/Items/" + itemId + "/Download";

    return addAuthorizationToUrl(url);
  }

  public static String addAuthorizationToUrl(String url) {
    var httpUrl = HttpUrl
      .parse(url)
      .newBuilder()
      .removeAllQueryParameters("apiKey")
      .removeAllQueryParameters("api_key")
      .addQueryParameter("apiKey", Bot.env.get("JELLYFIN_API_KEY"))
      .addQueryParameter("api_key", Bot.env.get("JELLYFIN_API_KEY"))
      .build();

    return httpUrl.toString();
  }

  public static String removeAuthorizationFromUrl(String url) {
    var httpUrl = HttpUrl
      .parse(url)
      .newBuilder()
      .removeAllQueryParameters("apiKey")
      .removeAllQueryParameters("api_key")
      .build();

    return httpUrl.toString();
  }

  public static Boolean isJellyfinUrl(String url) {
    return url.startsWith(Bot.env.get("JELLYFIN_URL"));
  }
}
