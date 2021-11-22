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
import com.pipedog.mixkit.messenger.constants.MessageKeyword;
import com.pipedog.mixkit.messenger.constants.MessageNumber;
import com.pipedog.mixkit.messenger.interfaces.IMessage2Server;
import com.pipedog.mixkit.messenger.interfaces.IMessageCallback;
import com.pipedog.mixkit.messenger.server.MessengerService;
import com.pipedog.mixkit.messenger.utils.CallbackIdGenerator;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.Map;

/**
 * 客户端抽象类
 * @author liang
 * @time 2021/11/22
 */
public class MessageClient implements IMessage2Server {

    private Gson mGson = new Gson();
    private Context mContext;
    private IMessageClientDelegate mDelegate;
    private Messenger mServerMessenger;
    private Messenger mClientMessenger = new Messenger(new ClientHandler());
    private ServiceConnection mServiceConnection = new ProcessesConnection();
    private boolean mIsConnected = false;

    private MessageClient() {
        // Use `MessageClient(Context)` instead of current method
    }

    public MessageClient(Context context, IMessageClientDelegate delegate) {
        this.mContext = context;
        this.mDelegate = delegate;
    }

    /**
     * 连接服务进程
     * @return true 连接成功，false 连接失败
     */
    public boolean startConnection() {
        boolean result = mContext.bindService(
                new Intent(mContext, MessengerService.class),
                mServiceConnection, Context.BIND_AUTO_CREATE);
        if (!result) {
            MixLogger.error("Create connection with server process failed!");
        }
        return result;
    }

    /**
     * 终止连接服务进程
     */
    public void stopConnection() {
        mContext.unbindService(mServiceConnection);
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
    public void request2Server(String processId, 
                               String moduleName,
                               String methodName,
                               Map<String, Object> parameter, 
                               String callbackId) {
        if (mServerMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        // TODO: 数据填充
        
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
    public void response2Server(String processId,
                                String callbackId,
                                Map<String, Object> result) {
        if (mServerMessenger == null) {
            return;
        }
        
        Bundle bundle = new Bundle();
        // TODO: 数据填充

        try {
            Message message = Message.obtain();
            message.replyTo = mClientMessenger;
            message.what = MessageNumber.RESPONSE_TO_SERVER;
            message.setData(bundle);
            mServerMessenger.send(message);
        } catch (Exception e) {
            MixLogger.error("Request to server message send failed, error = %s", e.toString());
        }
    }


    // INTERNAL METHODS

    private void receiveRequest2Client(Message message) {
        if (mDelegate == null) {
            return;
        }

        Bundle bundle = message.getData();

        String moduleName = bundle.getString(MessageKeyword.KEY_MODULE_NAME);
        String methodName = bundle.getString(MessageKeyword.KEY_METHOD_NAME);
        String callbackId = bundle.getString(MessageKeyword.KEY_CALLBACK_ID);

        String json = bundle.getString(MessageKeyword.KEY_PARAMETER_NAME);
        Map<String, Object> parameter = mGson.fromJson(json, Map.class);

        mDelegate.didReceiveRequestMessage(moduleName, methodName, parameter, callbackId);
    }

    private void receiveResponse2Client(Message message) {
        if (mDelegate == null) {
            return;
        }

        Bundle bundle = message.getData();

        String callbackId = bundle.getString(MessageKeyword.KEY_CALLBACK_ID);
        String json = bundle.getString(MessageKeyword.KEY_RESPONSE_DATA);
        Map<String, Object> result = mGson.fromJson(json, Map.class);

        mDelegate.didReceiveResponseMessage(callbackId, result);
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

            // TODO: 绑定 client 及注入 modules 信息到服务端等
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServerMessenger = null;
            mIsConnected = false;
        }
    }

}
