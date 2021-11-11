package test.java.tests;

import main.java.server.HttpRequest;
import main.java.server.HttpResponse;
import main.java.server.TopicService;
import org.junit.Test;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TopicServiceTest {
    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(
                new HttpRequest("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new HttpRequest("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        HttpResponse result1 = topicService.process(
                new HttpRequest("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
        Очередь отсутствует, т.к. еще не был подписан - получит пустую строку */
        HttpResponse result2 = topicService.process(
                new HttpRequest("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.getText(), is("temperature=18"));
        assertThat(result2.getText(), is(""));
    }

    @Test
    public void queueShouldBeCreatedForClient() {
        TopicService topicService = new TopicService();
        /* При первичной отправке клиентом запроса GET создается подписка на topic */
        topicService.process(new HttpRequest("GET", "topic", "weather", "client407"));
        assertThat(topicService.getClientsOfTopic("weather"), is(Set.of("client407")));
    }

    @Test
    public void topicNotShouldBeCreated() {
        TopicService topicService = new TopicService();
        /* С типом запроса POST не должны создаваться topic */
        topicService.process(new HttpRequest("POST", "topic", "weather", "temperature=18"));
        assertThat(topicService.isTopicContains("weather"), is(false));
    }
}
