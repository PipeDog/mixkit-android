package com.pipedog.mixkit.websdk.constants;

/**
 * 路由参数 Key/Value 定义
 * @author liang
 * @time 2022/01/28
 */
public interface RouteDef {

    // KEY CONSTANTS

    /** Web 加载的 URL 地址 */
    public static final String KEY_URL = "url";
    /** Web 主题风格（可选值为 "light|dark"，默认值为 "light"） */
    public static final String KEY_THEME = "theme";


    // VALUE CONSTANTS
    // VALUES FOR KEY `KEY_THEME`

    public static final String VALUE_THEME_LIGHT = "light";
    public static final String VALUE_THEME_DARK = "dark";

}
