package com.pipedog.messenger.clientdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pipedog.mixkit.kernel.MixResultCallback;
import com.pipedog.mixkit.messenger.IMessengerEngine;
import com.pipedog.mixkit.messenger.MessengerEngine;
import com.pipedog.mixkit.messenger.manager.ClientListenerManager;
import com.pipedog.mixkit.messenger.interfaces.IClientListener;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SendMsgClientActivity extends AppCompatActivity implements IClientListener {

    private Button mButton;
    private Button mExeButton;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        launchMessengerEngine();
        getViews();
        addEvents();
    }

    private void launchMessengerEngine() {

        IMessengerEngine engine = MessengerEngine.getInstance();

        // 1、初始化配置
        engine.setupConfiguration(new IMessengerEngine.IConfigurationCallback() {
            @Override
            public void setup(IMessengerEngine.IInitialConfiguration configuration) {
                configuration.setContext(getApplicationContext());
                configuration.setClientId("com.client.sourceApp");

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

    private void getViews() {
        mButton = findViewById(R.id.btn_send_msg);
        mExeButton = findViewById(R.id.btn_send_exe);
        mTextView = findViewById(R.id.tv_rcv_resp);
    }

    private void addEvents() {
        mButton.setOnClickListener(new OnClickHandler());
        mExeButton.setOnClickListener(new OnClickHandler());
    }

    private class OnClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mTextView.setText("Wait for response!");

            switch (v.getId()) {
                case R.id.btn_send_msg: {
                    String clientId = "com.client.mainApp";
                    String moduleName = "MessengerTestModule";
                    sendMessage(clientId, moduleName);
                } break;
                case R.id.btn_send_exe: {
                    String clientId = "com.client.executorApp";
                    String moduleName = "DemoModule";
                    sendMessage(clientId, moduleName);
                } break;
                default: break;
            }
        }
    }

    private void sendMessage(String clientId, String moduleName) {
        String methodName = "testMethod";

        Map<String, Object> m = Map.of("key1", "value1");
        List<Object> list = Arrays.asList("ele1", "ele2", "ele3");

        MessengerEngine.getInstance().sendMessage(clientId, moduleName, methodName,
                Arrays.asList("testStr", 111, 222, new MixResultCallback() {
                    @Override
                    public void invoke(Object[] response) {
                        MixLogger.info(response.toString());

                        Object code = response[0];
                        String msg = (String) response[1];
                        Map<String, Object> m = (Map<String, Object>) response[2];

                        StringBuilder builder = new StringBuilder();
                        builder.append("code = " + code.toString() + ", \n");
                        builder.append("msg = " + msg + ", \n");
                        builder.append("map = " + m.toString());

                        mTextView.setText(builder.toString());
                    }
                }, m, list));
    }

}