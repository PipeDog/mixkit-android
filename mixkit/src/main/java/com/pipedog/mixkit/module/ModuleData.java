package com.pipedog.mixkit.module;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 自定义 module 信息描述（允许 class 不同但 module 名相同，也就是说一个 MixModuleData 实例中可能包含多个 class 的导出信息）
 * @author liang
 */
public class ModuleData implements Serializable {

    /**
     * 类名列表（所有被导出为这个 module 名称的 class 都会出现在这个列表内）
     */
    public List<String> classes;

    /**
     * 方法信息表，key 为导出函数名，value 为对应方法描述信息
     */
    public Map<String, ModuleMethod> methods;

    /**
     * 常量表（运行时动态生成），Key - 常量名，Value - 常量值
     * 注意：
     *      Value 只支持原始数据类型，包括：int、float、double、String、Map、List 等，不支持自定义类型
     */
    public Map<String, Object> constantsTable;


    // OVERRIDE METHODS

    @Override
    public String toString() {
        return "ModuleData{" +
                "classes=" + classes +
                ", methods=" + methods +
                ", constantsTable=" + constantsTable +
                '}';
    }

}
