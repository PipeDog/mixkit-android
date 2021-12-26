package com.pipedog.mixkit.web_sdk.config;

import java.net.HttpCookie;
import java.util.List;

/**
 * WebSDK 配置接口定义
 * @author liang
 * @time 2021/12/26
 */
public interface IConfiguration {

    // SET COOKIE、USER-AGENT...

    /**
     * 注入数据获取接口
     */
    interface IFetcher {
        /**
         * 获取外部设置的 User-Agent 值
         */
        String getUserAgent();

        /**
         * 获取外部设置的 Cookie 列表
         */
        List<HttpCookie> getCookies();
    }

    /**
     * 注入数据访问器
     */
    void setFetcher(IFetcher fetcher);

    /**
     * 获取（注入）数据访问器
     * @return （注入）数据访问器实例
     */
    IFetcher getFetcher();


    // 自定义加载 URL 动作

    /**
     * 自定义加载 URL 动作接口
     */
    interface ILoadURLAction {
        /**
         * 接收 URL 并执行相应动作
         * @return 返回 true 表示处理成功，不再执行默认加载逻辑，
         *         返回 false 表示无法处理，将逻辑交还给系统继续执行
         */
        boolean loadUrl(String url);
    }

    /**
     * 注册自定义加载 URL 动作
     */
    void setLoadURLAction(ILoadURLAction action);

    /**
     * 获取自定义加载 URL 动作实现
     */
    ILoadURLAction getLoadURLAction();


    // bridge 功能校验

    /**
     * bridge 功能校验接口定义
     */
    interface IBridgeValidation {
        /**
         * 是否允许调用来自某个 URL 的 bridge 功能
         * @param url 发送该 bridge 消息的 URL 路径
         * @return true 表示校验通过，允许执行相应 bridge 功能，false 反之
         */
        boolean shouldCallBridgeFromUrl(String url);
    }

    /**
     * 注册 bridge 校验逻辑
     */
    void setBridgeValidation(IBridgeValidation bridgeValidation);

    /**
     * 获取 bridge 校验功能实现
     */
    IBridgeValidation getBridgeValidation();

}
