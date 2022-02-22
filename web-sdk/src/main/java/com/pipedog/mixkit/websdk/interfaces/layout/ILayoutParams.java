package com.pipedog.mixkit.websdk.interfaces.layout;

import android.view.ViewGroup;

public interface ILayoutParams {
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    int getWidth();
    int getHeight();
}
