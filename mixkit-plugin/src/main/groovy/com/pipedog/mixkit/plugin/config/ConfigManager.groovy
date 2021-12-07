package com.pipedog.mixkit.plugin.config

class ConfigManager {

    private ArrayList<ConfigItem> configItems;

    ConfigManager() {
        configItems = BuiltinConfigItems.getConfigItems()
    }

    ArrayList<ConfigItem> getConfigItems() {
        return configItems
    }

    @Override
    String toString() {
        return configItems.toString()
    }

}