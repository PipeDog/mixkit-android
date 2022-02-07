package com.pipedog.mixkit.compiler.bean;

import java.util.List;
import java.util.Map;

/**
 * 自定义 module 描述信息（允许 class 不同但 module 名相同）
 * @author liang
 */
public class ModuleBean {

    /**
     * 类名列表（所有被导出为这个 module 名称的 class 都会出现在这个列表内）
     */
    public List<String> classes;

    /**
     * 方法信息表，key 为导出函数名，value 为对应方法描述信息
     */
    public Map<String, MethodBean> methods;

}
