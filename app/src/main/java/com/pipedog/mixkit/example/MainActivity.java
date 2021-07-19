package com.pipedog.mixkit.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import com.pipedog.mixkit.parser.*;
import com.pipedog.mixkit.module.MixModuleManager;

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

    }
}