package com.pipedog.mixkit.module;

import java.io.Serializable;
import java.util.Map;

public class MixModuleData implements Serializable {

    public String className;
    public Map<String, MixModuleMethod> methods;

    @Override
    public String toString() {
        return "MixModuleData{" +
                "className='" + className + '\'' +
                ", methods=" + methods +
                '}';
    }

}
