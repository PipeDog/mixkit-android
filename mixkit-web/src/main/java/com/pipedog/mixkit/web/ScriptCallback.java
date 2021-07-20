package com.pipedog.mixkit.web;

public interface ScriptCallback {
    public void onReceiveValue(String value);
    public void onReceiveError(String error);
}
