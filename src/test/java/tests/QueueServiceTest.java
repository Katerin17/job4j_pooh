package test.java.tests;

import main.java.server.HttpRequest;
import main.java.server.HttpResponse;
import main.java.server.QueueService;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        /* Добавляем данные в очередь weather. Режим queue */
        queueService.process(
                new HttpRequest("POST", "queue", "weather", paramForPostMethod)
        );
        /* Забираем данные из очереди weather. Режим queue */
        HttpResponse result = queueService.process(
                new HttpRequest("GET", "queue", "weather", null)
        );
        assertThat(result.getText(), is("temperature=18"));
    }

    @Test
    public void queueSizeShouldBe3() {
        QueueService queueService = new QueueService();
        queueService.process(new HttpRequest("POST", "queue", "weather", "temperature=-1"));
        queueService.process(new HttpRequest("POST", "queue", "weather", "wind=SSW9km/h"));
        queueService.process(new HttpRequest("POST", "queue", "weather", "humidity=81%"));
        queueService.process(new HttpRequest("POST", "queue", "weather", "UVIndex=0"));
        queueService.process(new HttpRequest("GET", "queue", "weather", null));
        assertThat(queueService.getQueueSize("weather"), is(3));
    }

    @Test
    public void queueStorageSizeShouldBe3() {
        QueueService queueService = new QueueService();
        queueService.process(new HttpRequest("POST", "queue", "weather", "temperature=-1"));
        queueService.process(new HttpRequest("POST", "queue", "worldTime", "Moscow=22h52m"));
        queueService.process(new HttpRequest("POST", "queue", "stocks", "Apple=150,96"));
        assertThat(queueService.getQueueStorageSize(), is(3));
    }

    @Test
    public void requestShouldBeBad() {
        QueueService queueService = new QueueService();
        HttpResponse response = queueService.process(new HttpRequest("PULL", "queue", "weather", "temperature=18"));
        assertThat(response.getStatus(), is("400 INVALID REQUEST"));
    }

    @Test
    public void responseShouldBeEmpty() {
        QueueService queueService = new QueueService();
        HttpResponse response = queueService.process(new HttpRequest("GET", "queue", "weather", null));
        assertThat(response.getStatus(), is("204 NO CONTENT"));
    }
}



