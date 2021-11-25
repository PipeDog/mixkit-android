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

import androidx.annotation.NonNull;

import com.pipedog.mixkit.messenger.MessengerEngine;
import com.pipedog.mixkit.messenger.constants.MessageKeyword;
import com.pipedog.mixkit.messenger.constants.MessageNumber;
import com.pipedog.mixkit.messenger.interfaces.IMessage2Server;
import com.pipedog.mixkit.messenger.interfaces.IMessageClientDelegate;
import com.pipedog.mixkit.messenger.model.ErrorMessage;
import com.pipedog.mixkit.messenger.model.RegisterClientMessage;
import com.pipedog.mixkit.messenger.model.RequestMessage;
import com.pipedog.mixkit.messenger.model.ResponseMessage;
import com.pipedog.mixkit.messenger.utils.TraceIdGenerator;
import com.pipedog.mixkit.module.MixModuleManager;
import com.pipedog.mixkit.tool.MixLogger;

/**
 * 客户端抽象类
 * @author liang
 * @time 2021/11/22
 */
public class MessageClient implements IMessage2Server {

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
    public void request2Server(RequestMessage requestMessage) {
        if (mServerMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(MessageKeyword.KEY_REQUEST_DATA, requestMessage);
        
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
    public void response2Server(ResponseMessage responseMessage) {
        if (mServerMessenger == null) {
            return;
        }
        
        Bundle bundle = new Bundle();
        bundle.putSerializable(MessageKeyword.KEY_RESPONSE_DATA, responseMessage);

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

    @Override
    public void sendError2Server(ErrorMessage errorMessage) {

    }


    // HANDLE MESSAGE METHODS

    private void receiveRequest2Client(Message message) {
        if (mDelegate == null) {
            return;
        }

        Bundle bundle = message.getData();
        RequestMessage requestMessage = (RequestMessage) bundle.getSerializable(MessageKeyword.KEY_REQUEST_DATA);

        mDelegate.didReceiveRequestMessage(requestMessage);
    }

    private void receiveResponse2Client(Message message) {
        if (mDelegate == null) {
            return;
        }

        Bundle bundle = message.getData();
        ResponseMessage responseMessage = (ResponseMessage) bundle.getSerializable(MessageKeyword.KEY_RESPONSE_DATA);

        mDelegate.didReceiveResponseMessage(responseMessage);
    }


    // INTERNAL METHODS

    private void registerClient() {
        RegisterClientMessage registerClientMessage = new RegisterClientMessage(
                TraceIdGenerator.getTraceId(),
                MessengerEngine.getInstance().getClientId(),
                MixModuleManager.defaultManager().getModuleDataMap()
        );

        Bundle bundle = new Bundle();
        bundle.putSerializable(MessageKeyword.KEY_REGISTER_CLIENT, registerClientMessage);

        try {
            Message message = Message.obtain();
            message.what = MessageNumber.REGISTER_CLIENT;
            message.replyTo = mClientMessenger;
            message.setData(bundle);
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
            registerClient();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServerMessenger = null;
            mIsConnected = false;
        }
    }

}
