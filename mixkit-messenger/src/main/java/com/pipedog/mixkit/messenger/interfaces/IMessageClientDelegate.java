package com.pipedog.mixkit.messenger.interfaces;

import com.pipedog.mixkit.messenger.model.RequestMessage;
import com.pipedog.mixkit.messenger.model.ResponseMessage;

import java.util.List;

/**
 * 客户端代理接口
 * @author liang
 * @time 2021/11/22
 */
public interface IMessageClientDelegate {

    /**
     * 接收到请求执行消息
     * @param requestMessage 请求消息数据实例
     */
    void didReceiveRequestMessage(RequestMessage requestMessage);

    /**
     * 接收到响应消息
     * @param responseMessage 响应信息数据实例
     */
    void didReceiveResponseMessage(ResponseMessage responseMessage);

}
