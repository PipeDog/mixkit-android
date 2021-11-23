package com.pipedog.mixkit.messenger.interfaces;

import android.os.Parcelable;

import java.util.List;
import java.util.Map;

/**
 * 服务端（server）调用客户端（client）
 * @author liang
 * @time 2021/11/22
 */
public interface IMessage2Client {

    /**
     * 服务端（server）向客户端（client）发送请求执行消息
     *  第 2 步：server -> client2
     *
     * @param sourceClientId 源（通信发起）客户端 ID
     * @param targetClientId 目标客户端 ID
     * @param moduleName 模块名称
     * @param methodName 函数名
     * @param arguments 参数列表
     */
    void request2Client(String sourceClientId,
                        String targetClientId,
                        String moduleName,
                        String methodName,
                        List<Object> arguments);

    /**
     * 服务端（server）向客户端（client）发送响应消息
     *  第 4 步：server -> client1
     *
     * @param sourceClientId 源（通信发起）客户端 ID
     * @param targetClientId 目标（执行）客户端 ID
     * @param callbackId 请求响应回调 ID
     * @param response 响应结果列表
     */
    void response2Client(String sourceClientId,
                         String targetClientId,
                         String callbackId,
                         List<Object> response);

}
