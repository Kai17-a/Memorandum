import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;

public class HttpManager {
  public static void main(String[] args) {
    try {
      Map<String, String> headers = new HashMap<>();
      headers.put("Content-Type", "application/json");
      headers.put("Accept", "application/json");
      HttpResponse<String> response = delete("https://httpbin.org/delete");
      System.out.println(response.statusCode());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * URL存在チェック
   * 
   * @param urlStr
   * @return
   */
  public static boolean isExistURL(String urlStr) {
    URL url;
    int status = 0;
    try {
      url = new URL(urlStr);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("HEAD");
      conn.setInstanceFollowRedirects(false);
      conn.setConnectTimeout(1);
      conn.connect();
      status = conn.getResponseCode();
      conn.disconnect();
      return status == HttpURLConnection.HTTP_OK;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * HttpClientインスタンスを生成
   * 
   * @return HttpClientInstance
   */
  public static HttpClient createClient() {
    return HttpClient.newBuilder().version(Version.HTTP_2).followRedirects(Redirect.NORMAL).build();
  }

  /**
   * 
   * @param url
   * @return
   */
  public static HttpResponse<String> get(String url) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    return fetch(request);
  }

  /**
   * 
   * @param url
   * @param requestBody json文字列
   * @return
   */
  public static HttpResponse<String> post(String url, String requestBody)
      throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .POST(BodyPublishers.ofString(requestBody))
        .build();
    return fetch(request);
  }
  /**
   * 
   * @param url
   * @param requestBody json文字列
   * @param headers ヘッダー
   * @return
   */
  public static HttpResponse<String> post(String url, String requestBody, Map<String, String> headers)
      throws IOException, InterruptedException {
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .POST(BodyPublishers.ofString(requestBody));
    if (Objects.nonNull(headers)) {
      if (headers.size() > 0) {
        for (Map.Entry<String, String> m : headers.entrySet()) {
          requestBuilder.setHeader(m.getKey(), m.getValue());
        }
      }
    }
    return fetch(requestBuilder.build());
  }

  /**
   * 
   * @param url
   * @param requestBody json文字列
   * @return
   */
  public static HttpResponse<String> put(String url, String requestBody)
      throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .PUT(BodyPublishers.ofString(requestBody))
        .build();
    return fetch(request);
  }
  /**
   * 
   * @param url
   * @param requestBody json文字列
   * @param headers ヘッダー
   * @return
   */
  public static HttpResponse<String> put(String url, String requestBody, Map<String, String> headers)
      throws IOException, InterruptedException {
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .PUT(BodyPublishers.ofString(requestBody));
    if (Objects.nonNull(headers)) {
      if (headers.size() > 0) {
        for (Map.Entry<String, String> m : headers.entrySet()) {
          requestBuilder.setHeader(m.getKey(), m.getValue());
        }
      }
    }
    return fetch(requestBuilder.build());
  }

  public static HttpResponse<String> delete(String url) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).DELETE().build();
    return fetch(request);
  }

  /**
   * 
   * @param request
   * @return
   * @throws IOException
   * @throws InterruptedException
   */
  public static HttpResponse<String> fetch(HttpRequest request) throws IOException, InterruptedException {
    HttpClient client = createClient();
    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }
}
