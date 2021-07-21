package com.pipedog.mixkit.web;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.content.Context;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.view.InputEvent;

import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.pipedog.mixkit.module.MixModuleManager;
import com.pipedog.mixkit.tool.MixLogger;

import com.google.gson.Gson;

// Chrome debug address:
// chrome://inspect/#devices

public class MixWebView extends WebView implements IMixScriptEngine, IMixWebViewBridgeDelegate {

    // 包装外部传递进行来 WebViewClient，并且提供各种回调
    public class MixWebViewClient extends WebViewClient {

        private WebViewClient mWebViewClient;

        public MixWebViewClient(WebViewClient webViewClient) {
            mWebViewClient = webViewClient;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (mWebViewClient != null) {
                return mWebViewClient.shouldOverrideUrlLoading(view, url);
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (mWebViewClient != null) {
                return mWebViewClient.shouldOverrideUrlLoading(view, request);
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (mWebViewClient != null) {
                mWebViewClient.onPageStarted(view, url, favicon);
            }

            // inject export infos into webview when callback `onPageStarted`
            injectNativeModules();

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (mWebViewClient != null) {
                mWebViewClient.onPageFinished(view, url);
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);

            if (mWebViewClient != null) {
                mWebViewClient.onLoadResource(view, url);
            }
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);

            if (mWebViewClient != null) {
                mWebViewClient.onPageCommitVisible(view, url);
            }
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (mWebViewClient != null) {
                return mWebViewClient.shouldInterceptRequest(view, url);
            }
            return super.shouldInterceptRequest(view, url);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (mWebViewClient != null) {
                return mWebViewClient.shouldInterceptRequest(view, request);
            }
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
            super.onTooManyRedirects(view, cancelMsg, continueMsg);

            if (mWebViewClient != null) {
                mWebViewClient.onTooManyRedirects(view, cancelMsg, continueMsg);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            if (mWebViewClient != null) {
                mWebViewClient.onReceivedError(view, errorCode, description, failingUrl);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            if (mWebViewClient != null) {
                mWebViewClient.onReceivedError(view, request, error);
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);

            if (mWebViewClient != null) {
                mWebViewClient.onReceivedHttpError(view, request, errorResponse);
            }
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            super.onFormResubmission(view, dontResend, resend);

            if (mWebViewClient != null) {
                mWebViewClient.onFormResubmission(view, dontResend, resend);
            }
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);

            if (mWebViewClient != null) {
                mWebViewClient.doUpdateVisitedHistory(view, url, isReload);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);

            if (mWebViewClient != null) {
                mWebViewClient.onReceivedSslError(view, handler, error);
            }
        }

        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            super.onReceivedClientCertRequest(view, request);

            if (mWebViewClient != null) {
                mWebViewClient.onReceivedClientCertRequest(view, request);
            }
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);

            if (mWebViewClient != null) {
                mWebViewClient.onReceivedHttpAuthRequest(view, handler, host, realm);
            }
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            if (mWebViewClient != null) {
                return mWebViewClient.shouldOverrideKeyEvent(view, event);
            }
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            super.onUnhandledKeyEvent(view, event);

            if (mWebViewClient != null) {
                mWebViewClient.onUnhandledKeyEvent(view, event);
            }
        }

        // void onUnhandledInputEvent(WebView view, InputEvent event) can not override !!! why ???

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);

