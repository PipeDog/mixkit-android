package com.pipedog.mixkit.register

interface IRegisterInfo {
    String scanInterfaceName()
    List<String> scanSuperClasses()
    String codeInsertToClassName()
    String codeInsertToMethodName()
    String registerMethodName()
}