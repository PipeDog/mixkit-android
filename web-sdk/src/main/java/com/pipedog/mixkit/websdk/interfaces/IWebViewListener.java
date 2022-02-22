package com.pipedog.mixkit.websdk.interfaces;

import android.graphics.Bitmap;

/**
 * WebView 事件监听
 */
public interface IWebViewListener {

    /**
     * web 加载进度更新
     */
    default void onProgressChanged(IWebView view, int newProgress) {}

    /**
     * 接收到页面 title
     */
    default void onReceivedTitle(IWebView view, String title) {}

    /**
     * 是否应该覆盖将要加载 url 的动作（比如：拦截 url 进行短信发送，打电话等操作）
     * @return 返回 false 则 webView 继续加载该 url，返回 true 则表示
     *          该 url 被上层拦截并处理，webView 不再继续加载该 url
     */
    default boolean shouldOverrideUrlLoading(IWebView view, String url) {
        return false;
    }

    /**
     * 页面已经开始加载
     */
    default void onPageStarted(IWebView view, String url) {}

    /**
     * 页面加载完成
     */
    default void onPageFinished(IWebView view, String url) {}

    /**
     * 接收到错误
     */
    default void onReceivedError(IWebView view, int errorCode, String errorMessage) {}

}
