package com.pipedog.mixkit.example;

import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.annotation.MixMethod;

@MixModule(name = "MKTestModule")
public class MixTestModule {

    @MixMethod(name = "testExportMethod")
    public void testMethod() {

    }
}
