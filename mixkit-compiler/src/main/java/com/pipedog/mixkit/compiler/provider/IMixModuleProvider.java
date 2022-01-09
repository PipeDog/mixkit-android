package com.pipedog.mixkit.compiler.provider;

/**
 * 自定义 module 信息供应器接口，编译期生成的保存了自定义 module 的相关数据的 java 类会继承自这个接口
 * @author liang
 */
public interface IMixModuleProvider {

    /**
     * 获取已经（通过注解）注册的自定义 module 信息，该数据以 json 字符串的方式提供
     */
    String getRegisteredModulesJson();

}
