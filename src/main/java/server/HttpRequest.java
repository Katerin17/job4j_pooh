package main.java.server;

public class HttpRequest {
    /* GET or POST */
    private final String type;
    /* queue or topic */
    private final String mode;
    private final String sourceName;
    private final String parameters;

    public HttpRequest(String type, String mode, String sourceName, String parameters) {
        this.type = type;
        this.mode = mode;
        this.sourceName = sourceName;
        this.parameters = parameters;
    }

    public static HttpRequest of(String content) {
        String[] allContent = content.split("\r\n\r\n");
        String[] headers = allContent[0].split(" ");
        String type = headers[0];
        String[] modeNameParam = headers[1].split("/");
        String mode = modeNameParam[1];
        String name = modeNameParam[2];
        String param = null;
        if (modeNameParam.length > 3) {
            param = modeNameParam[3];
        }
        if (allContent.length > 1) {
            param = allContent[1];
        }

        return new HttpRequest(type, mode, name, param);
    }

    public String getType() {
        return type;
    }

    public String getMode() {
        return mode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParameters() {
        return parameters;
    }
}
