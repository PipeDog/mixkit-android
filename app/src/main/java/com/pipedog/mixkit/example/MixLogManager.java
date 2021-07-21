package com.pipedog.mixkit.example;

import com.pipedog.mixkit.kernel.IMixBridge;
import com.pipedog.mixkit.kernel.IMixBridgeModule;
import com.pipedog.mixkit.tool.MixLogger;
import com.pipedog.mixkit.annotation.*;

@MixModule(name = "LogManager")
public class MixLogManager implements IMixBridgeModule {

    @MixMethod(name = "log")
    public void log(String message) {
        MixLogger.info("Log from H5, msg = %s", message);
    }

    @Override
    public void bindBridge(IMixBridge bridge) {

    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }
}
