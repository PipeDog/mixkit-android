package com.pipedog.mixkit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * 注解导出方法信息
 * @author liang
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface MixMethod {

    /**
     * 自定义方法名（可以理解为方法唯一标识）
     */
    String name() default "";

}
