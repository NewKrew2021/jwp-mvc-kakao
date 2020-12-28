package core.mvc;

import core.mvc.asis.RequestMapping;
import core.mvc.tobe.AnnotationHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * request 를 처리하는 handler
 */
public interface RequestHandler{

    /**
     * handler 가 해당 request 를 지원하는지 여부.
     *
     * @param request
     * @return
     */
    boolean isSupport(HttpServletRequest request);

    void handle(HttpServletRequest request, HttpServletResponse response);

    static RequestHandler legacy(RequestMapping mapping) {
        return new LegacyRequestHandler(mapping);
    }

    static RequestHandler annotation(AnnotationHandlerMapping mapping){
        return new AnnotationSupportRequestHandler(mapping);
    }

}


