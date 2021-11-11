package main.java.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class MultiThreadServer {

    private final Map<String, Service> modes = new HashMap<>();

    public void start() {
        modes.put("queue", new QueueService());
        modes.put("topic", new TopicService());
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try (ServerSocket server = new ServerSocket(9000)) {
            while (!server.isClosed()) {
                Socket client = server.accept();
                executor.execute(() -> {
                    try (InputStream input = client.getInputStream();
                         PrintWriter output = new PrintWriter(client.getOutputStream())) {
                        if (input.available() != 0) {
                            byte[] buff = new byte[1_000_000];
                            int total = input.read(buff);
                            String text = new String(Arrays.copyOfRange(buff, 0, total), StandardCharsets.UTF_8);
                            HttpRequest request = HttpRequest.of(text);
                            HttpResponse response = modes.get(request.getMode()).process(request);
                            String ls = System.lineSeparator();
                            output.write("HTTP/1.1 " + response.getStatus() + ls);
                            output.write(response.getText().concat(ls));
                            output.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MultiThreadServer().start();
    }
}
