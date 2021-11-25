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

    default void didReceiveErrorMessage(ErrorMessage errorMessage) {
        MixLogger.error(errorMessage.toString());
    }


    // CONNECTION STATUS UPDATE

    default void onServiceConnected() {
        MixLogger.info("Connect server success!");
    }

    default void onServiceDisconnected() {
        MixLogger.warning("Disconnect with server!");
    }


    // SOURCE CLIENT METHODS

    default void didSendRegisterClientMessage(RegisterClientMessage registerClientMessage) {
        MixLogger.info(registerClientMessage.toString());
    }

    default void didSendRequestMessage(RequestMessage requestMessage) {
        MixLogger.info(requestMessage.toString());
    }

    default void didReceiveResponseMessage(ResponseMessage responseMessage) {
        MixLogger.info(responseMessage.toString());
    }


    // TARGET CLIENT METHODS

    default void didReceiveRequestMessage(RequestMessage requestMessage) {
        MixLogger.info(requestMessage.toString());
    }

    default void didSendResponseMessage(ResponseMessage responseMessage) {
        MixLogger.info(responseMessage.toString());
    }

}
