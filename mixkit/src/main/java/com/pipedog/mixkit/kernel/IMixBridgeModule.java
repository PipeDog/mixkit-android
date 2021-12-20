package com.pipedog.mixkit.kernel;

/**
 * 自定义 module 接口抽象
 * @author liang
 */
public interface IMixBridgeModule {

    /**
     * 绑定所属 bridge 实例
     */
    void setBridge(IMixBridge bridge);

    /**
     * 自定义 module 被初始构造时会被调用
     */
    default void load() {};

    /**
     * 自定义 module 被析构之前会被调用
     */
    default void unload() {};

}
