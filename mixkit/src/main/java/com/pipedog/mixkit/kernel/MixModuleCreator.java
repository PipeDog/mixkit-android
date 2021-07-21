package com.pipedog.mixkit.kernel;

import java.lang.Class;
import java.lang.reflect.*;
import java.util.Map;
import java.util.HashMap;

import com.pipedog.mixkit.tool.MixLogger;

public class MixModuleCreator {

    private IMixBridge mBridge;
    private Map<String, Object> mModuleMap;

    public MixModuleCreator(IMixBridge bridge) {
        mBridge = bridge;
        mModuleMap = new HashMap<String, Object>();
    }

    public Object getModule(String className) {
        if (className == null || className.isEmpty()) {
            return null;
        }

        Object module = mModuleMap.get(className);
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

        MixLogger.info(">>>>>>>======== cls named : %s", cls.getName());

        try {
            Constructor constructor = cls.getConstructor();
            MixLogger.info(">>>>>>>======== get constructor : %s", (constructor == null ? "failed" : "success"));

            module = constructor.newInstance();
            MixLogger.info(">>>>>>>======== create constructor : %s", (module == null ?
                    "failed" : "success"));

        } catch (Exception e) {
            MixLogger.error("Create module failed, class named : `%s`, exception : %s!",
                    className, e.toString());
            return null;
        }

        if (module instanceof IMixBridgeModule) {
            IMixBridgeModule iModule = (IMixBridgeModule)module;
            iModule.bindBridge(mBridge);
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
