package com.pipedog.mixkit.messenger.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.pipedog.mixkit.messenger.constants.MessageKeyword;
import com.pipedog.mixkit.messenger.constants.MessageNumber;
import com.pipedog.mixkit.messenger.interfaces.IMessage2Client;
import com.pipedog.mixkit.tool.MixLogger;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessengerDispatcher implements IMessage2Client {

    private Gson mGson = new Gson();
    private Messenger mServerMessenger = new Messenger(new ServerHandler());
    private Map<String, Messenger> mClientMessengers = new HashMap<String, Messenger>();
    private Map<String, Map<String, Object>> mModuleDataTable = new HashMap<>();

    public Messenger getMessenger() {
        return mServerMessenger;
    }

    
    // OVERRIDE METHODS FROM `IMessage2Client`

    @Override
    public void request2Client(String clientId,
                               String moduleName,
                               String methodName,
                               List<Object> arguments) {
        Messenger clientMessenger = mClientMessengers.get(clientId);
        if (clientMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(MessageKeyword.KEY_MODULE_NAME, moduleName);
        bundle.putString(MessageKeyword.KEY_METHOD_NAME, methodName);

        String argumentsJson = mGson.toJson(arguments);
        bundle.putString(MessageKeyword.KEY_ARGUMENTS_NAME, argumentsJson);

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
    public void response2Client(String clientId,
                                String callbackId,
                                List<Object> response) {
        Messenger clientMessenger = mClientMessengers.get(clientId);
        if (clientMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(MessageKeyword.KEY_CALLBACK_ID, callbackId);

        String responseJson = mGson.toJson(response);
        bundle.putString(MessageKeyword.KEY_RESPONSE_DATA, responseJson);

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
        // Get key
        Bundle bundle = message.getData();
        String clientId = bundle.getString(MessageKeyword.KEY_CLIENT_ID);

        // Get value then register into map
        Messenger clientMessenger = message.replyTo;
        mClientMessengers.put(clientId, clientMessenger);
    }

    private void receiveExportModules(Message message) {
        // Get key
        Bundle bundle = message.getData();
        String clientId = bundle.getString(MessageKeyword.KEY_CLIENT_ID);

        // Get value
        String moduleDataJson = bundle.getString(MessageKeyword.KEY_MODULE_DATA);
        Map<String, Object> moduleData = mGson.fromJson(moduleDataJson, Map.class);

        mModuleDataTable.put(clientId, moduleData);
    }

    private void receiveRequest2Server(Message message) {
        Bundle bundle = message.getData();

        String clientId = bundle.getString(MessageKeyword.KEY_CLIENT_ID);
        String moduleName = bundle.getString(MessageKeyword.KEY_MODULE_NAME);
        String methodName = bundle.getString(MessageKeyword.KEY_METHOD_NAME);
        String callbackId = bundle.getString(MessageKeyword.KEY_CALLBACK_ID);

        String argumentsJson = bundle.getString(MessageKeyword.KEY_ARGUMENTS_NAME);
        List<Object> arguments = mGson.fromJson(argumentsJson, List.class);

        request2Client(clientId, moduleName, methodName, arguments);
    }

    private void receiveResponse2Server(Message message) {
        Bundle bundle = message.getData();

        String clientId = bundle.getString(MessageKeyword.KEY_CLIENT_ID);
        String callbackId = bundle.getString(MessageKeyword.KEY_CALLBACK_ID);
        
        String responseJson = bundle.getString(MessageKeyword.KEY_RESPONSE_DATA);
        List<Object> response = mGson.fromJson(responseJson, List.class);
        
        response2Client(clientId, callbackId, response);
    }

    private void receiveUnregisterClient(Message message) {
        Bundle bundle = message.getData();
        String clientId = bundle.getString(MessageKeyword.KEY_CLIENT_ID);
        mClientMessengers.remove(clientId);
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
                case MessageNumber.EXPORT_MODULES: {
                    receiveExportModules(msg);
                } break;
                case MessageNumber.REQUEST_TO_SERVER: {
                    receiveRequest2Server(msg);
                } break;
                case MessageNumber.RESPONSE_TO_SERVER: {
                    receiveResponse2Server(msg);
                } break;
                case MessageNumber.UNREGISTER_CLIENT: {
                    receiveUnregisterClient(msg);
                } break;
                default: {
                    MixLogger.error("Unsupport message number = " + msg.what);
                } break;
            }
        }
    }

    // TODO: 请求信息检查，如果目标客户端提供信息无法匹配，则本次 request 无效，直接在 server 进行驳回

}
