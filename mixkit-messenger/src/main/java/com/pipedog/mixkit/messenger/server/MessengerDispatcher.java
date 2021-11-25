package com.pipedog.mixkit.messenger.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;

import androidx.annotation.NonNull;

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

    private Messenger mServerMessenger = new Messenger(new ServerHandler());
    private Map<String, Messenger> mClientMessengers = new HashMap<String, Messenger>();
    private Map<String, Map<String, MixModuleData>> mModuleDataTable = new HashMap<>();

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
            MixLogger.error(e.toString());
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
            MixLogger.error(e.toString());
        }
    }

    @Override
    public void sendError2Client(ErrorMessage errorMessage) {

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
                default: {
                    MixLogger.error("Unsupport message number = " + msg.what);
                } break;
            }
        }
    }

    // TODO: 请求信息检查，如果目标客户端提供信息无法匹配，则本次 request 无效，直接在 server 进行驳回

}
