package com.pipedog.mixkit.kernel;

public interface IMixBridgeModule {
    void bindBridge(IMixBridge bridge);
    void load();
    void unload();
}
