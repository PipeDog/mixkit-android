package com.pipedog.mixkit.web.interfaces;

/**
 * js 脚本执行结果回调接口
 * @author liang
 */
public interface ScriptCallback {
    public void onReceiveValue(String value);
    public void onReceiveError(String error);
}
