package com.pipedog.mixkit.websdk.interfaces.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * 错误视图接口定义
 */
public interface IErrorView extends IWidget {

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

    /**
     * 设置错误视图隐藏/展示（取值范围为 View.VISIBLE｜View.INVISIBLE｜View.GONE）
     */
    void setVisibility(int visibility);

}
