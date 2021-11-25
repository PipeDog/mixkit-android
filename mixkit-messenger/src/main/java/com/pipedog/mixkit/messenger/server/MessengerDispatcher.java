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
    public void request2Client(String sourceClientId,
                               String targetClientId,
                               String moduleName,
                               String methodName,
                               List<Object> arguments) {
        Messenger clientMessenger = mClientMessengers.get(targetClientId);
        if (clientMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(MessageKeyword.KEY_SOURCE_CLIENT_ID, sourceClientId);
        bundle.putString(MessageKeyword.KEY_TARGET_CLIENT_ID, targetClientId);
        bundle.putString(MessageKeyword.KEY_MODULE_NAME, moduleName);
        bundle.putString(MessageKeyword.KEY_METHOD_NAME, methodName);
        bundle.putSerializable(MessageKeyword.KEY_ARGUMENTS_NAME, (Serializable) arguments);

        Message message = Message.obtain();
        message.setData(bundle);
        message.what = MessageNumber.REQUEST_TO_CLIENT;

        try {
            clientMessenger.send(message);
        } catch (Exception e) {
            MixLogger.error(e.toString());
        }
    }

    @Override
    public void response2Client(String sourceClientId,
                                String targetClientId,
                                String callbackId,
                                List<Object> response) {
        Messenger clientMessenger = mClientMessengers.get(sourceClientId);
        if (clientMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(MessageKeyword.KEY_SOURCE_CLIENT_ID, sourceClientId);
        bundle.putString(MessageKeyword.KEY_TARGET_CLIENT_ID, targetClientId);
        bundle.putString(MessageKeyword.KEY_CALLBACK_ID, callbackId);
        bundle.putSerializable(MessageKeyword.KEY_RESPONSE_DATA, (Serializable) response);

        Message message = Message.obtain();
        message.setData(bundle);
        message.what = MessageNumber.RESPONSE_TO_CLIENT;

        try {
            clientMessenger.send(message);
        } catch (Exception e) {
            MixLogger.error(e.toString());
        }
    }


    // HANDLE MESSAGE METHODS

    private void receiveRegisterClient(Message message) {
        Messenger clientMessenger = message.replyTo;

        // Get key
        Bundle bundle = message.getData();
        String sourceClientId = bundle.getString(MessageKeyword.KEY_SOURCE_CLIENT_ID);

        // Get value
        Map<String, MixModuleData> moduleData =
                (Map<String, MixModuleData>) bundle.getSerializable(MessageKeyword.KEY_MODULE_DATA);

        // Register into map
        mClientMessengers.put(sourceClientId, clientMessenger);
        mModuleDataTable.put(sourceClientId, moduleData);
    }

    private void receiveRequest2Server(Message message) {
        Bundle bundle = message.getData();

        String sourceClientId = bundle.getString(MessageKeyword.KEY_SOURCE_CLIENT_ID);
        String targetClientId = bundle.getString(MessageKeyword.KEY_TARGET_CLIENT_ID);
        String moduleName = bundle.getString(MessageKeyword.KEY_MODULE_NAME);
        String methodName = bundle.getString(MessageKeyword.KEY_METHOD_NAME);
        List<Object> arguments = (List<Object>) bundle.getSerializable(MessageKeyword.KEY_ARGUMENTS_NAME);

        request2Client(sourceClientId, targetClientId, moduleName, methodName, arguments);
    }

    private void receiveResponse2Server(Message message) {
        Bundle bundle = message.getData();

        String sourceClientId = bundle.getString(MessageKeyword.KEY_SOURCE_CLIENT_ID);
        String targetClientId = bundle.getString(MessageKeyword.KEY_TARGET_CLIENT_ID);
        String callbackId = bundle.getString(MessageKeyword.KEY_CALLBACK_ID);
        List<Object> response = (List<Object>) bundle.getSerializable(MessageKeyword.KEY_RESPONSE_DATA);

        response2Client(sourceClientId, targetClientId, callbackId, response);
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
