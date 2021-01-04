package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Junit3TestRunner extends JunitTestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Object instance = clazz.newInstance();
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> invokeMethod(instance, method));
    }
}
