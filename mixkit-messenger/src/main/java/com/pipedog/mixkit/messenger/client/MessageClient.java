package com.pipedog.mixkit.messenger.client;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.NonNull;

public class MessageClient {

    private Messenger mServerMessenger;
    private Messenger mClientMessenger = new Messenger(new ClientHandler());

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServerMessenger = new Messenger(service);

            // TODO: 绑定 client 及注入 modules 信息到服务端等
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            // TODO: 指令处理
        }
    }

}
