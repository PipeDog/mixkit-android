package com.pipedog.mixkit.messenger.core;

import com.pipedog.mixkit.kernel.ResultCallback;
import com.pipedog.mixkit.messenger.ClientEngine;
import com.pipedog.mixkit.messenger.constants.ErrorCode;
import com.pipedog.mixkit.messenger.interfaces.IMessageClientDelegate;
import com.pipedog.mixkit.messenger.client.MessageClient;
import com.pipedog.mixkit.messenger.interfaces.IMessage2Server;
import com.pipedog.mixkit.messenger.model.ErrorMessage;
import com.pipedog.mixkit.messenger.model.RequestMessage;
import com.pipedog.mixkit.messenger.model.ResponseMessage;
import com.pipedog.mixkit.messenger.utils.CallbackIdGenerator;
import com.pipedog.mixkit.messenger.utils.TraceIdGenerator;
import com.pipedog.mixkit.tool.ThreadUtils;

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
        IMessengerBridgeDelegate, IMessageClientDelegate {

    private MessengerBridge mBridge;
    private MessageClient mClient;
    private Map<String, Map<String, ResultCallback>> mCallbackMap = new HashMap<>();

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

    public String sendMessage(String targetClientId,
                              String moduleName,
                              String methodName,
                              List<Object> arguments) {
        arguments = arguments != null ? arguments : new ArrayList<>();
        List<Object> serverArgs = new ArrayList<>();

        String traceId = TraceIdGenerator.getTraceId();
        Map<String, ResultCallback> callbacks = new HashMap<>();
        mCallbackMap.put(traceId, callbacks);

        for (Object arg : arguments) {
            Object serverArg = arg;
            boolean isCallback = arg instanceof ResultCallback;

            if (isCallback) {
                String callbackId = CallbackIdGenerator.getCallbackId();
                callbacks.put(callbackId, (ResultCallback) arg);
                serverArg = callbackId;
            }

            serverArgs.add(serverArg);
        }

        String sourceClientId = ClientEngine.getInstance().getClientId();
        mClient.request2Server(new RequestMessage(
                traceId, sourceClientId, targetClientId,
                moduleName, methodName, serverArgs));
        return traceId;
    }


    // OVERRIDE METHOD FROM `IMessengerBridgeDelegate`

    @Override
    public IMessage2Server serverCaller() {
        return mClient;
    }


    // OVERRIDE METHODS FROM `IMessageClientDelegate`

    @Override
    public void didReceiveRequestMessage(RequestMessage requestMessage) {
        Map<String, Object> metaData = new HashMap<>();
        metaData.put("moduleName", requestMessage.getModuleName());
        metaData.put("methodName", requestMessage.getMethodName());
        metaData.put("arguments", requestMessage.getArguments());

        // Extra parameters
        metaData.put("traceId", requestMessage.getTraceId());
        metaData.put("sourceClientId", requestMessage.getSourceClientId());
        metaData.put("targetClientId", requestMessage.getTargetClientId());

        ThreadUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                boolean result = mBridge.getExecutor().invokeMethod(metaData);
                if (!result) {
                    mClient.sendError2Server(new ErrorMessage(
                            requestMessage.getTraceId(), ErrorCode.ERR_INVOKE_METHOD_FAILED,
                            "Invoke method failed!" + requestMessage.toString(),
                            requestMessage.getSourceClientId(), requestMessage.getTargetClientId()));
                }
            }
        });
    }

    @Override
    public void didReceiveResponseMessage(ResponseMessage responseMessage) {
        String traceId = responseMessage.getTraceId();
        String callbackId = responseMessage.getCallbackId();

        Map<String, ResultCallback> callbacks = mCallbackMap.get(traceId);
        mCallbackMap.remove(traceId);

        ResultCallback callback = callbacks.get(callbackId);

        ThreadUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                callback.invoke(responseMessage.getResponse().toArray());
            }
        });
    }

    @Override
    public void didReceiveErrorMessage(ErrorMessage errorMessage) {
        mCallbackMap.remove(errorMessage.getTraceId());
    }

}
