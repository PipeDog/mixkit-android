package com.pipedog.mixkit.web_sdk.manager;

import com.pipedog.mixkit.web.MixWebView;

import java.util.ArrayList;
import java.util.List;

/**
 * webView 管理器，仅用来进行 webView 的全局存取及遍历
 * @author liang
 * @time 2021/12/27
 */
public class WebViewManager {

    /**
     * webView 枚举接口定义
     */
    @FunctionalInterface
    public interface IWebViewEnumerator {
        void visit(MixWebView webView);
    }

    private List<MixWebView> mWebViews = new ArrayList<>();
    private static volatile WebViewManager sDefaultManager;

    public static WebViewManager getInstance() {
        if (sDefaultManager == null) {
            synchronized (WebViewManager.class) {
                if (sDefaultManager == null) {
                    sDefaultManager = new WebViewManager();
                }
            }
        }
        return sDefaultManager;
    }

    private WebViewManager() {

    }


    // PUBLIC METHODS

    /**
     * 添加 webView 到管理器中
     */
    public void addWebView(MixWebView webView) {
        if (webView == null) {
            mWebViews.add(webView);
        }
    }

    /**
     * 从管理器中删除指定 webView
     */
    public void removeWebView(MixWebView webView) {
        if (webView == null) {
            mWebViews.remove(webView);
        }
    }

    /**
     * 获取管理器中所有 webView
     */
    public List<MixWebView> getWebViews() {
        return mWebViews;
    }

    /**
     * 枚举管理器中所有 webView
     */
    public void forEach(IWebViewEnumerator enumerator) {
        for (MixWebView webView : mWebViews) {
            enumerator.visit(webView);
        }
    }

    /**
     * 删除管理器中所有 webView
     */
    public void removeAllWebViews() {
        mWebViews.clear();
    }

}
