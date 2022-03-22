package com.pipedog.mixkit.websdk.interfaces;

import com.pipedog.mixkit.web.interfaces.IMixWebView;
import com.pipedog.mixkit.websdk.constants.WebStyle;

/**
 * WebView 扩展接口，在这里定义一些定制化功能函数，如主题设置等
 * @author liang
 * @time 2022/02/15
 */
public interface IWebViewExtra {

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


    // SUPPORT SETTING

    /**
     * 设置 UI 主题
     */
    void setWebTheme(@WebStyle.WebTheme int theme);

    /**
     * 设置是否需要 loading 视图
     */
    void setShowLoading(boolean show);

    /**
     * 设置是否监听生命周期（如果监听，则 js 侧会收到相应生命周期的通知）
     */
    void setObserveLifecycle(boolean observe);


    // GETTER METHODS

    /**
     * 获取最内层的 webView 容器
     */
    IMixWebView getMixWebView();

}
