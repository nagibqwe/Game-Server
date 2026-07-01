package com.backend.annotation.log;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldDesc {

    boolean show() default true;

    String desc() default "";

    boolean selectKey() default false;
}
