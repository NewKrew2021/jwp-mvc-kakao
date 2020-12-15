package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public void privateFieldAccess() throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class<Student> clazz = Student.class;
        Student student = clazz.newInstance();

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "reflection");

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 999);

        assertThat(student.getName()).isEqualTo("reflection");
        assertThat(student.getAge()).isEqualTo(999);
    }

    @Test
    public void constructQuestion() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor<Question> constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class);
        Question instance = constructor.newInstance("writer", "title", "contents");

        assertThat(instance.getWriter()).isEqualTo("writer");
        assertThat(instance.getTitle()).isEqualTo("title");
        assertThat(instance.getContents()).isEqualTo("contents");
    }
}
