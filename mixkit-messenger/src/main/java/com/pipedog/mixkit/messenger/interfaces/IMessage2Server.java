package com.pipedog.mixkit.messenger.interfaces;

import android.os.Parcelable;

import java.util.Map;

/**
 * 客户端（client）调用服务端（server）
 * @author liang
 * @time 2021/11/22
 */
public interface IMessage2Server {

    /**
     * 向服务端（server）发送请求消息
     *  第 1 步：client1 -> server
     *
     * @param clientId 目标客户端 ID 标识
     * @param moduleName 模块名
     * @param methodName 方法名
     * @param parameter 请求参数体
     * @param callbackId 请求响应回调 ID
     */
    void request2Server(String clientId,
                        String moduleName,
                        String methodName,
                        Map<String, Object> parameter,
                        String callbackId);

    /**
     * 向服务端（server）发送响应消息
     *  第 3 步：client2 -> server
     *  
     * @param clientId 目标客户端 ID 标识
     * @param callbackId 请求响应回调 ID
     * @param result 响应数据
     */
    void response2Server(String clientId,
                         String callbackId,
                         Map<String, Object> result);

}
