package core.mvc;

import core.mvc.asis.Controller;
import core.mvc.asis.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LegacyRequestHandler implements RequestHandler{

    private Logger logger = LoggerFactory.getLogger(LegacyRequestHandler.class);

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private RequestMapping requestMapping;


    public LegacyRequestHandler(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    @Override
    public boolean isSupport(HttpServletRequest request) {
        return requestMapping.hasMapping(request.getRequestURI());
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp) {
        Controller controller = requestMapping.findController(req.getRequestURI());
        try {
            String viewName = controller.execute(req, resp);
            move(viewName, req, resp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RequestHandlerException("LegacyRequestHandler 가 request 처리중 문제가 발생했습니다", e);
        }
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }
}
