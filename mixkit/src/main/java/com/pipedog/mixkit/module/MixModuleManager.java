package com.pipedog.mixkit.module;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.lang.Class;
import java.lang.reflect.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.pipedog.mixkit.path.Path;
import com.pipedog.mixkit.tool.MixLogger;
import com.pipedog.mixkit.compiler.provider.IMixModuleProvider;

public class MixModuleManager {

    private Gson mGson;
    private String mModuleDataJson;
    private Map<String, MixModuleData> mModuleDataMap;
    private Map<String, MixMethodInvoker> mInvokerMap;

    private volatile static MixModuleManager sDefaultManager;

    public static MixModuleManager defaultManager() {
        if (sDefaultManager == null) {
            synchronized (MixModuleManager.class) {
                if (sDefaultManager == null) {
                    sDefaultManager = new MixModuleManager();
                }
            }
        }
        return sDefaultManager;
    }

    private MixModuleManager() {
        mGson = new Gson();
        mModuleDataMap = new HashMap<String, MixModuleData>();
        mInvokerMap = new HashMap<String, MixMethodInvoker>();

        autoCallRegisterModuleProvider();
        mModuleDataJson = mGson.toJson(mModuleDataMap);
    }

    private void autoCallRegisterModuleProvider() {
        // Call this function in constructor
        // The code will be inserted automatically during compilation
        // The insert code will call function `registerModuleProvider` here
    }

    private void registerModuleProvider(IMixModuleProvider provider) {
        try {
            String json = provider.getRegisteredModulesJson();
            Type mapType = new TypeToken<Map<String, MixModuleData>>(){}.getType();
            Map<String, MixModuleData> map = mGson.fromJson(json, mapType);
            mModuleDataMap.putAll(map);
        } catch (Exception e) {
            MixLogger.error("Load parse failed, e : " + e.toString());
        }
    }

    public String getModuleDataJson() {
        return mModuleDataJson;
    }

    public Map<String, MixModuleData> getModuleDataMap() {
        return mModuleDataMap;
    }

    public MixModuleMethod getMethod(String moduleName, String methodName) {
        MixModuleData moduleData = mModuleDataMap.get(moduleName);
        if (moduleData == null) { return null; }

        MixModuleMethod method = moduleData.methods.get(methodName);
        return method;
    }

    public MixMethodInvoker getInvoker(String moduleName, String methodName) {
        String invokerId = formatInvokerId(moduleName, methodName);
        MixMethodInvoker invoker = mInvokerMap.get(invokerId);
        if (invoker != null) { return invoker; }

        MixModuleMethod method = getMethod(moduleName, methodName);
        if (method == null) { return null; }

        invoker = new MixMethodInvoker(method);
        mInvokerMap.put(invokerId, invoker);
        return invoker;
    }

    private String formatInvokerId(String moduleName, String methodName) {
        return moduleName + "_$_" + methodName;
    }

}