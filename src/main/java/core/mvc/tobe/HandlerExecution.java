package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Object controller;

    public HandlerExecution(Object controller, Method method) {
        this.method = method;
        this.controller = controller;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(controller, request, response);
        } catch( Exception e ){
            throw new HandlerExecutionException("@RequestMapping 된 method 호출과정에 문제가 발생했습니다", e);
        }
    }
}
