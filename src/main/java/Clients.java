import java.util.concurrent.CompletableFuture;

public class Clients {
    public static void main(String[] args) {
        QueueSubscriber subscriber1 = new QueueSubscriber();
        QueueSubscriber subscriber2 = new QueueSubscriber();
        QueuePublisher publisher = new QueuePublisher();
        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);
        CompletableFuture.runAsync(publisher);
    }
}
