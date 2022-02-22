package com.pipedog.mixkit.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.webkit.WebSettings;

import com.pipedog.mixkit.example.testplugin.ServiceManager;
import com.pipedog.mixkit.tool.MixLogger;
import com.pipedog.mixkit.web.interfaces.IWebViewBridgeListener;
import com.pipedog.mixkit.web.view.MixWKWebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MixWKWebView webView = findViewById(R.id.testWebView);
        webView.setWebContentsDebuggingEnabled(true);

        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.loadUrl("file:///android_asset/demo.html");

        Log.e("Mix-app", ">>>>> application launch finished!!!");
        MixLogger.info(">>>>> application launch finished!!!");

        webView.setWebViewBridgeListener(new IWebViewBridgeListener() {
            @Override
            public boolean onReceiveScriptMessage(MixWKWebView webView, String fromUrl, String message) {
                MixLogger.error("fromUrl = %s", fromUrl);
                return false;
            }

            @Override
            public void onParseMessageFailed(MixWKWebView webView, String fromUrl, String message) {

            }
        });

//        webView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                webView.invokeMethod("window.testInvokeLogic", new Object[]{}, new ScriptCallback() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        MixLogger.info(value);
//                    }
//
//                    @Override
//                    public void onReceiveError(String error) {
//                        MixLogger.error(error);
//                    }
//                });
//            }
//        }, 3000);

//        ServiceManager.getInstance().printAllServices();
        ServiceManager.printAllStaticServices();
    }
}