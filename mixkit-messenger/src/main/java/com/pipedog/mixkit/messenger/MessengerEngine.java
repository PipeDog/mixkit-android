package com.pipedog.mixkit.messenger;

import com.pipedog.mixkit.kernel.MixResultCallback;
import com.pipedog.mixkit.messenger.core.MessengerManager;

import java.util.Map;

public class MessengerEngine {
    
    private MessengerManager mMessengerManager;
    private volatile static MessengerEngine sMessengerEngine;

    public static MessengerEngine getInstance() {
        if (sMessengerEngine == null) {
            synchronized (MessengerEngine.class) {
                if (sMessengerEngine == null) {
                    sMessengerEngine = new MessengerEngine();
                }
            }
        }
        return sMessengerEngine;
    }

    private MessengerEngine() {
        mMessengerManager = new MessengerManager();
    }

    public boolean launch() {
        return mMessengerManager.startConnection();
    }

    public void close() {
        mMessengerManager.stopConnection();
    }

    public void sendMessage(String processId,
                            String moduleName,
                            String methodName,
                            Map<String, Object> parameter,
                            MixResultCallback callback) {
        mMessengerManager.sendMessage(processId, moduleName, methodName, parameter, callback);
    }

}
