package com.pipedog.mixkit.module;

import java.io.Serializable;
import java.util.List;

public class MixModuleMethod implements Serializable {

    public String className;
    public String methodName;
    public List<MixMethodParameter> parameters;

    @Override
    public String toString() {
        return "MixModuleMethod{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters=" + parameters +
                '}';
    }

}
