package com.pipedog.mixkit.messenger.constants;

/**
 * 错误码定义
 * @author liang
 * @time 2021/11/24
 */
public class ErrorCode {

    /**
     * 创建客户端到服务端的连接失败
     */
    public static final int ERR_CONNECTION_FAILED = 10000;

    /**
     * 注册客户端失败
     */
    public static final int ERR_REGISTER_CLIENT_FAILED = 20000;

    /**
     * 与服务端进程失去连接
     */
    public static final int ERR_DISCONNECT_SERVER = 30000;

    /**
     * 与目标（通信执行）客户端进程失去连接
     */
    public static final int ERR_DISCONNECT_TARGET_CLIENT = 40000;

    /**
     * 与源（通信发起）客户端进程失去连接
     */
    public static final int ERR_DISCONNECT_SOURCE_CLIENT = 40001;

    /**
     * 目标客户端执行通信（目标函数）失败
     */
    public static final int ERR_INVOKE_METHOD_FAILED = 50000;

}
