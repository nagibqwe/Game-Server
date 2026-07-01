package game.core.message;

import java.lang.annotation.*;

/**
 * @Desc TODO
 * @Date 2021/5/13 16:36
 * @Auth ZUncle
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Message {

    int id() default 0;

    Class<?> clazz() default Object.class;
}
