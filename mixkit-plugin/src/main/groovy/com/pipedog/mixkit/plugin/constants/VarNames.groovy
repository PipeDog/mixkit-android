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
     * 扫描继承自以下父类的 java 类
     */
    static final String VAR_SUPERCLASSES_NAMES = "superClasses"

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

    /**
     * 最终命中的类是否包含所指定的父类
     *      如配置 superClasses 为 [A, B, C] 三种父类，如果 enableSuperClasses 为 true，
     *      则最终的扫描结果会包含 A、B、C 三个类，反之则将 A、B、C 三个类排除在扫描结果之外
     */
    static final String VAR_CONTAINS_SUPERCLASSES = "containsSuperClasses"

}