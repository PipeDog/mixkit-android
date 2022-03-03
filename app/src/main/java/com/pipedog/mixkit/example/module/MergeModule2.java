package com.pipedog.mixkit.example.module;

import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.tool.MixLogger;

@MixModule(name = "TestMergeModule")
public class MergeModule2 {

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

}
