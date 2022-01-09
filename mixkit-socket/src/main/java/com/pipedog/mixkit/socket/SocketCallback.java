package com.pipedog.mixkit.socket;

public interface SocketCallback<T> {
    public void onReceiveFailure(T error);
}
