package com.pipedog.mixkit.plugin.config

/**
 * 内置配置项（支持 mixkit 功能）
 * @author liang
 * @time 2021/12/07
 */
class BuiltinConfigItems {

    static ArrayList<ConfigItem> getConfigItems() {
        ArrayList<ConfigItem> items = new ArrayList<>()
        items.add(getModuleProviderConfigItem())
        items.add(getParserProviderConfigItem())
        items.add(getMessengerServerListenerConfigItem())
        items.add(getMessageVerifierConfigItem())
        return items
    }


    // FOR EXPORT MODULES

    private static ConfigItem getModuleProviderConfigItem() {
        return new ConfigItem(
                "com.pipedog.mixkit.compiler.provider.IMixModuleProvider",
                "com.pipedog.mixkit.module.MixModuleManager",
                "autoCallRegisterModuleProvider",
                "registerModuleProvider",
                new ArrayList<String>() {{
                    add("com.pipedog.mixkit.compiler.dynamic.module")
                }}
        )
    }

    private static ConfigItem getParserProviderConfigItem() {
        return new ConfigItem(
                "com.pipedog.mixkit.compiler.provider.IMixMessageParserProvider",
                "com.pipedog.mixkit.parser.MixMessageParserManager",
                "autoCallRegisterParserProvider",
                "registerParserProvider",
                new ArrayList<String>() {{
                    add("com.pipedog.mixkit.compiler.dynamic.parser")
                }}
        )
    }


    // FOR LIB `mixkit-messenger`

    private static ConfigItem getMessengerServerListenerConfigItem() {
        return new ConfigItem(
                "com.pipedog.mixkit.messenger.interfaces.IServerListener",
                "com.pipedog.mixkit.messenger.manager.ServerListenerManager",
                "autoRegisterServerListener",
                "registerServerListener",
                null
        )
    }

    private static ConfigItem getMessageVerifierConfigItem() {
        return new ConfigItem(
                "com.pipedog.mixkit.messenger.interfaces.IMessageVerifier",
                "com.pipedog.mixkit.messenger.manager.MessageVerifierManager",
                "autoRegisterVerifier",
                "registerVerifier",
                null
        )
    }

}