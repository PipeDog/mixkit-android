package com.pipedog.messenger.executorclient;

import android.app.Activity;
import android.os.Bundle;

import com.pipedog.mixkit.messenger.IClientEngine;
import com.pipedog.mixkit.messenger.ClientEngine;
import com.pipedog.mixkit.messenger.manager.ClientListenerManager;
import com.pipedog.mixkit.messenger.interfaces.IClientListener;

public class ClientActivity extends Activity implements IClientListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        launchMessengerEngine();
    }

    private void launchMessengerEngine() {
        IClientEngine engine = ClientEngine.getInstance();

        // 1、初始化配置
        engine.setupConfiguration(new IClientEngine.IConfigurationCallback() {
            @Override
            public void setup(IClientEngine.IInitialConfiguration configuration) {
                configuration.setContext(getApplicationContext());
                configuration.setClientId("com.client.executorApp");

                // action 及 package 需要在服务端进程的 AndroidManifest.xml 中配置
                configuration.setAction("com.pipedog.testService");
                configuration.setPackage("com.pipedog.mixkit.example");
            }
        });

        // 2、启动引擎
        engine.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ClientEngine.getInstance().restart();
        ClientListenerManager.getInstance().bindListener(this);
    }

}