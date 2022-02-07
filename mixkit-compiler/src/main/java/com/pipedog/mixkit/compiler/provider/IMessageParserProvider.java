package com.pipedog.mixkit.compiler.provider;

/**
 * 消息解析器信息供应器接口，编译期生成的保存了消息解析器相关数据的 java 类会继承自这个接口
 * @author liang
 */
public interface IMessageParserProvider {

    /**
     * 获取已经（通过注解）注册的消息解析器信息，该数据以 json 字符串的方式提供
     */
    String getRegisteredMessageParsersJson();

}
