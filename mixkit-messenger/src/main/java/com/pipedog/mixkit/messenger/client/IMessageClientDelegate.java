package com.pipedog.mixkit.messenger.client;

import java.util.Map;

/**
 * 客户端代理接口
 * @author liang
 * @time 2021/11/22
 */
public interface IMessageClientDelegate {

    /**
     * 接收到请求执行消息
     * @param moduleName 模块名
     * @param methodName 方法名
     * @param parameter 参数集合
     * @param callbackId 回调 ID
     */
    void didReceiveRequestMessage(String moduleName,
                                  String methodName,
                                  Map<String, Object> parameter,
                                  String callbackId);

    /**
     * 接收到响应消息
     * @param callbackId 回调 ID
     * @param result 回调结果数据实体
     */
    void didReceiveResponseMessage(String callbackId,
                                   Map<String, Object> result);

}
