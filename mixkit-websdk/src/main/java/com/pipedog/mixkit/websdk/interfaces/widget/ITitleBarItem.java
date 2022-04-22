package com.pipedog.mixkit.websdk.interfaces.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * 导航栏按钮配置定义
 */
public interface ITitleBarItem {

    /**
     * item 唯一标识，用来区分按钮
     */
    String getItemId();

    /**
     * 按钮文案
     */
    String getText();

    /**
     * 按钮 icon
     */
    Drawable getDrawable();

    /**
     * 按钮点击回调
     */
    View.OnClickListener getListener();

}
