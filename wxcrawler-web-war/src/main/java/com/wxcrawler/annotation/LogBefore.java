package com.wxcrawler.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by RoyChan on 2018/6/7.
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface LogBefore{

    Class clazz();

    String msg() default "";
}