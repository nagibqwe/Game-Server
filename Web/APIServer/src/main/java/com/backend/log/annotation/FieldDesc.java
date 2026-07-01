package com.backend.log.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldDesc {

    boolean show() default true;

    String desc() default "";

    boolean selectKey() default false;
}
