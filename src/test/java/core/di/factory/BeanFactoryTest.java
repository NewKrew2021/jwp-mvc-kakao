package core.di.factory;

import com.google.common.collect.Sets;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.di.factory.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private Logger logger = LoggerFactory.getLogger(BeanFactoryTest.class);

    private Reflections reflections;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        reflections = new Reflections("core.di.factory.example");
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        beanFactory = new BeanFactory(preInstanticateClazz);
        beanFactory.initialize();
    }

    @Test
    void scanController(){
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        assertThat(controllers).containsExactly(QnaController.class);
        controllers.stream()
                .forEach(controller -> {
                    logger.debug(controller.getName());
                });
    }

    @Test
    void scanService(){
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        assertThat(services).containsExactly(MyQnaService.class);
        services.stream()
                .forEach(service -> {
                    logger.debug(service.getName());
                });
    }

    @Test
    void scanRepository(){
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
        assertThat(repositories).containsExactlyInAnyOrder(JdbcUserRepository.class, JdbcQuestionRepository.class);
        repositories.stream()
                .forEach(repository -> {
                    logger.debug(repository.getName());
                });
    }

    @Test
    public void di() throws Exception {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());
        assertNotNull(qnaController.getQnaService().getUserRepository());
        assertNotNull(qnaController.getQnaService().getQuestionRepository());

        MyQnaService qnaService = beanFactory.getBean(MyQnaService.class);
        assertThat(qnaController.getQnaService() == qnaService).isTrue();

        UserRepository userRepository = beanFactory.getBean(JdbcUserRepository.class);
        assertThat(userRepository == qnaController.getQnaService().getUserRepository()).isTrue();
        assertThat(userRepository == qnaService.getUserRepository()).isTrue();
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
