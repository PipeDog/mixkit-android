package com.pipedog.mixkit.kernel;

public interface IMixExecutor {
    void setBridge(IMixBridge bridge);
    boolean invokeMethod(Object metaData);
}
