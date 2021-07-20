package com.pipedog.mixkit.web;

import android.webkit.WebView;
import java.util.List;

public interface IMixScriptEngine {

    void invokeMethod(String method,
                      List<Object> arguments,
                      ScriptCallback resultCallback);

    void invokeMethod(String module,
                      String method,
                      List<Object> arguments,
                      ScriptCallback resultCallback);

    void executeScript(String script, ScriptCallback resultCallback);

}
