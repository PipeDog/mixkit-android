package com.pipedog.mixkit.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;

import android.content.Context;
import android.os.Bundle;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import android.util.Log;

import com.pipedog.mixkit.launch.Mix;
import com.pipedog.mixkit.launch.MixOptions;
import com.pipedog.mixkit.parser.*;
import com.pipedog.mixkit.module.MixModuleManager;
import com.pipedog.mixkit.tool.MixLogger;
import com.pipedog.mixkit.web.MixWebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, String> map = new HashMap<String, String>();
        map.put("testModuleName", "MKTestModule");
        map.put("testMethodName", "testExportMethod");

        try {
            IMixMessageParser parser = MixMessageParserManager.defaultManager().detectParser(map);
            if (parser == null) { return; }

            Logger.getGlobal().info("parser class name : " + parser.getClass().getName());
            Logger.getGlobal().info("parser modulename = " + parser.messageBody().moduleName() + ", " +
                    "methodName = " + parser.messageBody().methodName());
        } catch (Exception e) {
            Logger.getGlobal().info("Mix-Android" + e.toString());
        }

        MixModuleManager.defaultManager();

        Context context = null;
        try {
            MixOptions options = Mix.options();
            context = options.context;
        } catch (Exception e) {
            Logger.getGlobal().info("get context failed, e : " + e.toString());
            return;
        }

        //webView.loadUrl("file:///android_asset/index.html"); 加载assets目录中含有的index.html

        MixWebView webView = findViewById(R.id.testWebView);
        webView.setWebContentsDebuggingEnabled(true);

        webView.loadUrl("file:///android_asset/demo.html");

        Log.e("Mix-app", ">>>>> application launch finished!!!");
        MixLogger.info(">>>>> application launch finished!!!");
//        webView.loadUrl("https://www.baidu.com");
    }
}