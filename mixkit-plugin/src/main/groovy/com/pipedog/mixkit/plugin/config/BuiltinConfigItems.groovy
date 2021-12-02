package com.pipedog.mixkit.plugin.config

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
                "registerModuleProvider"
        )
    }

    private static ConfigItem getParserProviderConfigItem() {
        return new ConfigItem(
                "com.pipedog.mixkit.compiler.provider.IMixMessageParserProvider",
                "com.pipedog.mixkit.parser.MixMessageParserManager",
                "autoCallRegisterParserProvider",
                "registerParserProvider"
        )
    }


    // FOR LIB `mixkit-messenger`

    private static ConfigItem getMessengerServerListenerConfigItem() {
        return new ConfigItem(
                "com.pipedog.mixkit.messenger.interfaces.IServerListener",
                "com.pipedog.mixkit.messenger.manager.ServerListenerManager",
                "autoRegisterServerListener",
                "registerServerListener"
        )
    }

    private static ConfigItem getMessageVerifierConfigItem() {
        return new ConfigItem(
                "com.pipedog.mixkit.messenger.interfaces.IMessageVerifier",
                "com.pipedog.mixkit.messenger.manager.MessageVerifierManager",
                "autoRegisterVerifier",
                "registerVerifier"
        )
    }

}