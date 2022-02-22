package com.pipedog.mixkit.websdk.interfaces;

import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.List;

/**
 * 导航栏接口定义
 */
public interface ITitleBar {

    /**
     * 隐藏或展示返回按钮
     * @param visibility 取值范围为 （View.VISIBLE｜View.INVISIBLE｜View.GONE）
     */
    void setBackButtonVisibility(int visibility);

    /**
     * 隐藏或展示返回按钮旁的竖线
     * @param visibility 取值范围为 （View.VISIBLE｜View.INVISIBLE｜View.GONE）
     */
    void setVerticalLineVisibility(int visibility);

    /**
     * 设置标题
     */
    void setTitle(String title);

    /**
     * 设置 titleBar 背景
     */
    void setBackground(Drawable drawable);

    /**
     * 设置 titleBar 背景颜色
     */
    void setBackgroundColor(int color);

    /**
     * 设置 titleBar 右侧按钮
     */
    void setRightItems(List<ITitleBarItem> items);

    /**
     * 监听返回按钮点击事件
     */
    void setBackButtonOnClickListener(View.OnClickListener listener);

}
