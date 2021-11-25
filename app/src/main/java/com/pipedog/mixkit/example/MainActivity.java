package com.pipedog.mixkit.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;

import android.content.Context;
import android.os.Bundle;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import android.util.Log;
import android.webkit.WebSettings;

import com.pipedog.mixkit.parser.*;
import com.pipedog.mixkit.module.MixModuleManager;
import com.pipedog.mixkit.tool.MixLogger;
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
    }
}