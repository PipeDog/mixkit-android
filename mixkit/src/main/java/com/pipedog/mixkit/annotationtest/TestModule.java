package com.pipedog.mixkit.annotationtest;

import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.annotation.MixMethod;

@MixModule(name = "TestLibModule")
public class TestModule {

    @MixMethod(name = "testJSMethod")
    public void testMethod() {

    }
}
