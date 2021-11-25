package com.pipedog.mixkit.messenger.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * 跨进程通信服务
 * @author liang
 * @time 2021/11/23
 */
public class MessengerService extends Service {

    private MessengerDispatcher mMessengerDispatcher = new MessengerDispatcher();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessengerDispatcher.getMessenger().getBinder();
    }
    
}
