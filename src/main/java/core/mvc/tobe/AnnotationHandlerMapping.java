package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        initialize();
    }

    void initialize() {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class, true);
        controllers.stream()
                .map(it -> Pair.of(
                        instanceFactory.create(it),
                        Arrays.stream(it.getDeclaredMethods())
                                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                                .collect(Collectors.toList()))
                )
                .flatMap( instanceAndMethodsPair -> instanceAndMethodsPair.getValue()
                        .stream()
                        .map( method -> Pair.of( instanceAndMethodsPair.getKey(), method ))
                )
                .forEach( handlerKeyAndExecutionPair -> addHandlerExecution(handlerKeyAndExecutionPair.getKey(), handlerKeyAndExecutionPair.getValue()));
    }

    private void addHandlerExecution(Object instance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        handlerExecutions.put(
                new HandlerKey(requestMapping.value(), requestMapping.method()),
                new HandlerExecution(instance, method));

    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    public boolean hasHandler(HttpServletRequest request){
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.containsKey(new HandlerKey(requestUri, rm));
    }

    private static class Pair<K, V> {
        private final K key;
        private final V value;

        private Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public static <K, V> Pair <K, V> of(K key, V value){
            return new Pair(key, value);
        }
    }

}
