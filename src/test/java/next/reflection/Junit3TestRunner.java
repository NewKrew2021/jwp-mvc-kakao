package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(clazz.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

    }
}
