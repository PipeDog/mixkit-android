package com.pipedog.mixkit.compiler.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * 日志打印工具包装
 * @author liang
 */
public class Logger {

    private Messager mMessager;

    public Logger(Messager messager) {
        mMessager = messager;
    }

    public void info(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

}