            if (mWebViewClient != null) {
                mWebViewClient.onScaleChanged(view, oldScale, newScale);
            }
        }

        @Override
        public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
            super.onReceivedLoginRequest(view, realm, account, args);

            if (mWebViewClient != null) {
                mWebViewClient.onReceivedLoginRequest(view, realm, account, args);
            }
        }

        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
            if (mWebViewClient != null) {
                return mWebViewClient.onRenderProcessGone(view, detail);
            }
            return super.onRenderProcessGone(view, detail);
        }

        @Override
        public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
            super.onSafeBrowsingHit(view, request, threatType, callback);

            if (mWebViewClient != null) {
                mWebViewClient.onSafeBrowsingHit(view, request, threatType, callback);
            }
        }

    }

    private static final String MIX_KIT_NAME = "MixKit";

    private Gson mGson;
    private MixWebViewBridge mWebViewBridge;

    public MixWebView(Context context) {
        super(context);
        setupInitializeConfiguration();
    }

    public MixWebView(Context context, AttributeSet att) {
        super(context, att);
        setupInitializeConfiguration();
    }

    public MixWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupInitializeConfiguration();
    }

    private void setupInitializeConfiguration() {
        mGson = new Gson();
        mWebViewBridge = new MixWebViewBridge(this);

        // enable js bridge
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);

        // register event listener
        WebViewClient webViewClient = getWebViewClient();
        MixWebViewClient realClient = new MixWebViewClient(webViewClient);
        setWebViewClient(realClient);

        addJavascriptInterface(this, MIX_KIT_NAME);
    }

    private void injectNativeModules() {
        String format =
                "if (window.__mk_systemType != 2) { " +
                "   window.__mk_systemType = 2; " +
                "   window.__mk_nativeConfig = %s; " +
                "}";
        String json = MixWebInjector.getInjectionJson();
        String script = String.format(format, json);

        executeScript(script, new ScriptCallback() {
            @Override
            public void onReceiveValue(String value) {
                MixLogger.info("inject js script finished, return value : %s.", value);
            }

            @Override
            public void onReceiveError(String error) {
                MixLogger.error("inject js script failed, error : %s!", error);
            }
        });
    }

    @Override
    public void loadUrl(@NonNull String url) {
        injectNativeModules();
        super.loadUrl(url);
    }

    @JavascriptInterface
    public void postMessage(String message) {
        try {
            Map map = mGson.fromJson(message, Map.class);

            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                mWebViewBridge.executor().invokeMethod(map);
            } else {
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mWebViewBridge.executor().invokeMethod(map);
                    }
                });
            }
        } catch (Exception e) {
            MixLogger.error("invoke native method failed, message : %s.", message);
        }
    }

    @Override
    public void invokeMethod(String method,
                             List<Object> arguments,
                             ScriptCallback resultCallback) {
        invokeMethod(null, method, arguments, resultCallback);
    }

    @Override
    public void invokeMethod(String module,
                             String method,
                             List<Object> arguments,
                             ScriptCallback resultCallback) {
        if (method == null || method.isEmpty()) {
            resultCallback.onReceiveError("invoke method failed, " +
                    "argument `method` can not be null, check it!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        if (module != null && !module.isEmpty()) {
            sb.append(module);
            sb.append(".");
        }

        sb.append(method);
        sb.append("(");

        int numberOfArguments = arguments.size();
        for (int i = 0; i < numberOfArguments; i++) {
            Object obj = arguments.get(i);

            if (obj instanceof Arrays || obj instanceof List || obj instanceof Map) {
                String argument = mGson.toJson(obj);
                sb.append(argument);
            } else if (obj instanceof String) {
                String argument = String.format("'%s'", obj);
                sb.append(argument);
            } else if (obj instanceof Short ||
                    obj instanceof Integer ||
                    obj instanceof Long ||
                    obj instanceof Float ||
                    obj instanceof Double ||
                    obj instanceof Boolean ||
                    obj instanceof Character ||
                    obj instanceof CharSequence ||
                    obj instanceof char[]) {
                sb.append(obj);
            } else {
                sb.append("null");
                MixLogger.error("Detected invalid argument when append js script, arg : %s",
                        obj.toString());
            }

            if (i != numberOfArguments - 1) {
                sb.append(", ");
            }
        }

        sb.append(");");
        executeScript(sb.toString(), resultCallback);
    }

    @Override
    public void executeScript(String script, ScriptCallback resultCallback) {
        if (script == null || script.isEmpty()) {
            resultCallback.onReceiveError("execute script failed, " +
                    "argument script can not be null, check it!");
            return;
        }

        executeScriptOnMainThread(script, resultCallback);
    }

    private void executeScriptOnMainThread(String script, ScriptCallback resultCallback) {
        String formatScript = String.format("javascript:%s", script);

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            evaluateJavascript(formatScript, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    resultCallback.onReceiveValue(value);
                    }
            });
        } else {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    evaluateJavascript(formatScript, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            resultCallback.onReceiveValue(value);
                                    }
                    });
                }
            });
        }
    }

    @Override
    public IMixScriptEngine scriptEngine() {
        return (IMixScriptEngine)this;
    }
}
