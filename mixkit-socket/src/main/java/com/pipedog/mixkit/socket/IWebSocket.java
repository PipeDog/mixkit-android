package com.pipedog.mixkit.socket;

import androidx.annotation.Nullable;

public interface IWebSocket {
    public void bindDelegate(@Nullable IWebSocketDelegate delegate);
    public void sendData(Object data, @Nullable SocketCallback callback);
}
