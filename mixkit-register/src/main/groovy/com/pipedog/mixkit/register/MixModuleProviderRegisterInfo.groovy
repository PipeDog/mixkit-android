package com.pipedog.mixkit.register

/**
 * `MixModuleProvider` register info, configure the provider information here.
 * @auth liang
 * @since 2021/11/01 18:56
 */
class MixModuleProviderRegisterInfo implements IRegisterInfo {

    @Override
    String scanInterfaceName() {
        return "com.pipedog.mixkit.compiler.provider.IMixMessageParserProvider"
    }

    @Override
    List<String> scanSuperClasses() {
        // return nothing here...
        return null
    }

    @Override
    String codeInsertToClassName() {
        return "com.pipedog.mixkit.module.MixModuleManager"
    }

    @Override
    String codeInsertToMethodName() {
        return "autoCallRegisterModuleProvider"
    }

    @Override
    String registerMethodName() {
        return "registerModuleProvider"
    }

}