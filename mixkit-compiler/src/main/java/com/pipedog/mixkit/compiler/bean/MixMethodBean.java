package com.pipedog.mixkit.compiler.bean;

import java.util.List;

/**
 * 方法信息描述
 * @author liang
 */
public class MixMethodBean {

    /**
     * 完整类名
     */
    public String className;

    /**
     * 真实方法名
     */
    public String methodName;

    /**
     * 方法参数信息列表
     */
    public List<MixParameterBean> parameters;

}
