package com.pipedog.mixkit.messenger.core;

import android.content.Context;
import android.os.Message;
import android.os.Parcelable;

import com.pipedog.mixkit.kernel.MixResultCallback;
import com.pipedog.mixkit.messenger.MessengerEngine;
import com.pipedog.mixkit.messenger.client.IMessageClientDelegate;
import com.pipedog.mixkit.messenger.client.MessageClient;
import com.pipedog.mixkit.messenger.constants.MessageKeyword;
import com.pipedog.mixkit.messenger.interfaces.IMessage2Server;
import com.pipedog.mixkit.messenger.model.ErrorMessage;
import com.pipedog.mixkit.messenger.model.RequestMessage;
import com.pipedog.mixkit.messenger.model.ResponseMessage;
import com.pipedog.mixkit.messenger.utils.CallbackIdGenerator;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 跨进程通信入口类
 * @author liang
 * @time 2021/11/22
 */
public class MessengerManager implements
        IMessengerBridgeDelegate, IMessage2Server, IMessageClientDelegate {

    private MessengerBridge mBridge;
    private MessageClient mClient;
    private Map<String, MixResultCallback> mCallbackMap = new HashMap<>();

    public MessengerManager() {
        mBridge = new MessengerBridge(this);
        mClient = new MessageClient(this);
    }


    // PUBLIC METHODS

    public boolean startConnection() {
        return mClient.startConnection();
    }

    public void stopConnection() {
        mClient.stopConnection();
    }

    public void sendMessage(String clientId,
                            String moduleName,
                            String methodName,
                            List<Object> arguments) {
        arguments = arguments != null ? arguments : new ArrayList<>();
        List<Object> serverArgs = new ArrayList<>();

        for (Object arg : arguments) {
            Object serverArg = arg;
            boolean isCallback = arg instanceof MixResultCallback;

            if (isCallback) {
                String callbackId = CallbackIdGenerator.getCallbackId();
                mCallbackMap.put(callbackId, (MixResultCallback) arg);
                serverArg = callbackId;
            }

            serverArgs.add(serverArg);
        }

        String sourceClientId = MessengerEngine.getInstance().getClientId();
        request2Server(new RequestMessage(
                sourceClientId,
                clientId,
                moduleName,
                methodName,
                serverArgs
        ));
    }


    // OVERRIDE METHOD FROM `IMessengerBridgeDelegate`

    @Override
    public IMessage2Server serverCaller() {
        return mClient;
    }


    // OVERRIDE METHODS FROM `IMessage2Server`

    @Override
    public void request2Server(RequestMessage requestMessage) {
        if (!mClient.isConnected()) {
            return;
        }

        mClient.request2Server(requestMessage);
    }

    @Override
    public void response2Server(ResponseMessage responseMessage) {
        if (!mClient.isConnected()) {
            return;
        }

        mClient.response2Server(responseMessage);
    }

    @Override
    public void sendError2Server(ErrorMessage errorMessage) {

    }


    // OVERRIDE METHODS FROM `IMessageClientDelegate`

    @Override
    public void didReceiveRequestMessage(RequestMessage requestMessage) {
        Map<String, Object> metaData = new HashMap<>();
        metaData.put("moduleName", requestMessage.getModuleName());
        metaData.put("methodName", requestMessage.getMethodName());
        metaData.put("arguments", requestMessage.getArguments());

        // Extra parameters
        metaData.put("sourceClientId", requestMessage.getSourceClientId());
        metaData.put("targetClientId", requestMessage.getTargetClientId());
        mBridge.executor().invokeMethod(metaData);
    }

    @Override
    public void didReceiveResponseMessage(ResponseMessage responseMessage) {
        String callbackId = responseMessage.getCallbackId();
        if (callbackId == null || callbackId.isEmpty()) {
            return;
        }

        MixResultCallback callback = mCallbackMap.get(callbackId);
        mCallbackMap.remove(callbackId);

        callback.invoke(responseMessage.getResponse().toArray());
    }

}
