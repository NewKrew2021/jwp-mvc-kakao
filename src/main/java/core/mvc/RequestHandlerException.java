package core.mvc;

public class RequestHandlerException extends RuntimeException {
    public RequestHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
