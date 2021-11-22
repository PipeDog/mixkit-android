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

    private Context mContext;
    private Messenger mServerMessenger;
    private Messenger mClientMessenger = new Messenger(new ClientHandler());
    private ServiceConnection mServiceConnection = new ProcessesConnection();
    private boolean mIsConnected = false;

    private MessageClient() {
        // Use `MessageClient(Context)` instead of current method
    }

    public MessageClient(Context context) {
        this.mContext = context;
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


    // INTERNAL CLASSES

    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            // TODO: 指令处理
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
