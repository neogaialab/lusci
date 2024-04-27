package lib.java;

import java.io.IOException;
import java.io.InputStream;

import lib.gemini.FunctionCalling;

public class FileUtils {
  public static String readResourceFile(String fileName) throws IOException {
    InputStream inputStream = FunctionCalling.class.getClassLoader().getResourceAsStream(fileName);

    if (inputStream != null) {
      byte[] bytes = inputStream.readAllBytes();
      return new String(bytes);
    } else {
      throw new IOException("File not found in resources: " + fileName);
    }
  }
}
