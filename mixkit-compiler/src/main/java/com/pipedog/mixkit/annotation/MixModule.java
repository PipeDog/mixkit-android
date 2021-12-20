package com.pipedog.mixkit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * 注解导出 module 信息
 * @author liang
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface MixModule {

    /**
     * 自定义 module 名称（可以理解为 module 唯一标识）
     */
    String name() default "";

}
