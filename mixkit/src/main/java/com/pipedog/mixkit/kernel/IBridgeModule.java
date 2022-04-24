package com.pipedog.mixkit.kernel;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义 module 接口抽象
 * @author liang
 */
public interface IBridgeModule {

    /**
     * 绑定所属 bridge 实例
     */
    void setBridge(IBridge bridge);

    /**
     * 自定义 module 被初始构造时会被调用
     */
    default void load() {};

    /**
     * 自定义 module 被析构之前会被调用
     */
    default void unload() {};

    /**
     * 导出的常量表，允许 js 侧通过 NativeModules.ModuleName.ConstantName 对常量进行访问，
     * 注意：
     *      Value 只支持原始数据类型，包括：int、float、double、String、Map、List 等，不支持自定义类型；
     *      另外，如果要覆盖默认实现，记得在函数前边添加 public 关键字，否则这一函数的调用将会失效；
     */
    public static Map<String, Object> constantsToExport() {
        return new HashMap<>();
    };

}
