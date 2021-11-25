package com.pipedog.mixkit.messenger.constants;

/**
 * 错误码定义
 * @author liang
 * @time 2021/11/24
 */
public class ErrorCode {

    /**
     * 与服务端进程失去连接
     */
    public static final int ERR_DISCONNECT_SERVER = 10000;

    /**
     * 与目标（通信执行）客户端进程失去连接
     */
    public static final int ERR_DISCONNECT_TARGET_CLIENT = 20000;

    /**
     * 与源（通信发起）客户端进程失去连接
     */
    public static final int ERR_DISCONNECT_SOURCE_CLIENT = 20001;

}
