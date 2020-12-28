package core.mvc.tobe;

import core.di.factory.BeanFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

abstract class InstanceFactoryTest {
    InstanceFactory instanceFactory;
}

class AllNewFactorTest extends InstanceFactoryTest {

    @BeforeEach
    void setUp(){
        instanceFactory = InstanceFactory.allNew();
    }

    @DisplayName("항상 새로운 instance 가 생성된다")
    @Test
    void create(){
        assertThat(instanceFactory.create(SampleObj.class) == instanceFactory.create(SampleObj.class)).isFalse();
    }
}

class SimpleInstanceFactoryTest extends InstanceFactoryTest {

    @BeforeEach
    void setUp(){
        instanceFactory = new SimpleInstanceFactory(new BeanFactory(new HashSet<>(Arrays.asList(SampleObj.class))));
    }

}

class SampleObj {
    private String id = UUID.randomUUID().toString();
}