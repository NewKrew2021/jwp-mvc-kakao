package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    enum Modifier {
        PRIVATE(2), PUBLIC(1), PROTECTED(3), UNKNOWN(-1);

        private final int value;

        Modifier(int value) {
            this.value = value;
        }

        public static Modifier valueOf(int value){
            return Arrays.stream(Modifier.values())
                    .filter(modifier -> modifier.value == value )
                    .findFirst()
                    .orElse(UNKNOWN);
        }
    }

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        logger.debug(clazz.getName());

        // 생성자
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(constructor -> {
                    logger.debug(MessageFormat.format("constructors: {0} {1} ({2})",
                            Modifier.valueOf(constructor.getModifiers()).name(),
                            clazz.getName(),
                            Arrays.stream(constructor.getParameterTypes()).map(it -> it.getName()).collect(Collectors.joining(" ,"))));
                });

        // 필드
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    logger.debug(MessageFormat.format("fields: {0} {1}(type:{2})",Modifier.valueOf(field.getModifiers()).name(), field.getName(), field.getType() ));
                });

        // 매소드
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> {
                    logger.debug(MessageFormat.format("methods: {0} {1} {2} ({3})",
                            Modifier.valueOf(method.getModifiers()).name(),
                            method.getReturnType(),
                            method.getName(),
                            Arrays.stream(method.getParameters())
                                    .map(it -> it.getType().getName())
                                    .collect(Collectors.joining(" ,"))
                    ));

                });

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
