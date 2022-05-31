package com.pipedog.mixkit.plugin.config

import com.pipedog.mixkit.plugin.constants.VarNames
import com.pipedog.mixkit.plugin.utils.Logger

/**
 * 配置管理器（主要负责配置信息的收集与合并）
 * @author liang
 */
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
                    it.get(VarNames.VAR_SUPERCLASSES_NAMES),
                    it.get(VarNames.VAR_GENERATE_TO_CLASS_NAME),
                    it.get(VarNames.VAR_GENERATE_TO_METHOD_NAME),
                    it.get(VarNames.VAR_REGISTER_METHOD_NAME),
                    it.get(VarNames.VAR_SCAN_PACKAGE_NAMES),
                    it.get(VarNames.VAR_CONTAINS_SUPERCLASSES)
            )

            configItems.add(configItem)
        }

        Logger.e(configItems.toString())
    }

    ArrayList<ConfigItem> getConfigItems() {
        return configItems
    }

    void reset() {
        configItems.each {
            it.reset()
        }
    }

    @Override
    String toString() {
        return configItems.toString()
    }

}