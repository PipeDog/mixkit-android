package com.pipedog.mixkit.websdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pipedog.mixkit.web.interfaces.ScriptCallback;
import com.pipedog.mixkit.websdk.R;
import com.pipedog.mixkit.websdk.config.WebSDKConfiguration;
import com.pipedog.mixkit.websdk.constants.WebStyle;
import com.pipedog.mixkit.websdk.interfaces.IOpenWebView;
import com.pipedog.mixkit.websdk.interfaces.IWebView;
import com.pipedog.mixkit.websdk.interfaces.IWebViewListener;
import com.pipedog.mixkit.websdk.view.internal.webkit.WKWebView;

import java.util.Map;

public class OpenWebView extends FrameLayout implements IOpenWebView {

    private Map<String, Object> mExtraData;
    private int mTheme = WebStyle.WEB_THEME_LIGHT;
    private boolean mShowProgress = true;
    private boolean mObserveLifecycle = true;
    private WKWebView mWKWebView;


    // CONSTRUCTORS

    public OpenWebView(@NonNull Context context) {
        super(context);
    }

    public OpenWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MixWebView);
        mTheme = typedArray.getInt(R.styleable.MixWebView_mix_theme, WebStyle.WEB_THEME_LIGHT);
        mShowProgress = typedArray.getBoolean(R.styleable.MixWebView_mix_show_progress, true);
        mObserveLifecycle = typedArray.getBoolean(R.styleable.MixWebView_mix_observe_lifecycle, true);

        setupViews();
    }


    // PRIVATE METHODS

    private void setupViews() {
        int browserKernelType = WebSDKConfiguration.getInstance().getBrowserKernelType();

        // If you need to take control of the browser kernel, do the logic here.
        if (browserKernelType & KERNEL_TYPE_WEBKIT == KERNEL_TYPE_WEBKIT) {
            setupWKWebView();
            return;
        }

        throw new RuntimeException("[OpenWebView] Unsupport browser kernel type, check it!");
    }

    private void setupWKWebView() {
        mWKWebView = new WKWebView(getContext());
        mWKWebView.setTheme(mTheme);
        mWKWebView.setShowProgress(mShowProgress);
        mWKWebView.setObserveLifecycle(mObserveLifecycle);
        addView(mWKWebView);

        mWKWebView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
    }

    private IWebView getWebView() {
        if (mWKWebView != null) {
            return mWKWebView;
        }
        return null;
    }


    // OVERRIDE METHODS

    @Override
    public void setExtraData(Map<String, Object> extraData) {
        mExtraData = extraData;
    }

    @Override
    public Map<String, Object> getExtraData() {
        return mExtraData;
    }

    @Override
    public void setListener(IWebViewListener listener) {
        if (getWebView() == null) { return; }
        getWebView().setListener(listener);
    }

    @Override
    public void onDestruct() {
        if (getWebView() == null) { return; }
        getWebView().onDestruct();
    }

    @Override
    public void loadUrl(String url) {
        if (getWebView() == null) { return; }
        getWebView().loadUrl(url);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        if (getWebView() == null) { return; }
        getWebView().loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String failUrl) {
        if (getWebView() == null) { return; }
        getWebView().loadDataWithBaseURL(baseUrl, data, mimeType, encoding, failUrl);
    }

    @Override
    public void stopLoading() {
        if (getWebView() == null) { return; }
        getWebView().stopLoading();
    }

    @Override
    public void reload() {
        if (getWebView() == null) { return; }
        getWebView().reload();
    }

    @Override
    public boolean canGoBack() {
        if (getWebView() == null) { return false; }
        return getWebView().canGoBack();
    }

    @Override
    public void goBack() {
        if (getWebView() == null) { return; }
        getWebView().goBack();
    }

    @Override
    public boolean canGoForward() {
        if (getWebView() == null) { return false; }
        return getWebView().canGoForward();
    }

    @Override
    public void goForward() {
        if (getWebView() == null) { return; }
        getWebView().goForward();
    }

    @Override
    public void invokeMethod(String method, Object[] arguments, ScriptCallback resultCallback) {
        if (getWebView() == null) { return; }
        getWebView().invokeMethod(method, arguments, resultCallback);
    }

    @Override
    public void invokeMethod(String module, String method, Object[] arguments, ScriptCallback resultCallback) {
        if (getWebView() == null) { return; }
        getWebView().invokeMethod(module, method, arguments, resultCallback);
    }

    @Override
    public void evaluate(String script, ScriptCallback resultCallback) {
        if (getWebView() == null) { return; }
        getWebView().evaluate(script, resultCallback);
    }

    @Override
    public void setTheme(int theme) {
        if (getWebView() == null) { return; }
        getWebView().setTheme(theme);
    }

    @Override
    public void setShowProgress(boolean show) {
        if (getWebView() == null) { return; }
        getWebView().setShowProgress(show);
    }

    @Override
    public void setObserveLifecycle(boolean observe) {
        if (getWebView() == null) { return; }
        getWebView().setObserveLifecycle(observe);
    }

}
