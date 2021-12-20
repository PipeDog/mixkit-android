package com.pipedog.mixkit.module;

import java.io.Serializable;

/**
 * 函数参数信息描述
 * @author liang
 */
public class MixMethodParameter implements Serializable {

    /**
     * 参数（形参）名称
     */
    public String name;

    /**
     * 参数类型（完整类名），如：java.lang.String
     */
    public String type;

    @Override
    public String toString() {
        return "MixMethodParameter{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
