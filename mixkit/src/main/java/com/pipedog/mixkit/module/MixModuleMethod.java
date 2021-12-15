package com.pipedog.mixkit.module;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义 module 方法信息描述
 * @author liang
 */
public class MixModuleMethod implements Serializable {

    /**
     * 完整类名
     */
    public String className;

    /**
     * 真实函数名
     */
    public String methodName;

    /**
     * 方法参数信息列表
     */
    public List<MixMethodParameter> parameters;

    @Override
    public String toString() {
        return "MixModuleMethod{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters=" + parameters +
                '}';
    }

}
