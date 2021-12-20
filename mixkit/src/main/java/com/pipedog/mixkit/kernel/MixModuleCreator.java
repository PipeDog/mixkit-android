package com.pipedog.mixkit.kernel;

import java.lang.Class;
import java.lang.reflect.*;
import java.util.Map;
import java.util.HashMap;

import com.pipedog.mixkit.tool.MixLogger;

/**
 * module 构造器，所有的自定义 module 实例最终都由此类进行创建
 * @author liang
 */
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

        try {
            Constructor constructor = cls.getConstructor();
            module = constructor.newInstance();
        } catch (Exception e) {
            MixLogger.error("Create module failed, class named : `%s`, exception : %s!",
                    className, e.toString());
            return null;
        }

        if (module instanceof IMixBridgeModule) {
            IMixBridgeModule iModule = (IMixBridgeModule)module;
            iModule.setBridge(mBridge);
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
