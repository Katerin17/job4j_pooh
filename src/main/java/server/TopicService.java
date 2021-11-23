package main.java.server;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topicStorage = new ConcurrentHashMap<>();

    public Set<String> getClientsOfTopic(String topic) {
        Set<String> setOfClients = new HashSet<>();
        topicStorage.get(topic).forEach((key, value) -> setOfClients.add(key));
        return setOfClients;
    }

    public boolean isTopicContains(String name) {
        return topicStorage.containsKey(name);
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        String type = request.getType();
        String name = request.getSourceName();
        String param = request.getParameters();
        Map<String, ConcurrentLinkedQueue<String>> currentTopic = topicStorage.get(name);
        if ("POST".equals(type) && currentTopic != null) {
            for (Map.Entry<String, ConcurrentLinkedQueue<String>> entry: currentTopic.entrySet()) {
                entry.getValue().add(param);
            }
            return new HttpResponse("", HttpStatus.OK.getStatus());
        }
        if ("GET".equals(type)) {
            topicStorage.putIfAbsent(name, new ConcurrentHashMap<>());
            topicStorage.get(name).putIfAbsent(param, new ConcurrentLinkedQueue<>());
            currentTopic = topicStorage.get(name);
            String text = currentTopic.get(param).poll();
            if (text != null) {
                return new HttpResponse(text, HttpStatus.OK.getStatus());
            }
        } else {
            return new HttpResponse("", HttpStatus.NO_CONTENT.getStatus());
        }

        return new HttpResponse("", HttpStatus.BAD_REQUEST.getStatus());
    }
}
