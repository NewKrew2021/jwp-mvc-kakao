package core.mvc.asis;

import core.mvc.RequestHandler;
import core.mvc.RequestHandlers;
import core.mvc.tobe.AnnotationHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);


    private RequestHandlers requestHandlers;

    @Override
    public void init() throws ServletException {
        requestHandlers = new RequestHandlers(
                RequestHandler.legacy(new RequestMapping()),
                RequestHandler.annotation(new AnnotationHandlerMapping("next.controller.tobe")));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            RequestHandler requestHandler = requestHandlers.find(req);
            requestHandler.handle(req, resp);
        } catch ( RuntimeException e ){
            logger.debug(e.getMessage(), e);
            throw e;
        }
    }


}
