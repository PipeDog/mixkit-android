package com.pipedog.mixkit.kernel;

import java.lang.Class;
import java.lang.reflect.*;
import java.util.Map;
import java.util.HashMap;

import com.pipedog.mixkit.tool.MixLogger;

public class MixModuleCreator {

    private IMixBridge mBridge;
    private Map<String, IMixBridgeModule> mModuleMap;

    public MixModuleCreator(IMixBridge bridge) {
        mBridge = bridge;
        mModuleMap = new HashMap<String, IMixBridgeModule>();
    }

    public IMixBridgeModule getModule(String className) {
        if (className == null || className.isEmpty()) {
            return null;
        }

        IMixBridgeModule module = mModuleMap.get(className);
        if (module != null) {
            return module;
        }

        Class cls = null;
        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            MixLogger.error("Found class failed, class name is : `%s`.", className);
            return null;
        }

        try {
            module = (IMixBridgeModule)cls.getConstructor().newInstance();
        } catch (Exception e) {
            MixLogger.error("Create module failed, class named : `%s`.", className);
            return null;
        }

        if (module instanceof IMixBridgeModule) {
            IMixBridgeModule iModule = (IMixBridgeModule)module;
            iModule.load();
        }

        mModuleMap.put(className, module);
        return module;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        mModuleMap.forEach((key, value) -> {
            IMixBridgeModule iModule = (IMixBridgeModule)value;
            iModule.unload();
        });
    }
}
