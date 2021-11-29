package com.pipedog.mixkit.autoregister.builtin

import com.pipedog.mixkit.autoregister.RegisterInfo

class MixRegisterInfo {

    private static final String KEY_SCAN_INTERFACE_NAME = "scanInterface"
    private static final String KEY_CODE_INSERT_TO_CLASS_NAME = "codeInsertToClassName"
    private static final String KEY_CODE_INSERT_TO_METHOD_NAME = "codeInsertToMethodName"
    private static final String KEY_REAL_REGISTER_METHOD_NAME = "registerMethodName"

    public static List<Map<String, Object>> getBuiltinRegisterInfos() {
        List<Map<String, Object>> infos = new ArrayList<>()
        infos.add(getModuleProviderRegisterInfo())
        infos.add(getParserProviderRegisterInfo())
        infos.add(getMessengerServerListenerRegisterInfo())
        infos.add(getMessageVerifierRegisterInfo())
        return infos
    }


    // FOR EXPORT MODULES

    private static Map<String, Object> getModuleProviderRegisterInfo() {
        Map<String, Object> map = new HashMap<>()
        map.put(KEY_SCAN_INTERFACE_NAME, "com.pipedog.mixkit.compiler.provider.IMixModuleProvider")
        map.put(KEY_CODE_INSERT_TO_CLASS_NAME, "com.pipedog.mixkit.module.MixModuleManager")
        map.put(KEY_CODE_INSERT_TO_METHOD_NAME, "autoCallRegisterModuleProvider")
        map.put(KEY_REAL_REGISTER_METHOD_NAME, "registerModuleProvider")
        return map
    }

    private static Map<String, Object> getParserProviderRegisterInfo() {
        Map<String, Object> map = new HashMap<>()
        map.put(KEY_SCAN_INTERFACE_NAME, "com.pipedog.mixkit.compiler.provider.IMixMessageParserProvider")
        map.put(KEY_CODE_INSERT_TO_CLASS_NAME, "com.pipedog.mixkit.parser.MixMessageParserManager")
        map.put(KEY_CODE_INSERT_TO_METHOD_NAME, "autoCallRegisterParserProvider")
        map.put(KEY_REAL_REGISTER_METHOD_NAME, "registerParserProvider")
        return map
    }


    // FOR LIB `mixkit-messenger`

    private static Map<String, Object> getMessengerServerListenerRegisterInfo() {
        Map<String, Object> map = new HashMap<>()
        map.put(KEY_SCAN_INTERFACE_NAME, "com.pipedog.mixkit.messenger.interfaces.IServerListener")
        map.put(KEY_CODE_INSERT_TO_CLASS_NAME, "com.pipedog.mixkit.messenger.manager.ServerListenerManager")
        map.put(KEY_CODE_INSERT_TO_METHOD_NAME, "autoRegisterServerListener")
        map.put(KEY_REAL_REGISTER_METHOD_NAME, "registerServerListener")
        return map
    }

    private static Map<String, Object> getMessageVerifierRegisterInfo() {
        Map<String, Object> map = new HashMap<>()
        map.put(KEY_SCAN_INTERFACE_NAME, "com.pipedog.mixkit.messenger.interfaces.IMessageVerifier")
        map.put(KEY_CODE_INSERT_TO_CLASS_NAME, "com.pipedog.mixkit.messenger.manager.MessageVerifierManager")
        map.put(KEY_CODE_INSERT_TO_METHOD_NAME, "autoRegisterVerifier")
        map.put(KEY_REAL_REGISTER_METHOD_NAME, "registerVerifier")
        return map
    }

}