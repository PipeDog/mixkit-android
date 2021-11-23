package com.pipedog.mixkit.example;

import android.content.Context;
import android.app.Application;

import com.pipedog.mixkit.kernel.MixResultCallback;
import com.pipedog.mixkit.messenger.MessengerEngine;

import java.util.HashMap;
import java.util.Map;

public class MixApplication extends Application {

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        String clientId = "";
        String moduleName = "";
        String methodName = "";
        Map parameter = new HashMap<>();

        MessengerEngine.getInstance().sendMessage(clientId,
                moduleName, methodName, parameter, new MixResultCallback() {
            @Override
            public void invoke(Object[] arguments) {
                
            }
        });

    }

}
