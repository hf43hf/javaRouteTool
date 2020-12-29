package com.routetool.annotation;

import java.lang.annotation.*;

/**
 * @Author: hef
 * @version: 1.0
 * 用于标注需要路由的method
 * @Date: Create in 8:48 下午 2020/12/28
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RouteMethod {

    String value() default "";
}
