package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test instance = clazz.newInstance();

        Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(instance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
