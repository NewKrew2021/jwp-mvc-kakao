package core.mvc;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

public class RequestHandlerNotFoundException extends RuntimeException {
    public RequestHandlerNotFoundException(HttpServletRequest request) {
        super(MessageFormat.format("request({0} {1}) 를 처리할 RequestHandler 를 찾지 못했습니다", request.getMethod(), request.getRequestURI()));
    }
}
