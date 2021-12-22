package com.pipedog.mixkit.module;

import java.io.Serializable;
import java.util.Map;

/**
 * 自定义 module 信息描述（允许 class 不同但 module 名相同，也就是说一个 MixModuleData 实例中可能包含多个 class 的导出信息）
 * @author liang
 */
public class MixModuleData implements Serializable {

    /**
     * 方法信息表，key 为导出函数名，value 为对应方法描述信息
     */
    public Map<String, MixModuleMethod> methods;


    // OVERRIDE METHODS

    @Override
    public String toString() {
        return "MixModuleData{" +
                "methods=" + methods +
                '}';
    }

}
