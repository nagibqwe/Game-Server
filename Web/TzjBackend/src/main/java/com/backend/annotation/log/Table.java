package com.backend.annotation.log;

import com.backend.struct.log.TableType;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {

    int crossLogType() default 0;

    String name();

    int tableType() default TableType.No;

}
