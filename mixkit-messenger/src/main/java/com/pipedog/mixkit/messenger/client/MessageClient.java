package com.pipedog.mixkit.messenger.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.pipedog.mixkit.messenger.IMessengerEngine;
import com.pipedog.mixkit.messenger.MessengerEngine;
import com.pipedog.mixkit.messenger.constants.MessageKeyword;
import com.pipedog.mixkit.messenger.constants.MessageNumber;
import com.pipedog.mixkit.messenger.interfaces.IMessage2Server;
import com.pipedog.mixkit.messenger.server.MessengerService;
import com.pipedog.mixkit.messenger.utils.CallbackIdGenerator;
import com.pipedog.mixkit.module.MixModuleManager;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.Map;

/**
 * 客户端抽象类
 * @author liang
 * @time 2021/11/22
 */
public class MessageClient implements IMessage2Server {

    private Gson mGson = new Gson();
    private IMessageClientDelegate mDelegate;
    private Messenger mServerMessenger;
    private Messenger mClientMessenger = new Messenger(new ClientHandler());
    private ServiceConnection mServiceConnection = new ProcessesConnection();
    private boolean mIsConnected = false;

    private MessageClient() {

    }

    public MessageClient(IMessageClientDelegate delegate) {
        this.mDelegate = delegate;
    }

    /**
     * 连接服务进程
     * @return true 连接成功，false 连接失败
     */
    public boolean startConnection() {
        boolean result = false;

        Intent service = new Intent();
        service.setAction(MessengerEngine.getInstance().getAction());
        service.setPackage(MessengerEngine.getInstance().getPackage());

        try {
            getContext().startService(service);
            result = getContext().bindService(service, mServiceConnection, 0);
        } catch (Exception e) {
            MixLogger.error(e.toString());
            return false;
        }

        return result;
    }

    /**
     * 终止连接服务进程
     */
    public void stopConnection() {
        getContext().unbindService(mServiceConnection);
    }

    /**
     * 是否已连接
     * @return true 已连接，false 未连接或连接失败
     */
    public boolean isConnected() {
        return mIsConnected;
    }

    
    // OVERRIDE METHODS FROM `IMessage2Server`
    
    @Override
    public void request2Server(String clientId, 
                               String moduleName,
                               String methodName,
                               Map<String, Object> parameter, 
                               String callbackId) {
        if (mServerMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(MessageKeyword.KEY_CLIENT_ID, clientId);
        bundle.putString(MessageKeyword.KEY_MODULE_NAME, moduleName);
        bundle.putString(MessageKeyword.KEY_METHOD_NAME, methodName);
        bundle.putString(MessageKeyword.KEY_CALLBACK_ID, callbackId);

        String parameterJson = mGson.toJson(parameter);
        bundle.putString(MessageKeyword.KEY_PARAMETER_NAME, parameterJson);
        
        try {
            Message message = Message.obtain();
            message.replyTo = mClientMessenger;
            message.what = MessageNumber.REQUEST_TO_SERVER;
            message.setData(bundle);
            mServerMessenger.send(message);
        } catch (Exception e) {
            MixLogger.error("Request to server message send failed, error = %s", e.toString());
        }
    }

    @Override
    public void response2Server(String clientId,
                                String callbackId,
                                Map<String, Object> result) {
        if (mServerMessenger == null) {
            return;
        }
        
        Bundle bundle = new Bundle();
        bundle.putString(MessageKeyword.KEY_CLIENT_ID, clientId);
        bundle.putString(MessageKeyword.KEY_CALLBACK_ID, callbackId);

        String responseJson = mGson.toJson(result);
        bundle.putString(MessageKeyword.KEY_RESPONSE_DATA, responseJson);
        
        try {
            Message message = Message.obtain();
            message.replyTo = mClientMessenger;
            message.what = MessageNumber.RESPONSE_TO_SERVER;
            message.setData(bundle);
            mServerMessenger.send(message);
        } catch (Exception e) {
            MixLogger.error("Response to server message send failed, error = %s", e.toString());
        }
    }


    // HANDLE MESSAGE METHODS

    private void receiveRequest2Client(Message message) {
        if (mDelegate == null) {
            return;
        }

        Bundle bundle = message.getData();
        String moduleName = bundle.getString(MessageKeyword.KEY_MODULE_NAME);
        String methodName = bundle.getString(MessageKeyword.KEY_METHOD_NAME);
        String callbackId = bundle.getString(MessageKeyword.KEY_CALLBACK_ID);

        String parameterJson = bundle.getString(MessageKeyword.KEY_PARAMETER_NAME);
        Map<String, Object> parameter = mGson.fromJson(parameterJson, Map.class);

        mDelegate.didReceiveRequestMessage(moduleName, methodName, parameter, callbackId);
    }

    private void receiveResponse2Client(Message message) {
        if (mDelegate == null) {
            return;
        }

        Bundle bundle = message.getData();
        String callbackId = bundle.getString(MessageKeyword.KEY_CALLBACK_ID);
        String responseJson = bundle.getString(MessageKeyword.KEY_RESPONSE_DATA);
        Map<String, Object> result = mGson.fromJson(responseJson, Map.class);

        mDelegate.didReceiveResponseMessage(callbackId, result);
    }


    // INTERNAL METHODS

    private void bindClient() {
        Message message = Message.obtain();
        message.what = MessageNumber.REGISTER_CLIENT;
        message.replyTo = mClientMessenger;

        try {
            mServerMessenger.send(message);
        } catch (Exception e) {
            MixLogger.error(e.toString());
        }
    }

    private void registerModuleData() {
        Message message = Message.obtain();
        message.what = MessageNumber.EXPORT_MODULES;
        message.replyTo = mClientMessenger;

        Bundle bundle = new Bundle();
        String clientId = MessengerEngine.getInstance().getClientId();
        String moduleData = MixModuleManager.defaultManager().getModuleDataJson();
        bundle.putString(clientId, moduleData);

        message.setData(bundle);

        try {
            mServerMessenger.send(message);
        } catch (Exception e) {
            MixLogger.error(e.toString());
        }
    }

    private Context getContext() {
        return MessengerEngine.getInstance().getContext();
    }


    // INTERNAL CLASSES

    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MessageNumber.REQUEST_TO_CLIENT: {
                    receiveRequest2Client(msg);
                } break;
                case MessageNumber.RESPONSE_TO_CLIENT: {
                    receiveResponse2Client(msg);
                } break;
                default: {
                    MixLogger.error("Unsupport message number = " + msg.what);
                } break;
            }

        }
    }

    private class ProcessesConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServerMessenger = new Messenger(service);
            mIsConnected = true;

            // Init when connect success
            bindClient();
            registerModuleData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServerMessenger = null;
            mIsConnected = false;
        }
    }

}
