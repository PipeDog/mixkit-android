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
     *  第 2 步：server -> client2
     *  
     * @param processId 目标进程 ID
     * @param moduleName 模块名称
     * @param methodName 函数名
     * @param parameter 参数实体
     * @param callbackId 请求结果回调 ID
     */
    void request2Client(String processId,
                        String moduleName,
                        String methodName,
                        Map<String, Object> parameter,
                        String callbackId);

    /**
     * 服务端（server）向客户端（client）发送响应消息
     *  第 4 步：server -> client1
     *
     * @param processId 目标进程 ID
     * @param callbackId 请求响应回调 ID
     * @param result 响应结果实体
     */
    void response2Client(String processId,
                         String callbackId,
                         Map<String, Object> result);

}
