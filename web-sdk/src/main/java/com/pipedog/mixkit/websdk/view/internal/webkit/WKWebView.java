package com.pipedog.mixkit.websdk.view.internal.webkit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.pipedog.mixkit.tool.MixLogger;
import com.pipedog.mixkit.web.interfaces.IMixWebView;
import com.pipedog.mixkit.web.interfaces.IWebViewBridgeListener;
import com.pipedog.mixkit.web.interfaces.ScriptCallback;
import com.pipedog.mixkit.web.view.MixWKWebView;
import com.pipedog.mixkit.websdk.R;
import com.pipedog.mixkit.websdk.config.IWebSDKConfiguration;
import com.pipedog.mixkit.websdk.config.WebSDKConfiguration;
import com.pipedog.mixkit.websdk.constants.WebStyle;
import com.pipedog.mixkit.websdk.interfaces.IWebView;
import com.pipedog.mixkit.websdk.interfaces.IWebViewListener;
import com.pipedog.mixkit.websdk.interfaces.widget.IErrorView;
import com.pipedog.mixkit.websdk.interfaces.widget.ILoadingView;
import com.pipedog.mixkit.websdk.notification.NotificationCenter;
import com.pipedog.mixkit.websdk.utils.UserAgentFormat;
import com.pipedog.mixkit.websdk.utils.WebCookieManager;
import com.pipedog.mixkit.websdk.utils.WebLifecycleObserver;

public class WKWebView extends FrameLayout implements IWebView {

    private static final String ERR_ABOUT_BLANK = "about:blank";

    private MixWKWebView mWebView;
    private IErrorView mErrorView;
    private ILoadingView mLoadingView;
    private int mTheme = WebStyle.WEB_THEME_LIGHT;
    private boolean mShowLoading = true;
    private boolean mObserveLifecycle = true;
    private WebLifecycleObserver mLifecycleObserver;
    private IWebViewListener mListener;


    // CONSTRUCTORS

    public WKWebView(@NonNull Context context) {
        this(context, null);
    }

