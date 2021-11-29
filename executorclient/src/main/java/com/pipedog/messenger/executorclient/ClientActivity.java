package com.pipedog.messenger.executorclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pipedog.mixkit.messenger.IMessengerEngine;
import com.pipedog.mixkit.messenger.MessengerEngine;
import com.pipedog.mixkit.messenger.manager.ClientListenerManager;
import com.pipedog.mixkit.messenger.interfaces.IClientListener;

public class ClientActivity extends AppCompatActivity implements IClientListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        launchMessengerEngine();
    }

    private void launchMessengerEngine() {
        IMessengerEngine engine = MessengerEngine.getInstance();

        // 1、初始化配置
        engine.setupConfiguration(new IMessengerEngine.IConfigurationCallback() {
            @Override
            public void setup(IMessengerEngine.IInitialConfiguration configuration) {
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

        MessengerEngine.getInstance().restart();
        ClientListenerManager.getInstance().bindListener(this);
    }

}