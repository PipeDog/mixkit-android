package com.pipedog.mixkit.messenger;

import android.content.Context;

import com.pipedog.mixkit.kernel.MixResultCallback;
import com.pipedog.mixkit.messenger.core.MessengerManager;

import java.util.List;
import java.util.Map;

/**
 * 跨进程通信引擎实现类
 * @author liang
 * @time 2021/11/23
 */
public class MessengerEngine
        implements IMessengerEngine, IMessengerEngine.IInitialConfiguration {

    private Context mContext;
    private String mClientId;
    private String mAction;
    private String mPackage;

    private MessengerManager mMessengerManager = new MessengerManager();
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

    }

    
    // OVERRIDE METHODS FROM `IMessengerEngine`
    
    @Override
    public void setupConfiguration(IMessengerEngine.IConfigurationCallback callback) {
        callback.setup(this);
    }

    @Override
    public boolean start() {
        return mMessengerManager.startConnection();
    }

    @Override
    public boolean restart() {
        return mMessengerManager.startConnection();
    }

    @Override
    public void shutdown() {
        mMessengerManager.stopConnection();
    }

    @Override
    public String sendMessage(String clientId,
                              String moduleName,
                              String methodName,
                              List<Object> arguments) {
        return mMessengerManager.sendMessage(clientId, moduleName, methodName, arguments);
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
