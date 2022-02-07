package com.pipedog.mixkit.example;

import com.pipedog.mixkit.kernel.IBridge;
import com.pipedog.mixkit.kernel.IBridgeModule;
import com.pipedog.mixkit.tool.MixLogger;

import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.web.MixWKWebView;
import com.pipedog.mixkit.web.WebViewBridge;

@MixModule(name = "LogManager")
public class MixLogManager implements IBridgeModule {

    @MixMethod(name = "log")
    public void log(String message) {
        MixLogger.info("Log from H5, msg = %s", message);
    }

    @Override
    public void setBridge(IBridge bridge) {
        WebViewBridge webViewBridge = (WebViewBridge)bridge;
        MixWKWebView webView = (MixWKWebView)webViewBridge.bridgeDelegate();
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }
}
