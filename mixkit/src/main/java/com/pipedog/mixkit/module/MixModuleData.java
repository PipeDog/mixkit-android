package com.pipedog.mixkit.module;

import java.io.Serializable;
import java.util.Map;

/**
 * 自定义 module 信息描述
 * @author liang
 */
public class MixModuleData implements Serializable {

    /**
     * 类名
     */
    public String className;

    /**
     * 方法信息表，key 为导出函数名，value 为对应方法描述信息
     */
    public Map<String, MixModuleMethod> methods;

    @Override
    public String toString() {
        return "MixModuleData{" +
                "className='" + className + '\'' +
                ", methods=" + methods +
                '}';
    }

}
