package com.pipedog.mixkit.websdk.constants;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * UI 风格定制常量定义
 */
public interface WebStyle {

    // 主题定义

    @IntDef({
            WEB_THEME_LIGHT,
            WEB_THEME_DARK
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface WebTheme {}

    /** 浅色主题 */
    public static final int WEB_THEME_LIGHT = 0;
    /** 暗黑主题 */
    public static final int WEB_THEME_DARK = 1;

}
