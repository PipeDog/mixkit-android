package com.pipedog.mixkit.socket;

import androidx.annotation.Nullable;

public interface ISocketEngine {
    void sendData(Object data, @Nullable SocketCallback callback);
}
