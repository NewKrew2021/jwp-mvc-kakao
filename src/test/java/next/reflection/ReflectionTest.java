package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
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

    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());
        Student student = new Student();
        String name = "재성";
        int age = 10;

        setName(student, name);
        setAge(student, age);

        assertThat(student.getName()).isEqualTo(name);
        assertThat(student.getAge()).isEqualTo(age);
    }

    private void setName(Student student, String name) throws NoSuchFieldException, IllegalAccessException {
        Field nameField = Student.class.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(student, name);
    }

    private void setAge(Student student, int age) throws NoSuchFieldException, IllegalAccessException {
        Field ageField = Student.class.getDeclaredField("age");
        ageField.setAccessible(true);
        ageField.set(student, age);
    }

    @Test
    public void createQuestion() throws Exception {
        Class<Question> clazz = Question.class;
        long questionId = 1L;
        String writer = "writer";
        String title = "title";
        String content = "content";
        Date createdDate = Date.from(Instant.EPOCH);
        int countOfComment = 1;

        Constructor<Question> constructor1 = clazz.getConstructor(String.class, String.class, String.class);
        Question question1 = constructor1.newInstance(writer, title, content);
        assertThat(question1).isEqualTo(new Question(writer, title, content));

        Constructor<Question> constructor2 = clazz.getConstructor(Long.TYPE, String.class, String.class, String.class, Date.class, Integer.TYPE);
        Question question2 = constructor2.newInstance(questionId, writer, title, content, createdDate, countOfComment);
        assertThat(question2).isEqualTo(new Question(questionId, writer, title, content, createdDate, countOfComment));
    }
}
