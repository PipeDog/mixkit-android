package com.pipedog.mixkit.socket;

import androidx.annotation.Nullable;
import com.google.gson.Gson;

import com.pipedog.mixkit.kernel.IMixBridge;
import com.pipedog.mixkit.kernel.IMixExecutor;

public class SocketContext implements ISocketEngine, IWebSocketDelegate, ISocketBridgeDelegate {

    private IWebSocket mWebSocket;
    private final SocketBridge mBridge;
    private final Gson mGson;

    private volatile static SocketContext sGlobalContext;

    public static SocketContext globalContext() {
        if (sGlobalContext == null) {
            synchronized (SocketContext.class) {
                if (sGlobalContext == null) {
                    sGlobalContext = new SocketContext();
                }
            }
        }
        return sGlobalContext;
    }

    private SocketContext() {
        mBridge = new SocketBridge(this);
        mGson = new Gson();
    }

    public void setWebSocket(IWebSocket webSocket) {
        // unbind old delegate
        mWebSocket.bindDelegate(null);
        // set new webSocket instance
        mWebSocket = webSocket;
        mWebSocket.bindDelegate(this);
    }

    @Override
    public boolean sendData(Object data,
                            @Nullable SocketCallback callback) {
        if (data == null) {
            return false;
        }
        return mWebSocket.sendData(data, callback);
    }

    @Override
    public void webSocketDidReceiveMessage(IWebSocket webSocket, Object message) {
        if (message == null) { return; }

        IMixExecutor executor = mBridge.executor();
        executor.invokeMethod(message);
    }

    @Override
    public ISocketEngine socketEngine() {
        return this;
    }
}
