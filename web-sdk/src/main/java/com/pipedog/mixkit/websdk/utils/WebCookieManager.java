package com.pipedog.mixkit.websdk.utils;

import android.webkit.CookieManager;

import com.pipedog.mixkit.websdk.config.IWebSDKConfiguration;
import com.pipedog.mixkit.websdk.config.WebSDKConfiguration;

import java.net.HttpCookie;
import java.util.List;

/**
 * webkit cookie 管理器
 */
public class WebCookieManager {

    // PUBLIC METHODS

    /**
     * 清空 cookie
     */
    public static void removeAllCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookies(null);
        cookieManager.flush();
    }

    /**
     * 注册 cookie
     */
    public static void registerCookies() {
        IWebSDKConfiguration.IFetcher fetcher = WebSDKConfiguration.getInstance().getFetcher();
        if (fetcher == null) {
            return;
        }

        List<HttpCookie> cookies = fetcher.getCookies();
        if (cookies == null || cookies.isEmpty()) {
            return;
        }

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        for (HttpCookie cookie : cookies) {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("%s=%s;", cookie.getName(), cookie.getValue()));

            if (cookie.getDomain() != null && cookie.getDomain().length() > 0) {
                builder.append(String.format("domain=%s;", cookie.getDomain()));
            }

            if (cookie.getPath() != null && cookie.getPath().length() > 0) {
                builder.append(String.format("path=%s;", cookie.getPath()));
            } else {
                builder.append("path=/;");
            }
            cookieManager.setCookie(cookie.getDomain(), builder.toString());
        }
    }

}
