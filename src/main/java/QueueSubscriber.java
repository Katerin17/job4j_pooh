import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class QueueSubscriber implements Subscriber {

    @Override
    public void update() {
        CompletableFuture.runAsync(() -> {
            try {
                Connection connection = new Connection(new Socket("localhost", 3345));
                while (!QueuePublisher.getQueue().isEmpty()) {
                    connection.send(QueuePublisher.getQueue().poll());
                }
                connection.close();
                System.out.println("Closing connections...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}