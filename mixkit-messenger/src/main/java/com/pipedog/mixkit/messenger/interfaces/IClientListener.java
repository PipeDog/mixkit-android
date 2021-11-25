package com.pipedog.mixkit.messenger.interfaces;

import com.pipedog.mixkit.messenger.model.ErrorMessage;
import com.pipedog.mixkit.messenger.model.RegisterClientMessage;
import com.pipedog.mixkit.messenger.model.RequestMessage;
import com.pipedog.mixkit.messenger.model.ResponseMessage;
import com.pipedog.mixkit.tool.MixLogger;

/**
 * 客户端消息监听接口
 * @author liang
 * @time 2021/11/25
 */
public interface IClientListener {

    // HANDLE ERROR METHODS

    /**
     * 接收到错误消息
     * @param errorMessage 错误信息数据实体
     */
    default void didReceiveErrorMessage(ErrorMessage errorMessage) {
        MixLogger.error(errorMessage.toString());
    }


    // CONNECTION STATUS UPDATE

    /**
     * 服务连接成功
     */
    default void onServiceConnected() {
        MixLogger.info("Connect server success!");
    }

    /**
     * 服务连接断开
     */
    default void onServiceDisconnected() {
        MixLogger.warning("Disconnect with server!");
    }


    // SOURCE CLIENT METHODS

    /**
     * 发送注册客户端消息成功
     * @param registerClientMessage 注册客户端消息数据实体
     */
    default void didSendRegisterClientMessage(RegisterClientMessage registerClientMessage) {
        MixLogger.info(registerClientMessage.toString());
    }

    /**
     * 发送请求消息成功（此时角色为请求发起端）
     * @param requestMessage 请求消息数据实体
     */
    default void didSendRequestMessage(RequestMessage requestMessage) {
        MixLogger.info(requestMessage.toString());
    }

    /**
     * 接收到响应消息（此时角色为请求发起端）
     * @param responseMessage 响应消息数据实体
     */
    default void didReceiveResponseMessage(ResponseMessage responseMessage) {
        MixLogger.info(responseMessage.toString());
    }


    // TARGET CLIENT METHODS

    /**
     * 接收到请求消息（此时角色为请求响应端）
     * @param requestMessage 请求消息数据实体
     */
    default void didReceiveRequestMessage(RequestMessage requestMessage) {
        MixLogger.info(requestMessage.toString());
    }

    /**
     * 发送响应消息成功（此时角色为请求响应端）
     * @param responseMessage 响应消息数据实体
     */
    default void didSendResponseMessage(ResponseMessage responseMessage) {
        MixLogger.info(responseMessage.toString());
    }

}
