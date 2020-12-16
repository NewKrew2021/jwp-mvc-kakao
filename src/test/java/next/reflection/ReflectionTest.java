package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("class name: {}", clazz.getName());

        logger.debug("==== fields ==== ");
        for (Field field : clazz.getDeclaredFields()) {
            logger.debug("{} {} {}", Modifier.toString(field.getModifiers()), field.getType().getSimpleName(), field.getName());
        }

        logger.debug("==== constructors ==== ");
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            String parameters = Stream.of(constructor.getParameters())
                    .map(Parameter::getType)
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(", "));
            logger.debug("{} {}({})", Modifier.toString(constructor.getModifiers()), clazz.getSimpleName(), parameters);
        }

        logger.debug("==== methods ==== ");
        for (Method method : clazz.getDeclaredMethods()) {
            String parameters = Stream.of(method.getParameters())
                    .map(Parameter::getType)
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(", "));
            logger.debug("{} {} {}({})", Modifier.toString(method.getModifiers()), method.getReturnType().getSimpleName(), method.getName(), parameters);
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

        setValueOnPrivateField(student, "name", "reflection");
        setValueOnPrivateField(student, "age", 999);

        assertThat(student.getName()).isEqualTo("reflection");
        assertThat(student.getAge()).isEqualTo(999);
    }

    private void setValueOnPrivateField(Object instance, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    @Test
    public void constructor1() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor<Question> constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class);
        Question instance = constructor.newInstance("writer", "title", "contents");

        assertThat(instance.getWriter()).isEqualTo("writer");
        assertThat(instance.getTitle()).isEqualTo("title");
        assertThat(instance.getContents()).isEqualTo("contents");
    }

    @Test
    public void constructor2() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor<Question> constructor = clazz.getDeclaredConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);

        Date createdDate = new Date();
        Question instance = constructor.newInstance(1L, "writer", "title", "contents", createdDate, 3);

        assertThat(instance.getQuestionId()).isEqualTo(1L);
        assertThat(instance.getWriter()).isEqualTo("writer");
        assertThat(instance.getTitle()).isEqualTo("title");
        assertThat(instance.getContents()).isEqualTo("contents");
        assertThat(instance.getCreatedDate()).isEqualTo(createdDate);
        assertThat(instance.getCountOfComment()).isEqualTo(3);
    }
}
