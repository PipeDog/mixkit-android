package com.pipedog.mixkit.websdk.interfaces.widget;

import android.view.ViewGroup;

import com.pipedog.mixkit.websdk.constants.WebStyle;

public interface IWidget {

    /**
     * 获取组件宽度（单位：dp）
     */
    int getWidgetWidth();

    /**
     * 获取组件高度（单位：dp）
     */
    int getWidgetHeight();

    /**
     * 设置 UI 主题
     */
    default void setTheme(@WebStyle.WebTheme int theme) {

    }

}
