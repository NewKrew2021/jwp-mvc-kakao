package core.mvc;

import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationSupportRequestHandler implements RequestHandler {

    private final AnnotationHandlerMapping mapping;

    public AnnotationSupportRequestHandler(AnnotationHandlerMapping annotationHandlerMapping) {
        this.mapping = annotationHandlerMapping;
    }

    @Override
    public boolean isSupport(HttpServletRequest request) {
        return mapping.hasHandler(request);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        HandlerExecution handlerExecution = mapping.getHandler(request);
        ModelAndView mnv = handlerExecution.handle(request, response);

        try {
            mnv.getView().render(mnv.getModel(), request, response);
        } catch (Exception e) {
            throw new RuntimeException("view 를 render 하는중에 문제가 발생했습니다", e);
        }
    }
}
