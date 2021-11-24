package com.pipedog.mixkit.example;

import android.content.Context;
import android.app.Application;

import com.pipedog.mixkit.kernel.MixResultCallback;
import com.pipedog.mixkit.messenger.IMessengerEngine;
import com.pipedog.mixkit.messenger.MessengerEngine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MixApplication extends Application {

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
                configuration.setClientId("com.client.targetApp");

                // action 及 package 需要在服务端进程的 AndroidManifest.xml 中配置
                configuration.setAction("com.pipedog.testService");
                configuration.setPackage("com.pipedog.mixkit.example");
            }
        });

        // 2、启动引擎
        engine.start();
    }

}
