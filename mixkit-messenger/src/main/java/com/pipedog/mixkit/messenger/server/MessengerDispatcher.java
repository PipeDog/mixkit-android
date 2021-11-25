package com.pipedog.mixkit.messenger.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.pipedog.mixkit.messenger.constants.ErrorCode;
import com.pipedog.mixkit.messenger.constants.MessageKeyword;
import com.pipedog.mixkit.messenger.constants.MessageNumber;
import com.pipedog.mixkit.messenger.interfaces.IMessage2Client;
import com.pipedog.mixkit.messenger.model.ErrorMessage;
import com.pipedog.mixkit.messenger.model.RegisterClientMessage;
import com.pipedog.mixkit.messenger.model.RequestMessage;
import com.pipedog.mixkit.messenger.model.ResponseMessage;
import com.pipedog.mixkit.module.MixModuleData;
import com.pipedog.mixkit.tool.MixLogger;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessengerDispatcher implements IMessage2Client {

    private ServerListenerManager mServerListenerManager = new ServerListenerManager();
    private Messenger mServerMessenger = new Messenger(new ServerHandler());
    private Map<String, Messenger> mClientMessengers = new HashMap<String, Messenger>();
    private Map<String, Map<String, MixModuleData>> mModuleDataTable = new HashMap<>();

    public MessengerDispatcher() {

    }

    public Messenger getMessenger() {
        return mServerMessenger;
    }

    
    // OVERRIDE METHODS FROM `IMessage2Client`

    @Override
    public void request2Client(RequestMessage requestMessage) {
        Messenger clientMessenger = mClientMessengers.get(requestMessage.getTargetClientId());
        if (clientMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(MessageKeyword.KEY_REQUEST_DATA, requestMessage);

        try {
            Message message = Message.obtain();
            message.setData(bundle);
            message.what = MessageNumber.REQUEST_TO_CLIENT;
            clientMessenger.send(message);
        } catch (Exception e) {
            sendError2SourceClient(new ErrorMessage(
                    requestMessage.getTraceId(), ErrorCode.ERR_DISCONNECT_TARGET_CLIENT, e.toString(),
                    requestMessage.getSourceClientId(), requestMessage.getTargetClientId()));
        }
    }

    @Override
    public void response2Client(ResponseMessage responseMessage) {
        Messenger clientMessenger = mClientMessengers.get(responseMessage.getSourceClientId());
        if (clientMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(MessageKeyword.KEY_RESPONSE_DATA, responseMessage);

        try {
            Message message = Message.obtain();
            message.setData(bundle);
            message.what = MessageNumber.RESPONSE_TO_CLIENT;
            clientMessenger.send(message);
        } catch (Exception e) {
            sendError2TargetClient(new ErrorMessage(
                    responseMessage.getTraceId(), ErrorCode.ERR_DISCONNECT_SOURCE_CLIENT, e.toString(),
                    responseMessage.getSourceClientId(), responseMessage.getTargetClientId()));
        }
    }

    @Override
    public void sendError2SourceClient(ErrorMessage errorMessage) {
        Messenger clientMessenger = mClientMessengers.get(errorMessage.getSourceClientId());
        if (clientMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(MessageKeyword.KEY_ERROR_DATA, errorMessage);

        try {
            Message message = Message.obtain();
            message.setData(bundle);
            message.what = MessageNumber.ERROR_IN_COMMUNICATION;
            clientMessenger.send(message);
        } catch (Exception e) {
            mServerListenerManager.didFailSendMessage2SourceClient(errorMessage);
        }
    }

    @Override
    public void sendError2TargetClient(ErrorMessage errorMessage) {
        Messenger clientMessenger = mClientMessengers.get(errorMessage.getTargetClientId());
        if (clientMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(MessageKeyword.KEY_ERROR_DATA, errorMessage);

        try {
            Message message = Message.obtain();
            message.setData(bundle);
            message.what = MessageNumber.ERROR_IN_COMMUNICATION;
            clientMessenger.send(message);
        } catch (Exception e) {
            mServerListenerManager.didFailSendMessage2TargetClient(errorMessage);
        }
    }


    // HANDLE MESSAGE METHODS

    private void receiveRegisterClient(Message message) {
        Messenger clientMessenger = message.replyTo;

        Bundle bundle = message.getData();
        RegisterClientMessage clientMessage = (RegisterClientMessage) bundle.getSerializable(MessageKeyword.KEY_REGISTER_CLIENT);
        String sourceClientId = clientMessage.getSourceClientId();

        mClientMessengers.put(sourceClientId, clientMessenger);
        mModuleDataTable.put(sourceClientId, clientMessage.getModuleDataMap());
    }

    private void receiveRequest2Server(Message message) {
        Bundle bundle = message.getData();
        RequestMessage requestMessage = (RequestMessage) bundle.getSerializable(MessageKeyword.KEY_REQUEST_DATA);
        request2Client(requestMessage);
    }

    private void receiveResponse2Server(Message message) {
        Bundle bundle = message.getData();
        ResponseMessage responseMessage = (ResponseMessage) bundle.getSerializable(MessageKeyword.KEY_RESPONSE_DATA);
        response2Client(responseMessage);
    }

    private void receiveInvokeError(Message message) {
        Bundle bundle = message.getData();
        ErrorMessage errorMessage = (ErrorMessage) bundle.getSerializable(MessageKeyword.KEY_ERROR_DATA);
        sendError2SourceClient(errorMessage);
    }

    // INTERNAL CLASSES

    private class ServerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MessageNumber.REGISTER_CLIENT: {
                    receiveRegisterClient(msg);
                } break;
                case MessageNumber.REQUEST_TO_SERVER: {
                    receiveRequest2Server(msg);
                } break;
                case MessageNumber.RESPONSE_TO_SERVER: {
                    receiveResponse2Server(msg);
                } break;
                case MessageNumber.ERROR_WHEN_INVOKE: {
                    receiveInvokeError(msg);
                } break;
                default: {
                    MixLogger.error("Unsupport message number = " + msg.what);
                } break;
            }
        }
    }

}
