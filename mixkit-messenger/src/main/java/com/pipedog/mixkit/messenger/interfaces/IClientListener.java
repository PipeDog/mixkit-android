package com.pipedog.mixkit.messenger.interfaces;

import com.pipedog.mixkit.messenger.model.ErrorMessage;
import com.pipedog.mixkit.messenger.model.RequestMessage;
import com.pipedog.mixkit.messenger.model.ResponseMessage;

/**
 * 客户端消息监听接口
 * @author liang
 * @time 2021/11/25
 */
public interface IClientListener {

    // SOURCE CLIENT METHODS

    default void didSendRequestMessage(RequestMessage requestMessage) {

    }

    default void didReceiveResponseMessage(ResponseMessage responseMessage) {

    }


    // TARGET CLIENT METHODS

    default void didReceiveRequestMessage(RequestMessage requestMessage) {

    }

    default void didSendResponseMessage(ResponseMessage responseMessage) {

    }


    // HANDLE ERROR METHODS

    void didReceiveErrorMessage(ErrorMessage errorMessage);

}
