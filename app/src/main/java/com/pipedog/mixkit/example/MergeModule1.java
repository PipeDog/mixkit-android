package com.pipedog.mixkit.example;

import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.tool.MixLogger;

@MixModule(name = "TestMergeModule")
public class MergeModule1 {

    @MixMethod(name = "log1")
    public void log() {
        MixLogger.error("log something here");
    }

}
