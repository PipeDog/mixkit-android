package com.pipedog.mixkit.web;

import com.google.gson.Gson;
import com.pipedog.mixkit.module.MixModuleData;
import com.pipedog.mixkit.module.MixModuleManager;
import com.pipedog.mixkit.module.MixModuleMethod;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class MixWebInjector {

    private static String sModuleDataJson;

    public static String getInjectionJson() {
        if (sModuleDataJson != null) {
            return sModuleDataJson;
        }

        Map<String, Map> injectionMap = new HashMap<>();
        Map<String, MixModuleData> map = MixModuleManager.defaultManager().getModuleDataMap();

        for (Map.Entry<String, MixModuleData> entry : map.entrySet()) {
            String moduleName = entry.getKey();
            MixModuleData moduleData = entry.getValue();

            Map moduleMap = new HashMap<>();
            injectionMap.put(moduleName, moduleMap);

            Collection<String> methods = moduleData.methods.keySet();
            List<String> methodNames = new ArrayList<String>();
            for (String method : methods) {
                methodNames.add(method);
            }

            moduleMap.put("methods", methodNames);
        }

        Gson gson = new Gson();
        sModuleDataJson = gson.toJson(injectionMap);

        MixLogger.info(">>>>> sModuleDataJson : %s", sModuleDataJson);
        return sModuleDataJson;
    }

}
