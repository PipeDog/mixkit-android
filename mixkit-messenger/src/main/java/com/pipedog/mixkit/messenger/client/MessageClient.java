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

import com.pipedog.mixkit.messenger.IMessengerEngine;
import com.pipedog.mixkit.messenger.MessengerEngine;
import com.pipedog.mixkit.messenger.constants.ErrorCode;
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
        service.setAction(getEngine().getAction());
        service.setPackage(getEngine().getPackage());

        try {
            getContext().startService(service);
            result = getContext().bindService(service, mServiceConnection, 0);
        } catch (Exception e) {
            getListenerManager().didReceiveErrorMessage(new ErrorMessage(
                    null,
                    ErrorCode.ERR_CONNECTION_FAILED,
                    e.toString(),
                    getEngine().getClientId(),
                    null
            ));
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

            getListenerManager().didSendRequestMessage(requestMessage);
        } catch (Exception e) {
            getListenerManager().didReceiveErrorMessage(new ErrorMessage(
                    requestMessage.getTraceId(),
                    ErrorCode.ERR_DISCONNECT_SERVER,
                    e.toString(),
                    requestMessage.getSourceClientId(),
                    requestMessage.getTargetClientId()
            ));
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

            getListenerManager().didSendResponseMessage(responseMessage);
        } catch (Exception e) {
            getListenerManager().didReceiveErrorMessage(new ErrorMessage(
                    responseMessage.getTraceId(),
                    ErrorCode.ERR_DISCONNECT_SERVER,
                    e.toString(),
                    responseMessage.getSourceClientId(),
                    responseMessage.getTargetClientId()
            ));
        }
    }

    @Override
    public void sendError2Server(ErrorMessage errorMessage) {
        getListenerManager().didReceiveErrorMessage(errorMessage);
    }


    // HANDLE MESSAGE METHODS

    private void receiveRequest2Client(Message message) {
        Bundle bundle = message.getData();
        RequestMessage requestMessage = (RequestMessage) bundle.getSerializable(MessageKeyword.KEY_REQUEST_DATA);

        mDelegate.didReceiveRequestMessage(requestMessage);
        getListenerManager().didReceiveRequestMessage(requestMessage);
    }

    private void receiveResponse2Client(Message message) {
        Bundle bundle = message.getData();
        ResponseMessage responseMessage = (ResponseMessage) bundle.getSerializable(MessageKeyword.KEY_RESPONSE_DATA);

        mDelegate.didReceiveResponseMessage(responseMessage);
        getListenerManager().didReceiveResponseMessage(responseMessage);
    }

    private void receiveError(Message message) {
        Bundle bundle = message.getData();
        ErrorMessage errorMessage = (ErrorMessage) bundle.getSerializable(MessageKeyword.KEY_ERROR_DATA);

        mDelegate.didReceiveErrorMessage(errorMessage);
        getListenerManager().didReceiveErrorMessage(errorMessage);
    }


    // INTERNAL METHODS

    private void registerClient() {
        String traceId = TraceIdGenerator.getTraceId();
        String sourceClientId = getEngine().getClientId();
        RegisterClientMessage registerClientMessage = new RegisterClientMessage(
                traceId, sourceClientId,
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

            getListenerManager().didSendRegisterClientMessage(registerClientMessage);
        } catch (Exception e) {
            getListenerManager().didReceiveErrorMessage(new ErrorMessage(
                    traceId,
                    ErrorCode.ERR_REGISTER_CLIENT_FAILED,
                    e.toString(),
                    sourceClientId,
                    null
            ));
        }
    }


    // GETTER METHODS

    private IMessengerEngine getEngine() {
        return MessengerEngine.getInstance();
    }

    private ClientListenerManager getListenerManager() {
        return ClientListenerManager.getInstance();
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
                case MessageNumber.REQUEST_ERROR: {
                    receiveError(message);
                } break;
                case MessageNumber.RESPONSE_ERROR: {
                    receiveError(message);
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
            getListenerManager().onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServerMessenger = null;
            mIsConnected = false;

            getListenerManager().onServiceDisconnected();
        }
    }

}
