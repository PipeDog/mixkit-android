package com.pipedog.mixkit.example;

import android.content.Context;
import android.app.Application;

import com.pipedog.mixkit.messenger.IMessengerEngine;
import com.pipedog.mixkit.messenger.MessengerEngine;
import com.pipedog.mixkit.messenger.manager.ClientListenerManager;
import com.pipedog.mixkit.messenger.interfaces.IClientListener;

public class MixApplication extends Application implements IClientListener {

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        IMessengerEngine engine = MessengerEngine.getInstance();

        // 1、初始化配置
        engine.setupConfiguration(new IMessengerEngine.IConfigurationCallback() {
            @Override
            public void setup(IMessengerEngine.IInitialConfiguration configuration) {
                configuration.setContext(getApplicationContext());
                configuration.setClientId("com.client.mainApp");

                // action 及 package 需要在服务端进程的 AndroidManifest.xml 中配置
                configuration.setAction("com.pipedog.testService");
                configuration.setPackage("com.pipedog.mixkit.example");
            }
        });

        // 2、启动引擎
        engine.start();

        // 3、绑定监听
        ClientListenerManager.getInstance().bindListener(this);
    }

}
