package com.pipedog.mixkit.plugin.config

import com.pipedog.mixkit.plugin.constants.VarNames
import com.pipedog.mixkit.plugin.utils.Logger

class ConfigManager {

    public ArrayList<Map<String, Object>> registerItems = []
    private ArrayList<ConfigItem> configItems = []

    ConfigManager() {
        // Do nothing...
    }

    void mergeConfig() {
        // 1. 添加内置默认配置
        configItems.addAll(BuiltinConfigItems.getConfigItems())

        // 2. 添加外部配置
        if (registerItems == null || registerItems.isEmpty()) {
            return
        }

        registerItems.each { Map<String, Object> it ->
            ConfigItem configItem = new ConfigItem(
                    it.get(VarNames.VAR_INTERFACE_NAME),
                    it.get(VarNames.VAR_GENERATE_TO_CLASS_NAME),
                    it.get(VarNames.VAR_GENERATE_TO_METHOD_NAME),
                    it.get(VarNames.VAR_REGISTER_METHOD_NAME),
                    it.get(VarNames.VAR_SCAN_PACKAGE_NAMES)
            )

            configItems.add(configItem)
        }
    }

    ArrayList<ConfigItem> getConfigItems() {
        return configItems
    }

    @Override
    String toString() {
        return configItems.toString()
    }

}