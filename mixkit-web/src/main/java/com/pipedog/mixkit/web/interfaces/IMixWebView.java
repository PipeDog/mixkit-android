package com.pipedog.mixkit.web.interfaces;

public interface IMixWebView extends IScriptEngine, IWebViewBridgeDelegate {
    void setBridgeListener(IWebViewBridgeListener listener);
    IWebViewBridgeListener getBridgeListener();
}
