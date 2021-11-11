package test.java.tests;

import main.java.server.HttpRequest;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HttpClientRequestTest {

    @Test
    public void whenQueueModePostMethod() {
        String ls = System.lineSeparator();
        String content = "POST /queue/weather HTTP/1.1" + ls +
                "Host: localhost:9000" + ls +
                "User-Agent: curl/7.72.0" + ls +
                "Accept: */*" + ls +
                "Content-Length: 14" + ls +
                "Content-Type: application/x-www-form-urlencoded" + ls +
                "" + ls +
                "temperature=18" + ls;
        HttpRequest req = HttpRequest.of(content);
        assertThat(req.getType(), is("POST"));
        assertThat(req.getMode(), is("queue"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getParameters(), is("temperature=18"));
    }

    @Test
    public void whenQueueModeGetMethod() {
        String ls = System.lineSeparator();
        String content = "GET /queue/weather HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls + ls + ls;
        HttpRequest req = HttpRequest.of(content);
        assertThat(req.getType(), is("GET"));
        assertThat(req.getMode(), is("queue"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getParameters(), is(""));
    }

    @Test
    public void whenTopicModePostMethod() {
        String ls = System.lineSeparator();
        String content = "POST /topic/weather HTTP/1.1" + ls +
                "Host: localhost:9000" + ls +
                "User-Agent: curl/7.72.0" + ls +
                "Accept: */*" + ls +
                "Content-Length: 14" + ls +
                "Content-Type: application/x-www-form-urlencoded" + ls +
                "" + ls +
                "temperature=18" + ls;
        HttpRequest req = HttpRequest.of(content);
        assertThat(req.getType(), is("POST"));
        assertThat(req.getMode(), is("topic"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getParameters(), is("temperature=18"));
    }

    @Test
    public void whenTopicModeGetMethod() {
        String ls = System.lineSeparator();
        String content = "GET /topic/weather/client407 HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls + ls + ls;
        HttpRequest req = HttpRequest.of(content);
        assertThat(req.getType(), is("GET"));
        assertThat(req.getMode(), is("topic"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getParameters(), is("client407"));
    }

}
