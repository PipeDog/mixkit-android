package com.pipedog.mixkit.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.webkit.WebSettings;

import com.pipedog.mixkit.example.testplugin.ServiceManager;
import com.pipedog.mixkit.tool.MixLogger;
import com.pipedog.mixkit.web.IWebViewBridgeListener;
import com.pipedog.mixkit.web.MixWebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MixWebView webView = findViewById(R.id.testWebView);
        webView.setWebContentsDebuggingEnabled(true);

        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        webView.loadUrl("file:///android_asset/demo.html");

        Log.e("Mix-app", ">>>>> application launch finished!!!");
        MixLogger.info(">>>>> application launch finished!!!");

        webView.setWebViewBridgeListener(new IWebViewBridgeListener() {
            @Override
            public boolean onReceiveScriptMessage(MixWebView webView, String fromUrl, String message) {
                MixLogger.error("fromUrl = %s", fromUrl);
                return false;
            }

            @Override
            public void onParseMessageFailed(MixWebView webView, String fromUrl, String message) {

            }
        });

//        ServiceManager.getInstance().printAllServices();
        ServiceManager.printAllStaticServices();
    }
}