package com.pipedog.mixkit.messenger.core;

import android.content.Context;
import android.os.Message;
import android.os.Parcelable;

import com.pipedog.mixkit.launch.MixLaunchManager;
import com.pipedog.mixkit.messenger.client.IMessageClientDelegate;
import com.pipedog.mixkit.messenger.client.MessageClient;
import com.pipedog.mixkit.messenger.interfaces.IMessage2Server;
import com.pipedog.mixkit.messenger.interfaces.IMessageCallback;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 夸进程通信入口类
 * @author liang
 * @time 2021/11/22
 */
public class MessengerManager implements IMessengerBridgeDelegate, IMessage2Server, IMessageClientDelegate {

    private MessengerBridge mBridge;
    private MessageClient mClient;
    private volatile static MessengerManager sMessengerManager;

    public static MessengerManager getInstance() {
        if (sMessengerManager == null) {
            synchronized (MessengerManager.class) {
                if (sMessengerManager == null) {
                    sMessengerManager = new MessengerManager();
                }
            }
        }
        return sMessengerManager;
    }

    private MessengerManager() {
        mBridge = new MessengerBridge(this);
        
        Context context = MixLaunchManager.defaultManager().getContext();
        if (context == null) {
            MixLogger.error("Call method `registerContext` in class `MixLaunchManager` first!");
        }
        mClient = new MessageClient(context, this);
    }

    /**
     * 启动连接
     * @return true 连接成功，false 连接失败
     */
    public boolean startConnection() {
        return mClient.startConnection();
    }

    /**
     * 终止连接服务进程
     */
    public void stopConnection() {
        mClient.stopConnection();
    }


    // OVERRIDE METHOD FROM `IMessengerBridgeDelegate`

    @Override
    public IMessage2Server serverCaller() {
        return mClient;
    }


    // OVERRIDE METHODS FROM `IMessage2Server`

    @Override
    public void request2Server(String processId,
                               String moduleName,
                               String methodName,
                               Map<String, Object> parameter,
                               String callbackId) {
        if (!mClient.isConnected()) {
            return;
        }

        mClient.request2Server(processId, moduleName, methodName, parameter, callbackId);
    }

    @Override
    public void response2Server(String processId,
                                String callbackId,
                                Map<String, Object> result) {
        if (!mClient.isConnected()) {
            return;
        }

        mClient.response2Server(processId, callbackId, result);
    }


    // OVERRIDE METHODS FROM `IMessageClientDelegate`

    @Override
    public void didReceiveRequestMessage(String moduleName,
                                         String methodName,
                                         Map<String, Object> parameter,
                                         String callbackId) {

        Map<String, Object> metaData = new HashMap<>();
        metaData.put("moduleName", moduleName);
        metaData.put("methodName", methodName);

        List<Object> arguments = new ArrayList<>();
        arguments.add(parameter);
        arguments.add(callbackId);
        metaData.put("arguments", arguments);

        mBridge.executor().invokeMethod(metaData);
    }

    @Override
    public void didReceiveResponseMessage(String callbackId, Map<String, Object> result) {
        List<Object> arguments = new ArrayList<>();
        arguments.add(result);

        MessengerExecutor executor = (MessengerExecutor)mBridge.executor();
        executor.invokeCallback(arguments, callbackId);
    }

}
