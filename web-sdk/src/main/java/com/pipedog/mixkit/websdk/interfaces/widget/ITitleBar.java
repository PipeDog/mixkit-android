package com.pipedog.mixkit.websdk.interfaces.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.List;

/**
 * 导航栏接口定义
 */
public interface ITitleBar extends IWidget {

    /**
     * 设置标题
     */
    void setTitle(String title);

    /**
     * 隐藏/展示关闭按钮
     */
    void setCloseButtonVisibility(boolean visibility);

    /**
     * 设置 titleBar 背景
     */
    void setBackground(Drawable drawable);

    /**
     * 设置 titleBar 背景颜色
     */
    void setBackgroundColor(int color);

    /**
     * 重新设置 titleBar 所有右侧按钮
     */
    void setRightItems(List<ITitleBarItem> items);

    /**
     * 获取 titleBar 右侧按钮集合
     */
    List<ITitleBarItem> getRightItems();

    /**
     * 监听返回按钮点击事件
     */
    void setGoBackButtonListener(View.OnClickListener listener);

    /**
     * 监听关闭按钮点击事件
     */
    void setCloseButtonListener(View.OnClickListener listener);

}
