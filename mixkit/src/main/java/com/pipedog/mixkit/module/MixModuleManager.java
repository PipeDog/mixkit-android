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
    private Map<String, MixModuleData> mModuleDataMap;
    private volatile static MixModuleManager defaultManager;

    public static MixModuleManager defaultManager() {
        if (defaultManager == null) {
            synchronized (MixModuleManager.class) {
                if (defaultManager == null) {
                    defaultManager = new MixModuleManager();
                }
            }
        }
        return defaultManager;
    }

    private MixModuleManager() {
        mGson = new Gson();
        mModuleDataMap = new HashMap<String, MixModuleData>();

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

        MixLogger.info("load module infos : %s, size : %d", mGson.toJson(mModuleDataMap), mModuleDataMap.size());
    }

    public MixModuleMethod getMethod(String moduleName, String methodName) {
        MixModuleData moduleData = mModuleDataMap.get(moduleName);
        if (moduleData == null) { return null; }

        MixModuleMethod method = moduleData.methods.get(methodName);
        return method;
    }

}