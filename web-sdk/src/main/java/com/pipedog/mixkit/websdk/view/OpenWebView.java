package com.pipedog.mixkit.websdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pipedog.mixkit.websdk.interfaces.IOpenWebView;

public class OpenWebView extends FrameLayout implements IOpenWebView {

    public OpenWebView(@NonNull Context context) {
        super(context);
    }

    public OpenWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

}
