package com.pipedog.mixkit.kernel;

/**
 * 消息执行器抽象接口
 * @author liang
 */
public interface IMixExecutor {
    void setBridge(IMixBridge bridge);
    boolean invokeMethod(Object metaData);
}
