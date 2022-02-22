package com.pipedog.mixkit.websdk.interfaces;

/**
 * web 控制器接口
 */
public interface IWebViewActivity extends IOpenWebView {

    /**
     * 获取导航栏
     */
    ITitleBar getTitleBar();

    /**
     * 获取 webView
     */
    IOpenWebView getWebView();

}
