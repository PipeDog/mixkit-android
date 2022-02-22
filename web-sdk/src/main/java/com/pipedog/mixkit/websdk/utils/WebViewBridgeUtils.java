package com.pipedog.mixkit.websdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import com.pipedog.mixkit.kernel.IBridge;
import com.pipedog.mixkit.web.interfaces.IMixWebView;
import com.pipedog.mixkit.websdk.interfaces.IOpenWebView;
import com.pipedog.mixkit.web.kernel.WebViewBridge;
import com.pipedog.mixkit.websdk.view.WebViewActivity;

/**
 * webViewBridge 辅助工具
 * @author liang
 * @time 2022/01/10
 */
public class WebViewBridgeUtils {

    /**
     * 获取 bridge 对象绑定的 context 对象
     */
    public static Context getContext(IBridge bridge) {
        if (bridge == null || !(bridge instanceof WebViewBridge)) {
            return null;
        }

        IMixWebView webView = getWebView(bridge);
        if (webView == null) {
            return null;
        }

        Context context = ((View) webView).getContext();
        return context;
    }

    /**
     * 获取 bridge 对象绑定的 IMixWebView 视图，类型可能为：
     * MixWKWebView | NestedMixWKWebView
     */
    public static IMixWebView getWebView(IBridge bridge) {
        if (bridge == null || !(bridge instanceof WebViewBridge)) {
            return null;
        }

        WebViewBridge webViewBridge = (WebViewBridge) bridge;
        IMixWebView webView = (IMixWebView) webViewBridge.bridgeDelegate();
        if (webView == null || !(webView instanceof View)) {
            return null;
        }

        return webView;
    }

    /**
     * 获取 bridge 对象绑定的 IOpenWebView 视图，类型为 OpenWebView
     */
    public static IOpenWebView getOpenWebView(IBridge bridge) {
        if (bridge == null || !(bridge instanceof WebViewBridge)) {
            return null;
        }

        IMixWebView webView = getWebView(bridge);
        if (webView == null) { return null; }

        try {
            IOpenWebView openWebView = (IOpenWebView) ((View) webView)
                    .getParent().getParent().getParent();
            return openWebView;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 bridge 对象绑定的 WebViewActivity
     */
    public static WebViewActivity getWebViewActivity(IBridge bridge) {
        Activity activity = getActivity(bridge);
        if (activity == null) {
            return null;
        }
        if (!(activity instanceof WebViewActivity)) {
            return null;
        }
        return (WebViewActivity) activity;
    }

    /**
     * 获取 bridge 对象绑定的 Activity
     */
    public static Activity getActivity(IBridge bridge) {
        Context context = getContext(bridge);
        if (context == null) {
            return null;
        }

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        return null;
    }

}
