package com.pipedog.mixkit.messenger.server;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.NonNull;

public class MessengerDispatcher {

    private Messenger mServerMessenger = new Messenger(new ServerHandler());

    public Messenger getMessenger() {
        return mServerMessenger;
    }

    private class ServerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    }

}
