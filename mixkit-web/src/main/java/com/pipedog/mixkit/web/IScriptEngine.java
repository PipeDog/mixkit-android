package com.pipedog.mixkit.web;

import android.webkit.WebView;
import java.util.List;

/**
 * js 脚本执行器
 * @author liang
 */
public interface IScriptEngine {

    /**
     * 执行指定 js 方法
     *  最终脚本样式如：invokeJsFunction('a', 'b', ...)
     *
     * @param method js 方法名
     * @param arguments 参数列表
     * @param resultCallback 执行结果回调
     */
    void invokeMethod(String method,
                      Object[] arguments,
                      ScriptCallback resultCallback);

    /**
     * 执行指定 js 方法
     *  最终脚本样式如：invoker.invokeJsFunction('a', 'b')
     *
     * @param module module 变量名
     * @param method js 方法名
     * @param arguments 参数列表
     * @param resultCallback 执行结果回调
     */
    void invokeMethod(String module,
                      String method,
                      Object[] arguments,
                      ScriptCallback resultCallback);

    /**
     * 执行格式化后的 js 脚本
     * @param script js 脚本代码
     * @param resultCallback 执行结果回调
     */
    void evaluate(String script, ScriptCallback resultCallback);

}
