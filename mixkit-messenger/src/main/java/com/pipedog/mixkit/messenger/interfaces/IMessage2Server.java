package com.pipedog.mixkit.messenger.interfaces;

import android.os.Parcelable;

import com.pipedog.mixkit.messenger.model.ErrorMessage;
import com.pipedog.mixkit.messenger.model.RequestMessage;
import com.pipedog.mixkit.messenger.model.ResponseMessage;

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
     * @param requestMessage 请求消息数据实体
     */
    void request2Server(RequestMessage requestMessage);

    /**
     * 向服务端（server）发送响应消息
     *  第 3 步：client2 -> server
     * @param responseMessage 响应消息数据实体
     */
    void response2Server(ResponseMessage responseMessage);

    /**
     * 发送错误消息至服务端（server）
     * @param errorMessage 错误消息数据实体
     */
    void sendError2Server(ErrorMessage errorMessage);

}
