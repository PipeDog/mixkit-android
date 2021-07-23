package com.pipedog.mixkit.socket;

public interface IWebSocketDelegate {
    public void webSocketDidReceiveMessage(IWebSocket webSocket, Object message);
}
