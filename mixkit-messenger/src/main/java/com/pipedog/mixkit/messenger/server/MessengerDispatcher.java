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
import com.pipedog.mixkit.messenger.interfaces.IMessageCallback;
import com.pipedog.mixkit.tool.MixLogger;

import java.lang.reflect.Type;
import java.util.HashMap;
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
    public void request2Client(String processId,
                               String moduleName,
                               String methodName,
                               Map<String, Object> parameter,
                               String callbackId) {
        Messenger clientMessenger = mClientMessengers.get(processId);
        if (clientMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(MessageKeyword.KEY_MODULE_NAME, moduleName);
        bundle.putString(MessageKeyword.KEY_METHOD_NAME, methodName);
        bundle.putString(MessageKeyword.KEY_CALLBACK_ID, callbackId);

        String json = mGson.toJson(parameter);
        bundle.putString(MessageKeyword.KEY_PARAMETER_NAME, json);

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
    public void response2Client(String processId,
                                String callbackId,
                                Map<String, Object> result) {
        Messenger clientMessenger = mClientMessengers.get(processId);
        if (clientMessenger == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(MessageKeyword.KEY_CALLBACK_ID, callbackId);

        String json = mGson.toJson(result);
        bundle.putString(MessageKeyword.KEY_RESPONSE_DATA, json);

        Message message = Message.obtain();
        message.setData(bundle);
        message.what = MessageNumber.RESPONSE_TO_CLIENT;

        try {
            clientMessenger.send(message);
        } catch (Exception e) {
            MixLogger.error(e.toString());
        }
    }


    // INTERNAL METHODS

    private void registerClient(Message message) {
        // Get key
        Bundle bundle = message.getData();
        String processId = bundle.getString(MessageKeyword.KEY_PROCESS_ID);

        // Get value then register into map
        Messenger clientMessenger = message.replyTo;
        mClientMessengers.put(processId, clientMessenger);
    }

    private void exportModules(Message message) {
        // Get key
        Bundle bundle = message.getData();
        String processId = bundle.getString(MessageKeyword.KEY_PROCESS_ID);

        // Get value
        String json = bundle.getString(MessageKeyword.KEY_MODULE_DATA);
        Map<String, Object> moduleData = mGson.fromJson(json, Map.class);

        mModuleDataTable.put(processId, moduleData);
    }

    private void request2Server(Message message) {
        Bundle bundle = message.getData();

        String processId = bundle.getString(MessageKeyword.KEY_PROCESS_ID);
        String moduleName = bundle.getString(MessageKeyword.KEY_MODULE_NAME);
        String methodName = bundle.getString(MessageKeyword.KEY_METHOD_NAME);
        String callbackId = bundle.getString(MessageKeyword.KEY_CALLBACK_ID);

        String json = bundle.getString(MessageKeyword.KEY_PARAMETER_NAME);
        Map<String, Object> parameter = mGson.fromJson(json, Map.class);

        request2Client(processId, moduleName, methodName, parameter, callbackId);
    }

    private void response2Server(Message message) {
        Bundle bundle = message.getData();

        String processId = bundle.getString(MessageKeyword.KEY_PROCESS_ID);
        String callbackId = bundle.getString(MessageKeyword.KEY_CALLBACK_ID);
        
        String json = bundle.getString(MessageKeyword.KEY_RESPONSE_DATA);
        Map result = mGson.fromJson(json, Map.class);
        
        response2Client(processId, callbackId, result);
    }

    private void unregisterClient(Message message) {
        Bundle bundle = message.getData();
        String processId = bundle.getString(MessageKeyword.KEY_PROCESS_ID);
        mClientMessengers.remove(processId);
    }


    // INTERNAL CLASSES

    private class ServerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MessageNumber.REGISTER_CLIENT: {
                    registerClient(msg);
                } break;
                case MessageNumber.EXPORT_MODULES: {
                    exportModules(msg);
                } break;
                case MessageNumber.REQUEST_TO_SERVER: {
                    request2Server(msg);
                } break;
                case MessageNumber.RESPONSE_TO_SERVER: {
                    response2Server(msg);
                } break;
                case MessageNumber.UNREGISTER_CLIENT: {
                    unregisterClient(msg);
                } break;
                default: {
                    MixLogger.error("Unsupport message number = " + msg.what);
                } break;
            }
        }
    }

}
