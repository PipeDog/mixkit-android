package com.pipedog.mixkit.socket;

import androidx.annotation.Nullable;

public interface ISocketEngine {
    boolean sendData(Object data, @Nullable SocketCallback callback);
}
