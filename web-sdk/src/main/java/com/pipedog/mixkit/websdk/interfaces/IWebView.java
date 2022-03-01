package com.pipedog.mixkit.websdk.interfaces;

import com.pipedog.mixkit.web.interfaces.IScriptEngine;

/**
 * WebView 协议抽象，业务层不要直接使用实现了此接口的类，请使用 IOpenWebView 接口的实现类
 */
public interface IWebView extends IWebViewExtra, IScriptEngine {

    // LISTENER CALLBACK

    /**
     * 设置 webView 回调监听
     */
    void setListener(IWebViewListener listener);


    // LIFECYCLE METHODS

    /**
     * webView 资源、引用清理，此方法应该在 webView 被销毁前调用
     * 在这里进行一些清理动作，如一些对 webView 的强引用应该在这里被注销掉
     * （这里最终会调用 WebView 的 destory() 方法）
     */
    void onDestruct();


    // LOAD PAGE METHODS

    /**
     * 加载给定的 url（可以是本地路径，也可以是远程路径）
     */
    void loadUrl(String url);

    /**
     * 使用加载数据的方式加载 web 页面，@eg:
     *  webView.loadData(htmlString, "text/html", "UTF-8")
     */
    void loadData(String data, String mimeType, String encoding);

    /**
     * 一般不会用到此方法，使用方式同 Android WebView
     */
    void loadDataWithBaseURL(String baseUrl, String data,
                             String mimeType, String encoding, String failUrl);

    /**
     * 终止当前正在进行的加载
     */
    void stopLoading();

    /**
     * 刷新当前页面
     */
    void reload();


    // PAGE ACTION METHODS

    /**
     * 是否可以回到上一（web）页
     */
    boolean canGoBack();

    /**
     * 回到上一（web）页
     */
    void goBack();

    /**
     * 是否可以前进到下一（web）页
     */
    boolean canGoForward();

    /**
     * 前进到下一（web）页
     */
    void goForward();

}
