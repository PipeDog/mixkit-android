package com.pipedog.mixkit.example;

import android.content.Context;
import android.app.Application;

import com.pipedog.mixkit.kernel.MixResultCallback;
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

        String clientId = "";
        String moduleName = "";
        String methodName = "";

        // Arrays.asList("foo","bar");
        MessengerEngine.getInstance().sendMessage(clientId, moduleName,
                methodName, Arrays.asList("argument 1", "argument 2", new MixResultCallback() {
            @Override
            public void invoke(Object[] arguments) {

            }
        }));

    }

}
