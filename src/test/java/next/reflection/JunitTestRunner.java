package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JunitTestRunner {
    protected void invokeMethod(Object instance, Method method) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
