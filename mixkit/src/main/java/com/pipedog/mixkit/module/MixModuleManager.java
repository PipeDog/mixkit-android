package com.pipedog.mixkit.module;

import android.content.Context;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Class;
import java.lang.reflect.*;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.path.Path;
import com.pipedog.mixkit.launch.Mix;
import com.pipedog.mixkit.launch.MixOptions;
import com.pipedog.mixkit.tool.MixLogger;
import com.pipedog.mixkit.tool.ClassUtils;
import com.pipedog.mixkit.tool.MixProviderClassLoader;

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

        String packageName = Path.MIX_MODULE_PROVIDER_PACKAGE;
        List<Class<?>> providerClasses =
                MixProviderClassLoader.getClassesWithPackageName(packageName);

        // Load all class names
        for (Class<?> aClass : providerClasses) {
            try {
                Object provider = aClass.getConstructor().newInstance();
                Method method = aClass.getMethod(Path.MIX_MODULE_PROVIDER_METHOD);
                String json = (String)method.invoke(provider);

                Type mapType = new TypeToken<Map<String, MixModuleData>>(){}.getType();
                Map<String, MixModuleData> map = mGson.fromJson(json, mapType);
                mModuleDataMap.putAll(map);
            } catch (Exception e) {
                MixLogger.error("Load parse failed, e : " + e.toString());
            }
        }

        mModuleDataJson = mGson.toJson(mModuleDataMap);
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