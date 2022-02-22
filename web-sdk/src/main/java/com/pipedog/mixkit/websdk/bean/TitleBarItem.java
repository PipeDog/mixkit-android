package com.pipedog.mixkit.websdk.bean;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.pipedog.mixkit.websdk.interfaces.ITitleBarItem;

/**
 * 导航栏按钮实现
 */
public class TitleBarItem implements ITitleBarItem {

    private String mText;
    private Drawable mDrawable;
    private View.OnClickListener mListener;


    // CONSTRUCTORS

    public TitleBarItem(String text, View.OnClickListener listener) {
        mText = text;
        mListener = listener;
    }

    public TitleBarItem(Drawable drawable, View.OnClickListener listener) {
        mDrawable = drawable;
        mListener = listener;
    }


    // OVERRIDE METHODS FOR `ITitleBarItem`


    @Override
    public String getText() {
        return mText;
    }

    @Override
    public Drawable getDrawable() {
        return mDrawable;
    }

    @Override
    public View.OnClickListener getListener() {
        return mListener;
    }

}
