package com.muse.router.facade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by GuoWee on 2018/4/27.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Autowired {
    String name() default "";

    boolean required() default false;

    boolean throwOnNull() default false;

    String desc() default "none desc.";
}
