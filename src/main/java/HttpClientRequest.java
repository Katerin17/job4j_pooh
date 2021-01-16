import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("checkstyle:LineLength")
public class HttpClientRequest {
    private static int size = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executor = Executors.newFixedThreadPool(size);
    private String agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";
    private HttpClient httpClient = HttpClient.newBuilder()
            .executor(executor)
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public HttpRequest receiveGetRequest(String mode, String name) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://localhost/get/" + mode + "/" + name))
                .setHeader("User-Agent", agent)
                .build();
    }

    public HttpRequest receivePostRequest(String mode, HttpRequest.BodyPublisher body) {
        return HttpRequest.newBuilder()
                .POST(body)
                .uri(URI.create("https://localhost/post/" + mode))
                .setHeader("User-Agent", agent)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
    }

    public String getResponse(HttpRequest request) throws ExecutionException, InterruptedException {
        CompletableFuture<HttpResponse<String>> result = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return result.get().body();
    }
}
