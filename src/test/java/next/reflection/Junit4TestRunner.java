package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Junit4TestRunner extends JunitTestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Object instance = clazz.newInstance();
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> invokeMethod(instance, method));
    }
}
