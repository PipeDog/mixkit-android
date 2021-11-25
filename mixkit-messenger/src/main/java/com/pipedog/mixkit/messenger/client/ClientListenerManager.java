package com.pipedog.mixkit.messenger.client;

import com.pipedog.mixkit.messenger.interfaces.IClientListener;
import com.pipedog.mixkit.messenger.interfaces.IClientListenerManager;
import com.pipedog.mixkit.messenger.model.ErrorMessage;
import com.pipedog.mixkit.messenger.model.RequestMessage;
import com.pipedog.mixkit.messenger.model.ResponseMessage;

import java.util.HashSet;
import java.util.Set;

public class ClientListenerManager implements IClientListenerManager {

    private Set<IClientListener> mClientListeners = new HashSet<>();
    private volatile static ClientListenerManager sClientListenerManager;

    public static ClientListenerManager getInstance() {
        if (sClientListenerManager == null) {
            synchronized (ClientListenerManager.class) {
                sClientListenerManager = new ClientListenerManager();
            }
        }
        return sClientListenerManager;
    }

    private ClientListenerManager() {

    }

    public void bindListener(IClientListener listener) {
        if (listener == null) {
            return;
        }

        mClientListeners.add(listener);
    }

    public void unbindListener(IClientListener listener) {
        if (listener == null) {
            return;
        }

        mClientListeners.remove(listener);
    }

    public void removeAllListeners() {
        mClientListeners.clear();
    }


    // OVERRIDE METHODS FROM `IClientListenerManager`

    @Override
    public void didSendRequestMessage(RequestMessage requestMessage) {
        for (IClientListener listener : mClientListeners) {
            listener.didSendRequestMessage(requestMessage);
        }
    }

    @Override
    public void didReceiveResponseMessage(ResponseMessage responseMessage) {
        for (IClientListener listener : mClientListeners) {
            listener.didReceiveResponseMessage(responseMessage);
        }
    }

    @Override
    public void didReceiveRequestMessage(RequestMessage requestMessage) {
        for (IClientListener listener : mClientListeners) {
            listener.didReceiveRequestMessage(requestMessage);
        }
    }

    @Override
    public void didSendResponseMessage(ResponseMessage responseMessage) {
        for (IClientListener listener : mClientListeners) {
            listener.didSendResponseMessage(responseMessage);
        }
    }

    @Override
    public void didReceiveErrorMessage(ErrorMessage errorMessage) {
        for (IClientListener listener : mClientListeners) {
            listener.didReceiveErrorMessage(errorMessage);
        }
    }

}
