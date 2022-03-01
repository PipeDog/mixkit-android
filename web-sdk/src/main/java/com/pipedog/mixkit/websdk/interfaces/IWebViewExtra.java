package com.pipedog.mixkit.websdk.interfaces;

import com.pipedog.mixkit.websdk.constants.WebStyle;

/**
 * WebView 扩展接口，在这里定义一些定制化功能函数，如主题设置等
 * @author liang
 * @time 2022/02/15
 */
public interface IWebViewExtra {

    /**
     * 设置 UI 主题
     */
    void setTheme(@WebStyle.WebTheme int theme);

    /**
     * 设置是否需要进度条
     */
    void setShowProgress(boolean show);

    /**
     * 设置是否监听生命周期（如果监听，则 js 侧会收到相应生命周期的通知）
     */
    void setObserveLifecycle(boolean observe);

}
