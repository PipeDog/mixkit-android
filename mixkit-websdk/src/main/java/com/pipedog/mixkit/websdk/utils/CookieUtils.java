package com.pipedog.mixkit.websdk.utils;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用来快速构建 cookie 列表
 */
public class CookieUtils {

    /**
     * 创建 cookie 列表
     * @param domains 需要支持的域名列表
     * @param cookieValues 最终需要注入的 cookie 参数
     * @return 构建好的 cookie 列表，可以直接做为 IConfiguration.IFetcher 中 getCookies() 函数的返回值
     */
    public static List<HttpCookie> createCookieList(List<String> domains, Map<String, String> cookieValues) {
        List<HttpCookie> cookies = new ArrayList<>();

        if (domains == null || domains.isEmpty()) {
            return cookies;
        }
        if (cookieValues == null || cookieValues.isEmpty()) {
            return cookies;
        }

        for (String domain : domains) {
            for (Map.Entry<String, String> entry : cookieValues.entrySet()) {
                HttpCookie cookie = new HttpCookie(entry.getKey(), entry.getValue());
                cookie.setDomain(domain);
                cookie.setPath("/");
                cookies.add(cookie);
            }
        }

        return cookies;
    }

}
