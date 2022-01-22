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
                null,
                "com.pipedog.mixkit.module.MixModuleManager",
                "autoCallRegisterModuleProvider",
                "registerModuleProvider",
                new ArrayList<String>() {{
                    add("com.pipedog.mixkit.compiler.dynamic.module")
                }},
                false
        )
    }

    private static ConfigItem getParserProviderConfigItem() {
        return new ConfigItem(
                "com.pipedog.mixkit.compiler.provider.IMixMessageParserProvider",
                null,
                "com.pipedog.mixkit.parser.MixMessageParserManager",
                "autoCallRegisterParserProvider",
                "registerParserProvider",
                new ArrayList<String>() {{
                    add("com.pipedog.mixkit.compiler.dynamic.parser")
                }},
                false
        )
    }


    // FOR LIB `mixkit-messenger`

    private static ConfigItem getMessengerServerListenerConfigItem() {
        return new ConfigItem(
                "com.pipedog.mixkit.messenger.interfaces.IServerListener",
                null,
                "com.pipedog.mixkit.messenger.manager.ServerListenerManager",
                "autoRegisterServerListener",
                "registerServerListener",
                null,
                false
        )
    }

    private static ConfigItem getMessageVerifierConfigItem() {
        return new ConfigItem(
                "com.pipedog.mixkit.messenger.interfaces.IMessageVerifier",
                null,
                "com.pipedog.mixkit.messenger.manager.MessageVerifierManager",
                "autoRegisterVerifier",
                "registerVerifier",
                null,
                false
        )
    }

}