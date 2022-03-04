package com.pipedog.mixkit.websdk.interfaces.widget;

/**
 * web 加载状态视图接口定义
 */
public interface ILoadingView extends IWidget {

    /**
     * 开始加载
     */
    void startLoading();

    /**
     * 停止加载
     */
    void stopLoading();

    /**
     * 设置当前进度（取值范围：0~100）
     */
    default void setProgress(int progress) {

    };

    /**
     * 设置加载视图隐藏/展示（取值范围为 View.VISIBLE｜View.INVISIBLE｜View.GONE）
     */
    void setVisibility(int visibility);

}
