import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.http.HttpRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueuePublisher implements Publisher, Runnable {
    private static ConcurrentLinkedQueue<HttpRequest> queue = new ConcurrentLinkedQueue<>();
    private Set<Subscriber> setSubscribers = new HashSet<>();

    public static ConcurrentLinkedQueue<HttpRequest> getQueue() {
        return queue;
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        setSubscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        setSubscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers() {
        for (Subscriber s : setSubscribers) {
            s.update();
        }
    }

    @SuppressWarnings("checkstyle:LineLength")
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            HttpClientRequest httpClientRequest = new HttpClientRequest();
            while (reader.ready()) {
                String[] arr = reader.readLine().split("/");
                if (arr[0].equalsIgnoreCase("post")) {
                    queue.offer(httpClientRequest.receivePostRequest("queue", HttpRequest.BodyPublishers.ofString(arr[2])));
                } else if (arr[0].equalsIgnoreCase("get")) {
                    queue.offer(httpClientRequest.receiveGetRequest("queue", arr[2]));
                } else {
                    System.out.println("The request is incorrect.");
                }
                notifySubscribers();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
