package com.pipedog.mixkit.socket;

public interface SocketCallback<T> {
    public void onReceiveSuccess(T value);
    public void onReceiveFailure(T error);
}
