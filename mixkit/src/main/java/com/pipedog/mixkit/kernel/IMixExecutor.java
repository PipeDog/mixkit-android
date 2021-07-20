package com.pipedog.mixkit.kernel;

public interface IMixExecutor {
    void bindBridge(IMixBridge bridge);
    boolean invokeMethod(Object metaData);
}
