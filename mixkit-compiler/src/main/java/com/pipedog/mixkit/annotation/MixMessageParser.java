package com.pipedog.mixkit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * 注解导出自定义消息解析器
 * @author liang
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface MixMessageParser {

    /**
     * 自定义解析器名称（为了区分数据类型，无实际意义）
     */
    String name() default "";

}
