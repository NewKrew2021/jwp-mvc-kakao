package next.reflection;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        logger.debug(clazz.getName());

        // 생성자
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(constructor -> {
                    logger.debug(MessageFormat.format("constructors: {0} {1} ({2})",
                            Modifier.toString(constructor.getModifiers()),
                            clazz.getName(),
                            Arrays.stream(constructor.getParameterTypes()).map(it -> it.getName()).collect(Collectors.joining(", "))));
                });

        // 필드
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    logger.debug(MessageFormat.format("fields: {0} {1}(type:{2})",Modifier.toString(field.getModifiers()), field.getName(), field.getType() ));
                });

        // 매소드
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> {
                    logger.debug(MessageFormat.format("methods: {0} {1} {2} ({3})",
                            Modifier.toString(method.getModifiers()),
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

    @Test
    public void privateFieldAccess() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Class<Student> clazz = Student.class;

        Student student = clazz.getDeclaredConstructor().newInstance();

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "sehan");
        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 18);

        assertThat(student.getName()).isEqualTo("sehan");
        assertThat(student.getAge()).isEqualTo(18);

    }

    @Test
    void createInstanceByConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class clazz = Question.class;

        Constructor<Question> constructor1 = clazz.getDeclaredConstructor(String.class, String.class, String.class);
        Question q1 = constructor1.newInstance("nio", "title", "contents");

        RecursiveComparisonConfiguration config = new RecursiveComparisonConfiguration();
        config.ignoreFields("createdDate");
        assertThat(q1).usingRecursiveComparison(config)
                .isEqualTo(new Question("nio", "title", "contents"));

        Date now = new Date();
        Constructor<Question> constructor2 = clazz.getDeclaredConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);
        Question q2 = constructor2.newInstance(1L, "nio", "title", "contents", now, 0);

        assertThat(q2)
                .usingRecursiveComparison().isEqualTo(new Question(1L, "nio", "title", "contents", now, 0));
    }


}
