package com.pipedog.mixkit.websdk.interfaces.widget;

import android.view.ViewGroup;

import com.pipedog.mixkit.websdk.constants.WebStyle;

public interface IWidget {

    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    int getWidth();
    int getHeight();

    default void setTheme(@WebStyle.WebTheme int theme) {

    }

}