    public WKWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MixWebView);
        mTheme = typedArray.getInt(R.styleable.MixWebView_mix_theme, WebStyle.WEB_THEME_LIGHT);
        mShowLoading = typedArray.getBoolean(R.styleable.MixWebView_mix_show_loading, true);
        mObserveLifecycle = typedArray.getBoolean(R.styleable.MixWebView_mix_observe_lifecycle, true);

        setupViews();
        NotificationCenter.getInstance().addObserver(mWebView);
    }


    // PRIVATE METHODS

    private void setupViews() {
        // Create subviews
        mWebView = getWebView();
        mErrorView = getErrorView();
        mLoadingView = getLoadingView();

        // Add subviews
        addView(mWebView);
        addView((View) mErrorView);
        addView((View) mLoadingView);

        // Setup webView
        mWebView.setWebChromeClient(new WKWebChromeClient());
        mWebView.setWebViewClient(new WKWebViewClient());
        mWebView.setBridgeListener(new WKWebViewBridgeListener());

        IWebSDKConfiguration.IWebSettingsConfiguration conf =
                WebSDKConfiguration.getInstance().getWebSettingsConfiguration();
        if (conf != null) {
            conf.setup(mWebView.getSettings());
        }

        // Setup errorView
        mErrorView.setRetryButtonListener(new RetryListener());
    }

    private IWebSDKConfiguration.IWidgetCreator getWidgetCreator() {
        IWebSDKConfiguration.IWidgetCreator creator = WebSDKConfiguration.getInstance().getWidgetCreator();
        if (creator == null) {
            throw new RuntimeException("Call method `void setWidgetCreator(IWidgetCreator);` first!");
        }
        return creator;
    }

    private void setupUserAgent() {
        WebSettings settings = mWebView.getSettings();
        String defaultUserAgent = settings.getUserAgentString();
        String userAgent = UserAgentFormat.getUserAgent(defaultUserAgent);
        settings.setUserAgentString(userAgent);
    }

    private void setupCookies() {
        WebCookieManager.removeAllCookies();
        WebCookieManager.registerCookies();
    }

    private void observeLifecycle() {
        if (mWebView == null || mLifecycleObserver != null) {
            return;
        }
        mLifecycleObserver = new WebLifecycleObserver(mWebView);
        mLifecycleObserver.observe();
    }

    private void cancelObserveLifecycle() {
        if (mLifecycleObserver == null) {
            return;
        }
        mLifecycleObserver.cancel();
        mLifecycleObserver = null;
    }

    private void showLoadingView() {
        if (!mShowLoading) {
            return;
        }
        mLoadingView.setVisibility(true);
        mLoadingView.setProgress(0);
    }

    private void hideLoadingView() {
        mLoadingView.setVisibility(false);
    }

    private void setProgress(int progress) {
        mLoadingView.setProgress(progress);
    }

    private void showErrorView() {
        mErrorView.setVisibility(true);
    }

    private void hideErrorView() {
        mErrorView.setVisibility(false);
    }

    private void handleWebError(String url, int errorCode, String errorMessage) {
        String currentUrl = mWebView.getUrl();
        if (currentUrl != null && url != null && !currentUrl.equals(url)) {
            return;
        }

        if (errorCode == WebViewClient.ERROR_HOST_LOOKUP
                || errorCode == WebViewClient.ERROR_CONNECT
                || errorCode == WebViewClient.ERROR_TIMEOUT) {
            if (!url.equals(ERR_ABOUT_BLANK)) {
                mWebView.loadUrl(ERR_ABOUT_BLANK);
            }
            showErrorView();
        }

        if (mListener == null) {
            return;
        }
        mListener.onReceivedError(this, errorCode, errorMessage);
    }


    // PROTECTED METHODS
    // IF YOU NEED TO CUSTOMIZE SOMETHING, OVERRIDE THE FOLLOWING METHODS.

    protected MixWKWebView getWebView() {
        MixWKWebView webView = new MixWKWebView(getContext());
        webView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));

        webView.setWebContentsDebuggingEnabled(true);
        return webView;
    }

    protected IErrorView getErrorView() {
        IErrorView errorView = getWidgetCreator().getErrorView(getContext());
        if (errorView != null && !(errorView instanceof View)) {
            throw new RuntimeException("errorView must inherit from `View`!");
        }

        ((View) errorView).setLayoutParams(new FrameLayout.LayoutParams(
                errorView.getWidgetWidth(), errorView.getWidgetHeight()
        ));
        return errorView;
    }

    protected ILoadingView getLoadingView() {
        ILoadingView loadingView = getWidgetCreator().getLoadingView(getContext());
        if (loadingView != null && !(loadingView instanceof View)) {
            throw new RuntimeException("loadingView must inherit from `View`!");
        }

        ((View) loadingView).setLayoutParams(new FrameLayout.LayoutParams(
                loadingView.getWidgetWidth(), loadingView.getWidgetHeight()
        ));

        loadingView.setVisibility(false);
        return loadingView;
    }


    // PRIVATE CLASSES

    private class WKWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            setProgress(newProgress);

            if (mListener != null) {
                mListener.onProgressChanged(WKWebView.this, newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);

            if (mListener != null) {
                mListener.onReceivedTitle(WKWebView.this, title);
            }
        }
    }

    private class WKWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            MixLogger.error("[old] shouldOverrideUrlLoading, url : %s", url);

            // 业务层统一定义拦截操作
            IWebSDKConfiguration.ILoadURLAction action =
                    WebSDKConfiguration.getInstance().getLoadURLAction();
            if (action != null && action.loadUrl(url)) {
                return true;
            }
            // 异常 scheme 过滤
            if (url != null && !url.startsWith("http") && !url.startsWith("file")) {
                return true;
            }
            // webView 监听拦截
            if (mListener != null) {
                return mListener.shouldOverrideUrlLoading(WKWebView.this, url);
            }
            // 默认逻辑，继续加载该 url
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            MixLogger.error("[new] shouldOverrideUrlLoading, url : %s", url);

            IWebSDKConfiguration.ILoadURLAction action =
                    WebSDKConfiguration.getInstance().getLoadURLAction();
            if (action != null && action.loadUrl(url)) {
                return true;
            }
            if (url != null && !url.startsWith("http") && !url.startsWith("file")) {
                return true;
            }
            if (mListener != null) {
                return mListener.shouldOverrideUrlLoading(WKWebView.this, url);
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            MixLogger.error("onPageStarted, url : %s", url);
            super.onPageStarted(view, url, favicon);
            hideErrorView();
            showLoadingView();

            if (mListener != null) {
                mListener.onPageStarted(WKWebView.this, url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            MixLogger.error("onPageFinished, url : %s", url);
            super.onPageFinished(view, url);
            hideLoadingView();

            if (url.equals(ERR_ABOUT_BLANK)) {
                handleWebError(url, ERROR_HOST_LOOKUP, "Error :" + ERROR_HOST_LOOKUP);
                return;
            }

            if (mListener != null) {
                mListener.onPageFinished(WKWebView.this, url);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            MixLogger.error("[new] onReceivedError, url : %s, code : %d, message : %s",
                    request.getUrl().toString(), error.getErrorCode(), error.getDescription().toString());
            super.onReceivedError(view, request, error);
            handleWebError(request.getUrl().toString(), error.getErrorCode(), error.getDescription().toString());
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            MixLogger.error("[new] onReceivedError, url : %s, code : %d, message : %s",
                    failingUrl, errorCode, description);
            super.onReceivedError(view, errorCode, description, failingUrl);
            handleWebError(failingUrl, errorCode, description);
        }
    }

    private class WKWebViewBridgeListener implements IWebViewBridgeListener {
        @Override
        public boolean onReceiveScriptMessage(IMixWebView webView, String fromUrl, String message) {
            IWebSDKConfiguration.IBridgeValidation bridgeValidation =
                    WebSDKConfiguration.getInstance().getBridgeValidation();
            if (bridgeValidation == null) {
                return false;
            }
            if (bridgeValidation.shouldCallBridgeFromUrl(fromUrl)) {
                return false;
            }
            return true;
        }

        @Override
        public void onParseMessageFailed(IMixWebView webView, String fromUrl, String message) {
            MixLogger.error("Invoke bridge method failed, from url : %s, message : %s", fromUrl, message);
        }
    }

    private class RetryListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            reload();
        }
    }


    // OVERRIDE METHODS FOR `IWebView`

    @Override
    public void setListener(IWebViewListener listener) {
        mListener = listener;
    }

    @Override
    public void onDestruct() {
        NotificationCenter.getInstance().removeObserver(mWebView);
        cancelObserveLifecycle();

        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    @Override
    public void loadUrl(String url) {
        if (url == null || url.isEmpty()) {
            if (mListener != null) {
                mListener.onReceivedError(this, WebViewClient.ERROR_BAD_URL, "Invalid argument `url`!");
            }
            showErrorView();
            return;
        }

        if (!url.startsWith("http") && !url.startsWith("file")) {
            showErrorView();
            return;
        }

        showLoadingView();
        mWebView.loadUrl(url);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        showLoadingView();
        mWebView.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String failUrl) {
        showLoadingView();
        mWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, failUrl);
    }

    @Override
    public void stopLoading() {
        mWebView.stopLoading();
    }

    @Override
    public void reload() {
        if (mWebView.getUrl() == null || mWebView.getUrl().isEmpty()) {
            showErrorView();
            return;
        }

        // about:blank 的 case，暂时使用 goback 的方式处理，如果有问题，这里统一改为走 loadUrl 处理
        if (mWebView.getUrl().equals(ERR_ABOUT_BLANK)) {
            mWebView.goBack();
            return;
        }

        mWebView.reload();
    }

    @Override
    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    @Override
    public void goBack() {
        mWebView.goBack();
    }

    @Override
    public boolean canGoForward() {
        return mWebView.canGoForward();
    }

    @Override
    public void goForward() {
        mWebView.goForward();
    }

    @Override
    public void invokeMethod(String method, Object[] arguments, ScriptCallback resultCallback) {
        mWebView.invokeMethod(method, arguments, resultCallback);
    }

    @Override
    public void invokeMethod(String module, String method, Object[] arguments, ScriptCallback resultCallback) {
        mWebView.invokeMethod(module, method, arguments, resultCallback);
    }

    @Override
    public void evaluate(String script, ScriptCallback resultCallback) {
        mWebView.evaluate(script, resultCallback);
    }

    @Override
    public void setWebTheme(int theme) {
        mLoadingView.setTheme(theme);
        mErrorView.setTheme(theme);
    }

    @Override
    public void setShowLoading(boolean show) {
        mShowLoading = show;
    }

    @Override
    public void setObserveLifecycle(boolean observe) {
        mObserveLifecycle = observe;
    }


    // OVERRIDE METHODS

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupUserAgent();
        setupCookies();
        observeLifecycle();
        setWebTheme(mTheme);
    }

}
