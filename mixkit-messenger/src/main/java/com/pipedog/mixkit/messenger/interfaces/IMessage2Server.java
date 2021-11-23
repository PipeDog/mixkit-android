package com.pipedog.mixkit.messenger.interfaces;

import android.os.Parcelable;

import java.util.List;
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
     * @param arguments 参数列表，包含 callbackId
     */
    void request2Server(String clientId,
                        String moduleName,
                        String methodName,
                        List<Object> arguments);

    /**
     * 向服务端（server）发送响应消息
     *  第 3 步：client2 -> server
     *  
     * @param clientId 目标客户端 ID 标识
     * @param callbackId 请求响应回调 ID
     * @param response 响应数据列表
     */
    void response2Server(String clientId,
                         String callbackId,
                         List<Object> response);

}
