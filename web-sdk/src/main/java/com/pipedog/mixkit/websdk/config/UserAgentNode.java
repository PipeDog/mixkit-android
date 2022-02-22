package com.pipedog.mixkit.websdk.config;

/**
 * User-Agent 包装
 * 这里采用了 name/value 的方式来进行 UA 的包装，与实际 UA 的规范不符，仅是为了方便编码
 * User-Agent 说明地址如下：
 * https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/User-Agent
 */
public class UserAgentNode {

    private String name;
    private String value;


    // CONSTRUCTORS

    /**
     * User-Agent 信息构建
     * @param name User-Agent 的名称
     * @param value User-Agent 的值
     */
    public UserAgentNode(String name, String value) {
        this.name = name;
        this.value = value;
    }


    // GETTERS

    /**
     * User-Agent 的名称
     */
    public String getName() {
        return name;
    }

    /**
     * User-Agent 的值
     */
    public String getValue() {
        return value;
    }

}
