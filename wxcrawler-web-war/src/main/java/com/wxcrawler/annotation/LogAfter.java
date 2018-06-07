package com.wxcrawler.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by RoyChan on 2018/6/6.
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface LogAfter {

    Class clazz();

    String msg() default "";
}
