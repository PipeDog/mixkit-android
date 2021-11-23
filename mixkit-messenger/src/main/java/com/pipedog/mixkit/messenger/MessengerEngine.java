package com.pipedog.mixkit.messenger;

import android.content.Context;

import com.pipedog.mixkit.kernel.MixResultCallback;
import com.pipedog.mixkit.messenger.core.MessengerManager;

import java.util.Map;

public class MessengerEngine implements IMessengerEngine, IMessengerEngine.IInitialConfiguration {

    private Context mContext;
    private String mClientId;
    private String mAction;
    private String mPackage;
    private MessengerManager mMessengerManager;
    private volatile static IMessengerEngine sMessengerEngine;

    public static IMessengerEngine getInstance() {
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

    
    // OVERRIDE METHODS FROM `IMessengerEngine`
    
    @Override
    public void setupConfiguration(IMessengerEngine.IConfigurationCallback callback) {
        callback.setup(this);
    }

    @Override
    public boolean launch() {
        return mMessengerManager.startConnection();
    }

    @Override
    public void close() {
        mMessengerManager.stopConnection();
    }

    @Override
    public void sendMessage(String clientId,
                            String moduleName, 
                            String methodName,
                            Map<String, Object> parameter,
                            MixResultCallback callback) {
        mMessengerManager.sendMessage(clientId, moduleName, methodName, parameter, callback);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public String getClientId() {
        return mClientId;
    }

    @Override
    public String getAction() {
        return mAction;
    }

    @Override
    public String getPackage() {
        return mPackage;
    }


    // OVERRIDE METHODS FROM `IMessengerEngine.IInitialConfiguration`

    @Override
    public void setContext(Context context) {
        this.mContext = context;
    }

    @Override
    public void setClientId(String clientId) {
        this.mClientId = clientId;
    }

    @Override
    public void setAction(String action) {
        this.mAction = action;
    }

    @Override
    public void setPackage(String packageName) {
        this.mPackage = packageName;
    }
    
}
