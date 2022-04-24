package com.pipedog.mixkit.example.module;

import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.kernel.IBridge;
import com.pipedog.mixkit.kernel.IBridgeModule;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.HashMap;
import java.util.Map;

@MixModule(name = "TestMergeModule")
public class MergeModule2 implements IBridgeModule {

    @MixMethod(name = "print")
    public void print() {
        MixLogger.error("print something here");
    }

//    @MixMethod(name = "test1")
    public void test1000() {

    }

    @MixMethod(name = "test100")
    public void test100() {

    }

    @MixMethod(name = "test101")
    public void test101() {

    }

    @Override
    public void setBridge(IBridge bridge) {

    }

    public static Map<String, Object> constantsToExport() {
        MixLogger.error(">>>>> class : MergeModule2 constantsToExport");

        Map<String, Object> map = new HashMap<>();
        map.put("module2Value", "this is module2");
        map.put("intValue", 90);
        map.put("floatValue", 100.001);
        return map;
    }

}
