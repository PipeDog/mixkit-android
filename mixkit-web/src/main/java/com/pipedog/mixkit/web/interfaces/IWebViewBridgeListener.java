package com.pipedog.mixkit.web.interfaces;

import com.pipedog.mixkit.web.view.MixWKWebView;

/**
 * bridge 通信监听，你可以在这里进行消息拦截，并进行诸如：通信白名单校验、通信日志打印等内容
 * @author liang
 * @time 2022/01/09
 */
public interface IWebViewBridgeListener {

    /**
     * 接收到 js 侧脚本消息，如果有必要可以在这里进行动作拦截处理
     * @param webView 来源 web 视图
     * @param fromUrl 消息来源 url
     * @param message 接收到的消息内容
     * @return 如果返回 true 则表示外部处理该消息，如果返回 false 则执行内部默认逻辑
     */
    boolean onReceiveScriptMessage(MixWKWebView webView, String fromUrl, String message);

    /**
     * 内部解析脚本消息异常
     * @param webView 来源 web 视图
     * @param fromUrl 消息来源 url
     * @param message 接收到的消息内容
     */
    void onParseMessageFailed(MixWKWebView webView, String fromUrl, String message);

}
