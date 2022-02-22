package com.pipedog.mixkit.websdk.config;

import android.webkit.WebSettings;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
        List<UserAgentNode> getUserAgentList();

        /**
         * 获取外部设置的 Cookie 列表，可以使用 CookieUtils 中的 createCookieList() 方法进行快速创建
         * 注意：Crosswalk 内核要求传递全域名，webkit 内核可以只传递根域名
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


    // 设置支持的浏览器内核类型

    @IntDef({
            KERNEL_TYPE_WEBKIT,
            KERNEL_TYPE_ALL,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface BrowserKernelType {}

    /** 系统 webkit 内核 */
    public static final int KERNEL_TYPE_WEBKIT = 1 << 0;
    /** 所有内核 */
    public static final int KERNEL_TYPE_ALL = KERNEL_TYPE_WEBKIT;

    /**
     * 设置支持的浏览器内核，优先级：webkit > crosswalk
     */
    void setBrowserKernelType(@BrowserKernelType int kernelType);

    /**
     * 获取支持的浏览器内核
     */
    @BrowserKernelType int getBrowserKernelType();


    // 自定义 WebSettings 配置

    /**
     * 自定义 WebSettings 配置
     */
    interface IWebSettingsConfiguration {
        /** webkit 内核配置 */
        void setup(WebSettings settings);
    }

    /**
     * 注册 WebSettings 配置
     */
    void setWebSettingsConfiguration(IWebSettingsConfiguration conf);

    /**
     * 获取 WebSettings 配置
     */
    IWebSettingsConfiguration getWebSettingsConfiguration();

}
