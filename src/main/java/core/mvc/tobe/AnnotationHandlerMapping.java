package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {

    private final Object[] basePackage;
    private final InstanceFactory instanceFactory;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this(InstanceFactory.allNew(), basePackage);
    }

    public AnnotationHandlerMapping(InstanceFactory instanceFactory, Object... basePackage) {
        this.basePackage = basePackage;
        this.instanceFactory = instanceFactory;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class, true);
        controllers.stream()
                .map(it -> new Pair<>(
                        instanceFactory.create(it),
                        Arrays.stream(it.getDeclaredMethods())
                                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                                .collect(Collectors.toList()))
                )
                .forEach(controllerAndMethodsPair -> {
                    Object controller = controllerAndMethodsPair.getKey();
                    controllerAndMethodsPair.getValue()
                            .forEach(method -> addHandlerExecution(controller, method));
                });
    }

    private void addHandlerExecution(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        handlerExecutions.put(
                new HandlerKey(requestMapping.value(), requestMapping.method()),
                new HandlerExecution(controller, method));
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

}
