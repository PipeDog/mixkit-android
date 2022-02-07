package com.pipedog.mixkit.kernel;

/**
 * 消息执行器抽象接口
 * @author liang
 */
public interface IExecutor {
    void setBridge(IBridge bridge);
    boolean invokeMethod(Object metaData);
}
