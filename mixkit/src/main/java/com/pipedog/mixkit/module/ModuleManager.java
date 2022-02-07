package com.pipedog.mixkit.module;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.pipedog.mixkit.tool.MixLogger;
import com.pipedog.mixkit.compiler.provider.IModuleProvider;

/**
 * 自定义 module 管理器（通过注解导出的信息最终会在这里被收集并整理）
 * @author liang
 */
public class ModuleManager {

    private Gson mGson;
    private String mModuleDataJson;
    private Map<String, ModuleData> mModuleDataMap;
    private Map<String, MethodInvoker> mInvokerMap;

    private volatile static ModuleManager sDefaultManager;

    public static ModuleManager defaultManager() {
        if (sDefaultManager == null) {
            synchronized (ModuleManager.class) {
                if (sDefaultManager == null) {
                    sDefaultManager = new ModuleManager();
                }
            }
        }
        return sDefaultManager;
    }

    private ModuleManager() {
        mGson = new Gson();
        mModuleDataMap = new HashMap<String, ModuleData>();
        mInvokerMap = new HashMap<String, MethodInvoker>();

        autoCallRegisterModuleProvider();
        mModuleDataJson = mGson.toJson(mModuleDataMap);
    }

    private void autoCallRegisterModuleProvider() {
        // Call this function in constructor
        // The code will be inserted automatically during compilation
        // The insert code will call function `registerModuleProvider` here
    }

    private void registerModuleProvider(IModuleProvider provider) {
        try {
            String json = provider.getRegisteredModulesJson();
            Type mapType = new TypeToken<Map<String, ModuleData>>(){}.getType();
            Map<String, ModuleData> map = mGson.fromJson(json, mapType);
            mModuleDataMap.putAll(map);
        } catch (Exception e) {
            MixLogger.error("Load parse failed, e : " + e.toString());
        }
    }

    public String getModuleDataJson() {
        return mModuleDataJson;
    }

    public Map<String, ModuleData> getModuleDataMap() {
        return mModuleDataMap;
    }

    public ModuleMethod getMethod(String moduleName, String methodName) {
        ModuleData moduleData = mModuleDataMap.get(moduleName);
        if (moduleData == null) { return null; }

        ModuleMethod method = moduleData.methods.get(methodName);
        return method;
    }

    public MethodInvoker getInvoker(String moduleName, String methodName) {
        String invokerId = formatInvokerId(moduleName, methodName);
        MethodInvoker invoker = mInvokerMap.get(invokerId);
        if (invoker != null) { return invoker; }

        ModuleMethod method = getMethod(moduleName, methodName);
        if (method == null) { return null; }

        invoker = new MethodInvoker(method);
        mInvokerMap.put(invokerId, invoker);
        return invoker;
    }

    private String formatInvokerId(String moduleName, String methodName) {
        return moduleName + "_$_" + methodName;
    }

}