package com.pipedog.mixkit.messenger;

import android.content.Context;

import com.pipedog.mixkit.messenger.core.MessengerManager;

import java.util.List;

/**
 * 跨进程通信客户端引擎实现类
 * @author liang
 * @time 2021/11/23
 */
public class ClientEngine
        implements IClientEngine, IClientEngine.IInitialConfiguration {

    private Context mContext;
    private String mClientId;
    private String mAction;
    private String mPackage;

    private MessengerManager mMessengerManager = new MessengerManager();
    private volatile static IClientEngine sClientEngine;

    public static IClientEngine getInstance() {
        if (sClientEngine == null) {
            synchronized (ClientEngine.class) {
                if (sClientEngine == null) {
                    sClientEngine = new ClientEngine();
                }
            }
        }
        return sClientEngine;
    }

    private ClientEngine() {

    }

    
    // OVERRIDE METHODS FROM `IClientEngine`
    
    @Override
    public void setupConfiguration(IClientEngine.IConfigurationCallback callback) {
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


    // OVERRIDE METHODS FROM `IClientEngine.IInitialConfiguration`

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
