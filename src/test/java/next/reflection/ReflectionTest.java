package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        printFields(clazz);
        printConstructors(clazz);
        printMethods(clazz);
    }

    private void printFields(Class<Question> clazz) {
        System.out.println("[FIELDS]");
        for (Field field : clazz.getDeclaredFields()) {
            System.out.printf("field: %s %s%n", field.getType().getSimpleName(), field.getName());
        }
        System.out.println();
    }

    private void printConstructors(Class<Question> clazz) {
        System.out.println("[CONSTRUCTORS]");
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            System.out.printf("constructor: %s (%s)%n", constructor.getName(), toString(constructor.getParameters()));
        }
        System.out.println();
    }

    private String toString(Parameter[] parameters) {
        return Arrays.stream(parameters)
                .map(Parameter::getType)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(","));
    }

    private void printMethods(Class<Question> clazz) {
        System.out.println("[METHODS]");
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.printf("method: %s %s(%s)%n", method.getReturnType().getSimpleName(), method.getName(), toString(method.getParameters()));
        }
        System.out.println();
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
        }
    }
}
