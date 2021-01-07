package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class AnnotationHandlerMapping {
    private final String[] basePackages;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(String... basePackages) {
        this.basePackages = basePackages;
    }

    public void initialize() {
        try {
            initializeHandlerExecutions();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeHandlerExecutions() throws InstantiationException, IllegalAccessException {
        for (Class<?> clazz : new ControllerScanner(basePackages).scan()) {
            Object controllerObject = clazz.newInstance();
            Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> addHandlerExecution(controllerObject, method));
        }
    }

    private void addHandlerExecution(Object controllerObject, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMapping.method()),
                new HandlerExecution(method, controllerObject));
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
