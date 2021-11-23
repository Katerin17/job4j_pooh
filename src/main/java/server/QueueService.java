package main.java.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final Map<String, ConcurrentLinkedQueue<String>> queueStorage = new ConcurrentHashMap<>();

    public int getQueueStorageSize() {
        return queueStorage.size();
    }

    public int getQueueSize(String name) {
        return queueStorage.containsKey(name) ? queueStorage.get(name).size() : -1;
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        String type = request.getType();
        String queueName = request.getSourceName();
        if ("POST".equals(type)) {
            queueStorage.putIfAbsent(queueName, new ConcurrentLinkedQueue<>());
            queueStorage.get(queueName).add(request.getParameters());
            return new HttpResponse("", HttpStatus.OK.getStatus());
        }
        if ("GET".equals(type)) {
            if (!queueStorage.isEmpty()) {
                String text = queueStorage.get(queueName).poll();
                if (text != null) {
                    return new HttpResponse(text, HttpStatus.OK.getStatus());
                }
            } else {
                return new HttpResponse("", HttpStatus.NO_CONTENT.getStatus());
            }
        }
        return new HttpResponse("", HttpStatus.BAD_REQUEST.getStatus());
    }
}
