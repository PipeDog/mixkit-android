package com.pipedog.mixkit.register

/**
 * `MixMessageParserProvider` register info, configure the provider information here.
 * @auth liang
 * @since 2021/11/01 18:56
 */
class MixMessageParserProviderRegisterInfo implements IRegisterInfo {

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
        return "com.pipedog.mixkit.parser.MixMessageParserManager"
    }

    @Override
    String codeInsertToMethodName() {
        return "autoCallRegisterParserProvider"
    }

    @Override
    String registerMethodName() {
        return "registerParserProvider"
    }

}