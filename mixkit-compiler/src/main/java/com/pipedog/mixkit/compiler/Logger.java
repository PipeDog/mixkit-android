package com.pipedog.mixkit.compiler;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class Logger {

    private Messager mMessager;

    public Logger(Messager messager) {
        mMessager = messager;
    }

    public void info(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

}
