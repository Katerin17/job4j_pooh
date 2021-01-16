import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.HttpRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    private static int size = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executor = Executors.newFixedThreadPool(size);

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(3345)) {
            while (!server.isClosed()) {
                Socket client = server.accept();
                executor.execute(() -> {
                    try {
                        Connection connection = new Connection(client);
                        while (!client.isClosed()) {
                            HttpRequest entry = connection.receive();
                            HttpClientRequest httpClientRequest = new HttpClientRequest();
                            String s = httpClientRequest.getResponse(entry);
                        }
                        connection.close();
                        System.out.println("Closing connections...");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
