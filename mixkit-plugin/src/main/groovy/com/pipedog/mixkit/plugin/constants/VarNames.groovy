package com.pipedog.mixkit.plugin.constants

/**
 * 配置项变量名称
 * @author liang
 * @time 2021/12/13
 */
class VarNames {

    /**
     * 扫描实现此接口的类
     */
    static final String VAR_INTERFACE_NAME = "interfaceName"

    /**
     * 注册代码将会被生成到这个类中
     */
    static final String VAR_GENERATE_TO_CLASS_NAME = "generateToClassName"

    /**
     * 注册代码将会被生成到这个方法中
     */
    static final String VAR_GENERATE_TO_METHOD_NAME = "generateToMethodName"

    /**
     * 在 {@link #VAR_GENERATE_TO_CLASS_NAME} 类中的注册方法名
     */
    static final String VAR_REGISTER_METHOD_NAME = "registerMethodName"

    /**
     * 指定要扫描的包（数组类型）
     */
    static final String VAR_SCAN_PACKAGE_NAMES = "scanPackageNames"

}