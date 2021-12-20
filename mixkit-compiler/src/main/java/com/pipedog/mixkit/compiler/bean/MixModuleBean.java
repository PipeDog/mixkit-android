package com.pipedog.mixkit.compiler.bean;

import java.util.Map;

/**
 * 自定义 module 描述信息
 * @author liang
 */
public class MixModuleBean {

    /**
     * 完整类名
     */
    public String className;

    /**
     * 方法信息表，key 为导出函数名，value 为对应方法描述信息
     */
    public Map<String, MixMethodBean> methods;

}
