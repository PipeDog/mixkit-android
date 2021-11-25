package com.pipedog.mixkit.messenger.server;

import com.pipedog.mixkit.messenger.interfaces.IServerListener;
import com.pipedog.mixkit.messenger.interfaces.IServerListenerManager;
import com.pipedog.mixkit.messenger.model.ErrorMessage;
import com.pipedog.mixkit.tool.MixLogger;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * 服务监听管理器
 * @author liang
 * @time 2021/11/25
 */
public class ServerListenerManager implements IServerListenerManager {

    private Set<IServerListener> mListeners = new HashSet<>();

    public ServerListenerManager() {
        autoRegisterServerListener();
    }

    private void autoRegisterServerListener() {
        // Call this function in constructor
        // The code will be inserted automatically during compilation
        // The insert code will call function `registerServerListener` here
    }

    private void registerServerListener(IServerListener listener) {
        if (listener == null) {
            return;
        }

        mListeners.add(listener);
    }


    // OVERRIDE METHODS FROM `IServerListenerManager`

    @Override
    public void didFailSendMessage2SourceClient(ErrorMessage errorMessage) {
        for (IServerListener listener : mListeners) {
            listener.didFailSendMessage2SourceClient(errorMessage);
        }
    }

    @Override
    public void didFailSendMessage2TargetClient(ErrorMessage errorMessage) {
        for (IServerListener listener : mListeners) {
            listener.didFailSendMessage2TargetClient(errorMessage);
        }
    }

}
