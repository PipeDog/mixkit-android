package com.pipedog.mixkit.websdk.interfaces.widget;

import com.pipedog.mixkit.websdk.interfaces.layout.ILayoutParams;

/**
 * web 加载状态视图接口定义
 */
public interface ILoadingView extends ILayoutParams {

    /**
     * 开始加载
     */
    void startLoading();

    /**
     * 停止加载
     */
    void stopLoading();

    /**
     * 设置当前进度
     */
    default void setProgress(int progress) {

    };

    /**
     * 设置加载视图隐藏/展示
     */
    void setVisibility(boolean visibility);

}
