package com.pipedog.mixkit.module;

import com.pipedog.mixkit.tool.MixLogger;

import java.lang.reflect.*;
import java.util.List;

// http://www.51gjie.com/java/796.html
public class MixModuleMethod {

    public Class cls;
    public String className;
    public Method method;
    // native method name
    public String methodName;
    public Type[] genericParameterTypes;
    // transform by method `getTypeName` at interface `Type`
    public List<String> genericParameterTypeNames;

    public MixModuleMethod() {
        // Do nothing...
    }

    public static MixModuleMethod make(String className, String methodName) {
        MixModuleMethod method = new MixModuleMethod();
        method.className = className;
        method.methodName = methodName;

        try {
            method.cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            MixLogger.error("Can not found class named : `%s`", className);
            return null;
        }



        return method;
    }

}
