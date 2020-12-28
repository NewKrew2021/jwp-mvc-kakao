package core.mvc;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class RequestHandlers {

    private List<RequestHandler> handlers;

    public RequestHandlers(List<RequestHandler> handlers) {
        this.handlers = handlers;
    }

    public RequestHandlers(RequestHandler... handlers) {
        this(Arrays.asList(handlers));
    }

    public RequestHandler find(HttpServletRequest request){
        return handlers.stream()
                .filter(handler -> handler.isSupport(request))
                .findFirst()
                .orElseThrow( () -> new RequestHandlerNotFoundException(request));

    }
}
