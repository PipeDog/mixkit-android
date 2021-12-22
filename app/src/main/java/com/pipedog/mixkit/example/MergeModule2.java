package com.pipedog.mixkit.example;

import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.tool.MixLogger;

@MixModule(name = "TestMergeModule")
public class MergeModule2 {

    @MixMethod(name = "print")
    public void print() {
        MixLogger.error("print something here");
    }

}
