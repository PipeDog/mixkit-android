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
     * @param processId 目标进程标识
     * @param moduleName 模块名
     * @param methodName 方法名
     * @param parameter 请求参数体
     * @param callback 请求响应回调
     */
    void request2Server(String processId,
                        String moduleName,
                        String methodName,
                        Map<String, Parcelable> parameter,
                        IMessageCallback callback);

    /**
     * 向服务端（server）发送响应消息
     * @param callbackId 回调 ID
     * @param result 响应数据
     * @param callback 请求响应回调
     */
    void response2Server(String callbackId,
                         Map<String, Parcelable> result,
                         IMessageCallback callback);

}
