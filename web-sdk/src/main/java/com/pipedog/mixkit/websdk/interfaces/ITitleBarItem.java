package com.pipedog.mixkit.websdk.interfaces;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * 导航栏按钮配置定义
 */
public interface ITitleBarItem {

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
