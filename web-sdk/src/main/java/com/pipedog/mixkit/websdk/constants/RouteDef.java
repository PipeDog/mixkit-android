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
    /** Web 主题风格（可选值参考 WebStyle 中常量定义） */
    public static final String KEY_THEME = "theme";
    /** 是否展示进度条（loading 页）（0 展示，1 隐藏，默认为 0） */
    public static final String KEY_SHOW_LOADING = "show_loading";
    /** 是否监听页面声明周期并通知 web 侧（0 监听，1 不监听，默认为 0） */
    public static final String KEY_OBSERVE_LIFECYCLE = "observe_lifecycle";
    /** 是否需要解决视图嵌套导致的手势冲突（0 需要，1 不需要，默认为 0） */
    public static final String KEY_SUPPORT_NESTED = "support_nested";

}
