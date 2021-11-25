package com.pipedog.mixkit.module;

import java.io.Serializable;

public class MixMethodParameter implements Serializable {

    public String name;
    public String type;

    @Override
    public String toString() {
        return "MixMethodParameter{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
