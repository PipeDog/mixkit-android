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

        MessengerEngine engine = MessengerEngine.getInstance();

        // 1、初始化配置
        engine.setupConfiguration(new IMessengerEngine.IConfigurationCallback() {
            @Override
            public void setup(IMessengerEngine.IInitialConfiguration configuration) {
                configuration.setContext(getApplicationContext());
                configuration.setClientId("com.client.mainapp");

                // action 及 package 需要在 AndroidManifest.xml 中配置
                configuration.setAction("");
                configuration.setPackage("");
            }
        });

        // 2、启动引擎
        engine.launch();

        // 3、调用其他进程的方法并获取回调
        String clientId = "";
        String moduleName = "";
        String methodName = "";

        engine.sendMessage(clientId, moduleName, methodName,
                Arrays.asList("argument 1", "argument 2", new MixResultCallback() {
            @Override
            public void invoke(Object[] arguments) {

            }
        }));
    }

}
