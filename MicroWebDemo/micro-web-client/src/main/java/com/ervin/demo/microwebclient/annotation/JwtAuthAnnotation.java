package com.ervin.demo.microwebclient.annotation;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JwtAuthAnnotation {
    String value() default "";
}