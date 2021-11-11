package main.java.server;

public enum HttpStatus {

    OK,
    NO_CONTENT,
    BAD_REQUEST;

    public String getStatus() {
        switch (this) {
            case OK:
                return "200 OK";
            case NO_CONTENT:
                return "204 NO CONTENT";
            case BAD_REQUEST:
                return "400 INVALID REQUEST";
            default:
                return null;
        }
    }
}
