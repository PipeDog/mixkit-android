package com.pipedog.mixkit.web;

import android.webkit.WebView;
import java.util.List;

public interface IScriptEngine {

    void invokeMethod(String method,
                      Object[] arguments,
                      ScriptCallback resultCallback);

    void invokeMethod(String module,
                      String method,
                      Object[] arguments,
                      ScriptCallback resultCallback);

    void evaluate(String script, ScriptCallback resultCallback);

}
