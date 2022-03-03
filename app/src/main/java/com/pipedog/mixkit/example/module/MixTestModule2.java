package com.pipedog.mixkit.example.module;

import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.annotation.MixMethod;

@MixModule(name = "MKTestModule2")
public class MixTestModule2 {

    @MixMethod(name = "testMethodWith3Args")
    public void testMethod(int arg1, String arg2, Object arg3) {

    }
}
