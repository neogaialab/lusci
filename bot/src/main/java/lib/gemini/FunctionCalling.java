package lib.gemini;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.Bot;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FunctionCalling {

  public static JsonNode generate(
      String prompt,
      JsonNode declarationsNode) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="
        + Bot.env.get("GEMINI_API_KEY");

    var requestBody = objectMapper.createObjectNode();
    var contents = objectMapper.createObjectNode();
    var parts = objectMapper.createObjectNode();
    parts.put("text", prompt);
    contents.put("role", "user");
    contents.set("parts", parts);
    requestBody.set("contents", contents);

    var tools = objectMapper.createArrayNode();
    var tool = objectMapper.createObjectNode();
    tools.add(tool);
    tool.set("function_declarations", declarationsNode);

    requestBody.set("tools", tools);

    var request = new Request.Builder()
        .url(url)
        .post(RequestBody.create(requestBody.toString().getBytes()))
        .build();

    var response = Bot.httpClient.newCall(request).execute();
    var responseBody = response.body().string();

    return objectMapper.readTree(responseBody);
  }
}
