package core.annotation.web;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    @AliasFor("path")
    String value() default "";

    @AliasFor("value")
    String path() default "";
}
