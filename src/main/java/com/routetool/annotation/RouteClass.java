package com.routetool.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author: hef
 * @version: 1.0
 * 用于标注需要路由的class
 * @Date: Create in 7:14 下午 2020/12/28
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RouteClass {

    String value() default "";
}
