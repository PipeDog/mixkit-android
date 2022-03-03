package com.pipedog.mixkit.web.view;

import android.util.AttributeSet;
import android.content.Context;

import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.pipedog.mixkit.tool.JsonUtils;
import com.pipedog.mixkit.tool.MixLogger;

import com.google.gson.Gson;
import com.pipedog.mixkit.web.interfaces.IMixWebView;
import com.pipedog.mixkit.web.interfaces.IScriptEngine;
import com.pipedog.mixkit.web.interfaces.IWebViewBridgeListener;
import com.pipedog.mixkit.web.interfaces.ScriptCallback;
import com.pipedog.mixkit.web.kernel.WebViewBridge;
import com.pipedog.mixkit.web.utils.JavaScriptFormat;
import com.pipedog.mixkit.tool.ThreadUtils;
import com.pipedog.mixkit.web.utils.WebInjector;

/**
 * 支持 native-js 交互的 web 视图，使用时将系统的 WebView 替换成当
 * 前类即可，命名中的 WK~ 作用为标识此类使用了系统的 webkit 内核
 *
 * Chrome 调试地址如下（需要注意，当使用该地址调试时需要翻墙）：
 *  chrome://inspect/#devices
 *
 * 注意：
 *      MixWKWebView 仅提供了 bridge 交互的基础能力，其他诸如 User-Agent、Cookie 等
 *      信息的管理，以及 UI 定制等内容业务层应该统一抽象出一个 WebSDK 去进行处理，不建
 *      议在 mixkit-web 中直接进行二次开发，应该尽量避免功能上的耦合
 * @author liang
 */
public class MixWKWebView extends WebView implements IMixWebView {

    private static final String MIX_KIT_NAME = "MixKit";
    private static final int MIX_ANDROID_TYPE = 2;

    private Gson mGson;
    private WebViewBridge mWebViewBridge;
    private IWebViewBridgeListener mBridgeListener;


    // CONSTRUCTORS

    public MixWKWebView(Context context) {
        this(context, null);
    }

    public MixWKWebView(Context context, AttributeSet att) {
        this(context, att, 0);
    }

    public MixWKWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupInitializeConfiguration();
    }


    // PRIVATE METHODS

    private void setupInitializeConfiguration() {
        mGson = JsonUtils.getMapGson();
        mWebViewBridge = new WebViewBridge(this);

        // Enable js bridge
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        addJavascriptInterface(this, MIX_KIT_NAME);
    }

    private void eval(String script, ScriptCallback resultCallback) {
        String formatScript = String.format("javascript:%s", script);

        evaluateJavascript(formatScript, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (resultCallback == null) {
                    return;
                }
                resultCallback.onReceiveValue(value);
            }
        });
    }


    // PUBLIC METHODS

    @JavascriptInterface
    public void postMessage(String message) {
        try {
            Map<String, Object> map = mGson.fromJson(
                    message, new TypeToken<Map<String, Object>>(){}.getType());

            ThreadUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    IWebViewBridgeListener bridgeListener = getBridgeListener();
                    if (bridgeListener == null) {
                        mWebViewBridge.getExecutor().invokeMethod(map);
                        return;
                    }

                    MixWKWebView webViewThis = MixWKWebView.this;
                    String fromUrl = webViewThis.getUrl();

                    boolean shouldNotContinue = bridgeListener.
                            onReceiveScriptMessage(webViewThis, fromUrl, message);
                    if (shouldNotContinue) {
                        return;
                    }

                    boolean invokeResult = mWebViewBridge.getExecutor().invokeMethod(map);
                    if (!invokeResult) {
                        bridgeListener.onParseMessageFailed(webViewThis, fromUrl, message);
                    }
                }
            });
        } catch (Exception e) {
            MixLogger.error("invoke native method failed, message : %s.", message);
        }
    }

    @JavascriptInterface
    public int getSystemType() {
        return MIX_ANDROID_TYPE;
    }

    @JavascriptInterface
    public String getNativeConfig() {
        return WebInjector.getInjectionJson();
    }


    // OVERRIDE METHODS

    @Override
    public void invokeMethod(String method,
                             Object[] arguments,
                             ScriptCallback resultCallback) {
        invokeMethod(null, method, arguments, resultCallback);
    }

    @Override
    public void invokeMethod(String module,
                             String method,
                             Object[] arguments,
                             ScriptCallback resultCallback) {
        if (method == null || method.isEmpty()) {
            resultCallback.onReceiveError("invoke method failed, " +
                    "argument `method` can not be null, check it!");
            return;
        }

        String script = JavaScriptFormat.formatScript(module, method, arguments);
        evaluate(script, resultCallback);
    }

    @Override
    public void evaluate(String script, ScriptCallback resultCallback) {
        if (script == null || script.isEmpty()) {
            resultCallback.onReceiveError("execute script failed, " +
                    "argument script can not be null, check it!");
            return;
        }

        ThreadUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                eval(script, resultCallback);
            }
        });
    }

    @Override
    public IScriptEngine getScriptEngine() {
        return (IScriptEngine)this;
    }

    @Override
    public void setBridgeListener(IWebViewBridgeListener listener) {
        mBridgeListener = listener;
    }

    @Override
    public IWebViewBridgeListener getBridgeListener() {
        return mBridgeListener;
    }

}
