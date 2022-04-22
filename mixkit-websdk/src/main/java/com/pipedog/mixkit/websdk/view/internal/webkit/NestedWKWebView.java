package com.pipedog.mixkit.websdk.view.internal.webkit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pipedog.mixkit.web.view.MixWKWebView;
import com.pipedog.mixkit.websdk.config.IWebConfiguration;

/**
 * 基于系统 webkit 内核的包装的业务定制 web 容器，继承自 WKWebView，并解决了视图嵌套导致的手势冲突问题
 */
public class NestedWKWebView extends WKWebView {

    public NestedWKWebView(@NonNull Context context) {
        super(context);
    }

    public NestedWKWebView(@NonNull Context context, @Nullable IWebConfiguration configuration) {
        super(context, configuration);
    }

    public NestedWKWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected MixWKWebView getWebView() {
        MixWKWebView webView = new NestedMixWKWebView(getContext());
        webView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));

        webView.setWebContentsDebuggingEnabled(true);
        return webView;
    }

}
