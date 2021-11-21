package com.pipedog.mixkit.messenger.constants;

/**
 * 消息指令定义
 * @author liang
 * @time 2021/11/21
 */
public class MessageNumber {

    // 初始化指令定义

    /**
     * 注册客户端 Messenger
     */
    public static final int REGISTER_CLIENT = 1000;

    /**
     * 导出 modules 表信息
     */
    public static final int EXPORT_MODULES = 1001;


    // 功能执行调度指令
    // 服务端只负责将该指令的内容分发到具体的客户端 Messenger 中，不处理任何业务逻辑

    /**
     * 请求端（client1）向服务端（server）发送调用指定功能函数指令
     *  请求过程：client1 -> server -> client2
     *  响应过程：client2 -> server -> client1
     * 此条指令指请求过程中的 client1 -> server
     */
    public static final int REQUEST_TO_SERVER = 2000;

    /**
     * 服务端（server）下发调用指定功能指令到执行端（client2）
     *  请求过程：client1 -> server -> client2
     *  响应过程：client2 -> server -> client1
     * 此条指令指请求过程中的 server -> client2
     */
    public static final int REQUEST_TO_CLIENT = 2001;

    /**
     * 执行端（client2）处理功能或业务逻辑后回调给服务端（server）的响应指令
     *  请求过程：client1 -> server -> client2
     *  响应过程：client2 -> server -> client1
     * 此条指令指响应过程中的 client2 -> server
     */
    public static final int RESPONSE_TO_SERVER = 2002;

    /**
     * 服务端（server）收到响应后回调给请求端（client1）的响应指令
     *  请求过程：client1 -> server -> client2
     *  响应过程：client2 -> server -> client1
     * 此条指令指响应过程中的 server -> client1
     */
    public static final int RESPONSE_TO_CLIENT = 2003;
    
    
    // 析构指令
    
    /**
     * 注销客户端 Messenger 
     */
    public static final int UNREGISTER_CLIENT = 3000;

}
