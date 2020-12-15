package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("class name: {}", clazz.getName());
        for (Field field : clazz.getDeclaredFields()) {
            logger.debug("  field : {} {}", Modifier.toString(field.getModifiers()), field.getName());
        }

        for (Constructor constructor : clazz.getConstructors()) {
            String parameters = Stream.of(constructor.getParameters())
                    .map(Parameter::getType)
                    .map(Class::getTypeName)
                    .collect(Collectors.joining(", "));
            logger.debug("  constructor: {}({})", clazz.getSimpleName(), parameters);
        }

        for (Method method : clazz.getDeclaredMethods()) {
            String parameters = Stream.of(method.getParameters())
                    .map(Parameter::getType)
                    .map(Class::getTypeName)
                    .collect(Collectors.joining(", "));
            logger.debug("  method: {}({})", method.getName(), parameters);
        }
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
