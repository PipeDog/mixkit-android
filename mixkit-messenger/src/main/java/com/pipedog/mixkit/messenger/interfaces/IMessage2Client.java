package com.pipedog.mixkit.messenger.interfaces;

import android.os.Parcelable;

import java.util.Map;

/**
 * 服务端（server）调用客户端（client）
 * @author liang
 * @time 2021/11/22
 */
public interface IMessage2Client {

    /**
     * 服务端（server）向客户端（client）发送请求执行消息
     * @param moduleName 模块名称
     * @param methodName 函数名
     * @param parameter 参数实体
     * @param callback 请求结果回调
     */
    void request2Client(String moduleName, 
                        String methodName,
                        Map<String, Parcelable> parameter,
                        IMessageCallback callback);

    /**
     * 服务端（server）向客户端（client）发送响应消息
     * @param callbackId 回调 ID
     * @param result 响应结果实体
     * @param callback 请求响应回调
     */
    void response2Client(String callbackId,
                         Map<String, Parcelable> result,
                         IMessageCallback callback);

}
