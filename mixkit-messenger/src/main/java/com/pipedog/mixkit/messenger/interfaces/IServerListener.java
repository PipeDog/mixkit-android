package com.pipedog.mixkit.messenger.interfaces;

import com.pipedog.mixkit.messenger.model.ErrorMessage;

/**
 * 服务监听器接口
 * @author liang
 * @time 2021/11/25
 */
public interface IServerListener {

    /**
     * 向源（发起通信）客户端发送消息失败
     * @param errorMessage 错误消息实体
     */
    void didFailSendMessage2SourceClient(ErrorMessage errorMessage);

    /**
     * 向目标（功能执行）客户端发送消息失败
     * @param errorMessage 错误消息实体
     */
    void didFailSendMessage2TargetClient(ErrorMessage errorMessage);

}
