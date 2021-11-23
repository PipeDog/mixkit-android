package com.pipedog.mixkit.messenger.client;

import java.util.List;

/**
 * 客户端代理接口
 * @author liang
 * @time 2021/11/22
 */
public interface IMessageClientDelegate {

    /**
     * 接收到请求执行消息
     * @param sourceClientId 源（通信发起）客户端 ID
     * @param targetClientId 目标客户端 ID
     * @param moduleName 模块名
     * @param methodName 方法名
     * @param arguments 参数列表（包含回调 ID）
     */
    void didReceiveRequestMessage(String sourceClientId,
                                  String targetClientId,
                                  String moduleName,
                                  String methodName,
                                  List<Object> arguments);

    /**
     * 接收到响应消息
     * @param callbackId 回调 ID
     * @param response 回调结果数据列表
     */
    void didReceiveResponseMessage(String callbackId,
                                   List<Object> response);

}
