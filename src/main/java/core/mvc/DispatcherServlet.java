package core.mvc;

import core.mvc.handlermapping.AnnotationHandlerMapping;
import core.mvc.handlermapping.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private AnnotationHandlerMapping annotationMapping;

    @Override
    public void init() throws ServletException {
        annotationMapping = new AnnotationHandlerMapping("next.controller");
        annotationMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        HandlerExecution execution = annotationMapping.getHandler(req);
        service(req, resp, execution);
    }

    private void service(HttpServletRequest req, HttpServletResponse resp, HandlerExecution execution) throws ServletException {
        try {
            ModelAndView modelAndView = execution.handle(req, resp);
            modelAndView.getView().render(modelAndView.getModel(), req, resp);
        } catch (Throwable exception) {
            logError(exception);
            throw new ServletException(exception.getMessage());
        }
    }

    private void logError(Throwable exception) {
        logger.error("Exception : {}", exception);
    }
}
