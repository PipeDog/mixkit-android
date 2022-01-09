package com.pipedog.mixkit.messenger.interfaces;

import com.pipedog.mixkit.messenger.interfaces.IClientListener;

public interface IClientListenerManager extends IClientListener {
    void bindListener(IClientListener listener);
    void unbindListener(IClientListener listener);
    void removeAllListeners();
}
