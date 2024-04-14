package lib.jellyfin.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import core.Bot;
import lib.jellyfin.Errors;
import lib.jellyfin.models.Search;
import lib.jellyfin.models.SearchResult;
import lib.jellyfin.utils.ItemUrlUtils;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class ItemSearch {
  static HttpUrl getSearchItemUrl(String query, String itemTypes) {
    return HttpUrl
      .parse(Bot.env.get("JELLYFIN_URL") + "/Search/Hints")
      .newBuilder()
      .addQueryParameter("apiKey", Bot.env.get("JELLYFIN_API_KEY"))
      .addQueryParameter("searchTerm", query)
      .addQueryParameter("includeItemTypes", itemTypes)
      .build();
  }

  public static SearchResult searchMusic(String searchQuery) throws Errors.NoMatchesError, Errors.SearchError {
    var searchItemUrl = getSearchItemUrl(searchQuery, "Audio");

    var request = new Request.Builder()
      .url(searchItemUrl)
      .build();

    try (var response = Bot.httpClient.newCall(request).execute()) {
      var responseBody = response.body().string();

      ObjectMapper objectMapper = new ObjectMapper();
      Search jsonResult = objectMapper.readValue(responseBody, Search.class);

      if (jsonResult.TotalRecordCount() == 0) {
        throw new Errors.NoMatchesError();
      }

      var firstHint = jsonResult.SearchHints()[0];

      var trackName = firstHint.Name();
      var trackUrl = ItemUrlUtils.getItemStreamUrl(firstHint.ItemId());

      return new SearchResult(trackName, trackUrl);
    } catch (Exception e2) {
      if(e2 instanceof Errors.NoMatchesError) {
        throw new Errors.NoMatchesError();
      }

      e2.printStackTrace();

      throw new Errors.SearchError();
    }
  }
}
