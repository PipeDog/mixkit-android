package com.pipedog.mixkit.websdk.utils;

import android.content.Context;

/**
 * 像素转换工具
 */
public class DimensionUtils {

    /**
     * dp 转换为 px
     */
    public static final int dp2px(Context context, float dp) {
        if (dp <= 0) {
            return (int) dp;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px 转换为 dp
     */
    public static final int px2dp(Context context, float px) {
        if (px <= 0) {
            return (int) px;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * sp 转换为 px
     */
    public static final int sp2px(Context context, float sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    /**
     * px 转换为 sp
     */
    public static final int px2sp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scale + 0.5f);
    }

}
