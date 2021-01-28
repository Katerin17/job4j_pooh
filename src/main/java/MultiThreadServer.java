import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@SuppressWarnings("checkstyle:LineLength")
public class MultiThreadServer {
    private static int size = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executor = Executors.newFixedThreadPool(size);
    private static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(8080)) {
            while (!server.isClosed()) {
                Socket client = server.accept();
                executor.execute(() -> {
                    try (BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
                         PrintWriter output = new PrintWriter(client.getOutputStream())) {
                        if (input.ready()) {
                            String s = input.lines().collect(Collectors.joining());
                            System.out.println(s);
                            if (s.startsWith("POST")) {
                                if (s.matches("POST /queue/.+")) {
                                    queue.offer(s);
                                }
                            } else if (s.startsWith("GET")) {
                                if (s.matches("GET /queue/.+")) {
                                    queue.poll();
                                }
                            }
                        }
                        output.println("HTTP/1.1 200 OK");
                        output.println("Content-Type: text/html; charset=utf-8");
                        output.println();
                        output.println("<p>The request accepted.</p>");
                        output.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
