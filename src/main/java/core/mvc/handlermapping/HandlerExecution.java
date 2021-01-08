package core.mvc.handlermapping;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Method method;
    private final Object controllerObject;

    public HandlerExecution(Method method, Object controllerObject) {
        this.method = method;
        this.controllerObject = controllerObject;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (method != null && controllerObject != null) {
            return (ModelAndView) method.invoke(controllerObject, request, response);
        }
        return null;
    }
}
