package core.mvc.asis;

import core.mvc.DefaultView;
import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private RequestMapping rm;
    private AnnotationHandlerMapping annotationMapping;

    @Override
    public void init() throws ServletException {
        rm = new RequestMapping();
        rm.initMapping();
        annotationMapping = new AnnotationHandlerMapping("next.controller");
        annotationMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        HandlerExecution execution = annotationMapping.getHandler(req);
        if (execution == null) {
            serviceWithLegacy(req, resp, requestUri);
            return;
        }
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

    private void serviceWithLegacy(HttpServletRequest req, HttpServletResponse resp, String requestUri) throws ServletException {
        Controller controller = rm.findController(requestUri);
        try {
            String viewName = controller.execute(req, resp);
            move(viewName, req, resp);
        } catch (Throwable e) {
            logError(e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        new DefaultView(viewName).render(Collections.emptyMap(), req, resp);
    }
}
