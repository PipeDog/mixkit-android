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

/**
 * 自定义 module 信息注入器（js 侧会根据这里提供的数据去构建响应的实例及函数，以供 js 的业务层直接调用）
 * @author liang
 */
public class WebInjector {

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
        return sModuleDataJson;
    }

}
