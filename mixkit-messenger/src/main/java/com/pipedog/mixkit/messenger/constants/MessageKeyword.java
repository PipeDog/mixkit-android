package com.pipedog.mixkit.messenger.constants;

public class MessageKeyword {

    /**
     * 源客户端（发起通信端）ID
     */
    public static final String KEY_SOURCE_CLIENT_ID = "sourceClientId";

    /**
     * 目标客户端（执行端）ID
     */
    public static final String KEY_TARGET_CLIENT_ID = "targetClientId";

    /**
     * 导出模块信息
     */
    public static final String KEY_MODULE_DATA = "moduleData";
    
    /**
     * 模块名称
     */
    public static final String KEY_MODULE_NAME = "moduleName";
    
    /**
     * 函数名称
     */
    public static final String KEY_METHOD_NAME = "methodName";
    
    /**
     * 参数名称
     */
    public static final String KEY_ARGUMENTS_NAME = "arguments";

    /**
     * 回调 ID（仅在响应过程，即 client2 -> server -> client1 过程中会用到）
     */
    public static final String KEY_CALLBACK_ID = "callbackID";

    /**
     * 响应数据
     */
    public static final String KEY_RESPONSE_DATA = "responseData";
    
}
