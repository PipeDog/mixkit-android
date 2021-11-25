package com.pipedog.mixkit.messenger.interfaces;

import android.os.Parcelable;

import com.pipedog.mixkit.messenger.model.ErrorMessage;
import com.pipedog.mixkit.messenger.model.RequestMessage;
import com.pipedog.mixkit.messenger.model.ResponseMessage;

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
     * @param requestMessage 请求消息数据实体
     */
    void request2Client(RequestMessage requestMessage);

    /**
     * 服务端（server）向客户端（client）发送响应消息
     *  第 4 步：server -> client1
     * @param responseMessage 响应消息数据实体
     */
    void response2Client(ResponseMessage responseMessage);

    /**
     * 发送错误消息至客户端（client）
     * @param errorMessage 错误消息数据实体
     */
    void sendError2Client(ErrorMessage errorMessage);

}
