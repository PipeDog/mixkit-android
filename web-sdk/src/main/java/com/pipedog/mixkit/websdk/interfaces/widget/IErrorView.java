package com.pipedog.mixkit.websdk.interfaces.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.pipedog.mixkit.websdk.interfaces.layout.ILayoutParams;

/**
 * 错误视图接口定义
 */
public interface IErrorView extends ILayoutParams {

    /**
     * 设置错误图片
     */
    void setImage(Drawable drawable);

    /**
     * 设置错误提示文案
     */
    void setText(String text);

    /**
     * 设置重试按钮点击监听
     */
    void setRetryButtonListener(View.OnClickListener listener);

}
