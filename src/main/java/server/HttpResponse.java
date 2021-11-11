package main.java.server;

public class HttpResponse {

    private final String text;
    private final String status;

    public HttpResponse(String text, String status) {
        this.text = text;
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public String getStatus() {
        return status;
    }

}
