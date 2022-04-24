package com.pipedog.mixkit.example.module;

import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.kernel.IBridge;
import com.pipedog.mixkit.kernel.IBridgeModule;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.HashMap;
import java.util.Map;

@MixModule(name = "TestMergeModule")
public class MergeModule1 implements IBridgeModule {

    @MixMethod(name = "log1")
    public void log() {
        MixLogger.error("log something here");
    }

    @MixMethod(name = "test1")
    public void test1() {

    }

    @MixMethod(name = "test2")
    public void test2() {

    }

    @Override
    public void setBridge(IBridge bridge) {

    }

    public static Map<String, Object> constantsToExport() {
        MixLogger.error(">>>>> class : MergeModule1 constantsToExport");

        Map<String, Object> map = new HashMap<>();
        map.put("version", "v1.0.0");
        return map;
    };

}
