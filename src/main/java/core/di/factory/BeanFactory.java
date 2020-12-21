package core.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    private Object addBean(Class<?> clazz, Object object) {
        beans.put(clazz, object);
        return object;
    }

    public void initialize() {
        preInstanticateBeans.forEach(clazz -> addBean(clazz, createInstance(clazz)));
    }

    /**
     * 인스턴스를 생성한다
     *
     * @Inject 가 붙은 생성자를 우선으로 사용하고 없으면 default 생성자를 사용해서 인스턴스를 생성한다
     * 생성된 인스턴스는 beans 에 캐싱 된다.
     *
     * @param clazz
     * @return 이미 BeanFactory 에 등록되어 있으면 beans 에 있는 인스턴스를 return. 그렇지 않으면 새로 생성해서 return
     */
    private Object createInstance(Class clazz) {
        clazz = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);

        Object bean = getBean(clazz);
        if( bean != null ) return bean;

        Constructor injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        try {
            if (injectedConstructor != null) {
                List<Object> params = new ArrayList<>();
                for( Class paramType : injectedConstructor.getParameterTypes()){
                    params.add( createInstance(paramType) );
                }
                return addBean( clazz, injectedConstructor.newInstance(params.toArray()));
            }
            return addBean(clazz, BeanFactoryUtils.getDefaultConstructor(clazz).newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(clazz + " 생성도중 문제가 발생했습니다", e);
        }
    }

}
