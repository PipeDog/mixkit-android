package com.pipedog.mixkit.kernel;

public interface IMixBridgeModule {
    void setBridge(IMixBridge bridge);
    void load();
    void unload();
}
