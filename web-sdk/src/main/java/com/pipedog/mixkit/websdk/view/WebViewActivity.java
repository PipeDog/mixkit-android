package com.pipedog.mixkit.websdk.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.pipedog.mixkit.web.interfaces.ScriptCallback;
import com.pipedog.mixkit.websdk.R;
import com.pipedog.mixkit.websdk.config.IWebSDKConfiguration;
import com.pipedog.mixkit.websdk.config.WebSDKConfiguration;
import com.pipedog.mixkit.websdk.constants.RouteDef;
import com.pipedog.mixkit.websdk.constants.WebStyle;
import com.pipedog.mixkit.websdk.interfaces.IOpenWebView;
import com.pipedog.mixkit.websdk.interfaces.IWebView;
import com.pipedog.mixkit.websdk.interfaces.IWebViewActivity;
import com.pipedog.mixkit.websdk.interfaces.IWebViewListener;
import com.pipedog.mixkit.websdk.interfaces.widget.ITitleBar;

import java.util.Map;

public class WebViewActivity extends AppCompatActivity implements IWebViewActivity {

    private FrameLayout mRootView;
    private View mTitleBar;
    private OpenWebView mWebView;
    private IWebViewListener mListener;
    private int mTheme = WebStyle.WEB_THEME_LIGHT;
    private boolean mShowProgress = true;
    private boolean mObserveLifecycle = true;
    private String mUrl;

    // LIFECYCLES

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mix_activity_web_view);

        parseParams();
        setupViews();

        mWebView.loadUrl(mUrl);
    }


    // PRIVATE METHODS

    private void parseParams() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        mUrl = bundle.getString(RouteDef.KEY_URL);
        mTheme = bundle.getInt(RouteDef.KEY_THEME);
        mShowProgress = bundle.getInt(RouteDef.KEY_SHOW_PROGRESS);
        mObserveLifecycle = bundle.getInt(RouteDef.KEY_OBSERVE_LIFECYCLE);
    }

    private void setupViews() {
        mRootView = findViewById(R.id.mix_web_activity_root_view);
        createTitleBar();
        createOpenWebView();
    }


    // DYNAMIC CREATE VIEWS

    private void createTitleBar() {
        IWebSDKConfiguration.IWidgetCreator creator = WebSDKConfiguration.getInstance().getWidgetCreator();
        if (creator == null) {
            throw new RuntimeException("Call method `void setWidgetCreator(IWidgetCreator);` first!");
        }

        ITitleBar titleBar = creator.getTitleBar(this, mTheme);
        if (titleBar != null && !(titleBar instanceof View)) {
            throw new RuntimeException("titleBar must inherit from 'View'!");
        }

        mTitleBar = (View) titleBar;
        mTitleBar.setLayoutParams(new FrameLayout.LayoutParams(
                mTitleBar.getWidth(), mTitleBar.getHeight()
        ));

        titleBar.setGoBackButtonListener(new GoBackListener());
        titleBar.setCloseButtonListener(new CloseListener());
        mRootView.addView(mTitleBar);
    }

    private void createOpenWebView() {
        OpenWebView webView = new OpenWebView(this);
        webView.setListener(new PageLoadListener());
        webView.setThemeStyle(mTheme);
        webView.setShowProgress(mShowProgress);
        webView.setObserveLifecycle(mObserveLifecycle);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );

        lp.setMargins(0, ((ITitleBar) mTitleBar).getHeight(), 0, 0);
        webView.setLayoutParams(lp);
        mRootView.addView(webView);
    }


    // OVERRIDE METHODS

    @Override
    public IOpenWebView getWebView() {
        return mWebView;
    }

    @Override
    public void setExtraData(Map<String, Object> extraData) {
        mWebView.setExtraData(extraData);
    }

    @Override
    public Map<String, Object> getExtraData() {
        return mWebView.getExtraData();
    }

    @Override
    public void setListener(IWebViewListener listener) {
        mListener = listener;
    }

    @Override
    public void onDestruct() {
        mWebView.onDestruct();
    }

    @Override
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        mWebView.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String failUrl) {
        mWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, failUrl);
    }

    @Override
    public void stopLoading() {
        mWebView.stopLoading();
    }

    @Override
    public void reload() {
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
    public void setThemeStyle(int theme) {
        mWebView.setThemeStyle(theme);
    }

    @Override
    public void setShowProgress(boolean show) {
        mWebView.setShowProgress(show);
    }

    @Override
    public void setObserveLifecycle(boolean observe) {
        mWebView.setObserveLifecycle(observe);
    }


    // PRIVATE CLASSES

    private class GoBackListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            goBack();
        }
    }

    private class CloseListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    private class PageLoadListener implements IWebViewListener {
        @Override
        public void onProgressChanged(IWebView view, int newProgress) {
            if (mListener == null) { return; }
            mListener.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(IWebView view, String title) {
            ((ITitleBar) mTitleBar).setTitle(title);

            if (mListener == null) { return; }
            mListener.onReceivedTitle(view, title);
        }

        @Override
        public boolean shouldOverrideUrlLoading(IWebView view, String url) {
            if (mListener == null) { return false; }
            return mListener.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(IWebView view, String url) {
            if (mListener == null) { return; }
            mListener.onPageStarted(view, url);
        }

        @Override
        public void onPageFinished(IWebView view, String url) {
            if (mListener == null) { return; }
            mListener.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(IWebView view, int errorCode, String errorMessage) {
            if (mListener == null) { return; }
            mListener.onReceivedError(view, errorCode, errorMessage);
        }
    }

}