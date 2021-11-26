package com.pipedog.mixkit.kernel;

public interface IMixBridgeModule {

    void setBridge(IMixBridge bridge);

    default void load() {};
    default void unload() {};

}
