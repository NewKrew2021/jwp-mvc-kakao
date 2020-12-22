package core.mvc.tobe;

import core.di.factory.BeanFactory;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;

/**
 * clazz 인스턴스를 만들어 주는 Factory
 */
public interface InstanceFactory {

    Object create(Class<?> clazz);

    static InstanceFactory allNew() {
        return clazz -> {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new RuntimeException("class(controller) 인스턴스 생성중에 문제가 발생했습니다", e);
            }
        };
    }

}

class SimpleInstanceFactory implements InstanceFactory {

    private final BeanFactory beanFactory;

    public SimpleInstanceFactory(@Nonnull BeanFactory beanFactory) {
        if( beanFactory == null ) throw new IllegalArgumentException("BeanFactory 파라미터가 넘어오지 않았습니다");
        this.beanFactory = beanFactory;
    }

    @Override
    public Object create(Class<?> clazz){
        if( beanFactory == null )
            return newInstance(clazz);

        Object bean = beanFactory.getBean(clazz);
        if( bean == null ) {
            return newInstance(clazz);
        }
        return bean;
    }

    private Object newInstance(Class<?> clazz){
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException("class(controller) 인스턴스 생성중에 문제가 발생했습니다", e);
        }
    }
}
